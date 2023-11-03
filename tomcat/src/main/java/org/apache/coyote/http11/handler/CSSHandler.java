package org.apache.coyote.http11.handler;

import org.apache.coyote.http11.Utill.FileFinder;
import org.apache.coyote.http11.httpResponse.HttpResponse;
import org.apache.coyote.http11.HttpMethod;
import org.apache.coyote.http11.httprequest.HttpRequest;

public class CSSHandler implements Handler {

    @Override
    public HttpResponse handle(HttpRequest httpRequest) {
        FileFinder fileFinder = new FileFinder();
        String responseBody = fileFinder.fromPath(httpRequest.getPath());
        HttpResponse httpResponse = HttpResponse.success(responseBody);
        httpResponse.addHeader("Content-Type", "text/css");
        return httpResponse;
    }

    @Override
    public boolean supports(HttpRequest httpRequest) {
        String path = httpRequest.getPath();
        return HttpMethod.GET == httpRequest.getMethod() && path.endsWith(".css");
    }
}