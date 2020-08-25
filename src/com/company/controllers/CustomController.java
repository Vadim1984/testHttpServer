package com.company.controllers;

import com.company.dto.Request;
import java.net.Socket;

public interface CustomController {
    void handle(Request request, Socket client);
}
