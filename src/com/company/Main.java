package com.company;

import com.company.server.CustomHttpServer;

public class Main {

    public static void main(String[] args) {
        CustomHttpServer customHttpServer = new CustomHttpServer();
        customHttpServer.startServer();
    }
}

