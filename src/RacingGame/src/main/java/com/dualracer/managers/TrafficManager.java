package com.dualracer.managers;

import com.dualracer.core.GameConfig;
import com.dualracer.entities.Player;
import com.dualracer.entities.TrafficCar;
import com.dualracer.entities.VehicleType;
import com.dualracer.utils.Constants;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Manages traffic vehicle spawning, movement, pattern prevention,
 * and lifecycle independently for a single player's road.
 */
public class TrafficManager {

    private final int roadLeft;
    private final int roadRight;
    private final int roadWidth;
    private final double laneWidth;
    private final GameConfig config;
    private final Random random;

    private final List<TrafficCar> trafficCars;

    private double spawnTimer;
    private double currentInterval;
    private double totalElapsedTime;

    // Minimum distance between cars in the same lane
    private static final double MIN_SAME_LANE_GAP = 220.0;
    // Window in which at least 1 of 3 lanes must remain completely free
    private static final double CLEAR_LANE_WINDOW = 180.0;

    // Palette sets for varied vehicles
    private static final Color[] SPORTS_COLORS = {
            new Color(46, 204, 113),  // Emerald Green
            new Color(230, 126, 34),  // Racing Orange
            new Color(142, 68, 173),  // Royal Purple
            new Color(0, 188, 212),   // Electric Cyan
            new Color(211, 47, 47)    // Crimson Red
    };

    private static final Color[] SEDAN_COLORS = {
            new Color(189, 195, 199), // Silver
            new Color(245, 245, 245), // Pearl White
            new Color(26, 37, 48),    // Midnight Blue
            new Color(136, 27, 27),   // Wine Red
            new Color(44, 62, 80)     // Deep Slate
    };

    private static final Color[] SUV_COLORS = {
            new Color(96, 125, 139),  // Slate Gray
            new Color(46, 125, 50),   // Forest Green
            new Color(141, 110, 99),  // Desert Brown
            new Color(55, 71, 79)     // Dark Charcoal
    };

    private static final Color[] TRUCK_COLORS = {
            new Color(255, 255, 255), // Cargo White
            new Color(21, 101, 192),  // Industrial Blue
            new Color(255, 143, 0),   // Safety Amber
            new Color(66, 66, 66)     // Steel Gray
    };

    private static final Color TAXI_COLOR = new Color(251, 192, 45); // Iconic Taxi Yellow

    public TrafficManager(int roadLeft, int roadRight, GameConfig config) {
        this(roadLeft, roadRight, config, new Random());
    }

    public TrafficManager(int roadLeft, int roadRight, GameConfig config, Random random) {
        this.roadLeft = roadLeft;
        this.roadRight = roadRight;
        this.roadWidth = roadRight - roadLeft;
        this.laneWidth = (double) roadWidth / Constants.LANES_PER_ROAD;
        this.config = config;
        this.random = random;
        this.trafficCars = new ArrayList<>();
        this.totalElapsedTime = 0.0;
        this.currentInterval = calculateNextInterval();
        this.spawnTimer = currentInterval * 0.6; // initial warm-up spawn delay
    }

    /**
     * Updates all traffic on this road, removes off-screen cars,
     * and spawns new cars according to difficulty and progression.
     */
    public void update(double dt, double playerSpeed, Player player) {
        totalElapsedTime += dt;

        // 1. Update and move all traffic cars
        Iterator<TrafficCar> iterator = trafficCars.iterator();
        while (iterator.hasNext()) {
            TrafficCar car = iterator.next();
            car.update(dt, playerSpeed);

            // Remove cars once they leave the bottom of the screen
            if (car.isOffscreen(Constants.WINDOW_HEIGHT + 120)) {
                iterator.remove();
            }
        }

        // 2. Spawn timer progression
        spawnTimer -= dt;
        if (spawnTimer <= 0.0) {
            trySpawnTrafficCar(player);
            currentInterval = calculateNextInterval();
            spawnTimer = currentInterval;
        }
    }

