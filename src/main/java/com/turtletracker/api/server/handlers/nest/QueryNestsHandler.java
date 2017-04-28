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
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Ike [Admin@KudoDev.com]
 */
public class QueryNestsHandler extends AuthenticatedHandler {

    @Override
    public void handle(HttpExchange he, InputStream req, OutputStream res, String[] path) throws Exception {
        try {
            JSONObject json = new JSONObject(readString(req));
            String username = json.getString("username");
            String userId = getUserId(username);

            if (userId == null) {
                sendResponse(he, 404, "{\"err\":\"" + username + ": Not found...\"}");
                return;
            }
            Connection con = DatabaseConnection.getConnection();

            List<String> nestIds = new ArrayList<>();
            try (PreparedStatement stmt = con.prepareStatement("SELECT `nestId` FROM nest WHERE `userId` = ? AND `visible` = 1")) {
                stmt.setString(1, userId);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        nestIds.add(rs.getString(1));
                    }
                }
            }
            JSONObject outJson = new JSONObject();
            outJson.put("userId", userId);
            JSONArray nests = new JSONArray();
            for (String n : nestIds) {
                Nest nest = new Nest(n);
                nests.put(nest.getJSON());
            }
            outJson.put("nests", nests);
            sendResponse(he, 200, outJson.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean validate(Headers headers, String[] path) {
        return path.length == 3 && validateEdit(headers.get("Authorization").get(0));
    }

}
