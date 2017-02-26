/*
 * Copyright TurtleTracker 2017
 */
package com.turtletracker.api.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import org.json.JSONObject;

/**
 *
 * @author iyousuf
 */
public class DatabaseConnection {

    private static final HashMap<Integer, DatabaseConnection.ConWrapper> connections = new HashMap();
    private static boolean propsInited = false;
    private static long connectionTimeOut = 2 * 60 * 1000; // 2 minutes

    private static String dbURL, dbPassword, dbUser;

    public static void setUp(JSONObject config) {
        dbURL = config.getString("databaseURL");
        dbUser = config.getString("databaseUser");
        dbPassword = config.getString("databasePass");
    }

    public static Connection getConnection() {
        int threadId = (int) Thread.currentThread().getId();
        DatabaseConnection.ConWrapper ret = connections.get(threadId);
        if (ret == null) {
            Connection retCon = connectToDB();
            ret = new DatabaseConnection.ConWrapper(retCon);
            ret.id = threadId;
            connections.put(threadId, ret);
        }
        return ret.getConnection();
    }

    private static long getWaitTimeout(Connection con) throws SQLException {
        try (Statement stmt = con.createStatement()) {
            try (ResultSet rs = stmt.executeQuery("SHOW VARIABLES LIKE 'wait_timeout'")) {
                if (rs.next()) {
                    return Math.max(1000, rs.getInt(2) * 1000 - 1000);
                } else {
                    return -1;
                }
            }
        }
    }

    private static Connection connectToDB() {
        try {
            Connection con = DriverManager.getConnection(dbURL, dbUser, dbPassword);
            if (!propsInited) {
                long timeout = getWaitTimeout(con);
                if (timeout == -1) {
                } else {
                    connectionTimeOut = timeout;
                }
                propsInited = true;
            }
            return con;
        } catch (SQLException ex) {

        }
        return null;
    }

    private static class ConWrapper {

        private long lastAccessTime = 0;
        private Connection connection;
        protected int id;

        public ConWrapper(Connection con) {
            this.connection = con;
        }

        public Connection getConnection() {
            if (expiredConnection()) {
                try {
                    connection.close();
                } catch (SQLException err) {
                }
                this.connection = connectToDB();
            }

            lastAccessTime = System.currentTimeMillis();
            return this.connection;
        }

        public boolean expiredConnection() {
            if (lastAccessTime == 0) {
                return false;
            }
            try {
                return System.currentTimeMillis() - lastAccessTime >= connectionTimeOut || connection.isClosed();
            } catch (SQLException ex) {
                return true;
            }
        }
    }

    public static void closeAll() throws SQLException {
        for (DatabaseConnection.ConWrapper con : connections.values()) {
            con.connection.close();
        }
        connections.clear();
    }
}
