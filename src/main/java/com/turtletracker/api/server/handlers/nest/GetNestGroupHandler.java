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
public class GetNestGroupHandler extends AuthenticatedHandler {

    @Override
    public void handle(HttpExchange he, InputStream req, OutputStream res, String[] path) throws Exception {

        Connection con = DatabaseConnection.getConnection();

        int groupId = Integer.parseInt(path[3]);
        JSONObject json = new JSONObject();

        JSONArray nests = new JSONArray();
        try (PreparedStatement stmt = con.prepareStatement("SELECT `nestId`, `longitude`, `latitude` FROM nest WHERE `groupId` = ? AND `visible` = 1")) {
            stmt.setInt(1, groupId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    JSONObject nest = new JSONObject();
                    nest.put("nestId", rs.getString(1));

                    JSONObject location = new JSONObject();
                    location.put("longitude", rs.getDouble(2));
                    location.put("latitude", rs.getDouble(3));
                    nest.put("location", location);
                    nests.put(nest);
                }
            }
        }
        json.put("nests", nests);
        json.put("groupId", groupId);

        sendResponse(he, 200, json.toString());
    }

    @Override
    public boolean validate(Headers headers, String[] path) {
        return path.length == 4 && validateUser(headers.get("apiKey").get(0));
    }

}
