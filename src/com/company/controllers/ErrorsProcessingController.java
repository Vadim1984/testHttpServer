package com.company.controllers;

import com.company.util.ResponseBuilder;
import com.company.util.ResponseStatus;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ErrorsProcessingController {
    public void handleError(Exception ex, Socket client) {
        try (DataOutputStream outputToClient = new DataOutputStream(client.getOutputStream())) {
            String errorMessage = "{\"error\":" + "\"" + ex.getMessage() + "\"}";
            final String response = ResponseBuilder.buildResponse(ResponseStatus.BAD_REQUEST, errorMessage);
            outputToClient.writeBytes(response);
        } catch (IOException e) {
            System.out.println("An server error..." + ex.getMessage());
        }
    }
}
