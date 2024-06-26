package dev.firstdark.configeditor.server;

import com.google.gson.JsonObject;
import dev.firstdark.configeditor.Main;
import dev.firstdark.configeditor.converter.ConfigConverter;
import dev.firstdark.configeditor.server.responses.StandardResponse;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.UploadedFile;
import io.javalin.http.staticfiles.Location;
import io.javalin.json.JavalinGson;
import io.javalin.plugin.bundled.CorsPluginConfig;
import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BOMInputStream;

import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;

public class BackendServer {

    public BackendServer(int port) {
        Javalin javalin = Javalin.create(config -> {
            config.staticFiles.add("web", Location.EXTERNAL);
            config.bundledPlugins.enableCors(cors -> cors.addRule(CorsPluginConfig.CorsRule::anyHost));
            config.jsonMapper(new JavalinGson());
        });

        javalin.unsafeConfig().router.apiBuilder(() -> path("/v1", () -> {
           post("/parseupload", this::handleUpload);
           post("/saveconfig", this::handleSave);
        }));

        javalin.start(port);
    }

    private void handleSave(Context context) {
        try {
            JsonObject o = context.bodyAsClass(JsonObject.class);
            String config = ConfigConverter.INSTANCE.writeToToml(o);
            context.json(StandardResponse.success("Success", config));
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

}
