package dev.firstdark.configeditor.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dev.firstdark.configeditor.converter.ConfigConverter;
import dev.firstdark.configeditor.server.responses.SocketResponse;
import dev.firstdark.configeditor.server.responses.StandardResponse;
import dev.firstdark.configeditor.util.EncryptionUtil;
import io.javalin.Javalin;
import io.javalin.websocket.*;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class WebsocketServer {

    private final HashMap<String, WsContext> sessions = new HashMap<>();
    private final HashMap<String, WsContext> frontendSessions = new HashMap<>();

    private final Gson gson = new GsonBuilder().serializeNulls().create();

    public WebsocketServer(Javalin app) {
        app.ws("/ws/config", ws-> {
            ws.onConnect(this::onSocketConnect);
            ws.onMessage(this::onSocketMessage);
            ws.onClose(this::onSocketClose);
        });

        app.ws("/ws/frontend", ws-> {
            ws.onConnect(this::onFrontendSocketConnect);
            ws.onMessage(this::onFrontendSocketMessage);
            ws.onClose(this::onFrontendSocketClose);
        });
    }

    public void sendConfig(String config, String identifier) {
        if (!sessions.containsKey(identifier))
            return;

        EncryptionUtil ec = new EncryptionUtil(identifier);
        sessions.get(identifier).send(SocketResponse.of("WS_SAVE_CONFIG", ec.encrypt(config)));
    }

    private void onFrontendSocketClose(WsCloseContext ctx) {
        String id = ctx.queryParam("identifier");
        frontendSessions.remove(id);

        if (sessions.containsKey(id)) {
            sessions.get(id).closeSession(WsCloseStatus.NORMAL_CLOSURE, "WS_SESSION_TERMINATED");
        }
    }

    private void onFrontendSocketMessage(WsMessageContext ctx) {
        if (ctx.message().equalsIgnoreCase("ping"))
            return;

        String id = ctx.queryParam("identifier");

        if (id == null || id.isEmpty())
            return;

        SocketResponse r = gson.fromJson(ctx.message(), SocketResponse.class);

        if (r.getSocketCode().equalsIgnoreCase("WS_CHECK_SESSION")) {
            boolean valid = sessions.containsKey(id);
            ctx.send(gson.toJson(SocketResponse.of(valid? "WS_VALID_SESSION" : "WS_INVALID_SESSION", "")));
            return;
        }

        if (r.getSocketCode().equalsIgnoreCase("WS_GET_CONFIG") && sessions.containsKey(id)) {
            sessions.get(id).send(SocketResponse.of("WS_GET_CONFIG", ""));
        }
    }

    private void onFrontendSocketConnect(WsConnectContext ctx) {
        String identifier = ctx.queryParam("identifier");

        if (identifier == null || identifier.isEmpty()) {
            ctx.closeSession(WsCloseStatus.UNSUPPORTED_DATA, "Invalid connection request");
            return;
        }

        frontendSessions.put(identifier, ctx);
        ctx.send(gson.toJson(SocketResponse.of("WS_CONNECTED", "")));
    }

    private void onSocketClose(WsCloseContext ctx) {
        sessions.values().removeIf(context -> context.equals(ctx));

        if (frontendSessions.containsKey(ctx.queryParam("identifier"))) {
            frontendSessions.get(ctx.queryParam("identifier")).closeSession(WsCloseStatus.NORMAL_CLOSURE, "WS_SESSION_TERMINATED");
        }
    }

    private void onSocketMessage(WsMessageContext ctx) {
        String id = ctx.queryParam("identifier");

        if (id == null || id.isEmpty()) {
            return;
        }

        SocketResponse r = gson.fromJson(ctx.message(), SocketResponse.class);

        if (r.getSocketCode().equalsIgnoreCase("WS_SEND_CONFIG")) {
            EncryptionUtil ec = new EncryptionUtil(id);
            // TODO: Send file name from server
            StandardResponse rr = handleUpload(ec.decrypt(r.getMessage()), "simple-discord-link.toml");

            WsContext frontendSession = frontendSessions.get(id);

            if (frontendSession != null) {
                SocketResponse response;

                if (rr.isError()) {
                    response = SocketResponse.of("WS_CONFIG_ERROR", rr.getMessage());
                } else {
                    response = SocketResponse.of("WS_SEND_CONFIG", gson.toJson(rr.getData()));
                }

                frontendSession.send(gson.toJson(response));
            }
        }
    }

    private void onSocketConnect(WsConnectContext ctx) {
        String identifier = ctx.queryParam("identifier");

        if (identifier == null || identifier.isEmpty()) {
            ctx.closeSession(WsCloseStatus.UNSUPPORTED_DATA, "Invalid connection request");
            return;
        }

        sessions.put(identifier, ctx);
        ctx.send(gson.toJson(SocketResponse.of("WS_WAITING", "")));
    }

    private StandardResponse handleUpload(String content, String fileName) {
        try {
            ConfigConverter.INSTANCE.getTomlParser().parse(content);
        } catch (Exception e) {
            return StandardResponse.error("Unsupported or corrupt file");
        }

        try {
            JsonObject o = ConfigConverter.INSTANCE.readToJson(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)));
            o.addProperty("filename", fileName);
            return StandardResponse.success("Success", o);
        } catch (Exception e) {
            e.printStackTrace();
            return StandardResponse.error("Failed to process file: " + e.getMessage());
        }
    }

}
