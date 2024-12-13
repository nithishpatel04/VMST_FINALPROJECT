package org.example.vmst_finalproject;
import java.sql.*;

public class UserValidation {

    // Method to validate user credentials from the database
    public boolean validateUser(String username, String password) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Connect to the database using the Database class
            conn = Database.getConnection();

            // SQL query to check if the user exists in the users table
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);

            // Execute the query and get the result
            rs = stmt.executeQuery();

            // Check if user is found in the result set
            if (rs.next()) {
                // User found, return true (valid user)
                return true;
            } else {
                // User not found, return false (invalid credentials)
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;  // If there is an error with the connection/query
        } finally {
            try {
                // Close the database resources
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}