package org.apache.coyote.http11.controller;

import org.apache.coyote.http11.Service.RegisterService;
import org.apache.coyote.http11.request.HttpRequest;
import org.apache.coyote.http11.response.ContentType;
import org.apache.coyote.http11.response.HttpMethod;
import org.apache.coyote.http11.response.HttpResponse;
import org.apache.coyote.http11.response.HttpStatusCode;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class RegisterController implements Controller{
    private static final String INDEX_PAGE_URL = "index.html";
    private static final String REGISTER_PAGE_URL = "static/register.html";
    private static final String NOT_FOUND_PAGE_URL = "static/404.html";

    private RegisterService registerService = new RegisterService();
    @Override
    public HttpResponse handleRequest(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        String requestUri = httpRequest.getPath();
        String httpMethod = httpRequest.getMethod();

        URL resource = getClass()
                .getClassLoader()
                .getResource(REGISTER_PAGE_URL);

        File file = new File(resource.getFile());

        String responseBody = new String(Files.readAllBytes(file.toPath()));

        if(httpMethod.equals(HttpMethod.GET.getMethod())) {
            return httpResponse = new HttpResponse()
                    .statusCode(HttpStatusCode.OK.getCode())
                    .statusMessage(HttpStatusCode.OK.getMessage())
                    .addHeader("Content-Type", ContentType.findContentTypeFromUri(requestUri).getContentType())
                    .addHeader("Content-Length", String.valueOf(responseBody.getBytes().length))
                    .body(responseBody)
                    .build();

        } else if(httpMethod.equals(HttpMethod.POST.getMethod())) {
            String requestBody = httpRequest.getBody();
            Map<String, String> queryParameters = parseQueryParameters(requestBody);
            String account = queryParameters.get("account");
            String password = queryParameters.get("password");
            String email = queryParameters.get("email");

            if(registerService.register(account, password, email)) {
                return httpResponse = new HttpResponse()
                        .statusCode(HttpStatusCode.FOUND.getCode())
                        .statusMessage(HttpStatusCode.FOUND.getMessage())
                        .addHeader("Location", INDEX_PAGE_URL)
                        .build();
            }
        }
        return httpResponse = new HttpResponse()
                .statusCode(HttpStatusCode.NOT_FOUND.getCode())
                .statusMessage(HttpStatusCode.NOT_FOUND.getMessage())
                .addHeader("Location", NOT_FOUND_PAGE_URL)
                .build();
    }

    public Map<String, String> parseQueryParameters(String query) throws UnsupportedEncodingException {
        query = query.replace("\r\n", "");
        String[] params = query.split("&");

        Map<String, String> queryParameters = new HashMap<>();

        for (String param : params) {
            String[] keyValue = param.split("=");

            if (keyValue.length == 2) {
                String key = java.net.URLDecoder.decode(keyValue[0], "UTF-8");
                String value = java.net.URLDecoder.decode(keyValue[1], "UTF-8");
                queryParameters.put(key, value);
            }
        }
        return queryParameters;
    }
}

