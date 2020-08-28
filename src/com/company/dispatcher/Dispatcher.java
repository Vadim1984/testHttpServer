package com.company.dispatcher;

import com.company.controllers.CustomController;
import com.company.controllers.ErrorsProcessingController;
import com.company.dto.HttpRequest;
import com.company.dto.RequestMatcher;
import com.company.exceptions.ServerException;
import com.company.server.CustomHttpServer;
import com.company.util.RequestParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Dispatcher implements Runnable {
    private Socket client;
    private ErrorsProcessingController errorsProcessingController;

    public Dispatcher(Socket client) {
        this.errorsProcessingController = new ErrorsProcessingController();
        this.client = client;
    }

    @Override
    public void run() {
        try {
            BufferedReader clientBuffer = new BufferedReader(new InputStreamReader(client.getInputStream()));
            HttpRequest httpRequest = RequestParser.parse(clientBuffer);
            RequestMatcher currentRequestMatcher = new RequestMatcher(httpRequest);

            CustomController controller = CustomHttpServer.urlToControllerMapping.get(currentRequestMatcher);

            if(controller == null){
                String mappings = String.join(", ", CustomHttpServer.urlToControllerMapping.keySet().toString());
                throw new ServerException("path: " + httpRequest.getPath() + " cannot be processed. Available mappings: " + mappings);
            }

            controller.processRequest(httpRequest, client);
        } catch (IOException | ServerException e) {
            errorsProcessingController.handleError(e, client);
        }
    }
}
