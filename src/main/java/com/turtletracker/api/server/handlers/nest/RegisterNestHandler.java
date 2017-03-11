/*
 * Copyright TurtleTracker 2017
 */
package com.turtletracker.api.server.handlers.nest;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.turtletracker.api.server.AuthenticatedHandler;
import com.turtletracker.api.server.DatabaseConnection;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Ike [Admin@KudoDev.com]
 */
public class RegisterNestHandler extends AuthenticatedHandler {

    @Override
    public void handle(HttpExchange he, InputStream req, OutputStream res, String[] path) throws Exception {
        JSONObject json = new JSONObject(readString(req));
        Connection con = DatabaseConnection.getConnection();

        String nestId = generateId();
        int groupId = 0;
        if (json.keySet().contains("groupId")) {
            groupId = json.getInt("groupId");
        }
        if (json.keySet().contains("location")) {
            JSONObject location = json.getJSONObject("location");
            try (PreparedStatement stmt = con.prepareStatement("INSERT INTO nest (`nestId`, `groupId`, `longitude`, `latitude`) VALUES (?, ?, ?, ?)")) {
                stmt.setString(1, nestId);
                stmt.setInt(2, groupId);
                stmt.setDouble(3, location.getDouble("longitude"));
                stmt.setDouble(4, location.getDouble("latitude"));
                stmt.execute();
            }
        } else {
            try (PreparedStatement stmt = con.prepareStatement("INSERT INTO nest (`nestId`, `groupId`) VALUES (?, ?)")) {
                stmt.setString(1, nestId);
                stmt.setInt(2, groupId);
                stmt.execute();
            }
        }

        if (json.keySet().contains("photos")) {
            JSONArray photos = json.getJSONArray("photos");
            try (PreparedStatement stmt = con.prepareStatement("UPDATE photo SET `nestId` = ? WHERE `photoId` = ?")) {
                for (int i = 0; i < photos.length(); i++) {
                    String photoId = photos.getString(i);
                    stmt.setString(1, nestId);
                    stmt.setString(2, photoId);
                    stmt.addBatch();
                }
                stmt.executeBatch();
            }
        }

        sendResponse(he, 200, "{\"nestId\":\"" + nestId + "\"}");
    }

    @Override
    public boolean validate(Headers headers, String[] path) {
        return path.length == 3 && validateEdit(headers.get("Authorization").get(0));
    }

}
