/*
 * Copyright TurtleTracker 2017
 */
package com.turtletracker.api.server.handlers.user;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.turtletracker.api.server.AuthenticatedHandler;
import com.turtletracker.api.server.DatabaseConnection;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Ike [Admin@KudoDev.com]
 */
public class RegisterUserHandler extends AuthenticatedHandler {

    @Override
    public void handle(HttpExchange he, InputStream req, OutputStream res, String[] path) throws Exception {
        JSONObject json = new JSONObject(readString(req));
        Connection con = DatabaseConnection.getConnection();

        String userId = getUserId(json.getString("username"));

        if (userId != null) {
            sendResponse(he, 409, "{\"err\":\"User: " + json.getString("username") + " already exists...\"}");
            return;
        }

        userId = generateId();
        try (PreparedStatement stmt = con.prepareStatement("INSERT INTO user (`userId`, `username`, `password`, `firstName`, `lastName`, `role`) VALUES (?, ?, ?, ?, ?, ?)")) {
            stmt.setString(1, userId);
            stmt.setString(2, json.getString("username"));
            stmt.setString(3, getPassHash(json.getString("password")));
            stmt.setString(4, json.getString("firstName"));
            stmt.setString(5, json.getString("lastName"));
            stmt.setInt(6, json.getInt("role"));
            stmt.execute();
        }

        sendResponse(he, 201, "{\"userId\":\"" + userId + "\"}");
    }

    @Override
    public boolean validate(Headers headers, String[] path) {
        return path.length == 3 && validateAdmin(headers.get("Authorization").get(0));
    }

}
