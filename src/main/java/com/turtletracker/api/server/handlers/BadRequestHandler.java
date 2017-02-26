/*
 * Copyright TurtleTracker 2017
 */
package com.turtletracker.api.server.handlers;

import com.sun.net.httpserver.Headers;
import com.turtletracker.api.server.Handler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.json.JSONObject;

/**
 *
 * @author iyousuf
 */
public class BadRequestHandler extends Handler {

    @Override
    public void handle(HttpExchange he, InputStream req, OutputStream res, String[] path) throws Exception {
        String s = "Bad request";
        he.sendResponseHeaders(400, s.length());
        res.write(s.getBytes());
    }

    @Override
    public boolean validate(Headers headers, String[] path) {
        return true;
    }

}
