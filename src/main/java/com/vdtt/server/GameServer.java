package com.vdtt.server;

public class GameServer {

    public static boolean isStop;

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(Server::stop));
        Server.init();
        Server.start();
    }
}
