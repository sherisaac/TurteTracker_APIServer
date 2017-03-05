/*
 * Copyright TurtleTracker 2017
 */
package com.turtletracker.api.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Ike [Admin@KudoDev.com]
 */
public abstract class AuthenticatedHandler extends Handler {

    protected boolean validateUser(String apiKey) {
        Connection con = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = con.prepareStatement("SELECT `active`, `canWrite` FROM api_key WHERE apiKey = ? LIMIT 1")) {
            stmt.setString(1, apiKey);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean(1) && rs.getBoolean(2);
                }
            }
        } catch (SQLException ex) {
        }
        return false;
    }

}
