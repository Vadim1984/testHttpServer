package com.company.controllers;

import com.company.dto.HttpRequest;
import com.company.dto.RequestMatcher;
import com.company.enums.HttpMethod;
import com.company.util.ResponseBuilder;
import com.company.util.ResponseStatus;
import com.company.exceptions.ServerException;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.stream.Collectors;

public class TestController implements CustomController {

    @Override
    public void handle(HttpRequest httpRequest, Socket client) {
        try (DataOutputStream outputToClient = new DataOutputStream(client.getOutputStream())) {
            // 1. get data by request
            // 2. convert payload to the string
            // 3. populate response.
            String requestParams = httpRequest.getQueryParams().entrySet().stream()
                    .map(entry -> "\"" + entry.getKey() + "\":\"" + entry.getValue() + "\"")
                    .collect(Collectors.joining(","));
            String testResponseMessage = "{" + requestParams + "}";
            final String response = ResponseBuilder.buildResponse(ResponseStatus.OK, testResponseMessage);
            outputToClient.writeBytes(response);
        } catch (IOException e) {
            throw new ServerException(e);
        }
    }

    @Override
    public RequestMatcher getRequestMatcher() {
        return new RequestMatcher("/test", HttpMethod.GET, Arrays.asList("param"));
    }
}
