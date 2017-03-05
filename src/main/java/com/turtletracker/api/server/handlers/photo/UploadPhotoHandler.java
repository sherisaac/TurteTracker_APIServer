/*
 * Copyright TurtleTracker 2017
 */
package com.turtletracker.api.server.handlers.photo;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.turtletracker.api.server.AuthenticatedHandler;
import com.turtletracker.api.server.DatabaseConnection;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import org.json.JSONArray;

/**
 *
 * @author Ike [Admin@KudoDev.com]
 */
public class UploadPhotoHandler extends AuthenticatedHandler {

    @Override
    public void handle(HttpExchange he, InputStream req, OutputStream res, String[] path) throws Exception {
        String photoId = generateId();

        try (FileOutputStream out = new FileOutputStream("photos/" + photoId + ".jpg")) {
            int r;
            byte[] buf = new byte[1024];
            while ((r = req.read(buf)) != -1) {
                out.write(buf, 0, r);
            }
        }
        
        Connection con = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = con.prepareStatement("INSERT INTO photo (`photoId`, `filename`) VALUES (?, ?)")) {
            stmt.setString(1, photoId);
            stmt.setString(2, photoId + ".jpg");
            stmt.execute();
        }

        sendResponse(he, 200, "{\"photoId\": \"" + photoId + "\"}");
    }

    @Override
    public boolean validate(Headers headers, String[] path) {
        return path.length == 3 && validateUser(headers.get("apiKey").get(0));
    }

}
