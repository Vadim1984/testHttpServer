package com.company.util;

import java.util.Date;

public class ResponseBuilder {
    public static String buildSuccessResponse(String responseStatus, String responseBody){
        StringBuilder response = new StringBuilder();

        response.append(responseStatus);
        response.append(HttpResponseHeaders.NEW_LINE);
        response.append(HttpResponseHeaders.CONNECTION);
        response.append(HttpResponseHeaders.CLOSE);
        response.append(HttpResponseHeaders.NEW_LINE);
        response.append(HttpResponseHeaders.SERVER);
        response.append("my custom server");
        response.append(HttpResponseHeaders.NEW_LINE);
        response.append(HttpResponseHeaders.DATE);
        response.append(new Date());
        response.append(HttpResponseHeaders.NEW_LINE);
        response.append(HttpResponseHeaders.CONTENT_TYPE);
        response.append(MediaType.APPLICATION_JSON);
        response.append(HttpResponseHeaders.NEW_LINE);
        response.append(HttpResponseHeaders.CONTENT_LENGTH);
        if(responseBody != null) {
            response.append(responseBody.length());
        }
        response.append(HttpResponseHeaders.NEW_LINE);
        response.append(HttpResponseHeaders.NEW_LINE);
        if(responseBody != null) {
            response.append(responseBody);
        }

        return response.toString();
    }
}
