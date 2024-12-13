package org.example.vmst_finalproject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleLog {
    // Existing method to log maintenance activity into the vehicle_maintenance_log table
    public boolean logMaintenance(int vehicleId, String maintenanceDate, String description) {
        // Validate if the vehicle exists based on vehicleId
        if (!isVehicleIdExists(vehicleId)) {
            System.out.println("Vehicle not found for ID: " + vehicleId);
            return false; // Return false if the vehicle does not exist
        }

        // Retrieve the vehicle's make and model based on the vehicleId
        String vehicleQuery = "SELECT make, model FROM vehicles WHERE vehicle_id = ?";
        String vehicleMake = null;
        String vehicleModel = null;

        try (Connection conn = Database.getConnection(); PreparedStatement vehicleStatement = conn.prepareStatement(vehicleQuery)) {
            vehicleStatement.setInt(1, vehicleId); // Set vehicleId
            ResultSet vehicleResultSet = vehicleStatement.executeQuery();

            if (vehicleResultSet.next()) {
                vehicleMake = vehicleResultSet.getString("make");
                vehicleModel = vehicleResultSet.getString("model");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false if there was an error fetching vehicle details
        }

        // Now insert the maintenance log along with the vehicle's make and model
        String query = "INSERT INTO vehicle_maintenance_log (vehicle_id, maintenance_date, description, vehicle_make, vehicle_model) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Database.getConnection(); PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, vehicleId); // Set vehicleId
            statement.setString(2, maintenanceDate); // Set maintenance date
            statement.setString(3, description); // Set maintenance description
            statement.setString(4, vehicleMake); // Set vehicle make
            statement.setString(5, vehicleModel); // Set vehicle model

            int rowsAffected = statement.executeUpdate(); // Execute the insert query
            return rowsAffected > 0; // Return true if maintenance log was successfully added
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false if there was an error inserting the maintenance log
        }
    }

    // Existing method to save vehicle
    public boolean saveVehicle(Vehicle vehicle) {
        String query = "INSERT INTO vehicles (make, model, year, vin, license_plate) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Database.getConnection(); PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, vehicle.getMake());
            statement.setString(2, vehicle.getModel());
            statement.setInt(3, vehicle.getYear());
            statement.setString(4, vehicle.getVin());
            statement.setString(5, vehicle.getLicensePlate());

            int rowsAffected = statement.executeUpdate(); // Execute the insert query
            return rowsAffected > 0; // Return true if vehicle was successfully saved
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false if there was an error
        }
    }

    // New method to log maintenance activity with a different name
    public boolean logMaintenanceActivity(int vehicleId, String maintenanceDate, String description) {
        if (!isVehicleIdExists(vehicleId)) {
            System.out.println("Vehicle not found for ID: " + vehicleId);
            return false; // Return false if the vehicle does not exist
        }

        // Retrieve vehicle details and insert the maintenance log
        String vehicleQuery = "SELECT make, model FROM vehicles WHERE vehicle_id = ?";
        String vehicleMake = null;
        String vehicleModel = null;

        try (Connection conn = Database.getConnection(); PreparedStatement vehicleStatement = conn.prepareStatement(vehicleQuery)) {
            vehicleStatement.setInt(1, vehicleId); // Set vehicleId
            ResultSet vehicleResultSet = vehicleStatement.executeQuery();

            if (vehicleResultSet.next()) {
                vehicleMake = vehicleResultSet.getString("make");
                vehicleModel = vehicleResultSet.getString("model");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        // Insert the maintenance log
        String query = "INSERT INTO vehicle_maintenance_log (vehicle_id, maintenance_date, description, vehicle_make, vehicle_model) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Database.getConnection(); PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, vehicleId);
            statement.setString(2, maintenanceDate);
            statement.setString(3, description);
            statement.setString(4, vehicleMake);
            statement.setString(5, vehicleModel);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // New method to fetch all maintenance logs for technician's dashboard (renamed)
    public List<MaintenanceLog> fetchTechnicianMaintenanceLogs() {
        String query = "SELECT maintenance_date, description, vehicle_make, vehicle_model FROM vehicle_maintenance_log";
        List<MaintenanceLog> logs = new ArrayList<>();

        try (Connection conn = Database.getConnection(); PreparedStatement statement = conn.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String maintenanceDate = resultSet.getString("maintenance_date");
                String description = resultSet.getString("description");
                String vehicleMake = resultSet.getString("vehicle_make");
                String vehicleModel = resultSet.getString("vehicle_model");
                logs.add(new MaintenanceLog(maintenanceDate, description, vehicleMake, vehicleModel)); // Add each log entry to the list
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return logs; // Return the list of maintenance logs
    }

    // Method to check if the vehicleId exists in the vehicles table
    public boolean isVehicleIdExists(int vehicleId) {
        String query = "SELECT COUNT(*) FROM vehicles WHERE vehicle_id = ?"; // Use correct column name
        try (Connection conn = Database.getConnection(); PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, vehicleId); // Set the vehicle ID
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);
            return count > 0; // Return true if the vehicle ID exists
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false if there was an error
        }
    }

    public boolean TechnicianMaintenanceActivity(int vehicleId, String maintenanceDate, String description) {
        // Insert the maintenance log into the vehicle_status table using the provided vehicle_id
        String query = "INSERT INTO vehicle_status (vehicle_id, maintenance_date, description, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = Database.getConnection(); PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, vehicleId); // Use the provided vehicle_id
            statement.setString(2, maintenanceDate); // Set maintenance date
            statement.setString(3, description); // Set maintenance description
            statement.setString(4, "In Progress"); // Set status to "In Progress"

            int rowsAffected = statement.executeUpdate(); // Execute the insert query
            return rowsAffected > 0; // Return true if maintenance log was successfully added
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false if there was an error inserting the maintenance log
        }
    }

    public boolean updateVehicleStatus(int vehicleId, String newStatus) {
        // Update the status of the maintenance activity for the vehicle in the vehicle_status table
        String query = "UPDATE vehicle_status SET status = ? WHERE vehicle_id = ? AND status = 'In Progress'";

        try (Connection conn = Database.getConnection(); PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, newStatus); // Set the new status
            statement.setInt(2, vehicleId); // Set the vehicle ID

            int rowsAffected = statement.executeUpdate(); // Execute the update query
            return rowsAffected > 0; // Return true if the status was successfully updated
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false if there was an error
        }
    }

    // Method to fetch all maintenance logs
    public List<MaintenanceLog> getAllMaintenanceLogs() {
        String query = "SELECT maintenance_date, description, vehicle_make, vehicle_model FROM vehicle_maintenance_log";
        List<MaintenanceLog> logs = new ArrayList<>();

        try (Connection conn = Database.getConnection(); PreparedStatement statement = conn.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String maintenanceDate = resultSet.getString("maintenance_date");
                String description = resultSet.getString("description");
                String vehicleMake = resultSet.getString("vehicle_make");
                String vehicleModel = resultSet.getString("vehicle_model");
                logs.add(new MaintenanceLog(maintenanceDate, description, vehicleMake, vehicleModel)); // Add each log entry to the list
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return logs; // Return the list of maintenance logs
    }

    // Inner class to hold maintenance log details
    public static class MaintenanceLog {
        private String maintenanceDate;
        private String description;
        private String vehicleMake;
        private String vehicleModel;

        // Constructor to initialize the maintenance log details
        public MaintenanceLog(String maintenanceDate, String description, String vehicleMake, String vehicleModel) {
            this.maintenanceDate = maintenanceDate;
            this.description = description;
            this.vehicleMake = vehicleMake;
            this.vehicleModel = vehicleModel;
        }

        // Getters for the fields
        public String getMaintenanceDate() {
            return maintenanceDate;
        }

        public String getDescription() {
            return description;
        }

        public String getVehicleMake() {
            return vehicleMake;
        }

        public String getVehicleModel() {
            return vehicleModel;
        }
    }
}
