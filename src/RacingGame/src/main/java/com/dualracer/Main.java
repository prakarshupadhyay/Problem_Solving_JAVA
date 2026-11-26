package com.dualracer;

import com.dualracer.core.Game;
import com.dualracer.core.GameConfig;
import com.dualracer.core.GameLoop;
import com.dualracer.graphics.GamePanel;
import com.dualracer.managers.InputManager;
import com.dualracer.utils.Constants;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {

    public static void main(String[] args) {
        // Set cross-platform appearance & high DPI responsiveness
        System.setProperty("sun.java2d.opengl", "true");
        System.setProperty("sun.java2d.uiScale.enabled", "true");

        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // Fallback to default Swing Look and Feel
            }

            JFrame frame = new JFrame(Constants.GAME_TITLE + " — 2-Player Local Traffic Racing Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);

            GameConfig config = new GameConfig();
            InputManager inputManager = new InputManager();
            Game game = new Game(config, inputManager);
            GamePanel panel = new GamePanel(game, inputManager);

            frame.setContentPane(panel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            // Request keyboard focus for reliable input
            panel.requestFocusInWindow();

            // Start dedicated 60 FPS Game Loop
            GameLoop loop = new GameLoop(game, panel);
            loop.start();
        });
    }
}
