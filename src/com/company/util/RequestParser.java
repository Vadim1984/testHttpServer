package com.company.util;

import com.company.dto.Request;
import com.company.enums.Method;
import com.company.exceptions.ServerException;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
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
                String path = requestParams[1];

                if (GET.equals(requestMethod) && path.contains("?")) {
                    final int beginOfPathParams = path.indexOf("?");
                    request.setQueryParams(parsePathParams(path.substring(beginOfPathParams + 1)));
                    path = path.substring(0, beginOfPathParams);
                }

                request.setPath(path);
                request.setHttpVersion(requestParams[2]);
            } else {
                throw new ServerException("illegal url request: " + Arrays.toString(requestParams));
            }

            request.setHeaders(parseHeaders(clientBuffer));
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
            if (!nextLine.isEmpty()) {
                final String[] httpParams = nextLine.split(":", 2);
                if (httpParams.length == 2) {
                    final String headerName = httpParams[0];
                    final String headerValue = httpParams[1];

                    if (headerName == null || headerName.isEmpty()) {
                        throw new ServerException("url headerName name is missed ");
                    }

                    if (headerValue == null || headerValue.isEmpty()) {
                        throw new ServerException("url headerValue value is missed ");
                    }

                    headers.put(headerName, headerValue);
                } else {
                    throw new ServerException("url header should be in format: headerName:headerValue");
                }
            }
        }

        return headers;
    }

    public static Map<String, String> parsePathParams(String pathWithParams) {
        Map<String, String> params = new HashMap<>();

        if (pathWithParams == null || pathWithParams.isEmpty())
            throw new ServerException("url parameters should be in format: parameterName=parameterValue");

        final String[] stringParams = pathWithParams.split("&");

            Stream.of(stringParams)
                    .map(string -> string.split("="))
                    .peek(RequestParser::validateRequestParameter)
                    .filter(strings -> strings.length == 2)
                    .forEach(strings -> params.put(strings[0], strings[1]));

        return params;
    }

    private static void validateRequestParameter(String[] strings) {
        if (strings.length != 2)
            throw new ServerException("illegal state of url parameter: " + Arrays.toString(strings) + ", url parameters should be in format: parameterName=parameterValue");

        final String parameterName = strings[0];
        final String parameterValue = strings[1];

        if (parameterName == null || parameterName.isEmpty())
            throw new ServerException("url parameter name is missed");

        if (parameterValue == null || parameterValue.isEmpty())
            throw new ServerException("url parameter value is missed");
    }
}
