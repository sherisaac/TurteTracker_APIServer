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
public class UpdateNestHandler extends AuthenticatedHandler {

    @Override
    public void handle(HttpExchange he, InputStream req, OutputStream res, String[] path) throws Exception {
        JSONObject json = new JSONObject(readString(req));
        Connection con = DatabaseConnection.getConnection();

        int apiId = 0;
        try (PreparedStatement stmt = con.prepareStatement("SELECT `apiId` FROM api_key WHERE `apiKey` = ? LIMIT 1")) {
            stmt.setString(1, he.getRequestHeaders().get("apiKey").get(0));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                apiId = rs.getInt(1);
            }
        }

        String nestId = path[3];

        int groupId = 0;
        if (json.keySet().contains("groupId")) {
            groupId = json.getInt("groupId");
        }

        if (json.keySet().contains("location")) {
            JSONObject location = json.getJSONObject("location");
            try (PreparedStatement stmt = con.prepareStatement("UPDATE nest SET `groupId` = ?, `longitude` = ?, `latitude` = ?, `apiId` = ? WHERE `nestId` = ?")) {
                stmt.setInt(1, groupId);
                stmt.setDouble(2, location.getDouble("longitude"));
                stmt.setDouble(3, location.getDouble("latitude"));
                stmt.setInt(4, apiId);

                stmt.setString(5, nestId);
                stmt.execute();
                if (stmt.getUpdateCount() != 1) {
                    sendResponse(he, 404, nestId + ": Not found...");
                    return;
                }
            }
        } else {
            try (PreparedStatement stmt = con.prepareStatement("UPDATE nest SET `groupId` = ? WHERE `nestId` = ?")) {
                stmt.setInt(1, groupId);
                stmt.setString(2, nestId);
                stmt.execute();
                if (stmt.getUpdateCount() != 1) {
                    sendResponse(he, 404, nestId + ": Not found...");
                    return;
                }
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

        sendResponse(he, 200, nestId + ": Updated.");
    }

    @Override
    public boolean validate(Headers headers, String[] path) {
        return path.length == 4 && validateEdit(headers.get("Authorization").get(0));
    }

}
