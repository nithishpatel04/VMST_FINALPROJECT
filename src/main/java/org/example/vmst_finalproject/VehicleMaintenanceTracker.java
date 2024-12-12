package org.example.vmst_finalproject;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.time.LocalDate;
import javafx.scene.control.cell.PropertyValueFactory;



import java.util.List;

public class VehicleMaintenanceTracker extends Application {

    @Override
    public void start(Stage stage) {
        // Main Menu
        VBox mainMenu = createMainMenu(stage);
        Scene mainScene = new Scene(mainMenu, 600, 450);

        stage.setTitle("Vehicle Maintenance and Service Tracker");
        stage.setScene(mainScene);
        stage.show();
    }

    private VBox createMainMenu(Stage stage) {
        VBox mainMenu = new VBox(20);
        mainMenu.setPadding(new Insets(30));
        mainMenu.setStyle("-fx-alignment: center; -fx-spacing: 20; -fx-background-color: #F1F1F1;");

        Label welcomeLabel = new Label("Welcome to the Vehicle Maintenance Tracker");
        welcomeLabel.setFont(Font.font("Arial", 20));

        Button ownerButton = createStyledButton("Vehicle Owner");
        Button technicianButton = createStyledButton("Service Technician");
        Button adminButton = createStyledButton("Administrator");

        ownerButton.setOnAction(event -> stage.setScene(new Scene(createLoginScene("Vehicle Owner", stage), 600, 450)));
        technicianButton.setOnAction(event -> stage.setScene(new Scene(createLoginScene("Service Technician", stage), 600, 450)));
        adminButton.setOnAction(event -> stage.setScene(new Scene(createLoginScene("Administrator", stage), 600, 450)));

        mainMenu.getChildren().addAll(welcomeLabel, ownerButton, technicianButton, adminButton);
        return mainMenu;
    }

    private VBox createLoginScene(String title, Stage stage) {
        VBox loginScene = new VBox(20);
        loginScene.setPadding(new Insets(20));
        loginScene.setStyle("-fx-alignment: center; -fx-spacing: 20; -fx-background-color: #FAFAFA;");

        // Title for the Login form
        Label loginTitle = new Label(title + " Login");
        loginTitle.setFont(Font.font("Arial", 18));

        // Username input field
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter Username");
        usernameField.setStyle("-fx-padding: 10; -fx-border-radius: 5; -fx-background-color: #E5E5E5;");

        // Password input field
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");
        passwordField.setStyle("-fx-padding: 10; -fx-border-radius: 5; -fx-background-color: #E5E5E5;");

        // Label to display login status (success or failure message)
        Label statusLabel = new Label();
        statusLabel.setFont(Font.font("Arial", 12));
        statusLabel.setStyle("-fx-text-fill: red;");

        // Buttons
        Button loginButton = createStyledButton("Login");
        Button backButton = createStyledButton("Back");

        // Action for the Login button
        loginButton.setOnAction(event -> {
            // Using the validateUser method from UserValidation class to validate credentials
            UserValidation userValidation = new UserValidation();
            boolean isValid = userValidation.validateUser(usernameField.getText(), passwordField.getText());

            if (isValid) {
                // If the user is valid, check their role and redirect accordingly
                switch (title) {
                    case "Vehicle Owner":
                        stage.setScene(new Scene(createOwnerDashboard(stage), 600, 500));
                        break;
                    case "Service Technician":
                        stage.setScene(new Scene(createTechnicianDashboard(stage), 600, 500));
                        break;
                    case "Administrator":
                        stage.setScene(new Scene(createAdminDashboard(stage), 600, 500));
                        break;
                    default:
                        statusLabel.setText("Unknown role. Please try again.");
                        break;
                }
            } else {
                // Display the failure message in the statusLabel
                statusLabel.setText("Invalid credentials for " + title + ".");
            }
        });

        // Action for the Back button
        backButton.setOnAction(event -> stage.setScene(new Scene(createMainMenu(stage), 600, 450)));

        // Adding all elements to the login scene
        loginScene.getChildren().addAll(loginTitle, usernameField, passwordField, loginButton, backButton, statusLabel);
        return loginScene;
    }

