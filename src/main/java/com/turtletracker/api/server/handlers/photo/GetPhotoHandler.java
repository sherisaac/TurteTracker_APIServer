/*
 * Copyright TurtleTracker 2017
 */
package com.turtletracker.api.server.handlers.photo;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.turtletracker.api.server.DatabaseConnection;
import com.turtletracker.api.server.Handler;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Logger;

/**
 *
 * @author Ike [Admin@KudoDev.com]
 */
public class GetPhotoHandler extends Handler {

    private static final Logger logger = Logger.getLogger(GetPhotoHandler.class.getName());

    @Override
    public void handle(HttpExchange he, InputStream req, OutputStream res, String[] path) throws Exception {
        Connection con = DatabaseConnection.getConnection();
        String fileName = null;
        try (PreparedStatement stmt = con.prepareStatement("SELECT `filename` FROM photo WHERE photoId = ? AND `visible` = 1 LIMIT 1")) {
            stmt.setString(1, path[3]);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    fileName = rs.getString(1);
                }
            }
        }
        
        File f = new File("photos/" + fileName);
        if (fileName == null || !f.exists()) {
            sendResponse(he, 404, path[3] + ": Not found...");
            return;
        }

        he.getResponseHeaders().set("Content-type", "image/jpeg");
        he.sendResponseHeaders(200, f.length());
        FileInputStream in = new FileInputStream(f);
        int r;
        byte[] buf = new byte[1024];
        while ((r = in.read(buf)) != -1) {
            he.getResponseBody().write(buf, 0, r);
        }

    }

    @Override
    public boolean validate(Headers headers, String[] path) {
        return path.length == 4;
    }
}
