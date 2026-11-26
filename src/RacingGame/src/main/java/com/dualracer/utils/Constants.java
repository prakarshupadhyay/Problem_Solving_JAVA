package com.dualracer.utils;

import java.awt.Color;
import java.awt.Font;

public final class Constants {

    private Constants() {}

    // Window & Display
    public static final String GAME_TITLE = "DUAL RACER";
    public static final int WINDOW_WIDTH = 1280;
    public static final int WINDOW_HEIGHT = 720;
    public static final int TARGET_FPS = 60;
    public static final double TARGET_DELTA_TIME = 1.0 / TARGET_FPS;

    // Split-screen Layout
    public static final int HALF_WIDTH = WINDOW_WIDTH / 2; // 640
    public static final int DIVIDER_WIDTH = 8;
    
    // Road Geometry for each half
    public static final int ROAD_MARGIN_SIDE = 50; // Grass width on outer edge
    public static final int ROAD_MARGIN_DIVIDER = 20; // Shoulder before center divider
    public static final int LANES_PER_ROAD = 3;
    
    // Player 1 Road (Left side: 0 to 640)
    public static final int P1_ROAD_LEFT = ROAD_MARGIN_SIDE;
    public static final int P1_ROAD_RIGHT = HALF_WIDTH - (DIVIDER_WIDTH / 2) - ROAD_MARGIN_DIVIDER;
    public static final int P1_ROAD_WIDTH = P1_ROAD_RIGHT - P1_ROAD_LEFT;

    // Player 2 Road (Right side: 640 to 1280)
    public static final int P2_ROAD_LEFT = HALF_WIDTH + (DIVIDER_WIDTH / 2) + ROAD_MARGIN_DIVIDER;
    public static final int P2_ROAD_RIGHT = WINDOW_WIDTH - ROAD_MARGIN_SIDE;
    public static final int P2_ROAD_WIDTH = P2_ROAD_RIGHT - P2_ROAD_LEFT;

    // Car Dimensions
    public static final int CAR_WIDTH = 46;
    public static final int CAR_HEIGHT = 84;

    // Player Physics
    public static final double MIN_SPEED = 0.0;
    public static final double IDLE_SPEED = 60.0; // Base rolling speed
    public static final double MAX_NORMAL_SPEED = 320.0; // Normal top speed (px/sec or internal units)
    public static final double MAX_NITRO_SPEED = 460.0; // Top speed with nitro
    public static final double ACCELERATION_RATE = 220.0;
    public static final double NITRO_ACCEL_RATE = 380.0;
    public static final double BRAKE_RATE = 320.0;
    public static final double NATURAL_DECEL_RATE = 110.0;
    public static final double STEER_SPEED = 300.0; // Horizontal movement speed px/sec

    // Nitro System
    public static final double MAX_NITRO = 100.0;
    public static final double NITRO_DRAIN_RATE = 28.0; // percent per second
    public static final double NITRO_RECHARGE_RATE = 12.0; // percent per second

    // Gameplay Rules
    public static final int INITIAL_LIVES = 3;
    public static final double DEFAULT_MATCH_DURATION = 120.0; // seconds
    public static final double INVULNERABILITY_TIME = 2.0; // seconds after crash

    // Color Palette
    public static final Color COLOR_ASPHALT = new Color(38, 41, 46);
    public static final Color COLOR_ASPHALT_LINE = new Color(245, 245, 245, 210);
    public static final Color COLOR_SHOULDER_LINE = new Color(255, 204, 0);
    public static final Color COLOR_GRASS = new Color(34, 112, 45);
    public static final Color COLOR_GRASS_STRIPE = new Color(40, 126, 52);
    public static final Color COLOR_DIVIDER = new Color(20, 20, 24);
    public static final Color COLOR_DIVIDER_NEON = new Color(0, 240, 255);
    public static final Color COLOR_P1_RED = new Color(225, 30, 45);
    public static final Color COLOR_P2_BLUE = new Color(30, 120, 245);
    public static final Color COLOR_HUD_BG = new Color(15, 18, 25, 200);

    // Fonts
    public static final Font FONT_TITLE = new Font("Arial", Font.BOLD, 42);
    public static final Font FONT_SUBTITLE = new Font("Arial", Font.BOLD, 22);
    public static final Font FONT_HUD_LABEL = new Font("Arial", Font.BOLD, 13);
    public static final Font FONT_HUD_VALUE = new Font("Arial", Font.BOLD, 18);
    public static final Font FONT_HUGE = new Font("Arial", Font.BOLD, 72);
}
