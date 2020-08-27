package com.company;

import com.company.controllers.CustomController;
import com.company.controllers.ErrorsProcessingController;
import com.company.controllers.TestController;
import com.company.dispatcher.Dispatcher;
import com.company.exceptions.ServerException;
import com.company.util.Constants;
import com.company.util.ServerConfig;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Main {

    public static Map<String, CustomController> urlToControllerMapping = new LinkedHashMap<>();

    public static void main(String[] args) {
        init();
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
        try {
            ServerSocket server = new ServerSocket(ServerConfig.LOCAL_HOST_PORT, ServerConfig.BACKLOG, InetAddress.getByName(ServerConfig.LOCAL_HOST_IP_ADDRESS));
            System.out.println("TCP Server Waiting for client on port 5000");

            while (true) {
                Socket connected = server.accept();
                Runnable dispatcher = new Dispatcher(connected);
                executor.execute(dispatcher);
                System.out.println("Thread pull size: " + executor.getPoolSize());
                System.out.println("Thread pull queue: " + executor.getQueue().size());
            }

        } catch (IOException | ServerException e) {
            System.out.println(e.getMessage());
        } finally {
            executor.shutdown();
        }
    }

    private static void init(){
        urlToControllerMapping.put("/test", new TestController());
    }
}

