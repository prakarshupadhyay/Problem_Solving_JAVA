package com.dualracer.entities;

import com.dualracer.utils.Constants;
import com.dualracer.utils.MathUtils;

import java.awt.Color;
import java.awt.Graphics2D;

public class Player extends Car {

    private final int id;
    private final String name;
    private final Color primaryColor;
    private final Color secondaryColor;
    private final double minBoundX;
    private final double maxBoundX;

    private int lives;
    private double nitro;
    private boolean nitroActive;
    private double score;
    private double distance; // in meters
    private int overtakes;
    private double invulnerableTimer;
    private double roadOffset;

    // Visual steer tilt (-1.0 to 1.0)
    private double steerTilt;

    public Player(int id, String name, Color primaryColor, Color secondaryColor,
                  double startX, double startY, double minBoundX, double maxBoundX) {
        super(startX, startY, Constants.CAR_WIDTH, Constants.CAR_HEIGHT, Constants.IDLE_SPEED);
        this.id = id;
        this.name = name;
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        this.minBoundX = minBoundX;
        this.maxBoundX = maxBoundX;

        this.lives = Constants.INITIAL_LIVES;
        this.nitro = Constants.MAX_NITRO;
        this.nitroActive = false;
        this.score = 0.0;
        this.distance = 0.0;
        this.overtakes = 0;
        this.invulnerableTimer = 0.0;
        this.roadOffset = 0.0;
        this.steerTilt = 0.0;
    }

    @Override
    public void update(double dt) {
        // Fallback update without input
        update(dt, false, false, false, false, false);
    }

    public void update(double dt, boolean accel, boolean brake, boolean left, boolean right, boolean nitroKey) {
        // 1. Invulnerability countdown
        if (invulnerableTimer > 0) {
            invulnerableTimer = Math.max(0, invulnerableTimer - dt);
        }

        // 2. Nitro logic
        if (nitroKey && nitro > 2.0 && accel) {
            nitroActive = true;
            nitro = Math.max(0.0, nitro - Constants.NITRO_DRAIN_RATE * dt);
            if (nitro <= 0.0) {
                nitroActive = false;
            }
        } else {
            nitroActive = false;
            // Recharge nitro slowly when not active
            nitro = Math.min(Constants.MAX_NITRO, nitro + Constants.NITRO_RECHARGE_RATE * dt);
        }

        // 3. Acceleration & Speed
        double currentMaxSpeed = nitroActive ? Constants.MAX_NITRO_SPEED : Constants.MAX_NORMAL_SPEED;
        double currentAccelRate = nitroActive ? Constants.NITRO_ACCEL_RATE : Constants.ACCELERATION_RATE;

        if (accel) {
            speed += currentAccelRate * dt;
            if (speed > currentMaxSpeed) {
                speed = Math.max(currentMaxSpeed, speed - Constants.NATURAL_DECEL_RATE * dt);
            }
        } else if (brake) {
            speed -= Constants.BRAKE_RATE * dt;
            if (speed < Constants.MIN_SPEED) {
                speed = Constants.MIN_SPEED;
            }
        } else {
            // Natural coasting / friction towards idle speed
            if (speed > Constants.IDLE_SPEED) {
                speed -= Constants.NATURAL_DECEL_RATE * dt;
                if (speed < Constants.IDLE_SPEED) {
                    speed = Constants.IDLE_SPEED;
                }
            } else if (speed < Constants.IDLE_SPEED) {
                speed += Constants.NATURAL_DECEL_RATE * 0.5 * dt;
            }
        }

        speed = MathUtils.clamp(speed, Constants.MIN_SPEED, Constants.MAX_NITRO_SPEED);

        // 4. Steering and boundary clamping
        double steerSpeed = Constants.STEER_SPEED;
        double steerFactor = (speed > 5.0) ? Math.min(1.0, 0.35 + 0.65 * (speed / Constants.MAX_NORMAL_SPEED)) : 0.15;
        
        double targetTilt = 0.0;
        if (left && !right) {
            x -= steerSpeed * steerFactor * dt;
            targetTilt = -1.0;
        } else if (right && !left) {
            x += steerSpeed * steerFactor * dt;
            targetTilt = 1.0;
        }

        // Smooth steering tilt animation
        steerTilt = MathUtils.lerp(steerTilt, targetTilt, Math.min(1.0, dt * 10.0));

        // Strict road boundary restriction
        if (x < minBoundX) {
            x = minBoundX;
            speed = Math.max(Constants.IDLE_SPEED * 0.5, speed - Constants.BRAKE_RATE * 0.5 * dt); // shoulder drag
        } else if (x > maxBoundX) {
            x = maxBoundX;
            speed = Math.max(Constants.IDLE_SPEED * 0.5, speed - Constants.BRAKE_RATE * 0.5 * dt);
        }

        // 5. Road scroll offset
        roadOffset = (roadOffset + speed * dt) % 100000.0;

        // 6. Metrics progression
        double distanceDelta = (speed * dt) * 0.05; // scaled to meters
        distance += distanceDelta;
        score += distanceDelta * 1.5;
    }

    public void crashPenalty() {
        if (isInvulnerable()) {
            return;
        }
        lives = Math.max(0, lives - 1);
        speed = Math.max(Constants.MIN_SPEED, speed * 0.35); // severe speed drop
        invulnerableTimer = Constants.INVULNERABILITY_TIME;
    }

    public void addOvertakeBonus() {
        overtakes++;
        score += 50.0; // 50 points per clean overtake
    }

    public boolean isInvulnerable() {
        return invulnerableTimer > 0;
    }

    public boolean isBlinkingVisible() {
        if (!isInvulnerable()) {
            return true;
        }
        // Blink every 100ms when invulnerable
        return ((int) (invulnerableTimer * 10)) % 2 == 0;
    }

    public int getDisplaySpeedKmh() {
        // Convert internal speed to realistic arcade display km/h (approx 0 to 240 km/h)
        return (int) Math.round((speed / Constants.MAX_NORMAL_SPEED) * 160.0 + (nitroActive ? 40.0 : 0.0));
    }

    @Override
    public void render(Graphics2D g2d) {
        // Car rendering is delegated to the centralized Renderer
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Color getPrimaryColor() {
        return primaryColor;
    }

    public Color getSecondaryColor() {
        return secondaryColor;
    }

    public double getMinBoundX() {
        return minBoundX;
    }

    public double getMaxBoundX() {
        return maxBoundX;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public double getNitro() {
        return nitro;
    }

    public void setNitro(double nitro) {
        this.nitro = MathUtils.clamp(nitro, 0.0, Constants.MAX_NITRO);
    }

    public boolean isNitroActive() {
        return nitroActive;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public double getDistance() {
        return distance;
    }

    public int getOvertakes() {
        return overtakes;
    }

    public double getRoadOffset() {
        return roadOffset;
    }

    public double getSteerTilt() {
        return steerTilt;
    }
}
