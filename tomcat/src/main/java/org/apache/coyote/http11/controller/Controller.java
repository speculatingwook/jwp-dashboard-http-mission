package org.apache.coyote.http11.controller;

import org.apache.coyote.http11.request.HttpRequest;
import org.apache.coyote.http11.response.HttpResponse;

import java.io.IOException;

public interface Controller {
    HttpResponse handleRequest(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException;
}