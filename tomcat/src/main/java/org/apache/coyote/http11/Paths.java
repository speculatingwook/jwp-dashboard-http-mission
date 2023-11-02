package org.apache.coyote.http11;

public enum Paths {
    INDEX("/index.html", "text/html"),
    CSS("/css/styles.css", "text/css"),
    LOGIN("/login.html", "text/html");

    private final String path;
    private final String contentType;
    Paths(String path, String contentType) {
        this.path = path;
        this.contentType = contentType;
    }

    public String getPath() {
        return path;
    }

    public String createPath() {
        return "static" + path;
    }

    public String getContentType() {
        return contentType;
    }

}