package com.dualracer.entities;

public enum VehicleType {
    SEDAN("Sedan", 42, 76, 65.0, 110.0),
    SPORTS("Sports", 44, 74, 95.0, 140.0),
    SUV("SUV", 46, 84, 55.0, 95.0),
    TRUCK("Truck", 48, 120, 40.0, 75.0),
    TAXI("Taxi", 42, 76, 70.0, 115.0);

    private final String displayName;
    private final int width;
    private final int height;
    private final double minBaseSpeed;
    private final double maxBaseSpeed;

    VehicleType(String displayName, int width, int height, double minBaseSpeed, double maxBaseSpeed) {
        this.displayName = displayName;
        this.width = width;
        this.height = height;
        this.minBaseSpeed = minBaseSpeed;
        this.maxBaseSpeed = maxBaseSpeed;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public double getMinBaseSpeed() {
        return minBaseSpeed;
    }

    public double getMaxBaseSpeed() {
        return maxBaseSpeed;
    }
}
