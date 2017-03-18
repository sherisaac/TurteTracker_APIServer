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
public class GetNestFamilyHandler extends AuthenticatedHandler {

    @Override
    public void handle(HttpExchange he, InputStream req, OutputStream res, String[] path) throws Exception {

        Connection con = DatabaseConnection.getConnection();

        String family = path[3];
        List<String> nestIds = new ArrayList<>();
        try (PreparedStatement stmt = con.prepareStatement("SELECT `nestId` FROM nest WHERE `family` = ? AND `visible` = 1")) {
            stmt.setString(1, family);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    nestIds.add(rs.getString(1));
                }
            }
        }
        JSONObject json = new JSONObject();
        json.put("family", family);
        JSONArray nests = new JSONArray();
        for (String n : nestIds) {
            Nest nest = new Nest(n);
            nests.put(nest.getJSON());
        }
        json.put("nests", nests);
        
        sendResponse(he, 200, json.toString());
    }

    @Override
    public boolean validate(Headers headers, String[] path) {
        return path.length == 4 && headers.containsKey("Authorization") && validateEdit(headers.get("Authorization").get(0));
    }

}
