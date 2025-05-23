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
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;

import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;

public class BackendServer {

    private final WebsocketServer websocketServer;

    public BackendServer(int port) {
        Javalin javalin = Javalin.create(config -> {
            config.staticFiles.add("web", Location.EXTERNAL);
            config.bundledPlugins.enableCors(cors -> cors.addRule(CorsPluginConfig.CorsRule::anyHost));
            config.jsonMapper(new JavalinGson());

            if (new File("web/index.html").exists()) {
                config.spaRoot.addFile("/", "web/index.html", Location.EXTERNAL);
            }

        });

        javalin.unsafeConfig().router.apiBuilder(() -> path("/v1", () -> {
           post("/parseupload", this::handleUpload);
            post("/parseembed", this::handleEmbed);
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
            o.addProperty("original", IOUtils.toString(f.content()));
            context.json(StandardResponse.success("Success", o));
        } catch (Exception e) {
            e.printStackTrace();
            context.json(StandardResponse.error("Failed to process file: " + e.getMessage()));
        }
    }

    private void handleEmbed(Context context) {
        UploadedFile f = context.uploadedFile("file");
        if (f == null) {
            context.json(StandardResponse.error("No uploaded file found"));
            return;
        }

        try {
            String data = IOUtils.toString(f.content(), StandardCharsets.UTF_8);
            context.json(StandardResponse.success("Success", data));
        } catch (Exception e) {
            e.printStackTrace();
            context.json(StandardResponse.error("Failed to process file: " + e.getMessage()));
        }
    }

}
