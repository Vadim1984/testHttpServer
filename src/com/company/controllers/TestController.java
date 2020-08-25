package com.company.controllers;

import com.company.dto.Request;
import com.company.util.ResponseBuilder;
import com.company.util.ResponseStatus;
import com.company.exceptions.ServerException;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class TestController implements CustomController {
    @Override
    public void handle(Request request, Socket client) {
        try (DataOutputStream outputToClient = new DataOutputStream(client.getOutputStream())) {
            // 1. get data by request
            // 2. convert payload to the string
            // 3. populate response.
            String testResponseBody = "{" + "\r\n" + "\"testField\" : \"testValue\"" + "\r\n" + "}" + "\r\n";
            final String response = ResponseBuilder.buildSuccessResponse(ResponseStatus.OK, testResponseBody);
            outputToClient.writeBytes(response);
        } catch (IOException e) {
            throw new ServerException(e);
        }
    }
}
