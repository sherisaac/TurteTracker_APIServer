/*
 * Copyright TurtleTracker 2017
 */
package com.turtletracker.api.server;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Base64;

/**
 *
 * @author Ike [Admin@KudoDev.com]
 */
public abstract class AuthenticatedHandler extends Handler {

    protected boolean validateEdit(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Basic")) {
            return false;
        }
        Connection con = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = con.prepareStatement("SELECT `userName`, `password`, `role` FROM user WHERE `username` = ? AND `role` > 0 LIMIT 1")) {
            String base64 = authHeader.substring(6);
            System.out.println(authHeader);
            String[] creds = new String(Base64.getDecoder().decode(base64), "UTF-8").split(":", 2);
            stmt.setString(1, creds[0]);
            System.out.println(creds[0]);
            System.out.println(creds[1]);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString(2).equals(getPassHash(creds[1]));
                }
            }
        } catch (Exception ex) {
        }
        return false;
    }

    private String getPassHash(String pass) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(pass.getBytes("UTF-8"));
        byte[] b = md.digest();
        StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < b.length; i++) {
            hexString.append(Integer.toHexString(0xFF & b[i]));
        }
        System.out.println("Pass: " + hexString.toString());
        return hexString.toString();
    }
}