    private VBox createOwnerDashboard(Stage stage) {
        VBox ownerDashboard = new VBox(20);
        ownerDashboard.setPadding(new Insets(20));
        ownerDashboard.setStyle("-fx-alignment: center; -fx-spacing: 20; -fx-background-color: #F1F1F1;");

        Label title = new Label("Vehicle Owner Dashboard");
        title.setFont(Font.font("Arial", 18));

        Button registerVehicleButton = createStyledButton("Register Vehicle");
        Button logMaintenanceButton = createStyledButton("Log Maintenance");
        Button viewHistoryButton = createStyledButton("View Maintenance History");
        Button backButton = createStyledButton("Back to Main Menu");

        registerVehicleButton.setOnAction(event -> stage.setScene(new Scene(createVehicleRegistrationForm(stage), 600, 450)));

        logMaintenanceButton.setOnAction(event -> stage.setScene(new Scene(createLogMaintenanceForm(stage), 600, 450)));

        viewHistoryButton.setOnAction(event -> stage.setScene(new Scene(createViewMaintenanceHistoryScene(stage), 600, 450)));

        backButton.setOnAction(event -> stage.setScene(new Scene(createMainMenu(stage), 600, 450)));

        ownerDashboard.getChildren().addAll(title, registerVehicleButton, logMaintenanceButton, viewHistoryButton, backButton);
        return ownerDashboard;
    }

    private VBox createLogMaintenanceForm(Stage stage) {
        VBox maintenanceForm = new VBox(15);
        maintenanceForm.setPadding(new Insets(20));
        maintenanceForm.setStyle("-fx-alignment: center; -fx-spacing: 15; -fx-background-color: #EAEAEA;");

        Label formTitle = new Label("Log Vehicle Maintenance");
        formTitle.setFont(Font.font("Arial", 16));

        TextField vehicleIdField = new TextField();
        vehicleIdField.setPromptText("Enter Vehicle ID");

        TextField maintenanceDateField = new TextField();
        maintenanceDateField.setPromptText("Enter Maintenance Date (YYYY-MM-DD)");

        TextField descriptionField = new TextField();
        descriptionField.setPromptText("Enter Maintenance Description");

        Button saveButton = createStyledButton("Save Maintenance");
        Button backButton = createStyledButton("Back");

        saveButton.setOnAction(event -> {
            String vehicleIdText = vehicleIdField.getText();
            String maintenanceDate = maintenanceDateField.getText();
            String description = descriptionField.getText();

            // Validate inputs (Remove validation check as per your request)
            try {
                int vehicleId = Integer.parseInt(vehicleIdText);  // Convert vehicleId to integer

                VehicleLog vehicleLog = new VehicleLog();

                // Check if the vehicleId exists in the vehicles table
                if (!vehicleLog.isVehicleIdExists(vehicleId)) {
                    showAlert("Error", "Vehicle ID does not exist in the database.");
                    return; // If vehicleId doesn't exist, return
                }

                // Log the maintenance activity
                boolean isLogged = vehicleLog.logMaintenance(vehicleId, maintenanceDate, description);

                if (isLogged) {
                    showAlert("Maintenance Logged", "Maintenance activity has been logged successfully.");
                } else {
                    showAlert("Error", "An error occurred while logging the maintenance.");
                }

            } catch (NumberFormatException e) {
                showAlert("Invalid Input", "Please enter a valid numeric value for Vehicle ID.");
            }
        });

        backButton.setOnAction(event -> stage.setScene(new Scene(createOwnerDashboard(stage), 600, 450)));

        maintenanceForm.getChildren().addAll(formTitle, vehicleIdField, maintenanceDateField, descriptionField, saveButton, backButton);
        return maintenanceForm;
    }


