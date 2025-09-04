import jakarta.servlet.ServletException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
    private static Connection connection = null;

    public static Connection getConnection() throws ServletException, SQLException {
        if (connection != null && !connection.isClosed()) {
            return connection;
        }

        try {
            // Load properties
            Properties props = new Properties();
            props.load(DBConnection.class.getClassLoader().getResourceAsStream("db.properties"));
            String dbUrl = props.getProperty("db.url");
            String dbUser = props.getProperty("db.user");
            String dbPassword = props.getProperty("db.password");

            if (dbUrl == null || dbUser == null || dbPassword == null) {
                throw new ServletException("Database configuration not found");
            }

            // Load MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Create and return connection
            connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            return connection;

        } catch (Exception e) {
            throw new ServletException("Unable to connect to database", e);
        }
    }
}
