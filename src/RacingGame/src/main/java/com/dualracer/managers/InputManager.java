package com.dualracer.managers;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Manages simultaneous multi-key keyboard input using Swing Key Bindings
 * with WHEN_IN_FOCUSED_WINDOW to ensure rock-solid local multiplayer controls.
 */
public class InputManager {

    // Player 1 input flags
    private final AtomicBoolean p1Accelerate = new AtomicBoolean(false);
    private final AtomicBoolean p1Brake = new AtomicBoolean(false);
    private final AtomicBoolean p1SteerLeft = new AtomicBoolean(false);
    private final AtomicBoolean p1SteerRight = new AtomicBoolean(false);
    private final AtomicBoolean p1Nitro = new AtomicBoolean(false);

    // Player 2 input flags
    private final AtomicBoolean p2Accelerate = new AtomicBoolean(false);
    private final AtomicBoolean p2Brake = new AtomicBoolean(false);
    private final AtomicBoolean p2SteerLeft = new AtomicBoolean(false);
    private final AtomicBoolean p2SteerRight = new AtomicBoolean(false);
    private final AtomicBoolean p2Nitro = new AtomicBoolean(false);

    // System keys
    private final AtomicBoolean escapePressed = new AtomicBoolean(false);

    public InputManager() {}

    /**
     * Attaches key bindings to the specified Swing component.
     */
    public void registerBindings(JComponent component) {
        InputMap inputMap = component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = component.getActionMap();

        // Player 1 Bindings: W, S, A, D, SPACE
        bindKey(inputMap, actionMap, KeyEvent.VK_W, "P1_ACCEL", p1Accelerate);
        bindKey(inputMap, actionMap, KeyEvent.VK_S, "P1_BRAKE", p1Brake);
        bindKey(inputMap, actionMap, KeyEvent.VK_A, "P1_LEFT", p1SteerLeft);
        bindKey(inputMap, actionMap, KeyEvent.VK_D, "P1_RIGHT", p1SteerRight);
        bindKey(inputMap, actionMap, KeyEvent.VK_SPACE, "P1_NITRO", p1Nitro);

        // Player 2 Bindings: UP, DOWN, LEFT, RIGHT, SHIFT
        bindKey(inputMap, actionMap, KeyEvent.VK_UP, "P2_ACCEL", p2Accelerate);
        bindKey(inputMap, actionMap, KeyEvent.VK_DOWN, "P2_BRAKE", p2Brake);
        bindKey(inputMap, actionMap, KeyEvent.VK_LEFT, "P2_LEFT", p2SteerLeft);
        bindKey(inputMap, actionMap, KeyEvent.VK_RIGHT, "P2_RIGHT", p2SteerRight);
        bindKey(inputMap, actionMap, KeyEvent.VK_SHIFT, "P2_NITRO", p2Nitro);

        // Global Escape for Pause
        bindKeyPressOnly(inputMap, actionMap, KeyEvent.VK_ESCAPE, "SYS_PAUSE", escapePressed);
    }

    private void bindKey(InputMap inputMap, ActionMap actionMap, int keyCode, String keyName, AtomicBoolean targetFlag) {
        // Pressed
        String pressKey = keyName + "_PRESS";
        inputMap.put(KeyStroke.getKeyStroke(keyCode, 0, false), pressKey);
        actionMap.put(pressKey, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                targetFlag.set(true);
            }
        });

        // Released
        String releaseKey = keyName + "_RELEASE";
        inputMap.put(KeyStroke.getKeyStroke(keyCode, 0, true), releaseKey);
        actionMap.put(releaseKey, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                targetFlag.set(false);
            }
        });
    }

    private void bindKeyPressOnly(InputMap inputMap, ActionMap actionMap, int keyCode, String keyName, AtomicBoolean targetFlag) {
        String pressKey = keyName + "_PRESS";
        inputMap.put(KeyStroke.getKeyStroke(keyCode, 0, false), pressKey);
        actionMap.put(pressKey, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                targetFlag.set(true);
            }
        });
    }

    // Player 1 Getters
    public boolean isP1Accelerate() {
        return p1Accelerate.get();
    }

    public boolean isP1Brake() {
        return p1Brake.get();
    }

    public boolean isP1SteerLeft() {
        return p1SteerLeft.get();
    }

    public boolean isP1SteerRight() {
        return p1SteerRight.get();
    }

    public boolean isP1Nitro() {
        return p1Nitro.get();
    }

    // Player 2 Getters
    public boolean isP2Accelerate() {
        return p2Accelerate.get();
    }

    public boolean isP2Brake() {
        return p2Brake.get();
    }

    public boolean isP2SteerLeft() {
        return p2SteerLeft.get();
    }

    public boolean isP2SteerRight() {
        return p2SteerRight.get();
    }

    public boolean isP2Nitro() {
        return p2Nitro.get();
    }

    // System Getters & Resets
    public boolean pollEscapePressed() {
        return escapePressed.getAndSet(false);
    }

    public void resetAll() {
        p1Accelerate.set(false);
        p1Brake.set(false);
        p1SteerLeft.set(false);
        p1SteerRight.set(false);
        p1Nitro.set(false);

        p2Accelerate.set(false);
        p2Brake.set(false);
        p2SteerLeft.set(false);
        p2SteerRight.set(false);
        p2Nitro.set(false);

        escapePressed.set(false);
    }
}
