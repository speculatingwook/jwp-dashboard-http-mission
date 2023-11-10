package org.apache.coyote.http11.response;

import org.apache.coyote.http11.Cookie;

import java.util.ArrayList;
import java.util.List;

public class HttpResponseHeader {
    private final List<String> headers;

    public HttpResponseHeader(String status) {
        this.headers = new ArrayList<>();
        this.headers.add("HTTP/1.1 " + status + " ");
    }

    public String getHeaders() {
        return String.join("\r\n", headers);
    }

    public HttpResponseHeader addContentType(String contentType) {
        headers.add("Content-Type: " + contentType + ";charset=utf-8 ");
        return this;
    }

    public HttpResponseHeader addContentLength(int contentLength) {
        headers.add("Content-Length: " + contentLength + " ");
        return this;
    }

    public HttpResponseHeader setCookie(Cookie cookie) {
        if (!cookie.isKeyExist("JSESSIONID")) {
            cookie.setJSessionId();
            headers.add("Set-Cookie: " + cookie.toRaw("JSESSIONID"));
        }
        return this;
    }

}