    private VBox createViewMaintenanceHistoryScene(Stage stage) {
        VBox historyScene = new VBox(15);
        historyScene.setPadding(new Insets(20));
        historyScene.setStyle("-fx-alignment: center; -fx-spacing: 15; -fx-background-color: #EAEAEA;");

        Label title = new Label("Maintenance History");
        title.setFont(Font.font("Arial", 16));

        ListView<String> maintenanceHistoryList = new ListView<>();

        // Fetch all maintenance logs
        VehicleLog vehicleLog = new VehicleLog();
        List<VehicleLog.MaintenanceLog> logs = vehicleLog.getAllMaintenanceLogs();

        // Populate ListView with logs
        for (VehicleLog.MaintenanceLog log : logs) {
            String logEntry = "Maintenance Date: " + log.getMaintenanceDate() +
                    " - Description: " + log.getDescription();
            maintenanceHistoryList.getItems().add(logEntry);
        }

        Button backButton = createStyledButton("Back to Owner Dashboard");
        backButton.setOnAction(event -> stage.setScene(new Scene(createOwnerDashboard(stage), 600, 450)));

        historyScene.getChildren().addAll(title, maintenanceHistoryList, backButton);
        return historyScene;
    }

    private VBox createVehicleRegistrationForm(Stage stage) {
        VBox registrationForm = new VBox(15);
        registrationForm.setPadding(new Insets(20));
        registrationForm.setStyle("-fx-alignment: center; -fx-spacing: 15; -fx-background-color: #EAEAEA;");

        Label formTitle = new Label("Enter Vehicle Details");
        formTitle.setFont(Font.font("Arial", 16));

        TextField makeField = new TextField();
        makeField.setPromptText("Enter Vehicle Make");

        TextField modelField = new TextField();
        modelField.setPromptText("Enter Vehicle Model");

        TextField yearField = new TextField();
        yearField.setPromptText("Enter Vehicle Year");

        TextField vinField = new TextField();
        vinField.setPromptText("Enter Vehicle VIN");

        TextField licensePlateField = new TextField();
        licensePlateField.setPromptText("Enter Vehicle License Plate");

        Button saveButton = createStyledButton("Save Vehicle");
        Button backButton = createStyledButton("Back");

        saveButton.setOnAction(event -> {
            // Gather input values
            String make = makeField.getText();
            String model = modelField.getText();
            String yearText = yearField.getText();
            String vin = vinField.getText();
            String licensePlate = licensePlateField.getText();

            // Validate inputs
            if (make.isEmpty() || model.isEmpty() || yearText.isEmpty() || vin.isEmpty() || licensePlate.isEmpty()) {
                showAlert("Validation Error", "Please fill in all fields.");
                return;
            }

            try {
                int year = Integer.parseInt(yearText);  // Convert year to integer

                // Validate year (e.g., check if it's a valid 4-digit year)
                if (year < 1886 || year > 2024) {  // Cars were invented in 1886
                    showAlert("Invalid Year", "Please enter a valid vehicle year.");
                    return;
                }

                // Create Vehicle object without ID (id is auto-generated in the database)
                Vehicle vehicle = new Vehicle(make, model, year, vin, licensePlate);

                // Save the vehicle data to the database using VehicleLog
                VehicleLog vehicleLog = new VehicleLog();
                boolean isSaved = vehicleLog.saveVehicle(vehicle);

                if (isSaved) {
                    showAlert("Vehicle Saved", "The vehicle has been registered successfully.");
                } else {
                    showAlert("Database Error", "Failed to save the vehicle.");
                }

            } catch (NumberFormatException e) {
                showAlert("Invalid Input", "Please enter a valid numeric value for the year.");
            }
        });

        backButton.setOnAction(event -> {
            // Go back to the previous screen (Owner Dashboard or other scene)
            stage.setScene(new Scene(createOwnerDashboard(stage), 600, 450));
        });

        registrationForm.getChildren().addAll(formTitle, makeField, modelField, yearField, vinField, licensePlateField, saveButton, backButton);
        return registrationForm;
    }


