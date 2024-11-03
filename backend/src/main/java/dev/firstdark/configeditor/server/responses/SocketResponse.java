package dev.firstdark.configeditor.server.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(staticName = "of")
@Getter
public class SocketResponse {

    private String socketCode;
    private String message;

}
