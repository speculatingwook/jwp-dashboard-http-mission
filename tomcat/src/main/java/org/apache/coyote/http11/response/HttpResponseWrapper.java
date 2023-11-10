package org.apache.coyote.http11.response;

import java.util.NoSuchElementException;

import org.apache.coyote.http11.*;
import org.apache.coyote.http11.login.LoginHandler;
import org.apache.coyote.http11.request.HttpRequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apache.coyote.http11.HttpMethod.GET;
import static org.apache.coyote.http11.HttpMethod.POST;

public class HttpResponseWrapper {

    private static final Logger log = LoggerFactory.getLogger(HttpResponseWrapper.class);

    private HttpResponseHeader header;
    private HttpResponseBody body;
    private Paths paths;
    private Cookie cookie;


    public HttpResponseWrapper(HttpRequestWrapper request) {
        try {
            parseResponse(request);
        } catch (NoSuchElementException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void parseResponse(HttpRequestWrapper request) {
        HttpMethod method = HttpMethod.valueOf(request.getMethod());
        String path= request.getPath();
        this.cookie = request.getCookie();
        switch (method) {
            case GET:
                response(path);
                break;
            case POST:
                login(path, request);
                register(path);
                break;
        }
        invalidRequest(path);
    }

    private void invalidRequest(String path) {
        if (header == null || body == null) {
            body = HttpResponseBody.of(Paths.NOT_FOUND.createPath());
            header = new HttpResponseHeader(StatusCode.NOT_FOUND.getStatus())
                    .addContentType(Paths.NOT_FOUND.getContentType())
                    .addContentLength(body.getContentLength())
                    .setCookie(cookie);
            throw new NoSuchElementException("해당 페이지를 찾을 수 없습니다: " + path);
        }
    }

    private void login(String path, HttpRequestWrapper request) {
        log.info(path);
        if (Paths.LOGIN.getPath().contains(path)) {
            LoginHandler login = new LoginHandler(request.getRequestBody().getValue("account"), request.getRequestBody().getValue("password"));
            if (login.checkUser()) {
                body = HttpResponseBody.of(Paths.INDEX.createPath());
                header = new HttpResponseHeader(StatusCode.FOUND.getStatus())
                        .addContentType(Paths.INDEX.getContentType())
                        .addContentLength(body.getContentLength())
                        .setCookie(cookie);
            }
            else{
                body = HttpResponseBody.of(Paths.UNAUTHORIZED.createPath());
                header = new HttpResponseHeader(StatusCode.UNAUTHORIZED.getStatus())
                        .addContentType(Paths.UNAUTHORIZED.getContentType())
                        .addContentLength(body.getContentLength())
                        .setCookie(cookie);
            }
        }
    }
    private void register(String path) {
            if (path.equals("/register")) {
                body = HttpResponseBody.of(Paths.INDEX.createPath());
                header = new HttpResponseHeader(StatusCode.OK.getStatus())
                        .addContentType(Paths.INDEX.getContentType())
                        .addContentLength(body.getContentLength())
                        .setCookie(cookie);
            }
    }

    private void response(String path) {
            for (Paths paths : Paths.values()) {
                String convertedPath = pathConvert(path, paths.getContentType());
                if (convertedPath.equals(paths.getPath())) {
                    body = HttpResponseBody.of(paths.createPath());
                    header = new HttpResponseHeader(StatusCode.OK.getStatus())
                            .addContentType(paths.getContentType())
                            .addContentLength(body.getContentLength())
                            .setCookie(cookie);
                }
            }
    }

    private String pathConvert(String path, String contentType) {
        if ((!path.contains(".html")) && contentType.equals("text/html")) {
            return path + ".html";
        }
        return path;
    }


    public String getResponse() {
        return String.join("\r\n", header.getHeaders(), "", body.getBodyContext());
    }
}