    private VBox createTechnicianDashboard(Stage stage) {
        VBox technicianDashboard = new VBox(20);
        technicianDashboard.setPadding(new Insets(20));
        technicianDashboard.setStyle("-fx-alignment: center; -fx-spacing: 20; -fx-background-color: #F1F1F1;");

        Label title = new Label("Service Technician Dashboard");
        title.setFont(Font.font("Arial", 18));

        Button assignedVehiclesButton = createStyledButton("View Assigned Vehicles");
        Button logMaintenanceButton = createStyledButton("Log Maintenance");
        Button updateStatusButton = createStyledButton("Update Vehicle Status");
        Button backButton = createStyledButton("Back to Main Menu");

        assignedVehiclesButton.setOnAction(event -> stage.setScene(new Scene(createAssignedVehiclesScene(stage), 600, 450)));
        logMaintenanceButton.setOnAction(event -> stage.setScene(new Scene(createLogMaintenanceScene(stage), 600, 450)));
        updateStatusButton.setOnAction(event -> stage.setScene(new Scene(createUpdateVehicleStatusScene(stage), 600, 450)));

        backButton.setOnAction(event -> stage.setScene(new Scene(createMainMenu(stage), 600, 450)));

        technicianDashboard.getChildren().addAll(title, assignedVehiclesButton, logMaintenanceButton, updateStatusButton, backButton);
        return technicianDashboard;
    }

    private VBox createAssignedVehiclesScene(Stage stage) {
        VBox layout = createBaseScene("Assigned Vehicles");

        Label instructions = new Label("List of assigned vehicles for the technician:");
        instructions.setFont(Font.font("Arial", 14));

        ListView<String> maintenanceListView = new ListView<>();

        // Fetch maintenance logs using the VehicleLog class directly
        VehicleLog vehicleLog = new VehicleLog();
        List<VehicleLog.MaintenanceLog> maintenanceLogs = vehicleLog.getAllMaintenanceLogs();

        // Populate the ListView with maintenance data, including vehicle make and model
        for (VehicleLog.MaintenanceLog log : maintenanceLogs) {
            String logDetails = String.format("Make: %s - Model: %s - Date: %s - Description: %s",
                    log.getVehicleMake(), log.getVehicleModel(), log.getMaintenanceDate(), log.getDescription());
            maintenanceListView.getItems().add(logDetails);
        }

        Button backButton = createStyledButton("Back");
        backButton.setOnAction(event -> stage.setScene(new Scene(createTechnicianDashboard(stage), 600, 450)));

        layout.getChildren().addAll(instructions, maintenanceListView, backButton);
        return layout;
    }

