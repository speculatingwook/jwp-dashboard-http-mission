package org.apache.coyote.response;

import nextstep.util.Constant;

import java.util.ArrayList;
import java.util.List;

public class ResponseHeader {
    private List<String> header;

    private ResponseHeader() {
        header = new ArrayList<>();
    }

    public static ResponseHeader of(String viewPath, ResponseBody responseBody, HttpStatus httpStatus, String cookie) {
        ResponseHeader responseHeader = new ResponseHeader();
        responseHeader.header.add(Constant.HTTP_VERSION + " " + httpStatus.toString() + " ");
        responseHeader.header.add("Content-Type: " + convertContentType(viewPath));
        responseHeader.header.add("Content-Length: " + convertLength(responseBody));
        if (cookie != null) {
            responseHeader.header.add("Set-Cookie: " + cookie);
        }
        return responseHeader;
    }

    private static String convertLength(ResponseBody responseBody) {
        return String.valueOf(responseBody.length());
    }

    private static String convertContentType(String viewPath) {
        return ContentType.from(viewPath) + ";charset=utf-8";

    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        header.stream()
                .forEach(
                        h -> {
                            stringBuilder.append(h)
                                    .append("\n");
                        }
                );
        stringBuilder.append("\r\n");
        return stringBuilder.toString();
    }
}