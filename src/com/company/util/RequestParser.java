package com.company.util;

import com.company.dto.Request;
import com.company.enums.Method;
import com.company.exceptions.ServerException;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static com.company.enums.Method.GET;

public class RequestParser {
    public static Request parse(BufferedReader clientBuffer) {
        Request request = new Request();

        try {
            String requestString = clientBuffer.readLine();
            final String[] requestParams = requestString.split(" ", 3);

            if (requestParams.length == 3) {
                final Method requestMethod = Method.valueOf(requestParams[0]);
                request.setMethod(requestMethod);
                final String pathWithParams = requestParams[1];
                String path = requestParams[1];

                if (GET.equals(requestMethod) && path.contains("?")) {
                    final int beginOfPathParams = path.indexOf("?");
                    request.setQueryParams(parsePathParams(path.substring(beginOfPathParams + 1)));
                    path = path.substring(0, beginOfPathParams);
                }
                request.setPath(path);
                request.setHttpVersion(requestParams[2]);
            }

            request.setHeaders(parseHeaders(clientBuffer));
            String headerLine = requestString;
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServerException(e);
        }

        return request;
    }

    public static Map<String, String> parseHeaders(BufferedReader clientBuffer) throws IOException {
        Map<String, String> headers = new HashMap<>();

        while (clientBuffer.ready()) {
            String nextLine = clientBuffer.readLine();
            if (nextLine != "") {
                final String[] httpParams = nextLine.split(":", 2);
                if (httpParams.length == 2) {
                    headers.put(httpParams[0], httpParams[1]);
                }
            }
        }

        return headers;
    }

    public static Map<String, String> parsePathParams(String pathWithParams) throws IOException {
        Map<String, String> params = new HashMap<>();

        final String[] stringParams = pathWithParams.split("&");

        Stream.of(stringParams)
                .map(string -> string.split("="))
                .filter(strings -> strings.length == 2)
                .forEach(strings -> params.put(strings[0], strings[1]));

        return params;
    }
}
