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
import java.util.Base64;
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
        String authHeader = he.getRequestHeaders().get("Authorization").get(0);
        String base64 = authHeader.substring(6);
        String[] creds = new String(Base64.getDecoder().decode(base64), "UTF-8").split(":", 2);

        String userId = getUserId(creds[0]);
        
        Connection con = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = con.prepareStatement("INSERT INTO photo (`photoId`, `filename`, `userId`) VALUES (?, ?, ?)")) {
            stmt.setString(1, photoId);
            stmt.setString(2, photoId + ".jpg");
            stmt.setString(3, userId);
            stmt.execute();
        }

        sendResponse(he, 201, "{\"photoId\": \"" + photoId + "\"}");
    }

    @Override
    public boolean validate(Headers headers, String[] path) {
        return path.length == 3 && validateEdit(headers.get("Authorization").get(0));
    }

}
