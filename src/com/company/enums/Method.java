package com.company.enums;

public enum Method {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE");

    private String method;

    Method(String method){
        this.method = method;
    }
}
