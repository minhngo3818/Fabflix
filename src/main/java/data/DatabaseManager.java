package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static final String URL = "jdbc:mysql://localhost:3306/moviedb";
    private static final String USERNAME = "mytestuser";
    private static final String PASSWORD = "My6$Password";

    private Connection connection = null;


    // Method to get a database connection

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            synchronized (DatabaseManager.class) {
                if (connection == null || connection.isClosed()) {
                    try {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                    } catch (ClassNotFoundException | SQLException e) {
                        e.printStackTrace();
                        throw new SQLException("Failed to connect to the database.");
                    }
                }
            }
        }
        return connection;
    }

    // Close the connection
    public void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
