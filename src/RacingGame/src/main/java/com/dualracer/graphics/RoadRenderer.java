package com.dualracer.graphics;

import com.dualracer.entities.Player;
import com.dualracer.utils.Constants;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;

public class RoadRenderer {

    private static final int DASH_LENGTH = 44;
    private static final int DASH_GAP = 36;
    private static final int DASH_CYCLE = DASH_LENGTH + DASH_GAP; // 80px
    private static final int CURB_SEGMENT = 30;

    public RoadRenderer() {}

    public void render(Graphics2D g2d, Player p1, Player p2) {
        // 1. Draw entire background (grass)
        renderEnvironment(g2d, p1, p2);

        // 2. Draw Left Road (Player 1)
        renderSingleRoad(g2d, Constants.P1_ROAD_LEFT, Constants.P1_ROAD_RIGHT, p1.getRoadOffset());

        // 3. Draw Right Road (Player 2)
        renderSingleRoad(g2d, Constants.P2_ROAD_LEFT, Constants.P2_ROAD_RIGHT, p2.getRoadOffset());

        // 4. Draw Center Split Divider
        renderDivider(g2d);
    }

    private void renderEnvironment(Graphics2D g2d, Player p1, Player p2) {
        // Base grass fill
        g2d.setColor(Constants.COLOR_GRASS);
        g2d.fillRect(0, 0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);

        // Scrolling grass stripes for Player 1 side (0 to 640)
        double p1Offset = p1.getRoadOffset() % (CURB_SEGMENT * 2);
        for (int y = -CURB_SEGMENT * 2 + (int) p1Offset; y < Constants.WINDOW_HEIGHT + CURB_SEGMENT; y += CURB_SEGMENT * 2) {
            g2d.setColor(Constants.COLOR_GRASS_STRIPE);
            g2d.fillRect(0, y, Constants.P1_ROAD_LEFT, CURB_SEGMENT);
            g2d.fillRect(Constants.P1_ROAD_RIGHT, y, Constants.HALF_WIDTH - Constants.P1_ROAD_RIGHT, CURB_SEGMENT);
        }

        // Scrolling grass stripes for Player 2 side (640 to 1280)
        double p2Offset = p2.getRoadOffset() % (CURB_SEGMENT * 2);
        for (int y = -CURB_SEGMENT * 2 + (int) p2Offset; y < Constants.WINDOW_HEIGHT + CURB_SEGMENT; y += CURB_SEGMENT * 2) {
            g2d.setColor(Constants.COLOR_GRASS_STRIPE);
            g2d.fillRect(Constants.HALF_WIDTH, y, Constants.P2_ROAD_LEFT - Constants.HALF_WIDTH, CURB_SEGMENT);
            g2d.fillRect(Constants.P2_ROAD_RIGHT, y, Constants.WINDOW_WIDTH - Constants.P2_ROAD_RIGHT, CURB_SEGMENT);
        }
    }

    private void renderSingleRoad(Graphics2D g2d, int roadLeft, int roadRight, double roadOffset) {
        int roadWidth = roadRight - roadLeft;

        // 1. Curbs / Rumble strips on left and right shoulder
        int curbWidth = 12;
        int curbOffset = (int) (roadOffset % (CURB_SEGMENT * 2));
        for (int y = -CURB_SEGMENT * 2 + curbOffset; y < Constants.WINDOW_HEIGHT + CURB_SEGMENT; y += CURB_SEGMENT) {
            boolean isRed = ((y / CURB_SEGMENT) % 2) == 0;
            g2d.setColor(isRed ? new Color(210, 40, 40) : Color.WHITE);
            // Left shoulder curb
            g2d.fillRect(roadLeft - curbWidth, y, curbWidth, CURB_SEGMENT);
            // Right shoulder curb
            g2d.fillRect(roadRight, y, curbWidth, CURB_SEGMENT);
        }

        // 2. Dark Asphalt road surface with subtle asphalt gradient
        GradientPaint asphaltPaint = new GradientPaint(
                roadLeft, 0, new Color(34, 37, 42),
                roadRight, 0, new Color(42, 45, 50)
        );
        g2d.setPaint(asphaltPaint);
        g2d.fillRect(roadLeft, 0, roadWidth, Constants.WINDOW_HEIGHT);

        // 3. Road edge lines (solid yellow)
        g2d.setColor(Constants.COLOR_SHOULDER_LINE);
        g2d.setStroke(new BasicStroke(4.0f));
        g2d.drawLine(roadLeft + 2, 0, roadLeft + 2, Constants.WINDOW_HEIGHT);
        g2d.drawLine(roadRight - 2, 0, roadRight - 2, Constants.WINDOW_HEIGHT);

        // 4. Dashed lane markings (3 lanes => 2 dividing lines)
        double laneWidth = (double) roadWidth / Constants.LANES_PER_ROAD;
        int startY = (int) (roadOffset % DASH_CYCLE) - DASH_CYCLE;

        g2d.setStroke(new BasicStroke(3.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.setColor(Constants.COLOR_ASPHALT_LINE);

        for (int lane = 1; lane < Constants.LANES_PER_ROAD; lane++) {
            int laneX = (int) (roadLeft + lane * laneWidth);
            for (int y = startY; y < Constants.WINDOW_HEIGHT + DASH_CYCLE; y += DASH_CYCLE) {
                g2d.drawLine(laneX, y, laneX, y + DASH_LENGTH);
            }
        }
    }

    private void renderDivider(Graphics2D g2d) {
        int dividerX = Constants.HALF_WIDTH - (Constants.DIVIDER_WIDTH / 2);

        // Dark metallic body
        GradientPaint dividerPaint = new GradientPaint(
                dividerX, 0, new Color(15, 15, 20),
                dividerX + Constants.DIVIDER_WIDTH, 0, new Color(30, 32, 40)
        );
        g2d.setPaint(dividerPaint);
        g2d.fillRect(dividerX, 0, Constants.DIVIDER_WIDTH, Constants.WINDOW_HEIGHT);

        // Neon core stripe
        g2d.setColor(Constants.COLOR_DIVIDER_NEON);
        g2d.setStroke(new BasicStroke(2.0f));
        g2d.drawLine(Constants.HALF_WIDTH, 0, Constants.HALF_WIDTH, Constants.WINDOW_HEIGHT);
    }
}
