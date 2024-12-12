package org.example.vmst_finalproject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;

public class UserManager {
    // Database connection method
    private Connection connect() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/VehicleMaintenancedb"; // Adjust as needed
        String user = "root";
        String password = "sql@040901";
        return DriverManager.getConnection(url, user, password);
    }

    // Save a new user to the database
    public boolean saveUserToDatabase(String username, String role, String password) {
        String query = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, role);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Update an existing user's information
    public boolean updateUserInDatabase(int userId, String newUsername, String newRole) {
        String query = "UPDATE users SET username = ?, role = ? WHERE id = ?";
        try (Connection conn = connect(); PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, newUsername);
            statement.setString(2, newRole);
            statement.setInt(3, userId);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete a user from the database
    public boolean deleteUserFromDatabase(int userId) {
        String query = "DELETE FROM users WHERE id = ?";
        try (Connection conn = connect(); PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, userId);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Fetch all user activity records from the vehicle_status table
    public List<UserActivityRecord> getAllUserActivities() {
        String query = "SELECT vehicle_id, maintenance_date, description, status FROM vehicle_status";
        List<UserActivityRecord> activities = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  // Date format

        try (Connection conn = connect(); PreparedStatement statement = conn.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int vehicleId = resultSet.getInt("vehicle_id");
                // Fetch the maintenance_date and check for null
                Date maintenanceDate = resultSet.getDate("maintenance_date");
                String formattedDate = null;

                if (maintenanceDate != null) {
                    // Convert to java.util.Date for formatting
                    formattedDate = sdf.format(maintenanceDate);
                }
                String description = resultSet.getString("description");
                String status = resultSet.getString("status");

                // Add the record to the list
                activities.add(new UserActivityRecord(vehicleId, formattedDate, description, status));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return activities;
    }

    // Inner class to hold user activity details
    public static class UserActivityRecord {
        private int vehicleId;
        private String maintenanceDate;
        private String description;
        private String status;

        // Constructor to initialize user activity details
        public UserActivityRecord(int vehicleId, String maintenanceDate, String description, String status) {
            this.vehicleId = vehicleId;
            this.maintenanceDate = maintenanceDate;
            this.description = description;
            this.status = status;
        }

        // Getters for the fields
        public int getVehicleId() {
            return vehicleId;
        }

        public String getMaintenanceDate() {
            return maintenanceDate;
        }

        public String getDescription() {
            return description;
        }

        public String getStatus() {
            return status;
        }
    }
}
