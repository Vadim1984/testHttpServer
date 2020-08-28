package com.company.controllers;

import com.company.dto.HttpRequest;
import com.company.dto.RequestMatcher;
import com.company.exceptions.ServerException;

import java.net.Socket;

public interface CustomController {

    default void processRequest(HttpRequest httpRequest, Socket client){
        if(isSupport(httpRequest)){
            handle(httpRequest, client);
        }
    }

    default boolean isSupport(HttpRequest httpRequest) {
        final RequestMatcher requestMatcher = getRequestMatcher();

        if(!requestMatcher.match(httpRequest)){
            throw new ServerException("request not supported, required parameters: " + requestMatcher);
        }

        return true;
    }

    RequestMatcher getRequestMatcher();
    void handle(HttpRequest httpRequest, Socket client);
}
