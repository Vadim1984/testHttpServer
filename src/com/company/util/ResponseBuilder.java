package com.company.util;

import java.util.Date;

public class ResponseBuilder {
    public static String buildResponse(String responseStatus, String responseMessage){
        StringBuilder response = new StringBuilder();
        response.append(Constants.HTTP_VERSION);
        response.append(Constants.SPACE);
        response.append(responseStatus);
        response.append(System.lineSeparator());
        response.append(HttpResponseHeaders.CONNECTION);
        response.append(Constants.CLOSE);
        response.append(System.lineSeparator());
        response.append(HttpResponseHeaders.SERVER);
        response.append(Constants.SERVER_NAME);
        response.append(System.lineSeparator());
        response.append(HttpResponseHeaders.DATE);
        response.append(new Date());
        response.append(System.lineSeparator());
        response.append(HttpResponseHeaders.CONTENT_TYPE);
        response.append(MediaType.APPLICATION_JSON);
        response.append(System.lineSeparator());
        response.append(HttpResponseHeaders.CONTENT_LENGTH);
        if(responseMessage != null) {
            response.append(responseMessage.length());
        }
        response.append(System.lineSeparator());
        response.append(System.lineSeparator());
        if(responseMessage != null) {
            response.append(responseMessage);
        }

        return response.toString();
    }
}
