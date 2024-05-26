package dev.firstdark.configeditor;

import dev.firstdark.configeditor.server.BackendServer;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        new BackendServer(3000);
    }




}