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
public class GetNestHandler extends AuthenticatedHandler {

    @Override
    public void handle(HttpExchange he, InputStream req, OutputStream res, String[] path) throws Exception {
        JSONObject json = new JSONObject();

        Connection con = DatabaseConnection.getConnection();

        String nestId = path[3];
        json.put("nestId", nestId);

        try (PreparedStatement stmt = con.prepareStatement("SELECT `groupId`, `longitude`, `latitude` FROM nest WHERE `nestId` = ? AND `visible` = 1 LIMIT 1")) {
            stmt.setString(1, nestId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    sendResponse(he, 404, nestId + ": Not found...");
                    return;
                }
                json.put("groupId", rs.getInt(1));
                JSONObject location = new JSONObject();
                location.put("longitude", rs.getDouble(2));
                location.put("latitude", rs.getDouble(3));
                json.put("location", location);
            }
        }

        JSONArray photos = new JSONArray();
        try (PreparedStatement stmt = con.prepareStatement("SELECT `photoId` FROM photo WHERE `nestId` = ?")) {
            stmt.setString(1, nestId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    photos.put(rs.getString(1));
                }
            }
        }
        json.put("photos", photos);

        sendResponse(he, 200, json.toString());
    }

    @Override
    public boolean validate(Headers headers, String[] path) {
        return path.length == 4 && validateAPIKey(headers.get("apiKey").get(0));
    }

}
