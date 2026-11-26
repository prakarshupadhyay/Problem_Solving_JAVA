package com.dualracer.ui;

import com.dualracer.entities.Player;
import com.dualracer.utils.Constants;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;

public class HUD {

    private static final int HUD_HEIGHT = 74;

    public HUD() {}

    public void render(Graphics2D g2d, Player p1, Player p2, double timeRemaining) {
        // 1. Semi-transparent dark banner background
        GradientPaint hudBg = new GradientPaint(
                0, 0, new Color(12, 14, 20, 230),
                0, HUD_HEIGHT, new Color(18, 22, 30, 210)
        );
        g2d.setPaint(hudBg);
        g2d.fillRect(0, 0, Constants.WINDOW_WIDTH, HUD_HEIGHT);

        // Bottom accent border
        g2d.setColor(new Color(60, 70, 90, 180));
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawLine(0, HUD_HEIGHT, Constants.WINDOW_WIDTH, HUD_HEIGHT);

        // 2. Center match timer & match title
        renderCenterTimer(g2d, timeRemaining);

        // 3. Render Player 1 HUD (Left side: 15 to 540)
        renderPlayerHUD(g2d, p1, 20, 8, Constants.COLOR_P1_RED, "PLAYER 1 (RED)", "W/A/S/D • SPACE (Nitro)");

        // 4. Render Player 2 HUD (Right side: 740 to 1260)
        renderPlayerHUD(g2d, p2, Constants.HALF_WIDTH + 35, 8, Constants.COLOR_P2_BLUE, "PLAYER 2 (BLUE)", "ARROWS • R-SHIFT (Nitro)");
    }

    private void renderPlayerHUD(Graphics2D g2d, Player player, int startX, int startY, Color themeColor, String label, String controlHint) {
        // Player badge pill
        g2d.setColor(themeColor);
        g2d.fillRoundRect(startX, startY, 130, 22, 10, 10);
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        g2d.drawString(label, startX + 10, startY + 16);

        // Control hint underneath badge
        g2d.setFont(new Font("Arial", Font.PLAIN, 10));
        g2d.setColor(new Color(180, 190, 205));
        g2d.drawString(controlHint, startX + 2, startY + 36);

        // Lives (Hearts)
        int heartX = startX + 145;
        int heartY = startY + 4;
        for (int i = 0; i < Constants.INITIAL_LIVES; i++) {
            boolean hasLife = i < player.getLives();
            drawHeart(g2d, heartX + (i * 20), heartY, hasLife ? new Color(255, 45, 65) : new Color(70, 75, 85));
        }

        // Speed Display
        int speedX = startX + 225;
        g2d.setFont(new Font("Arial", Font.BOLD, 22));
        g2d.setColor(Color.WHITE);
        String speedStr = player.getDisplaySpeedKmh() + " KM/H";
        g2d.drawString(speedStr, speedX, startY + 22);

        g2d.setFont(new Font("Arial", Font.PLAIN, 11));
        g2d.setColor(new Color(160, 175, 195));
        g2d.drawString("Speed", speedX, startY + 36);

        // Nitro Bar
        int nitroX = startX + 340;
        int nitroY = startY + 8;
        int barW = 100;
        int barH = 12;

        g2d.setColor(new Color(30, 35, 45));
        g2d.fillRoundRect(nitroX, nitroY, barW, barH, 6, 6);

        int fillW = (int) ((player.getNitro() / Constants.MAX_NITRO) * barW);
        if (fillW > 0) {
            Color nitroColor = player.isNitroActive() ? new Color(0, 245, 255) : new Color(0, 160, 240);
            g2d.setColor(nitroColor);
            g2d.fillRoundRect(nitroX, nitroY, fillW, barH, 6, 6);
        }
        g2d.setColor(new Color(100, 115, 140));
        g2d.setStroke(new BasicStroke(1.0f));
        g2d.drawRoundRect(nitroX, nitroY, barW, barH, 6, 6);

        g2d.setFont(new Font("Arial", Font.BOLD, 10));
        g2d.setColor(new Color(0, 220, 255));
        g2d.drawString("NITRO " + (int) player.getNitro() + "%", nitroX, startY + 34);

        // Distance & Score
        int scoreX = startX + 460;
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        g2d.setColor(new Color(255, 215, 0));
        g2d.drawString((int) player.getScore() + " pts", scoreX, startY + 18);

        g2d.setFont(new Font("Arial", Font.PLAIN, 11));
        g2d.setColor(new Color(200, 210, 220));
        g2d.drawString((int) player.getDistance() + " m", scoreX, startY + 34);
    }

    private void renderCenterTimer(Graphics2D g2d, double timeRemaining) {
        int centerX = Constants.HALF_WIDTH;

        // Circular or pill timer badge
        g2d.setColor(new Color(25, 28, 38));
        g2d.fillRoundRect(centerX - 48, 6, 96, 42, 12, 12);
        g2d.setColor(new Color(0, 240, 255, 140));
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawRoundRect(centerX - 48, 6, 96, 42, 12, 12);

        // Time text
        int seconds = (int) Math.ceil(Math.max(0, timeRemaining));
        String timeStr = String.format("%02d:%02d", seconds / 60, seconds % 60);

        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        g2d.setColor(seconds <= 10 ? new Color(255, 60, 60) : Color.WHITE);
        FontMetrics fm = g2d.getFontMetrics();
        int strW = fm.stringWidth(timeStr);
        g2d.drawString(timeStr, centerX - strW / 2, 32);

        // Subtle sub-label
        g2d.setFont(new Font("Arial", Font.BOLD, 9));
        g2d.setColor(new Color(150, 165, 180));
        String title = "TIME REMAINING";
        int titleW = g2d.getFontMetrics().stringWidth(title);
        g2d.drawString(title, centerX - titleW / 2, 44);

        // Game Title badge right below divider
        g2d.setColor(new Color(0, 240, 255));
        g2d.setFont(new Font("Arial", Font.BOLD, 11));
        String gameName = "DUAL RACER";
        int gnW = g2d.getFontMetrics().stringWidth(gameName);
        g2d.drawString(gameName, centerX - gnW / 2, 64);
    }

    private void drawHeart(Graphics2D g2d, int x, int y, Color color) {
        g2d.setColor(color);
        // Simple 14x14 heart polygon
        int[] hx = {x + 7, x + 13, x + 14, x + 13, x + 7, x + 1, x, x + 1};
        int[] hy = {y + 13, y + 6, y + 4, y + 1, y + 4, y + 1, y + 4, y + 6};
        g2d.fillPolygon(hx, hy, 8);
    }
}
