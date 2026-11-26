package com.dualracer.entities;

import java.awt.Color;
import java.awt.Graphics2D;

public class TrafficCar extends Car {

    private final VehicleType vehicleType;
    private final Color bodyColor;
    private final Color detailColor;
    private final int laneIndex;
    private final double baseSpeed;
    private boolean passedByPlayer;

    public TrafficCar(double x, double y, VehicleType vehicleType, Color bodyColor, Color detailColor,
                      int laneIndex, double baseSpeed) {
        super(x, y, vehicleType.getWidth(), vehicleType.getHeight(), baseSpeed);
        this.vehicleType = vehicleType;
        this.bodyColor = bodyColor;
        this.detailColor = detailColor;
        this.laneIndex = laneIndex;
        this.baseSpeed = baseSpeed;
        this.passedByPlayer = false;
    }

    @Override
    public void update(double dt) {
        // Fallback update assuming average player speed of 100.0
        update(dt, 100.0);
    }

    /**
     * Updates traffic position relative to player speed.
     * Traffic always moves downward towards the bottom of the screen.
     * When player accelerates, traffic rushes down faster.
     */
    public void update(double dt, double playerSpeed) {
        // Ensure minimum downward motion so traffic always progresses forward on screen
        double effectiveDownwardSpeed = Math.max(35.0, playerSpeed - baseSpeed);
        this.y += effectiveDownwardSpeed * dt;
    }

    public boolean isOffscreen(double bottomThreshold) {
        return this.y > bottomThreshold;
    }

    @Override
    public void render(Graphics2D g2d) {
        // Rendering is handled by Renderer for consistency
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public Color getBodyColor() {
        return bodyColor;
    }

    public Color getDetailColor() {
        return detailColor;
    }

    public int getLaneIndex() {
        return laneIndex;
    }

    public double getBaseSpeed() {
        return baseSpeed;
    }

    public boolean isPassedByPlayer() {
        return passedByPlayer;
    }

    public void setPassedByPlayer(boolean passedByPlayer) {
        this.passedByPlayer = passedByPlayer;
    }
}
