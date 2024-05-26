package dev.firstdark.configeditor.server.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StandardResponse {

    private final boolean error;
    private final String message;
    private final Object data;

    StandardResponse(boolean error, String message, Object data) {
        this.error = error;
        this.message = message;
        this.data = data;
    }

    public static StandardResponse error(String message) {
        return new StandardResponse(true, message, null);
    }

    public static StandardResponse success(String message) {
        return new StandardResponse(false, message, null);
    }

    public static StandardResponse success(String message, Object data) {
        return new StandardResponse(false, message, data);
    }

}
