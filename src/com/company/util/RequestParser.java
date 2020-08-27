package com.company.util;

import com.company.dto.HttpRequest;
import com.company.enums.HttpMethod;
import com.company.exceptions.ServerException;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static com.company.enums.HttpMethod.GET;

public class RequestParser {
    public static HttpRequest parse(BufferedReader clientBuffer) {
        HttpRequest httpRequest = new HttpRequest();

        try {
            String firstRequestString = clientBuffer.readLine();
            final String[] generalRequestParams = firstRequestString.split(" ", 3);

            if (generalRequestParams.length == 3) {
                final HttpMethod requestHttpMethod = HttpMethod.valueOf(generalRequestParams[0]);
                httpRequest.setHttpMethod(requestHttpMethod);
                String path = generalRequestParams[1];

                if (GET.equals(requestHttpMethod) && path.contains("?")) {
                    final int beginOfPathParams = path.indexOf("?");
                    httpRequest.setQueryParams(parsePathParams(path.substring(beginOfPathParams + 1)));
                    path = path.substring(0, beginOfPathParams);
                }

                httpRequest.setPath(path);
                httpRequest.setHttpVersion(generalRequestParams[2]);
            } else {
                throw new ServerException("illegal url request: " + Arrays.toString(generalRequestParams));
            }

            httpRequest.setHeaders(parseHeaders(clientBuffer));
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServerException(e);
        }

        return httpRequest;
    }

    private static Map<String, String> parseHeaders(BufferedReader clientBuffer) throws IOException {
        Map<String, String> headers = new HashMap<>();

        while (clientBuffer.ready()) {
            String nextRequestLine = clientBuffer.readLine();
            if (!nextRequestLine.isEmpty()) {
                String[] httpHeaderNameAndValue = nextRequestLine.split(":", 2);
                validateHeader(httpHeaderNameAndValue);
                String headerName = httpHeaderNameAndValue[0];
                String headerValue = httpHeaderNameAndValue[1];
                headers.put(headerName, headerValue);
            }
        }

        return headers;
    }

    private static Map<String, String> parsePathParams(String pathParams) {
        Map<String, String> params = new HashMap<>();

        if (pathParams == null || pathParams.isEmpty())
            throw new ServerException("url parameters should be in format: parameterName=parameterValue");

        final String[] stringParams = pathParams.split("&");

        Stream.of(stringParams)
                .map(string -> string.split("="))
                .peek(RequestParser::validateRequestParameter)
                .filter(strings -> strings.length == 2)
                .forEach(strings -> params.put(strings[0], strings[1]));

        return params;
    }

    private static void validateRequestParameter(String[] parameterNameAndValue) {
        if (parameterNameAndValue.length != 2)
            throw new ServerException("illegal state of url parameter: " + Arrays.toString(parameterNameAndValue) + ", url parameters should be in format: parameterName=parameterValue");

        String parameterName = parameterNameAndValue[0];
        String parameterValue = parameterNameAndValue[1];

        if (parameterName == null || parameterName.isEmpty())
            throw new ServerException("url parameter name is missed");

        if (parameterValue == null || parameterValue.isEmpty())
            throw new ServerException("url parameter value is missed");
    }

    private static void validateHeader(String[] httpHeaderNameAndValue) {
        if (httpHeaderNameAndValue.length != 2)
            throw new ServerException("illegal state of request header: " + Arrays.toString(httpHeaderNameAndValue) +", url header should be in format: headerName:headerValue");

        String headerName = httpHeaderNameAndValue[0];
        String headerValue = httpHeaderNameAndValue[1];

        if (headerName == null || headerName.isEmpty())
            throw new ServerException("url headerName name is missed ");

        if (headerValue == null || headerValue.isEmpty())
            throw new ServerException("url headerValue value is missed ");
    }
}
