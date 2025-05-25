package org.velihangozek.javarentalcli.util;

import org.velihangozek.javarentalcli.exception.VeloDataAccessException;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
    private static final String PROPS_FILE = "/application.properties";
    private static String url;
    private static String username;
    private static String password;

    static {
        try (InputStream in = DBConnection.class.getResourceAsStream(PROPS_FILE)) {
            Properties props = new Properties();
            props.load(in);
            url = props.getProperty("jdbc.url");
            username = props.getProperty("jdbc.username");
            password = props.getProperty("jdbc.password");
        } catch (Exception e) {
            throw new ExceptionInInitializerError("Failed to load DB config: " + e.getMessage());
        }
    }

    private DBConnection() {
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new VeloDataAccessException("Unable to get DB connection", e);
        }
    }
}