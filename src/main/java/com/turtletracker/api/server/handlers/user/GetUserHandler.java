/*
 * Copyright TurtleTracker 2017
 */
package com.turtletracker.api.server.handlers.user;

import com.turtletracker.api.server.handlers.photo.*;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.turtletracker.api.server.DatabaseConnection;
import com.turtletracker.api.server.Handler;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Logger;
import org.json.JSONObject;

/**
 *
 * @author Ike [Admin@KudoDev.com]
 */
public class GetUserHandler extends Handler {

    private static final Logger logger = Logger.getLogger(GetUserHandler.class.getName());

    @Override
    public void handle(HttpExchange he, InputStream req, OutputStream res, String[] path) throws Exception {
        String username = path[3];
        String userId = getUserId(username);

        if (userId == null) {
            sendResponse(he, 404, "{\"err\":\"" + username + ": Not found...\"}");
            return;
        }

        Connection con = DatabaseConnection.getConnection();
        JSONObject json = new JSONObject();
        json.put("username", username);
        json.put("userId", userId);
        try (PreparedStatement stmt = con.prepareStatement("SELECT `firstName`, `lastName`, `role`, `createDate` FROM user WHERE userId = ? LIMIT 1")) {
            stmt.setString(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                json.put("firstName", rs.getString(1));
                json.put("lastName", rs.getString(2));
                json.put("role", rs.getInt(3));
                json.put("createDate", rs.getString(4));
            }
        }

        sendResponse(he, 200, json.toString());
    }

    @Override
    public boolean validate(Headers headers, String[] path) {
        return path.length == 4;
    }
}