    private VBox createLogMaintenanceScene(Stage stage) {
        VBox layout = createBaseScene("Log Maintenance Activity");

        Label instructions = new Label("Enter maintenance details:");
        instructions.setFont(Font.font("Arial", 14));

        TextField vehicleIdField = new TextField();
        vehicleIdField.setPromptText("Enter Vehicle ID");

        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Select Maintenance Date");

        TextArea maintenanceDetails = new TextArea();
        maintenanceDetails.setPromptText("Enter maintenance details");
        maintenanceDetails.setPrefRowCount(4);

        Button saveButton = createStyledButton("Save");
        saveButton.setOnAction(event -> {
            String vehicleIdText = vehicleIdField.getText();
            LocalDate scheduleDate = datePicker.getValue();
            String description = maintenanceDetails.getText();

            // Validate the input fields
            if (vehicleIdText.isEmpty() || scheduleDate == null || description.isEmpty()) {
                System.out.println("Please enter all fields.");
                return;
            }

            try {
                int vehicleId = Integer.parseInt(vehicleIdText); // Parse vehicleId as an integer

                // Convert LocalDate to string in the desired format
                String scheduleDateStr = scheduleDate.toString(); // "YYYY-MM-DD"

                // Create an instance of VehicleLog and save the maintenance log
                VehicleLog vehicleLog = new VehicleLog();
                boolean isSaved = vehicleLog.TechnicianMaintenanceActivity(vehicleId, scheduleDateStr, description);

                if (isSaved) {
                    System.out.println("Maintenance details logged successfully.");
                    showAlert("Details Saved", "Maintenance details logged successfully.");
                } else {
                    System.out.println("Failed to log maintenance details.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid Vehicle ID. Please enter a valid numeric vehicle ID.");
            }
        });

        Button backButton = createStyledButton("Back");
        backButton.setOnAction(event -> stage.setScene(new Scene(createTechnicianDashboard(stage), 600, 450)));

        layout.getChildren().addAll(instructions, vehicleIdField, datePicker, maintenanceDetails, saveButton, backButton);
        return layout;
    }


    private VBox createUpdateVehicleStatusScene(Stage stage) {
        VBox layout = createBaseScene("Update Vehicle Status");

        Label instructions = new Label("Update vehicle status:");
        instructions.setFont(Font.font("Arial", 14));

        TextField vehicleIdField = new TextField();  // Accept vehicleId directly
        vehicleIdField.setPromptText("Enter Vehicle ID");

        TextField statusField = new TextField();
        statusField.setPromptText("Enter New Status");

        Button updateButton = createStyledButton("Update");
        updateButton.setOnAction(event -> {
            String vehicleIdText = vehicleIdField.getText();
            String newStatus = statusField.getText();

            // Validate the input fields
            if (vehicleIdText.isEmpty() || newStatus.isEmpty()) {
                System.out.println("Please enter both Vehicle ID and status.");
                return;
            }

            try {
                int vehicleId = Integer.parseInt(vehicleIdText); // Convert the vehicleId to an integer

                // Create an instance of VehicleLog to update the vehicle status
                VehicleLog vehicleLog = new VehicleLog();

                // Update vehicle status in the vehicle_status table
                boolean isUpdated = vehicleLog.updateVehicleStatus(vehicleId, newStatus);

                if (isUpdated) {
                    System.out.println("Vehicle status updated successfully.");
                    showAlert("Update Successful", "Vehicle status has been updated successfully.");
                } else {
                    System.out.println("Failed to update vehicle status.");
                    showAlert("Update Failed", "Could not update vehicle status. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid numeric value for Vehicle ID.");
                showAlert("Invalid Input", "Vehicle ID must be a numeric value.");
            }
        });

        Button backButton = createStyledButton("Back");
        backButton.setOnAction(event -> stage.setScene(new Scene(createTechnicianDashboard(stage), 600, 450)));

        layout.getChildren().addAll(instructions, vehicleIdField, statusField, updateButton, backButton);
        return layout;
    }



    private VBox createBaseScene(String titleText) {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-alignment: center; -fx-spacing: 15; -fx-background-color: #EAEAEA;");

        Label title = new Label(titleText);
        title.setFont(Font.font("Arial", 18));
        layout.getChildren().add(title);
        return layout;
    }

    private VBox createAdminDashboard(Stage stage) {
        VBox adminDashboard = new VBox(20);
        adminDashboard.setPadding(new Insets(20));
        adminDashboard.setStyle("-fx-alignment: center; -fx-spacing: 20; -fx-background-color: #F1F1F1;");

        Label title = new Label("Administrator Dashboard");
        title.setFont(Font.font("Arial", 18));

        Button manageUsersButton = createStyledButton("Manage Users");
        Button overseeActivitiesButton = createStyledButton("Oversee System Activities");
        Button backButton = createStyledButton("Back to Main Menu");

        manageUsersButton.setOnAction(event -> stage.setScene(new Scene(createManageUsersScene(stage), 600, 450)));
        overseeActivitiesButton.setOnAction(event -> stage.setScene(new Scene(createOverseeActivitiesScene(stage), 600, 450)));

        backButton.setOnAction(event -> stage.setScene(new Scene(createMainMenu(stage), 600, 450)));

        adminDashboard.getChildren().addAll(title, manageUsersButton, overseeActivitiesButton, backButton);
        return adminDashboard;
    }
    private VBox createManageUsersScene(Stage stage) {
        VBox layout = createBaseScene("Manage Users");

        Label instructions = new Label("Manage user accounts and roles:");
        instructions.setFont(Font.font("Arial", 14));

        Button addUserButton = createStyledButton("Add New User");
        Button editUserButton = createStyledButton("Edit User Details");
        Button deleteUserButton = createStyledButton("Delete User");

        // Set actions for the buttons to navigate to the corresponding forms
        addUserButton.setOnAction(event -> stage.setScene(new Scene(createAddUserScene(stage), 600, 450)));
        editUserButton.setOnAction(event -> stage.setScene(new Scene(createEditUserScene(stage), 600, 450)));
        deleteUserButton.setOnAction(event -> stage.setScene(new Scene(createDeleteUserScene(stage), 600, 450)));

        // Back button to navigate to Admin Dashboard
        Button backButton = createStyledButton("Back");
        backButton.setOnAction(event -> stage.setScene(new Scene(createAdminDashboard(stage), 600, 450)));

        layout.getChildren().addAll(instructions, addUserButton, editUserButton, deleteUserButton, backButton);
        return layout;
    }

    private VBox createAddUserScene(Stage stage) {
        VBox layout = createBaseScene("Add New User");

        Label instructions = new Label("Enter new user details:");
        instructions.setFont(Font.font("Arial", 14));

        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter Username");

        TextField roleField = new TextField();
        roleField.setPromptText("Enter Role (e.g., Admin, Technician)");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");

        // Create an instance of UserManager to call methods for saving data
        UserManager userManager = new UserManager();

        Button saveButton = createStyledButton("Save");
        saveButton.setOnAction(event -> {
            String username = usernameField.getText();
            String role = roleField.getText();
            String password = passwordField.getText();

            // Validate the input fields
            if (username.isEmpty() || role.isEmpty() || password.isEmpty()) {
                System.out.println("Please fill in all fields.");
                showAlert("Validation Error", "All fields are required.");
                return;
            }

            // Call the saveUserToDatabase method from UserManager
            boolean isSaved = userManager.saveUserToDatabase(username, role, password);

            if (isSaved) {
                System.out.println("User added successfully.");
                showAlert("Success", "User added successfully.");
            } else {
                System.out.println("Failed to add user.");
                showAlert("Error", "Could not add user. Please try again.");
            }
        });

        Button backButton = createStyledButton("Back");
        backButton.setOnAction(event -> stage.setScene(new Scene(createManageUsersScene(stage), 600, 450)));

        layout.getChildren().addAll(instructions, usernameField, roleField, passwordField, saveButton, backButton);
        return layout;
    }

    private VBox createEditUserScene(Stage stage) {
        VBox layout = createBaseScene("Edit User Details");

        Label instructions = new Label("Edit existing user details:");
        instructions.setFont(Font.font("Arial", 14));

        TextField userIdField = new TextField();
        userIdField.setPromptText("Enter User ID");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter New Username");

        TextField roleField = new TextField();
        roleField.setPromptText("Enter New Role");

        // Create an instance of UserManager to call methods for updating data
        UserManager userManager = new UserManager();

        Button saveButton = createStyledButton("Save Changes");
        saveButton.setOnAction(event -> {
            String userId = userIdField.getText();
            String newUsername = usernameField.getText();
            String newRole = roleField.getText();

            // Validate the input fields
            if (userId.isEmpty() || newUsername.isEmpty() || newRole.isEmpty()) {
                System.out.println("Please fill in all fields.");
                showAlert("Validation Error", "All fields are required.");
                return;
            }

            try {
                int userIdInt = Integer.parseInt(userId);

                // Call the updateUserInDatabase method from UserManager
                boolean isUpdated = userManager.updateUserInDatabase(userIdInt, newUsername, newRole);

                if (isUpdated) {
                    System.out.println("User details updated successfully.");
                    showAlert("Success", "User details updated successfully.");
                } else {
                    System.out.println("Failed to update user details.");
                    showAlert("Error", "Could not update user details. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid User ID format.");
                showAlert("Validation Error", "User ID must be numeric.");
            }
        });

        Button backButton = createStyledButton("Back");
        backButton.setOnAction(event -> stage.setScene(new Scene(createManageUsersScene(stage), 600, 450)));

        layout.getChildren().addAll(instructions, userIdField, usernameField, roleField, saveButton, backButton);
        return layout;
    }

    private VBox createDeleteUserScene(Stage stage) {
        VBox layout = createBaseScene("Delete User");

        Label instructions = new Label("Delete an existing user:");
        instructions.setFont(Font.font("Arial", 14));

        TextField userIdField = new TextField();
        userIdField.setPromptText("Enter User ID");

        Button deleteButton = createStyledButton("Delete User");
        deleteButton.setOnAction(event -> {
            String userId = userIdField.getText();

            // Validate the input field
            if (userId.isEmpty()) {
                System.out.println("Please enter the User ID.");
                showAlert("Validation Error", "User ID is required.");
                return;
            }

            try {
                int userIdInt = Integer.parseInt(userId);

                // Create an instance of UserManager and call deleteUserFromDatabase
                UserManager userManager = new UserManager();
                boolean isDeleted = userManager.deleteUserFromDatabase(userIdInt);

                if (isDeleted) {
                    System.out.println("User deleted successfully.");
                    showAlert("Success", "User deleted successfully.");
                } else {
                    System.out.println("Failed to delete user.");
                    showAlert("Error", "Could not delete user. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid User ID format.");
                showAlert("Validation Error", "User ID must be numeric.");
            }
        });

        Button backButton = createStyledButton("Back");
        backButton.setOnAction(event -> stage.setScene(new Scene(createManageUsersScene(stage), 600, 450)));

        layout.getChildren().addAll(instructions, userIdField, deleteButton, backButton);
        return layout;
    }

    private VBox createOverseeActivitiesScene(Stage stage) {
        VBox layout = createBaseScene("Oversee System Activities");

        Label instructions = new Label("View and monitor system activities:");
        instructions.setFont(Font.font("Arial", 14));

        Button activityLogButton = createStyledButton("View Activity Log");
        Button systemStatusButton = createStyledButton("Check System Status");

        // Create a label to display the system status message (initially empty)
        Label systemStatusLabel = new Label();
        systemStatusLabel.setFont(Font.font("Arial", 14));
        systemStatusLabel.setStyle("-fx-text-fill: green;"); // Optionally set a color for the message

        // Action for 'View Activity Log' button
        activityLogButton.setOnAction(event -> {
            // Switch to the view user activity log scene
            stage.setScene(new Scene(createViewMaintenanceActivityLogScene(stage), 600, 450));
        });

        // Action for 'Check System Status' button
        systemStatusButton.setOnAction(event -> {
            // Update the system status label to display the message
            systemStatusLabel.setText("The system is working");
        });

        Button backButton = createStyledButton("Back");
        backButton.setOnAction(event -> stage.setScene(new Scene(createAdminDashboard(stage), 600, 450)));

        // Add the components to the layout
        layout.getChildren().addAll(instructions, activityLogButton, systemStatusButton, systemStatusLabel, backButton);
        return layout;
    }

    private VBox createViewMaintenanceActivityLogScene(Stage stage) {
        VBox layout = createBaseScene("View Maintenance Activity Log");

        Label instructions = new Label("View all maintenance activity logs:");
        instructions.setFont(Font.font("Arial", 14));

        // Fetch maintenance activity records using UserManager
        UserManager userManager = new UserManager();
        List<UserManager.UserActivityRecord> activities = userManager.getAllUserActivities(); // Keep using the existing method

        // Create a table to display the maintenance activities
        TableView<UserManager.UserActivityRecord> table = new TableView<>();

        // Define the columns for the table
        TableColumn<UserManager.UserActivityRecord, Integer> vehicleIdColumn = new TableColumn<>("Vehicle ID");
        vehicleIdColumn.setCellValueFactory(new PropertyValueFactory<>("vehicleId"));  // Assuming these properties exist in UserActivityRecord

        TableColumn<UserManager.UserActivityRecord, String> maintenanceDateColumn = new TableColumn<>("Maintenance Date");
        maintenanceDateColumn.setCellValueFactory(new PropertyValueFactory<>("maintenanceDate"));  // Change 'activityDate' to 'maintenanceDate'

        TableColumn<UserManager.UserActivityRecord, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<UserManager.UserActivityRecord, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Add the columns to the table
        table.getColumns().addAll(vehicleIdColumn, maintenanceDateColumn, descriptionColumn, statusColumn);

        // Set the table data (maintenance activity records)
        table.getItems().setAll(activities);

        // Back button to return to the previous scene
        Button backButton = createStyledButton("Back");
        backButton.setOnAction(event -> stage.setScene(new Scene(createAdminDashboard(stage), 600, 450)));

        // Add components to the layout
        layout.getChildren().addAll(instructions, table, backButton);
        return layout;
    }


    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setFont(Font.font("Arial", 14));
        button.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 10px 20px; -fx-border-radius: 5px;");
        return button;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch();
    }
}