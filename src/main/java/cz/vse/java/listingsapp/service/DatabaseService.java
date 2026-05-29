package cz.vse.java.listingsapp.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DatabaseService {
    // Use volatile to ensure that multiple threads handle the instance variable correctly
    private static volatile DatabaseService instance;
    private Connection connection;

    private DatabaseService() {
        try {
            Properties props = new Properties();
            // Use getResourceAsStream for robustly loading resources from the classpath
            try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
                if (input == null) {
                    // Throw an exception to indicate a critical configuration error
                    throw new RuntimeException("Unable to find config.properties in the classpath.");
                }
                props.load(input);
            }

            connection = DriverManager.getConnection(props.getProperty("db.url"), props.getProperty("db.user"), props.getProperty("db.password"));
        } catch (SQLException | IOException e) {
            // Wrap the original exception in a RuntimeException to signal a fatal initialization error
            throw new RuntimeException("Failed to connect to the database.", e);
        }
    }

    // Double-checked locking for thread-safe and efficient singleton initialization
    public static DatabaseService getInstance() {
        if (instance == null) {
            synchronized (DatabaseService.class) {
                if (instance == null) {
                    instance = new DatabaseService();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createTables() {
        String sqlScript;
        // Use getResourceAsStream to read the schema file from the classpath
        try (InputStream in = getClass().getResourceAsStream("/schema.sql")) {
            if (in == null) {
                System.err.println("schema.sql not found in classpath");
                return;
            }
            sqlScript = new String(in.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        try (Statement statement = connection.createStatement()) {
            // This simple split works for this schema but can be fragile for more complex SQL.
            // For more complex scenarios, consider a dedicated SQL script runner library.
            String[] statements = sqlScript.split(";");

            for (String sql : statements) {
                // Execute each statement if it's not just whitespace
                if (!sql.trim().isEmpty()) {
                    statement.execute(sql);
                }
            }
            System.out.println("Tables created successfully from schema.sql.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
