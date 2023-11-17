package org.apache.coyote.response;

public enum StatusCode {

    OK("200 OK"),
    FOUND("302 Found"),
    UNAUTHORIZED("401 Unauthorized"),
    NOT_FOUND("404 Not Found");


    private final String statusCode;

    StatusCode(final String statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public String toString() {
        return this.statusCode;
    }
}