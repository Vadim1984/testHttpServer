package com.company.dispatcher;

import com.company.controllers.CustomController;
import com.company.controllers.ErrorsProcessingController;
import com.company.controllers.TestController;
import com.company.dto.Request;
import com.company.exceptions.ServerException;
import com.company.util.RequestParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.joining;

public class Dispatcher implements Runnable {
    private Socket client;
    private Map<String, CustomController> urlToControllerMapping;
    private ErrorsProcessingController errorsProcessingController;


    public Dispatcher(Socket client) {
        this.errorsProcessingController = new ErrorsProcessingController();
        urlToControllerMapping = new HashMap<>();
        urlToControllerMapping.put("/test", new TestController());
        this.client = client;
    }

    @Override
    public void run() {
        try {
            BufferedReader clientBuffer = new BufferedReader(new InputStreamReader(client.getInputStream()));
            Request request = RequestParser.parse(clientBuffer);

            final boolean controllerWasFound = urlToControllerMapping.entrySet().stream()
                    .anyMatch(entry -> entry.getKey().equals(request.getPath()));

            final String mappings = urlToControllerMapping.entrySet().stream()
                    .map(Map.Entry::getKey)
                    .collect(joining(", "));

            if(!controllerWasFound){
                throw new ServerException("path: " + request.getPath() + " cannot be processed. Available mappings: " + mappings);
            }

            urlToControllerMapping.entrySet().stream()
                    .filter(entry -> entry.getKey().equals(request.getPath()))
                    .forEach(entry -> entry.getValue().handle(request, client));

        } catch (IOException e) {
            throw new ServerException(e);
        } catch (ServerException e) {
            errorsProcessingController.handleError(e, client);
        }
    }
}
