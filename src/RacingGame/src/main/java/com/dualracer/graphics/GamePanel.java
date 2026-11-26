package com.dualracer.graphics;

import com.dualracer.core.Game;
import com.dualracer.managers.InputManager;
import com.dualracer.utils.Constants;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class GamePanel extends JPanel {

    private final Game game;
    private final InputManager inputManager;

    public GamePanel(Game game, InputManager inputManager) {
        super(true); // Double-buffered
        this.game = game;
        this.inputManager = inputManager;

        setPreferredSize(new Dimension(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT));
        setBackground(Constants.COLOR_ASPHALT);
        setFocusable(true);

        // Register rock-solid Swing Key Bindings on this component
        inputManager.registerBindings(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        game.render(g2d);
    }
}
