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
import java.util.Base64;
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
        String authHeader = he.getRequestHeaders().get("Authorization").get(0);
        String base64 = authHeader.substring(6);
        String[] creds = new String(Base64.getDecoder().decode(base64), "UTF-8").split(":", 2);

        String userId = getUserId(creds[0]);

        String nestId = generateId();
        String family = "general";
        if (json.keySet().contains("family")) {
            family = json.getString("family");
        }

        String notes = "";
        if (json.keySet().contains("notes")) {
            notes = json.getString("notes");
        }

        if (json.keySet().contains("location")) {
            JSONObject location = json.getJSONObject("location");
            try (PreparedStatement stmt = con.prepareStatement("INSERT INTO nest (`nestId`, `family`, `longitude`, `latitude`, `userId`, `notes`) VALUES (?, ?, ?, ?, ?, ?)")) {
                stmt.setString(1, nestId);
                stmt.setString(2, family);
                stmt.setDouble(3, location.getDouble("longitude"));
                stmt.setDouble(4, location.getDouble("latitude"));
                stmt.setString(5, userId);
                stmt.setString(6, notes);
                stmt.execute();
            }
        } else {
            try (PreparedStatement stmt = con.prepareStatement("INSERT INTO nest (`nestId`, `family`, `userId`, `notes`) VALUES (?, ?, ?, ?)")) {
                stmt.setString(1, nestId);
                stmt.setString(2, family);
                stmt.setString(3, userId);
                stmt.setString(4, notes);
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

        sendResponse(he, 201, "{\"nestId\":\"" + nestId + "\"}");
    }

    @Override
    public boolean validate(Headers headers, String[] path) {
        return path.length == 3 && validateEdit(headers.get("Authorization").get(0));
    }

}
