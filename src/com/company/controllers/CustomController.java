package com.company.controllers;

import com.company.dto.HttpRequest;

import java.net.Socket;

public interface CustomController {
    void handle(HttpRequest httpRequest, Socket client);
}
