package com.dualracer;

import com.dualracer.core.GameConfig;
import com.dualracer.entities.Player;
import com.dualracer.entities.TrafficCar;
import com.dualracer.entities.VehicleType;
import com.dualracer.managers.TrafficManager;
import com.dualracer.utils.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class Phase2Test {

    private GameConfig config;
    private TrafficManager p1TrafficManager;
    private TrafficManager p2TrafficManager;
    private Player player1;
    private Player player2;

    @BeforeEach
    public void setup() {
        config = new GameConfig();

        double p1StartX = (Constants.P1_ROAD_LEFT + Constants.P1_ROAD_RIGHT) / 2.0 - (Constants.CAR_WIDTH / 2.0);
        player1 = new Player(1, "Player 1", Constants.COLOR_P1_RED, Color.WHITE,
                p1StartX, 560.0, Constants.P1_ROAD_LEFT, Constants.P1_ROAD_RIGHT - Constants.CAR_WIDTH);

        double p2StartX = (Constants.P2_ROAD_LEFT + Constants.P2_ROAD_RIGHT) / 2.0 - (Constants.CAR_WIDTH / 2.0);
        player2 = new Player(2, "Player 2", Constants.COLOR_P2_BLUE, Color.WHITE,
                p2StartX, 560.0, Constants.P2_ROAD_LEFT, Constants.P2_ROAD_RIGHT - Constants.CAR_WIDTH);

        p1TrafficManager = new TrafficManager(Constants.P1_ROAD_LEFT, Constants.P1_ROAD_RIGHT, config, new Random(42));
        p2TrafficManager = new TrafficManager(Constants.P2_ROAD_LEFT, Constants.P2_ROAD_RIGHT, config, new Random(99));
    }

    @Test
    @DisplayName("Verify Vehicle Types Have Distinct Dimensions, Speeds, and Names")
    public void testVehicleTypeProperties() {
        for (VehicleType type : VehicleType.values()) {
            assertNotNull(type.getDisplayName());
            assertTrue(type.getWidth() > 30, "Width should be realistic for top-down arcade");
            assertTrue(type.getHeight() > 50, "Height should be realistic for top-down arcade");
            assertTrue(type.getMaxBaseSpeed() >= type.getMinBaseSpeed());
        }

        // Specifically test Truck dimensions (heavier & longer) vs Sports car
        assertTrue(VehicleType.TRUCK.getHeight() > VehicleType.SEDAN.getHeight());
        assertTrue(VehicleType.SPORTS.getMaxBaseSpeed() > VehicleType.TRUCK.getMaxBaseSpeed());
    }

    @Test
    @DisplayName("Verify Traffic Moves Vertically Downward Toward Bottom of Screen")
    public void testTrafficDownwardMovement() {
        TrafficCar car = new TrafficCar(100.0, 50.0, VehicleType.SEDAN, Color.WHITE, Color.GRAY, 0, 70.0);
        double initialY = car.getY();

        // Update with player driving at 160.0 px/s for 1.0 second
        car.update(1.0, 160.0);

        assertTrue(car.getY() > initialY, "Traffic car Y must increase (move downward)");
        assertEquals(initialY + (160.0 - 70.0), car.getY(), 0.001);
    }

    @Test
    @DisplayName("Verify Player Acceleration Increases Downward Traffic Speed")
    public void testTrafficSpeedRelativeAcceleration() {
        TrafficCar carSlow = new TrafficCar(100.0, 50.0, VehicleType.SEDAN, Color.WHITE, Color.GRAY, 0, 70.0);
        TrafficCar carFast = new TrafficCar(100.0, 50.0, VehicleType.SEDAN, Color.WHITE, Color.GRAY, 0, 70.0);

        // Player 1 at idle speed vs player 2 at full speed
        carSlow.update(1.0, Constants.IDLE_SPEED);
        carFast.update(1.0, Constants.MAX_NORMAL_SPEED);

        assertTrue(carFast.getY() > carSlow.getY(), "Traffic should move downward faster when player drives faster");
    }

    @Test
    @DisplayName("Verify Traffic Stays Strictly Within Its Own Road Bounds")
    public void testTrafficStaysInsideRoadBounds() {
        // Spawn multiple cars for Player 1
        for (int i = 0; i < 20; i++) {
            p1TrafficManager.trySpawnTrafficCar(player1);
            p1TrafficManager.update(0.5, player1.getSpeed(), player1);
        }

        for (TrafficCar car : p1TrafficManager.getTrafficCars()) {
            assertTrue(car.getX() >= Constants.P1_ROAD_LEFT, "P1 car X must be >= P1_ROAD_LEFT");
            assertTrue(car.getX() + car.getWidth() <= Constants.P1_ROAD_RIGHT, "P1 car right must be <= P1_ROAD_RIGHT");
        }

        // Spawn multiple cars for Player 2
        for (int i = 0; i < 20; i++) {
            p2TrafficManager.trySpawnTrafficCar(player2);
            p2TrafficManager.update(0.5, player2.getSpeed(), player2);
        }

        for (TrafficCar car : p2TrafficManager.getTrafficCars()) {
            assertTrue(car.getX() >= Constants.P2_ROAD_LEFT, "P2 car X must be >= P2_ROAD_LEFT");
            assertTrue(car.getX() + car.getWidth() <= Constants.P2_ROAD_RIGHT, "P2 car right must be <= P2_ROAD_RIGHT");
        }
    }

    @Test
    @DisplayName("Verify Player 1 and Player 2 Roads and Traffic Never Interact or Overlap")
    public void testRoadAndTrafficIsolation() {
        // Spawn traffic on both
        for (int i = 0; i < 15; i++) {
            p1TrafficManager.trySpawnTrafficCar(player1);
            p2TrafficManager.trySpawnTrafficCar(player2);
            p1TrafficManager.update(0.2, 100.0, player1);
            p2TrafficManager.update(0.2, 150.0, player2);
        }

        // Center divider is at 640
        for (TrafficCar car : p1TrafficManager.getTrafficCars()) {
            assertTrue(car.getX() + car.getWidth() < Constants.HALF_WIDTH, "P1 traffic must stay on left of divider");
        }

        for (TrafficCar car : p2TrafficManager.getTrafficCars()) {
            assertTrue(car.getX() > Constants.HALF_WIDTH, "P2 traffic must stay on right of divider");
        }
    }

    @Test
    @DisplayName("Verify Impossible Traffic Blockage is Prevented (At Least 1 Lane Always Free)")
    public void testImpossiblePatternPrevention() {
        // Attempt aggressive spawning in a loop
        for (int step = 0; step < 50; step++) {
            p1TrafficManager.trySpawnTrafficCar(player1);

            // Check any vertical window of 180px near the top of the road
            int[] occupiedLanes = new int[3];
            for (TrafficCar car : p1TrafficManager.getTrafficCars()) {
                if (car.getY() < 180.0) {
                    occupiedLanes[car.getLaneIndex()]++;
                }
            }

            int lanesWithCars = 0;
            for (int count : occupiedLanes) {
                if (count > 0) lanesWithCars++;
            }

            assertTrue(lanesWithCars < 3, "All 3 lanes must NEVER be blocked simultaneously in the spawn zone!");
            p1TrafficManager.update(0.1, 120.0, player1);
        }
    }

    @Test
    @DisplayName("Verify Traffic Does Not Spawn on Top of Player Car")
    public void testTrafficDoesNotSpawnOnPlayer() {
        for (int i = 0; i < 30; i++) {
            p1TrafficManager.trySpawnTrafficCar(player1);
        }

        for (TrafficCar car : p1TrafficManager.getTrafficCars()) {
            // Spawn occurs at top off-screen (Y < 0), while player is at Y = 560
            assertTrue(car.getY() < player1.getY() - 200, "Traffic car should spawn far above player position");
            assertFalse(car.getBounds().intersects(player1.getBounds()), "Spawned car must not intersect player");
        }
    }

    @Test
    @DisplayName("Verify Offscreen Traffic Removal Prevents Memory Leaks")
    public void testOffscreenRemovalNoMemoryLeak() {
        // Force spawn cars
        for (int i = 0; i < 10; i++) {
            p1TrafficManager.trySpawnTrafficCar(player1);
        }

        int countBefore = p1TrafficManager.getTrafficCars().size();
        assertTrue(countBefore > 0);

        // Advance simulation for 20 seconds at high speed so all cars exit screen bottom
        for (int step = 0; step < 200; step++) {
            p1TrafficManager.update(0.1, Constants.MAX_NITRO_SPEED, player1);
        }

        // All older cars should have been cleaned up and removed once Y > WINDOW_HEIGHT + 120
        for (TrafficCar car : p1TrafficManager.getTrafficCars()) {
            assertTrue(car.getY() <= Constants.WINDOW_HEIGHT + 120, "No car beyond threshold should remain in list");
        }
    }

    @Test
    @DisplayName("Verify Difficulty Scaling Adjusts Spawn Intervals")
    public void testDifficultyScaling() {
        GameConfig easyConfig = new GameConfig();
        easyConfig.setDifficulty(GameConfig.Difficulty.EASY);

        GameConfig hardConfig = new GameConfig();
        hardConfig.setDifficulty(GameConfig.Difficulty.HARD);

        TrafficManager easyMgr = new TrafficManager(Constants.P1_ROAD_LEFT, Constants.P1_ROAD_RIGHT, easyConfig, new Random(1));
        TrafficManager hardMgr = new TrafficManager(Constants.P1_ROAD_LEFT, Constants.P1_ROAD_RIGHT, hardConfig, new Random(1));

        assertTrue(easyMgr.getCurrentInterval() > hardMgr.getCurrentInterval(),
                "Hard difficulty should have shorter spawn interval than Easy");
    }
}
