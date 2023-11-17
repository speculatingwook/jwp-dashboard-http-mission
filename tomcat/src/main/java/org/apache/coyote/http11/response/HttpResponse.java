package org.apache.coyote.http11.response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {

    private int statusCode;
    private String statusMessage;
    private Map<String, String> headers;
    private String body;

    public HttpResponse() {
        this.headers = new HashMap<>();
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String generateHttpResponse() throws IOException {
        // 상태 라인 및 헤더
        String response = "HTTP/1.1 " + statusCode + " " + statusMessage + "\r\n";
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            response += entry.getKey() + ": " + entry.getValue() + "\r\n";
        }

        // 바디
        return response += "\r\n" + body;
    }

    // 빌더 패턴 메소드
    public HttpResponse statusCode(int statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public HttpResponse statusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
        return this;
    }

    public HttpResponse addHeader(String name, String value) {
        this.headers.put(name, value);
        return this;
    }

    public HttpResponse body(String body) {
        this.body = body;
        return this;
    }

    public HttpResponse build() {
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setStatusCode(statusCode);
        httpResponse.setStatusMessage(statusMessage);
        httpResponse.setHeaders(headers);
        httpResponse.setBody(body);
        return httpResponse;
    }
}