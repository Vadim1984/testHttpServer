package com.company.dto;

import com.company.enums.HttpMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class RequestMatcher {
    private HttpMethod httpMethod;
    private String path;
    private List<String> queryParamsNames;

    public RequestMatcher(String path, HttpMethod httpMethod, List<String> params) {
        this.path = path;
        this.httpMethod = httpMethod;
        this.queryParamsNames = params;
    }

    public RequestMatcher(HttpRequest request) {
        this.path = request.getPath();
        this.httpMethod = request.getHttpMethod();
        this.queryParamsNames = new ArrayList<>(request.getQueryParams().keySet());
    }

    public boolean match(HttpRequest request) {
        boolean isPathMatch = path.equals(request.getPath());
        boolean isHttpMethodMatch = httpMethod.equals(request.getHttpMethod());
        boolean isQueryParamsNamesMatch = false;
        Set<String> queryParametersNames = request.getQueryParams().keySet();

        if (!queryParamsNames.isEmpty() && !queryParametersNames.isEmpty() && queryParametersNames.containsAll(queryParamsNames)) {
            isQueryParamsNamesMatch = true;
        }

        return isPathMatch && isHttpMethodMatch && isQueryParamsNamesMatch;
    }

    @Override
    public String toString() {
        return "RequestMatcher{" +
                "httpMethod=" + httpMethod +
                ", path='" + path + '\'' +
                ", queryParamsNames=" + queryParamsNames +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestMatcher that = (RequestMatcher) o;
        return httpMethod == that.httpMethod &&
                Objects.equals(queryParamsNames, that.queryParamsNames);
    }

    @Override
    public int hashCode() {
        return Objects.hash(httpMethod, queryParamsNames);
    }

    public String getPath() {
        return path;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public List<String> getParams() {
        return queryParamsNames;
    }

}
