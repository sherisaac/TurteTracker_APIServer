/*
 * Copyright TurtleTracker 2017
 */
package com.turtletracker.api.server;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

/**
 *
 * @author iyousuf
 */
public abstract class Handler {

    public abstract void handle(HttpExchange he, InputStream req, OutputStream res, String[] path) throws Exception;

    public abstract boolean validate(Headers headers, String[] path);

    protected String readString(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int r;
        final byte[] buf = new byte[1024];
        while ((r = in.read(buf)) != -1) {
            out.write(buf, 0, r);
        }
        out.flush();
        return new String(out.toByteArray());
    }

    protected String generateId() {
        Random r = new Random();
        String c = "abcdefghijklmnopqrstuvwxyz0123456789";
        char[] out = new char[15];
        for (int i = 0; i < 15; i++) {
            out[i] = c.charAt(r.nextInt(c.length()));
        }
        return new String(out);
    }

    protected void sendResponse(HttpExchange he, int code, String s) throws IOException {
        he.sendResponseHeaders(code, s.length());
        he.getResponseBody().write(s.getBytes());
        he.getResponseBody().close();
    }
}