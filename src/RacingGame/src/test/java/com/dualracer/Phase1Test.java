package com.dualracer;

import com.dualracer.entities.Player;
import com.dualracer.utils.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.Color;

import static org.junit.jupiter.api.Assertions.*;

public class Phase1Test {

    private Player player1;
    private Player player2;

    @BeforeEach
    public void setup() {
        double p1StartX = (Constants.P1_ROAD_LEFT + Constants.P1_ROAD_RIGHT) / 2.0 - (Constants.CAR_WIDTH / 2.0);
        player1 = new Player(
                1,
                "Player 1",
                Constants.COLOR_P1_RED,
                Color.WHITE,
                p1StartX,
                560.0,
                Constants.P1_ROAD_LEFT,
                Constants.P1_ROAD_RIGHT - Constants.CAR_WIDTH
        );

        double p2StartX = (Constants.P2_ROAD_LEFT + Constants.P2_ROAD_RIGHT) / 2.0 - (Constants.CAR_WIDTH / 2.0);
        player2 = new Player(
                2,
                "Player 2",
                Constants.COLOR_P2_BLUE,
                Color.WHITE,
                p2StartX,
                560.0,
                Constants.P2_ROAD_LEFT,
                Constants.P2_ROAD_RIGHT - Constants.CAR_WIDTH
        );
    }

    @Test
    @DisplayName("Verify Split-Screen Geometry has no overlapping roads")
    public void testSplitScreenGeometry() {
        assertEquals(1280, Constants.WINDOW_WIDTH);
        assertEquals(720, Constants.WINDOW_HEIGHT);
        assertEquals(640, Constants.HALF_WIDTH);

        // Player 1 road must be completely in the left half
        assertTrue(Constants.P1_ROAD_LEFT >= 0);
        assertTrue(Constants.P1_ROAD_RIGHT < Constants.HALF_WIDTH);

        // Player 2 road must be completely in the right half
        assertTrue(Constants.P2_ROAD_LEFT > Constants.HALF_WIDTH);
        assertTrue(Constants.P2_ROAD_RIGHT <= Constants.WINDOW_WIDTH);

        // Roads should not overlap
        assertTrue(Constants.P1_ROAD_RIGHT < Constants.P2_ROAD_LEFT);
    }

    @Test
    @DisplayName("Verify Player Initial State")
    public void testPlayerInitialState() {
        assertEquals(1, player1.getId());
        assertEquals(2, player2.getId());
        assertEquals(Constants.INITIAL_LIVES, player1.getLives());
        assertEquals(Constants.INITIAL_LIVES, player2.getLives());
        assertEquals(Constants.MAX_NITRO, player1.getNitro(), 0.001);
        assertEquals(0.0, player1.getScore(), 0.001);
        assertEquals(0.0, player1.getDistance(), 0.001);
        assertFalse(player1.isNitroActive());
    }

    @Test
    @DisplayName("Verify Acceleration and Braking Mechanics")
    public void testAccelerationAndBraking() {
        double initialSpeed = player1.getSpeed();
        assertEquals(Constants.IDLE_SPEED, initialSpeed, 0.001);

        // Accelerate for 0.5s
        player1.update(0.5, true, false, false, false, false);
        assertTrue(player1.getSpeed() > initialSpeed, "Car speed should increase when accelerating");

        // Brake for 0.5s
        double acceleratedSpeed = player1.getSpeed();
        player1.update(0.5, false, true, false, false, false);
        assertTrue(player1.getSpeed() < acceleratedSpeed, "Car speed should decrease when braking");
    }

    @Test
    @DisplayName("Verify Road Boundary Restrictions for Left and Right")
    public void testRoadBoundaries() {
        // Force steer Left for 10 seconds -> must clamp exactly at minBoundX
        for (int i = 0; i < 100; i++) {
            player1.update(0.1, true, false, true, false, false);
        }
        assertEquals(player1.getMinBoundX(), player1.getX(), 0.001, "Player 1 should not cross left road boundary");

        // Force steer Right for 10 seconds -> must clamp exactly at maxBoundX
        for (int i = 0; i < 100; i++) {
            player1.update(0.1, true, false, false, true, false);
        }
        assertEquals(player1.getMaxBoundX(), player1.getX(), 0.001, "Player 1 should not cross right road boundary");
    }

    @Test
    @DisplayName("Verify Nitro boosts acceleration and consumes nitro energy")
    public void testNitroMechanics() {
        // Accelerate with Nitro
        player1.update(0.5, true, false, false, false, true);
        assertTrue(player1.isNitroActive(), "Nitro should be active");
        assertTrue(player1.getNitro() < Constants.MAX_NITRO, "Nitro energy should decrease");

        // Release Nitro -> should recharge
        double drainedNitro = player1.getNitro();
        player1.update(0.5, false, false, false, false, false);
        assertFalse(player1.isNitroActive(), "Nitro should be inactive");
        assertTrue(player1.getNitro() > drainedNitro, "Nitro energy should recharge");
    }

    @Test
    @DisplayName("Verify Player 1 and Player 2 are completely independent")
    public void testPlayerIndependence() {
        // Player 1 accelerates and steers left
        player1.update(0.5, true, false, true, false, false);

        // Player 2 brakes and steers right
        player2.update(0.5, false, true, false, true, false);

        assertTrue(player1.getSpeed() > player2.getSpeed(), "P1 speed should differ from P2");
        assertNotEquals(player1.getX(), player2.getX());
        assertNotEquals(player1.getDistance(), player2.getDistance());
    }
}
