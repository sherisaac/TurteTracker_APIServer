/*
 * Copyright TurtleTracker 2017
 */
package com.turtletracker.api.server.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;

/**
 *
 * @author IkeOTL
 */
public class HTTPSConfirmHandler implements HttpHandler {

    private final String retString;

    public HTTPSConfirmHandler(String retString) {
        this.retString = retString;
    }

    @Override
    public void handle(HttpExchange he) throws IOException {
        he.getResponseHeaders().add("Content-type", "text/plain");
        sendResponse(he, 200, retString);
    }

    protected void sendResponse(HttpExchange he, int code, String s) throws IOException {
        he.sendResponseHeaders(code, s.length());
        he.getResponseBody().write(s.getBytes());
        he.getResponseBody().close();
    }
}
