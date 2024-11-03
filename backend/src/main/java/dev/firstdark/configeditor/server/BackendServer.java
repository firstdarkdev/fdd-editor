package dev.firstdark.configeditor.server;

import com.google.gson.JsonObject;
import dev.firstdark.configeditor.converter.ConfigConverter;
import dev.firstdark.configeditor.server.responses.StandardResponse;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.UploadedFile;
import io.javalin.http.staticfiles.Location;
import io.javalin.json.JavalinGson;
import io.javalin.plugin.bundled.CorsPluginConfig;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;

public class BackendServer {

    private final WebsocketServer websocketServer;

    public BackendServer(int port) {
        Javalin javalin = Javalin.create(config -> {
            config.staticFiles.add("web", Location.EXTERNAL);
            config.bundledPlugins.enableCors(cors -> cors.addRule(CorsPluginConfig.CorsRule::anyHost));
            config.jsonMapper(new JavalinGson());
            config.spaRoot.addFile("/", "web/index.html", Location.EXTERNAL);
        });

        javalin.unsafeConfig().router.apiBuilder(() -> path("/v1", () -> {
           post("/parseupload", this::handleUpload);
           post("/saveconfig", this::handleSave);
        }));

        websocketServer = new WebsocketServer(javalin);

        javalin.start(port);
    }

    private void handleSave(Context context) {
        boolean isSocket = context.queryParam("isSocket") != null && context.queryParam("isSocket").equalsIgnoreCase("true");
        String id = context.queryParam("identifier");

        try {
            JsonObject o = context.bodyAsClass(JsonObject.class);
            String config = ConfigConverter.INSTANCE.writeToToml(o);
            context.json(StandardResponse.success("Success", config));

            if (isSocket) {
                websocketServer.sendConfig(config, id);
            }
        } catch (Exception e) {
            e.printStackTrace();
            context.json(StandardResponse.error(e.getMessage()));
        }
    }

    private void handleUpload(Context context) {
        UploadedFile f = context.uploadedFile("file");
        if (f == null) {
            context.json(StandardResponse.error("No uploaded file found"));
            return;
        }

        try {
            ConfigConverter.INSTANCE.getTomlParser().parse(f.content());
        } catch (Exception e) {
            context.json(StandardResponse.error("Unsupported or corrupt file"));
            return;
        }

        try {
            JsonObject o = ConfigConverter.INSTANCE.readToJson(f.content());
            o.addProperty("filename", f.filename());
            context.json(StandardResponse.success("Success", o));
        } catch (Exception e) {
            e.printStackTrace();
            context.json(StandardResponse.error("Failed to process file: " + e.getMessage()));
        }
    }

    private static String getMimeType(String filePath) {
        if (filePath.endsWith(".js")) return "application/javascript";
        if (filePath.endsWith(".css")) return "text/css";
        if (filePath.endsWith(".html")) return "text/html";
        if (filePath.endsWith(".png")) return "image/png";
        if (filePath.endsWith(".jpg") || filePath.endsWith(".jpeg")) return "image/jpeg";
        if (filePath.endsWith(".svg")) return "image/svg+xml";
        // Add other file types as needed
        return "application/octet-stream"; // Default MIME type
    }

}
