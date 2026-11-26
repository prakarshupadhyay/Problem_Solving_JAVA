package com.dualracer.core;

import com.dualracer.graphics.GamePanel;
import com.dualracer.utils.Constants;

public class GameLoop implements Runnable {

    private final Game game;
    private final GamePanel panel;

    private volatile boolean running = false;
    private Thread gameThread;

    private static final long OPTIMAL_TIME_NS = 1_000_000_000L / Constants.TARGET_FPS;

    public GameLoop(Game game, GamePanel panel) {
        this.game = game;
        this.panel = panel;
    }

    public synchronized void start() {
        if (running) {
            return;
        }
        running = true;
        gameThread = new Thread(this, "DualRacer-GameLoop");
        gameThread.setDaemon(true);
        gameThread.start();
    }

    public synchronized void stop() {
        running = false;
        if (gameThread != null) {
            try {
                gameThread.join(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();

        while (running) {
            long now = System.nanoTime();
            long elapsedNs = now - lastTime;
            lastTime = now;

            double dt = elapsedNs / 1_000_000_000.0;
            // Cap delta time to prevent physics anomalies on frame hitch
            if (dt > 0.1) {
                dt = 0.1;
            }

            // 1. Update Game Physics & State
            game.update(dt);

            // 2. Request Swing Repaint
            panel.repaint();

            // 3. Sleep to target 60 FPS accurately
            long frameDurationNs = System.nanoTime() - now;
            long sleepNs = OPTIMAL_TIME_NS - frameDurationNs;

            if (sleepNs > 0) {
                try {
                    long sleepMs = sleepNs / 1_000_000L;
                    int sleepNanos = (int) (sleepNs % 1_000_000L);
                    Thread.sleep(sleepMs, sleepNanos);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            } else {
                // Yield thread if frame took slightly longer
                Thread.yield();
            }
        }
    }

    public boolean isRunning() {
        return running;
    }
}
