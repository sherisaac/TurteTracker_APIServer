/*
 * Copyright TurtleTracker 2017
 */
package com.turtletracker.api.server.handlers.photo;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.turtletracker.api.server.AuthenticatedHandler;
import com.turtletracker.api.server.DatabaseConnection;
import com.turtletracker.api.server.Handler;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ike [Admin@KudoDev.com]
 */
public class DeletePhotoHandler extends AuthenticatedHandler {

    private static final Logger logger = Logger.getLogger(DeletePhotoHandler.class.getName());

    @Override
    public void handle(HttpExchange he, InputStream req, OutputStream res, String[] path) throws Exception {
        Connection con = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = con.prepareStatement("UPDATE photo SET `visible` = 0 WHERE photoId = ?")) {
            String photoId = path[3];
            stmt.setString(1, photoId);
            stmt.execute();
            if (stmt.getUpdateCount() == 1) {
                sendResponse(he, 200, photoId + ": Deleted.");
                return;
            }

            sendResponse(he, 404, "Photo not found...");
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public boolean validate(Headers headers, String[] path) {
        return path.length == 4 && validateEdit(headers.get("Authorization").get(0));
    }

}
