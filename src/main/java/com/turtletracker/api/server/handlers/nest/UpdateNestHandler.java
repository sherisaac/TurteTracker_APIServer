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

        String nestId = path[3];

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
            try (PreparedStatement stmt = con.prepareStatement("UPDATE nest SET `family` = ?, `longitude` = ?, `latitude` = ?, `notes` = ? WHERE `nestId` = ?")) {
                stmt.setString(1, family);
                stmt.setDouble(2, location.getDouble("longitude"));
                stmt.setDouble(3, location.getDouble("latitude"));
                stmt.setString(4, notes);
                stmt.setString(5, nestId);
                stmt.execute();
                if (stmt.getUpdateCount() != 1) {
                    sendResponse(he, 404, "{\"err\":\"" + nestId + ": Not found...\"}");
                    return;
                }
            }
        } else {
            try (PreparedStatement stmt = con.prepareStatement("UPDATE nest SET `groupId` = ?, `notes` = ? WHERE `nestId` = ?")) {
                stmt.setString(1, family);
                stmt.setString(2, notes);
                stmt.setString(3, nestId);
                stmt.execute();
                if (stmt.getUpdateCount() != 1) {
                    sendResponse(he, 404, "{\"err\":\"" + nestId + ": Not found...\"}");
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

        sendResponse(he, 200, "{\"msg\":\"" + nestId + ": Updated\"}");
    }

    @Override
    public boolean validate(Headers headers, String[] path) {
        return path.length == 4 && validateEdit(headers.get("Authorization").get(0));
    }

}
