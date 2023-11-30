package org.apache.coyote.http11.controller;

import org.apache.coyote.http11.HttpResponse;
import org.apache.coyote.http11.enums.ContentType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;


public abstract class AbstractController implements Controller {

    private static final Map<String, ContentType> CONTENT_TYPES = new HashMap<>();

    static {
        // 일반적인 파일 확장자에 대한 MIME 타입 매핑
        CONTENT_TYPES.put("html", ContentType.HTML);
        CONTENT_TYPES.put("css", ContentType.CSS);
        CONTENT_TYPES.put("js", ContentType.JS);
    }
    public void getStaticResourceFile(String path, HttpResponse httpResponse) throws IOException {
        File resourceFile = new File("/Users/hayoon/spring/jwp-dashboard-http-mission/tomcat/src/main/resources/static" + path);
        if (resourceFile.exists()) {
            byte[] content = Files.readAllBytes(Path.of(resourceFile.getAbsolutePath()));
            String responseBody = new String(content);
            String fileExtension = getFileExtension(path);
            httpResponse.setResponseBody(responseBody);
            httpResponse.setStatusCode(200); // HTTP 상태코드 200 (OK)
            httpResponse.setContentType(CONTENT_TYPES.get(fileExtension));
        } else {
            if(path.equals("/")) {
                httpResponse.setResponseBody("Hello world!");
                httpResponse.setContentType(ContentType.HTML);
                httpResponse.setStatusCode(200);
            } else {
                String responseBody = "Resource not found.";
                httpResponse.setResponseBody(responseBody);
                httpResponse.setStatusCode(404); // HTTP 상태코드 404 (Not Found)
                httpResponse.setContentType(ContentType.HTML);
            }
        }
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex != -1 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1).toLowerCase();
        }
        return "";
    }

    public void redirectToHome(String location, HttpResponse httpResponse) {
        httpResponse.setStatusCode(302);
        httpResponse.addHeader("Location", location);
        httpResponse.setContentType(ContentType.HTML);
    }

}