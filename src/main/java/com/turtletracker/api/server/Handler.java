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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author iyousuf
 */
public abstract class Handler {

    public abstract void handle(HttpExchange he, InputStream req, OutputStream res, String[] path) throws Exception;

    public abstract boolean validate(Headers headers, String[] path);

    protected Map<String, String> getQueryMap(String query) {
        String[] params = query.split("&");
        Map<String, String> map = new HashMap<>();
        for (String param : params) {
            String[] p = param.split("=");
            String name = p[0];
            String value = p.length == 2 ? p[1] : null;
            map.put(name, value);
        }
        return map;
    }

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

    protected String getUserId(String username) {
        Connection con = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = con.prepareStatement("SELECT `userId` FROM user WHERE `username` = ? LIMIT 1")) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString(1);
                }
            }
        } catch (Exception ex) {
        }
        return null;
    }

    protected String generateId() {
        Random r = new Random();
        String c = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
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
