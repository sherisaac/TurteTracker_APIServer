/*
 * Copyright TurtleTracker 2017
 */
package com.turtletracker.api.server.handlers.nest;

import com.turtletracker.api.server.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author IkeOTL
 */
public class Nest {

    private String nestId;
    private String family;
    private String userId = "";
    private double longitude = 0, latitude = 0;
    private boolean visible = false;
    private String createDate, lastUpdate = "";
    private String notes;
    private List<String> photos = new ArrayList<>();

    public Nest(String nestId) throws SQLException, NestNotFoundException {
        this.nestId = nestId;

        Connection con = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = con.prepareStatement("SELECT `family`, `longitude`, `latitude`, `createDate`, `lastUpdate`, `notes` FROM nest WHERE `nestId` = ? AND `visible` = 1 LIMIT 1")) {
            stmt.setString(1, nestId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    throw new NestNotFoundException(nestId);
                }
                family = rs.getString(1);
                longitude = rs.getDouble(2);
                latitude = rs.getDouble(3);
                createDate = rs.getString(4);
                lastUpdate = rs.getString(5);
                notes = rs.getString(6);
            }
        }

        try (PreparedStatement stmt = con.prepareStatement("SELECT `photoId` FROM photo WHERE `nestId` = ? AND `visible` = 1")) {
            stmt.setString(1, nestId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    photos.add(rs.getString(1));
                }
            }
        }
    }

    public JSONObject getJSON() {
        JSONObject json = new JSONObject();
        json.put("nestId", nestId);
        json.put("family", family);

        json.put("notes", notes);

        json.put("photos", photos);

        JSONObject location = new JSONObject();
        location.put("longitude", longitude);
        location.put("latitude", latitude);
        json.put("location", location);

        json.put("createDate", createDate);
        json.put("lastUpdate", lastUpdate);

        return json;
    }

    public String getNestId() {
        return nestId;
    }

    public String getFamily() {
        return family;
    }

    public String getUserId() {
        return userId;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public boolean isVisible() {
        return visible;
    }

    public String getCreateDate() {
        return createDate;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public List<String> getPhotos() {
        return photos;
    }

}
