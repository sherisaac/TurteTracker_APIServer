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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map.Entry;
import org.json.JSONObject;

/**
 *
 * @author Ike [Admin@KudoDev.com]
 */
public class UpdateUserHandler extends AuthenticatedHandler {

    private List<String> whiteList = new ArrayList<>(Arrays.asList("password", "firstname", "lastname", "role"));

    @Override
    public void handle(HttpExchange he, InputStream req, OutputStream res, String[] path) throws Exception {
        String username = path[3];
        String userId = getUserId(username);

        if (userId == null) {
            sendResponse(he, 404, "{\"err\":\"" + username + ": Not found...\"}");
            return;
        }

        JSONObject json = new JSONObject(readString(req));

        String authHeader = he.getRequestHeaders().get("Authorization").get(0);
        String base64 = authHeader.substring(6);
        String[] creds = new String(Base64.getDecoder().decode(base64), "UTF-8").split(":", 2);

        String curUserId = getUserId(creds[0]);

        if (!curUserId.equals(userId) && !isAdmin(creds[0])) {
            sendResponse(he, 401, "{\"err\":\"Unauthorized\"}");
            return;
        }

        Connection con = DatabaseConnection.getConnection();
        con.setAutoCommit(false);
        for (Entry<String, Object> entry : json.toMap().entrySet()) {
            String column = entry.getKey().toLowerCase();
            if (!whiteList.contains(column)) {
                continue;
            }

            try (PreparedStatement stmt = con.prepareStatement("UPDATE user SET `" + column + "` = ? WHERE `userId` = ?")) {
                if ("password".equals(column)) {
                    String newPass = getPassHash((String) entry.getValue());
                    stmt.setString(1, newPass);
                } else {
                    stmt.setObject(1, entry.getValue());
                }
                stmt.setObject(2, userId);
                stmt.execute();
            }
        }
        con.commit();
        con.setAutoCommit(true);

        sendResponse(he, 200, "{\"msg\":\"User: " + username + ": Updated\"}");
    }

    @Override
    public boolean validate(Headers headers, String[] path) {
        return path.length == 4 && validateEdit(headers.get("Authorization").get(0));
    }

}
