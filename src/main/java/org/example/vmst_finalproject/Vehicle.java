package org.example.vmst_finalproject;

public class Vehicle {
    private int id;  // Assuming this will be auto-generated by the database
    private String make;
    private String model;
    private int year;
    private String vin;
    private String licensePlate;

    // Constructor excluding ID (auto-generated by the database)
    public Vehicle(String make, String model, int year, String vin, String licensePlate) {
        this.make = make;
        this.model = model;
        this.year = year;
        this.vin = vin;
        this.licensePlate = licensePlate;
    }

    // Constructor including ID for when we retrieve a vehicle from the database
    public Vehicle(int id, String make, String model, int year, String vin, String licensePlate) {
        this.id = id;
        this.make = make;
        this.model = model;
        this.year = year;
        this.vin = vin;
        this.licensePlate = licensePlate;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getMake() { return make; }
    public void setMake(String make) { this.make = make; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public String getVin() { return vin; }
    public void setVin(String vin) { this.vin = vin; }

    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }
}