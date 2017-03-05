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

/**
 *
 * @author Ike [Admin@KudoDev.com]
 */
public class DeleteNestHandler extends AuthenticatedHandler {

    @Override
    public void handle(HttpExchange he, InputStream req, OutputStream res, String[] path) throws Exception {
        String nestId = path[3];
        Connection con = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = con.prepareStatement("UPDATE nest SET `visible` = 0 WHERE `nestId` = ?")) {
            stmt.setString(1, nestId);
            stmt.execute();
            if (stmt.getUpdateCount() == 1) {
                sendResponse(he, 200, nestId + ": Deleted.");
                return;
            }

            sendResponse(he, 404, nestId + ": Not found...");
        }

    }

    @Override
    public boolean validate(Headers headers, String[] path) {
        return path.length == 4 && validateEdit(headers.get("Authorization").get(0));
    }

}
