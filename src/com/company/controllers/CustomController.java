package com.company.controllers;

import com.company.dto.HttpRequest;
import com.company.dto.RequestMatcher;

import java.net.Socket;

public interface CustomController {
    RequestMatcher getRequestMatcher();
    void handle(HttpRequest httpRequest, Socket client);
}
