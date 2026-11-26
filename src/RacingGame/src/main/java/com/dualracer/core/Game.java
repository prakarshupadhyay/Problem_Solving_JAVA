package com.dualracer.core;

import com.dualracer.entities.Player;
import com.dualracer.graphics.Renderer;
import com.dualracer.managers.InputManager;
import com.dualracer.managers.TrafficManager;
import com.dualracer.ui.HUD;
import com.dualracer.utils.Constants;

import java.awt.Color;
import java.awt.Graphics2D;

public class Game {

    private GameState state;
    private final GameConfig config;
    private final InputManager inputManager;
    private final Renderer renderer;
    private final HUD hud;

    private Player player1;
    private Player player2;
    private TrafficManager p1TrafficManager;
    private TrafficManager p2TrafficManager;
    private double matchTimer;

    public Game(GameConfig config, InputManager inputManager) {
        this.config = config;
        this.inputManager = inputManager;
        this.renderer = new Renderer();
        this.hud = new HUD();
        this.state = GameState.RACING; // Direct to racing for Phase 1 prototype
        initGame();
    }

    public void initGame() {
        // Player 1: Centered on Left Road (P1_ROAD_LEFT to P1_ROAD_RIGHT)
        double p1StartX = (Constants.P1_ROAD_LEFT + Constants.P1_ROAD_RIGHT) / 2.0 - (Constants.CAR_WIDTH / 2.0);
        double p1StartY = Constants.WINDOW_HEIGHT - 160.0;
        this.player1 = new Player(
                1,
                "Player 1",
                Constants.COLOR_P1_RED,
                Color.WHITE,
                p1StartX,
                p1StartY,
                Constants.P1_ROAD_LEFT,
                Constants.P1_ROAD_RIGHT - Constants.CAR_WIDTH
        );

        // Player 2: Centered on Right Road (P2_ROAD_LEFT to P2_ROAD_RIGHT)
        double p2StartX = (Constants.P2_ROAD_LEFT + Constants.P2_ROAD_RIGHT) / 2.0 - (Constants.CAR_WIDTH / 2.0);
        double p2StartY = Constants.WINDOW_HEIGHT - 160.0;
        this.player2 = new Player(
                2,
                "Player 2",
                Constants.COLOR_P2_BLUE,
                Color.WHITE,
                p2StartX,
                p2StartY,
                Constants.P2_ROAD_LEFT,
                Constants.P2_ROAD_RIGHT - Constants.CAR_WIDTH
        );

        // Independent Traffic Managers for each player's road
        this.p1TrafficManager = new TrafficManager(Constants.P1_ROAD_LEFT, Constants.P1_ROAD_RIGHT, config);
        this.p2TrafficManager = new TrafficManager(Constants.P2_ROAD_LEFT, Constants.P2_ROAD_RIGHT, config);

        this.matchTimer = config.getMatchDuration();
    }

    public void update(double dt) {
        if (state == GameState.RACING) {
            // Player 1 Input Update
            boolean p1Accel = inputManager.isP1Accelerate();
            boolean p1Brake = inputManager.isP1Brake();
            boolean p1Left = inputManager.isP1SteerLeft();
            boolean p1Right = inputManager.isP1SteerRight();
            boolean p1Nitro = inputManager.isP1Nitro();
            player1.update(dt, p1Accel, p1Brake, p1Left, p1Right, p1Nitro);

            // Player 2 Input Update
            boolean p2Accel = inputManager.isP2Accelerate();
            boolean p2Brake = inputManager.isP2Brake();
            boolean p2Left = inputManager.isP2SteerLeft();
            boolean p2Right = inputManager.isP2SteerRight();
            boolean p2Nitro = inputManager.isP2Nitro();
            player2.update(dt, p2Accel, p2Brake, p2Left, p2Right, p2Nitro);

            // Traffic updates for both roads independently
            p1TrafficManager.update(dt, player1.getSpeed(), player1);
            p2TrafficManager.update(dt, player2.getSpeed(), player2);

            // Timer update
            if (matchTimer > 0) {
                matchTimer = Math.max(0, matchTimer - dt);
            }
        }
    }

    public void render(Graphics2D g2d) {
        renderer.setupRenderingHints(g2d);

        // 1. Draw Roads & Divider
        renderer.renderRoads(g2d, player1, player2);

        // 2. Draw Traffic for Player 1 & Player 2
        renderer.renderTraffic(g2d, p1TrafficManager.getTrafficCars());
        renderer.renderTraffic(g2d, p2TrafficManager.getTrafficCars());

        // 3. Draw Player Cars
        renderer.renderPlayerCar(g2d, player1);
        renderer.renderPlayerCar(g2d, player2);

        // 4. Draw HUD
        hud.render(g2d, player1, player2, matchTimer);
    }

    // Getters
    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public TrafficManager getP1TrafficManager() {
        return p1TrafficManager;
    }

    public TrafficManager getP2TrafficManager() {
        return p2TrafficManager;
    }

    public GameConfig getConfig() {
        return config;
    }

    public double getMatchTimer() {
        return matchTimer;
    }
}