    /**
     * Attempts to spawn a traffic car while guaranteeing no impossible blockage.
     */
    public boolean trySpawnTrafficCar(Player player) {
        // Evaluate lane occupancy in the spawn region
        boolean[] laneBlockedNearTop = new boolean[Constants.LANES_PER_ROAD];
        for (TrafficCar car : trafficCars) {
            // Check if car is in the top spawn zone
            if (car.getY() < CLEAR_LANE_WINDOW) {
                int lane = car.getLaneIndex();
                if (lane >= 0 && lane < Constants.LANES_PER_ROAD) {
                    laneBlockedNearTop[lane] = true;
                }
            }
        }

        // Count how many lanes currently have a car in the top region
        int occupiedCount = 0;
        List<Integer> availableLanes = new ArrayList<>();
        for (int i = 0; i < Constants.LANES_PER_ROAD; i++) {
            if (laneBlockedNearTop[i]) {
                occupiedCount++;
            } else {
                availableLanes.add(i);
            }
        }

        // Rule: NEVER occupy all 3 lanes in the same vertical window
        // If 2 lanes already have cars near the top, we must not spawn, or only pick the 1 remaining
        // if there is safe spacing. To keep the road fair and traversable, if occupied >= 2, delay spawn!
        if (occupiedCount >= 2 || availableLanes.isEmpty()) {
            return false;
        }

        // Shuffle available lanes to pick randomly
        Collections.shuffle(availableLanes, random);
        int selectedLane = availableLanes.get(0);

        // Pick vehicle type with realistic distribution
        VehicleType type = selectRandomVehicleType();

        // Calculate spawn position
        double spawnY = -(type.getHeight() + 40.0 + (random.nextDouble() * 30.0));

        // Safety check: verify no car in this lane is too close to spawnY
        for (TrafficCar car : trafficCars) {
            if (car.getLaneIndex() == selectedLane) {
                if (Math.abs(car.getY() - spawnY) < MIN_SAME_LANE_GAP) {
                    return false; // too close, abort spawn this tick
                }
            }
        }

        // Ensure spawn is far above player
        if (player != null && Math.abs(player.getY() - spawnY) < 250.0) {
            return false;
        }

        // Determine X coordinate strictly centered in chosen lane
        double laneCenterX = roadLeft + (selectedLane + 0.5) * laneWidth;
        double carX = laneCenterX - (type.getWidth() / 2.0);

        // Pick speed and colors
        double baseSpeed = type.getMinBaseSpeed() + random.nextDouble() * (type.getMaxBaseSpeed() - type.getMinBaseSpeed());
        Color bodyColor = selectBodyColor(type);
        Color detailColor = selectDetailColor(type, bodyColor);

        TrafficCar newCar = new TrafficCar(carX, spawnY, type, bodyColor, detailColor, selectedLane, baseSpeed);
        trafficCars.add(newCar);
        return true;
    }

    private VehicleType selectRandomVehicleType() {
        int roll = random.nextInt(100);
        if (roll < 35) {
            return VehicleType.SEDAN;    // 35%
        } else if (roll < 55) {
            return VehicleType.SUV;      // 20%
        } else if (roll < 75) {
            return VehicleType.SPORTS;   // 20%
        } else if (roll < 90) {
            return VehicleType.TAXI;     // 15%
        } else {
            return VehicleType.TRUCK;    // 10%
        }
    }

    private Color selectBodyColor(VehicleType type) {
        return switch (type) {
            case SPORTS -> SPORTS_COLORS[random.nextInt(SPORTS_COLORS.length)];
            case SEDAN -> SEDAN_COLORS[random.nextInt(SEDAN_COLORS.length)];
            case SUV -> SUV_COLORS[random.nextInt(SUV_COLORS.length)];
            case TRUCK -> TRUCK_COLORS[random.nextInt(TRUCK_COLORS.length)];
            case TAXI -> TAXI_COLOR;
        };
    }

    private Color selectDetailColor(VehicleType type, Color body) {
        if (type == VehicleType.TAXI) {
            return Color.BLACK; // Taxi checkerboard pattern
        } else if (type == VehicleType.SPORTS) {
            return Color.WHITE; // Racing stripes
        } else if (type == VehicleType.TRUCK) {
            return new Color(180, 185, 195); // Metallic container rib
        }
        return body.darker();
    }

    private double calculateNextInterval() {
        // Base interval scales down from ~1.9s to ~0.95s over 90 seconds
        double progression = Math.min(1.0, totalElapsedTime / 90.0);
        double baseInterval = 1.9 - (progression * 0.95);

        // Difficulty multiplier (Easy: 1.2x, Normal: 1.0x, Hard: 0.8x)
        double difficultyMultiplier = (config != null) ? config.getDifficulty().getSpawnIntervalMultiplier() : 1.0;
        baseInterval *= difficultyMultiplier;

        // Jitter: +/- 0.30s
        double jitter = (random.nextDouble() - 0.5) * 0.6;
        return Math.max(0.65, baseInterval + jitter);
    }

    public void clear() {
        trafficCars.clear();
        totalElapsedTime = 0.0;
        spawnTimer = calculateNextInterval();
    }

    public List<TrafficCar> getTrafficCars() {
        return trafficCars;
    }

    public int getRoadLeft() {
        return roadLeft;
    }

    public int getRoadRight() {
        return roadRight;
    }

    public double getLaneWidth() {
        return laneWidth;
    }

    public double getSpawnTimer() {
        return spawnTimer;
    }

    public double getCurrentInterval() {
        return currentInterval;
    }
}
