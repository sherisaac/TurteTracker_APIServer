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
import java.sql.ResultSet;
import java.util.Map;
import java.util.logging.Logger;

/**
 *
 * @author Ike [Admin@KudoDev.com]
 */
public class AuthUserHandler extends AuthenticatedHandler {

    //private static final Logger logger = Logger.getLogger(AuthUserHandler.class.getName());

    @Override
    public void handle(HttpExchange he, InputStream req, OutputStream res, String[] path) throws Exception {
        String query = he.getRequestURI().getQuery();

        if (query == null) {
            sendResponse(he, 401, "{\"err\":\"Invalid credentials\"}");
            return;
        }

        Map<String, String> queryMap = getQueryMap(he.getRequestURI().getQuery());
        Connection con = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = con.prepareStatement("SELECT `username`, `password`, `role` FROM user WHERE `username` = ? AND `role` > 0  LIMIT 1")) {
            stmt.setString(1, queryMap.get("username"));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    if (rs.getString(2).equals(getPassHash(queryMap.get("password")))) {
                        sendResponse(he, 200, "{\"msg\":\"Valid credentials\",\"role\":\"" + rs.getString(3) + "\"}");
                        return;
                    }
                }
            }
        }

        sendResponse(he, 401, "{\"err\":\"Invalid credentials\"}");

    }

    @Override
    public boolean validate(Headers headers, String[] path) {
        return path.length == 3;
    }
}
