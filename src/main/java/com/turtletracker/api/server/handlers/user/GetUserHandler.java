/*
 * Copyright TurtleTracker 2017
 */
package com.turtletracker.api.server.handlers.user;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.turtletracker.api.server.DatabaseConnection;
import com.turtletracker.api.server.Handler;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Ike [Admin@KudoDev.com]
 */
public class GetUserHandler extends Handler {

    //private static final Logger logger = Logger.getLogger(GetUserHandler.class.getName());

    @Override
    public void handle(HttpExchange he, InputStream req, OutputStream res, String[] path) throws Exception {

        Connection con = DatabaseConnection.getConnection();
        if (path.length == 4) {
            JSONObject json = new JSONObject();
            String username = path[3];
            String userId = getUserId(username);
            json.put("username", username);
            json.put("userId", userId);
            try (PreparedStatement stmt = con.prepareStatement("SELECT `firstName`, `lastName`, `role`, `createDate` FROM user WHERE userId = ? AND `role` > 0 LIMIT 1")) {
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
            return;
        }

        JSONArray users = new JSONArray();
        try (PreparedStatement stmt = con.prepareStatement("SELECT `userId`, `firstName`, `lastName`, `role`, `username` FROM user WHERE `role` > 0")) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    JSONObject j = new JSONObject();
                    j.put("userId", rs.getString(1));
                    j.put("firstName", rs.getString(2));
                    j.put("lastName", rs.getString(3));
                    j.put("role", rs.getInt(4));
                    j.put("username", rs.getString(5));
                    users.put(j);
                }
            }
        }

        sendResponse(he, 200, users.toString());

    }

    @Override
    public boolean validate(Headers headers, String[] path) {
        return path.length == 4 || path.length == 3;
    }
}
