/*
 * Copyright TurtleTracker 2017
 */
package com.turtletracker.api.server.handlers.csv;

import com.turtletracker.api.server.handlers.nest.*;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.turtletracker.api.server.AuthenticatedHandler;
import com.turtletracker.api.server.DatabaseConnection;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Ike [Admin@KudoDev.com]
 */
public class GetNestCSVHandler extends AuthenticatedHandler {

    @Override
    public void handle(HttpExchange he, InputStream req, OutputStream res, String[] path) throws Exception {

        StringBuilder sb = new StringBuilder();
        sb.append("\"nestId\",\"family\",\"longitude\",\"latitude\",\"createDate\",\"lastUpdate\",\"notes\"\n");
        Connection con = DatabaseConnection.getConnection();
        
        try (PreparedStatement stmt = con.prepareStatement("SELECT `nestId`, `family`, `longitude`, `latitude`, `createDate`, `lastUpdate`, `notes` FROM nest WHERE `visible` = 1 LIMIT 9999")) {
            try (ResultSet rs = stmt.executeQuery()) {
                boolean looped = false;
                while (rs.next()) {
                    if (looped) {
                        sb.append("\n");
                    }
                    sb.append("\"").append(rs.getString(1)).append("\"").append(",");
                    sb.append("\"").append(rs.getString(2)).append("\"").append(",");
                    sb.append("\"").append(rs.getDouble(3)).append("\"").append(",");
                    sb.append("\"").append(rs.getDouble(4)).append("\"").append(",");
                    sb.append("\"").append(rs.getString(5)).append("\"").append(",");
                    sb.append("\"").append(rs.getString(6)).append("\"").append(",");
                    sb.append("\"").append(rs.getString(7).replace("\"", "\"\"")).append("\"");
                    looped = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        sendResponse(he, 200, sb.toString());
    }

    @Override
    public boolean validate(Headers headers, String[] path) {
        return path.length == 3 && validateEdit(headers.get("Authorization").get(0));
    }

}
