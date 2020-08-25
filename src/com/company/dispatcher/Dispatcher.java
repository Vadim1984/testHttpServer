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
            CustomController controller = urlToControllerMapping.get(request.getPath());

            if(controller == null){
                final String mappings = String.join(", ", urlToControllerMapping.keySet());
                throw new ServerException("path: " + request.getPath() + " cannot be processed. Available mappings: " + mappings);
            }

            controller.handle(request, client);
        } catch (IOException | ServerException e) {
            errorsProcessingController.handleError(e, client);
        }
    }
}
