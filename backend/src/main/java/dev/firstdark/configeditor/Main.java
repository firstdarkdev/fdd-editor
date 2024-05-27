package dev.firstdark.configeditor;

import dev.firstdark.configeditor.server.BackendServer;

public class Main {

    public static void main(String[] args) {
        new BackendServer(3000);
    }

}