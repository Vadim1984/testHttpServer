package com.company;

import com.company.dispatcher.Dispatcher;
import com.company.exceptions.ServerException;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(5000, 10, InetAddress.getByName("127.0.0.1"));
            System.out.println("TCPServer Waiting for client on port 5000");

            while (true) {
                Socket connected = server.accept();
                Runnable dispatcher = new Dispatcher(connected);
                new Thread(dispatcher).start();
            }

        } catch (IOException | ServerException e) {
            System.out.println(e.getMessage());
        }
    }
}

