package com.dualracer.graphics;

import com.dualracer.entities.Player;
import com.dualracer.entities.TrafficCar;
import com.dualracer.entities.VehicleType;
import com.dualracer.utils.Constants;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

public class Renderer {

    private final RoadRenderer roadRenderer;

    public Renderer() {
        this.roadRenderer = new RoadRenderer();
    }

    public void setupRenderingHints(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
    }

    public void renderRoads(Graphics2D g2d, Player p1, Player p2) {
        roadRenderer.render(g2d, p1, p2);
    }

    public void renderPlayerCar(Graphics2D g2d, Player player) {
        if (!player.isBlinkingVisible()) {
            return; // Invulnerability blink
        }

        int x = (int) player.getX();
        int y = (int) player.getY();
        int w = player.getWidth();
        int h = player.getHeight();

        AffineTransform oldTx = g2d.getTransform();

        // Subtle rotation around car center for steering feel (tilt)
        double cx = x + w / 2.0;
        double cy = y + h / 2.0;
        double tiltAngle = Math.toRadians(player.getSteerTilt() * 5.0);
        g2d.rotate(tiltAngle, cx, cy);

        // 1. Soft Drop Shadow
        g2d.setColor(new Color(0, 0, 0, 75));
        g2d.fillRoundRect(x + 5, y + 8, w, h, 18, 18);

        // 2. Nitro Flames / Speed Exhaust Particles
        if (player.isNitroActive()) {
            drawNitroExhaust(g2d, x, y, w, h);
        }

        // 3. Wheels / Tires (4 wheels peeking slightly at corners)
        drawWheels(g2d, x, y, w, h);

        // 4. Car Main Body
        drawCarBody(g2d, x, y, w, h, player.getPrimaryColor(), player.getSecondaryColor());

        // 5. Headlight beams
        drawHeadlights(g2d, x, y, w, h);

        g2d.setTransform(oldTx);
    }

    public void renderTraffic(Graphics2D g2d, List<TrafficCar> trafficCars) {
        if (trafficCars == null || trafficCars.isEmpty()) {
            return;
        }
        for (TrafficCar car : trafficCars) {
            renderTrafficCar(g2d, car);
        }
    }

    public void renderTrafficCar(Graphics2D g2d, TrafficCar car) {
        int x = (int) car.getX();
        int y = (int) car.getY();
        int w = car.getWidth();
        int h = car.getHeight();

        // 1. Drop Shadow
        g2d.setColor(new Color(0, 0, 0, 70));
        g2d.fillRoundRect(x + 4, y + 6, w, h, 14, 14);

        // 2. Wheels / Tires
        if (car.getVehicleType() == VehicleType.TRUCK) {
            drawTruckWheels(g2d, x, y, w, h);
        } else {
            drawWheels(g2d, x, y, w, h);
        }

        // 3. Vehicle Body by Type
        switch (car.getVehicleType()) {
            case SPORTS -> drawSportsTraffic(g2d, x, y, w, h, car.getBodyColor(), car.getDetailColor());
            case SEDAN -> drawSedanTraffic(g2d, x, y, w, h, car.getBodyColor(), car.getDetailColor());
            case SUV -> drawSuvTraffic(g2d, x, y, w, h, car.getBodyColor(), car.getDetailColor());
            case TRUCK -> drawTruckTraffic(g2d, x, y, w, h, car.getBodyColor(), car.getDetailColor());
            case TAXI -> drawTaxiTraffic(g2d, x, y, w, h, car.getBodyColor(), car.getDetailColor());
        }
    }

    private void drawSportsTraffic(Graphics2D g2d, int x, int y, int w, int h, Color body, Color detail) {
        Color darker = body.darker();
        GradientPaint paint = new GradientPaint(x, y, body.brighter(), x + w, y + h, darker);
        g2d.setPaint(paint);
        g2d.fillRoundRect(x, y, w, h, 18, 18);

        // Border
        g2d.setColor(new Color(255, 255, 255, 50));
        g2d.setStroke(new BasicStroke(1.0f));
        g2d.drawRoundRect(x, y, w, h, 18, 18);

        // Racing Stripes
        g2d.setColor(detail);
        int mid = x + w / 2;
        g2d.fillRect(mid - 4, y + 2, 3, h - 4);
        g2d.fillRect(mid + 1, y + 2, 3, h - 4);

        // Windshield & Rear Window
        Color glass = new Color(20, 35, 50, 230);
        g2d.setColor(glass);
        g2d.fillRoundRect(x + 6, y + 18, w - 12, 14, 6, 6);
        g2d.fillRoundRect(x + 7, y + 46, w - 14, 10, 5, 5);

        // Roof
        g2d.setColor(darker);
        g2d.fillRect(x + 8, y + 32, w - 16, 14);

        // Spoiler
        g2d.setColor(new Color(30, 30, 35));
        g2d.fillRoundRect(x + 2, y + h - 6, w - 4, 6, 3, 3);

        // Lights
        drawStandardLights(g2d, x, y, w, h);
    }

    private void drawSedanTraffic(Graphics2D g2d, int x, int y, int w, int h, Color body, Color detail) {
        Color darker = body.darker();
        GradientPaint paint = new GradientPaint(x, y, body.brighter(), x, y + h, darker);
        g2d.setPaint(paint);
        g2d.fillRoundRect(x, y, w, h, 14, 14);

        g2d.setColor(new Color(255, 255, 255, 45));
        g2d.setStroke(new BasicStroke(1.0f));
        g2d.drawRoundRect(x, y, w, h, 14, 14);

        // Windshield & Rear Window
        Color glass = new Color(25, 40, 55, 220);
        g2d.setColor(glass);
        g2d.fillRoundRect(x + 5, y + 20, w - 10, 14, 6, 6);
        g2d.fillRoundRect(x + 6, y + 50, w - 12, 10, 5, 5);

        // Roof
        g2d.setColor(darker);
        g2d.fillRect(x + 7, y + 34, w - 14, 16);

        // Hood subtle line
        g2d.setColor(new Color(0, 0, 0, 40));
        g2d.drawLine(x + 6, y + 16, x + w - 6, y + 16);

        // Lights
        drawStandardLights(g2d, x, y, w, h);
    }

    private void drawSuvTraffic(Graphics2D g2d, int x, int y, int w, int h, Color body, Color detail) {
        Color darker = body.darker();
        GradientPaint paint = new GradientPaint(x, y, body.brighter(), x, y + h, darker);
        g2d.setPaint(paint);
        g2d.fillRoundRect(x, y, w, h, 12, 12);

        // Dark heavy bumper trim
        g2d.setColor(new Color(35, 38, 42));
        g2d.fillRect(x + 4, y, w - 8, 4);
        g2d.fillRect(x + 4, y + h - 4, w - 8, 4);

        // Windshield & Windows
        Color glass = new Color(20, 30, 45, 230);
        g2d.setColor(glass);
        g2d.fillRoundRect(x + 5, y + 22, w - 10, 15, 5, 5);
        g2d.fillRoundRect(x + 5, y + 58, w - 10, 12, 5, 5);

        // Roof Rack (2 silver longitudinal bars)
        g2d.setColor(new Color(190, 195, 205));
        g2d.fillRect(x + 8, y + 36, 3, 22);
        g2d.fillRect(x + w - 11, y + 36, 3, 22);
        g2d.drawLine(x + 11, y + 42, x + w - 11, y + 42);
        g2d.drawLine(x + 11, y + 52, x + w - 11, y + 52);

        // Lights
        drawStandardLights(g2d, x, y, w, h);
    }

    private void drawTruckTraffic(Graphics2D g2d, int x, int y, int w, int h, Color body, Color detail) {
        int cabH = 34;
        int hitchGap = 4;
        int containerY = y + cabH + hitchGap;
        int containerH = h - cabH - hitchGap;

        // 1. Truck Cab (front)
        GradientPaint cabPaint = new GradientPaint(x + 2, y, body.brighter(), x + w - 2, y + cabH, body.darker());
        g2d.setPaint(cabPaint);
        g2d.fillRoundRect(x + 2, y, w - 4, cabH, 10, 10);

        // Cab windshield
        g2d.setColor(new Color(25, 40, 55, 230));
        g2d.fillRoundRect(x + 6, y + 14, w - 12, 14, 4, 4);

        // Cab Sun Visor
        g2d.setColor(new Color(20, 20, 25));
        g2d.fillRect(x + 4, y + 4, w - 8, 4);

        // Side Mirrors
        g2d.setColor(new Color(40, 40, 45));
        g2d.fillRect(x - 3, y + 12, 4, 8);
        g2d.fillRect(x + w - 1, y + 12, 4, 8);

        // 2. Cargo Container (rear)
        GradientPaint containerPaint = new GradientPaint(x, containerY, new Color(235, 238, 242), x, containerY + containerH, new Color(195, 200, 208));
        g2d.setPaint(containerPaint);
        g2d.fillRoundRect(x, containerY, w, containerH, 6, 6);

        // Container corrugated ribs
        g2d.setColor(new Color(165, 172, 182));
        g2d.setStroke(new BasicStroke(1.2f));
        for (int ribY = containerY + 8; ribY < containerY + containerH - 6; ribY += 8) {
            g2d.drawLine(x + 3, ribY, x + w - 3, ribY);
        }

        // Rear Bumper Hazard Tape / Tailgate
        g2d.setColor(new Color(220, 40, 40));
        g2d.fillRect(x + 2, containerY + containerH - 4, w - 4, 3);
        g2d.setColor(Color.YELLOW);
        for (int hx = x + 4; hx < x + w - 6; hx += 8) {
            g2d.fillRect(hx, containerY + containerH - 4, 4, 3);
        }

        // Headlights on Cab
        g2d.setColor(new Color(255, 255, 210));
        g2d.fillOval(x + 4, y + 2, 7, 5);
        g2d.fillOval(x + w - 11, y + 2, 7, 5);
    }

    private void drawTruckWheels(Graphics2D g2d, int x, int y, int w, int h) {
        int wheelW = 8;
        int wheelH = 16;
        g2d.setColor(new Color(25, 25, 25));

        // Front steering axle
        g2d.fillRoundRect(x - 3, y + 12, wheelW, wheelH, 4, 4);
        g2d.fillRoundRect(x + w - wheelW + 3, y + 12, wheelW, wheelH, 4, 4);

        // Rear dual axles (4 wheels)
        g2d.fillRoundRect(x - 3, y + h - 36, wheelW, wheelH, 4, 4);
        g2d.fillRoundRect(x + w - wheelW + 3, y + h - 36, wheelW, wheelH, 4, 4);
        g2d.fillRoundRect(x - 3, y + h - 18, wheelW, wheelH, 4, 4);
        g2d.fillRoundRect(x + w - wheelW + 3, y + h - 18, wheelW, wheelH, 4, 4);
    }

    private void drawTaxiTraffic(Graphics2D g2d, int x, int y, int w, int h, Color body, Color detail) {
        Color darker = body.darker();
        GradientPaint paint = new GradientPaint(x, y, body.brighter(), x, y + h, darker);
        g2d.setPaint(paint);
        g2d.fillRoundRect(x, y, w, h, 14, 14);

        // Checkerboard decals down the side
        int checkerSize = 4;
        for (int cy = y + 14; cy < y + h - 16; cy += checkerSize * 2) {
            g2d.setColor(Color.BLACK);
            g2d.fillRect(x + 1, cy, checkerSize, checkerSize);
            g2d.fillRect(x + w - 1 - checkerSize, cy, checkerSize, checkerSize);
            g2d.setColor(Color.WHITE);
            g2d.fillRect(x + 1, cy + checkerSize, checkerSize, checkerSize);
            g2d.fillRect(x + w - 1 - checkerSize, cy + checkerSize, checkerSize, checkerSize);
        }

        // Windshield & Windows
        Color glass = new Color(25, 40, 55, 220);
        g2d.setColor(glass);
        g2d.fillRoundRect(x + 5, y + 20, w - 10, 14, 5, 5);
        g2d.fillRoundRect(x + 6, y + 50, w - 12, 10, 5, 5);

        // Roof
        g2d.setColor(darker);
        g2d.fillRect(x + 7, y + 34, w - 14, 16);

        // Illuminated "TAXI" Roof Sign
        g2d.setColor(new Color(255, 245, 170));
        g2d.fillRoundRect(x + w / 2 - 12, y + 37, 24, 9, 3, 3);
        g2d.setColor(new Color(180, 140, 20));
        g2d.drawRoundRect(x + w / 2 - 12, y + 37, 24, 9, 3, 3);
        g2d.setFont(new Font("Arial", Font.BOLD, 7));
        g2d.setColor(Color.BLACK);
        g2d.drawString("TAXI", x + w / 2 - 8, y + 44);

        // Lights
        drawStandardLights(g2d, x, y, w, h);
    }

    private void drawStandardLights(Graphics2D g2d, int x, int y, int w, int h) {
        // Headlights
        g2d.setColor(new Color(255, 255, 215));
        g2d.fillOval(x + 4, y + 2, 7, 5);
        g2d.fillOval(x + w - 11, y + 2, 7, 5);

        // Taillights
        g2d.setColor(new Color(240, 30, 30));
        g2d.fillRect(x + 4, y + h - 3, 8, 2);
        g2d.fillRect(x + w - 12, y + h - 3, 8, 2);
    }

    private void drawWheels(Graphics2D g2d, int x, int y, int w, int h) {
        int wheelW = 8;
        int wheelH = 18;
        g2d.setColor(new Color(25, 25, 25));

        // Front left & right
        g2d.fillRoundRect(x - 3, y + 14, wheelW, wheelH, 4, 4);
        g2d.fillRoundRect(x + w - wheelW + 3, y + 14, wheelW, wheelH, 4, 4);

        // Rear left & right
        g2d.fillRoundRect(x - 3, y + h - 28, wheelW, wheelH, 4, 4);
        g2d.fillRoundRect(x + w - wheelW + 3, y + h - 28, wheelW, wheelH, 4, 4);

        // Wheel silver rim details
        g2d.setColor(new Color(170, 175, 185));
        g2d.fillRect(x - 1, y + 19, 4, 8);
        g2d.fillRect(x + w - 3, y + 19, 4, 8);
        g2d.fillRect(x - 1, y + h - 23, 4, 8);
        g2d.fillRect(x + w - 3, y + h - 23, 4, 8);
    }

    private void drawCarBody(Graphics2D g2d, int x, int y, int w, int h, Color primary, Color secondary) {
        // Main aerodynamic chassis gradient
        Color lighterShade = primary.brighter();
        Color darkerShade = primary.darker();
        GradientPaint bodyPaint = new GradientPaint(
                x, y, lighterShade,
                x + w, y + h, darkerShade
        );
        g2d.setPaint(bodyPaint);
        g2d.fillRoundRect(x, y, w, h, 20, 20);

        // Tapered front nose
        Polygon nose = new Polygon();
        nose.addPoint(x + 4, y + 12);
        nose.addPoint(x + w / 2, y);
        nose.addPoint(x + w - 4, y + 12);
        g2d.fillPolygon(nose);

        // Chassis metallic border highlight
        g2d.setColor(new Color(255, 255, 255, 60));
        g2d.setStroke(new BasicStroke(1.2f));
        g2d.drawRoundRect(x, y, w, h, 20, 20);

        // Racing Stripes down the center
        g2d.setColor(secondary);
        int stripeW = 4;
        int stripeGap = 3;
        int midX = x + w / 2;
        g2d.fillRect(midX - stripeW - stripeGap / 2, y + 3, stripeW, h - 8);
        g2d.fillRect(midX + stripeGap / 2, y + 3, stripeW, h - 8);

        // Tinted Windshield (Glass)
        GradientPaint glassPaint = new GradientPaint(
                x, y + 22, new Color(25, 45, 65, 220),
                x, y + 38, new Color(10, 20, 35, 240)
        );
        g2d.setPaint(glassPaint);
        g2d.fillRoundRect(x + 6, y + 22, w - 12, 16, 8, 8);
        // Glass specular glare
        g2d.setColor(new Color(255, 255, 255, 90));
        g2d.drawLine(x + 9, y + 25, x + 18, y + 34);

        // Rear Window
        g2d.setPaint(glassPaint);
        g2d.fillRoundRect(x + 7, y + 52, w - 14, 12, 6, 6);

        // Cabin Roof
        g2d.setColor(darkerShade);
        g2d.fillRect(x + 9, y + 38, w - 18, 14);

        // Side Mirrors
        g2d.setColor(primary);
        g2d.fillRoundRect(x - 4, y + 25, 6, 8, 3, 3);
        g2d.fillRoundRect(x + w - 2, y + 25, 6, 8, 3, 3);

        // Rear Spoiler
        g2d.setColor(new Color(30, 30, 35));
        g2d.fillRoundRect(x + 2, y + h - 7, w - 4, 7, 4, 4);
        g2d.setColor(new Color(255, 255, 255, 50));
        g2d.drawRoundRect(x + 2, y + h - 7, w - 4, 7, 4, 4);

        // LED Taillights (Glowing Red)
        g2d.setColor(new Color(255, 30, 30));
        g2d.fillRect(x + 5, y + h - 2, 9, 3);
        g2d.fillRect(x + w - 14, y + h - 2, 9, 3);
    }

    private void drawHeadlights(Graphics2D g2d, int x, int y, int w, int h) {
        // Bright Xenon LED lamps
        g2d.setColor(new Color(255, 255, 220));
        g2d.fillOval(x + 5, y + 2, 8, 6);
        g2d.fillOval(x + w - 13, y + 2, 8, 6);

        // Subtle forward light projection
        GradientPaint lightBeamLeft = new GradientPaint(
                x + 9, y + 2, new Color(255, 255, 200, 50),
                x + 5, y - 50, new Color(255, 255, 200, 0)
        );
        g2d.setPaint(lightBeamLeft);
        Polygon beamLeft = new Polygon();
        beamLeft.addPoint(x + 6, y + 2);
        beamLeft.addPoint(x + 13, y + 2);
        beamLeft.addPoint(x + 20, y - 60);
        beamLeft.addPoint(x - 5, y - 60);
        g2d.fillPolygon(beamLeft);

        GradientPaint lightBeamRight = new GradientPaint(
                x + w - 9, y + 2, new Color(255, 255, 200, 50),
                x + w - 13, y - 50, new Color(255, 255, 200, 0)
        );
        g2d.setPaint(lightBeamRight);
        Polygon beamRight = new Polygon();
        beamRight.addPoint(x + w - 13, y + 2);
        beamRight.addPoint(x + w - 6, y + 2);
        beamRight.addPoint(x + w + 5, y - 60);
        beamRight.addPoint(x + w - 20, y - 60);
        g2d.fillPolygon(beamRight);
    }

    private void drawNitroExhaust(Graphics2D g2d, int x, int y, int w, int h) {
        int exhaustY = y + h + 2;
        int flameLen = 22 + (int) (Math.random() * 12);

        // Cyan/Electric Blue outer flame
        g2d.setColor(new Color(0, 200, 255, 180));
        g2d.fillOval(x + 10, exhaustY, 8, flameLen);
        g2d.fillOval(x + w - 18, exhaustY, 8, flameLen);

        // Bright white/yellow core
        g2d.setColor(new Color(255, 255, 255, 240));
        g2d.fillOval(x + 12, exhaustY, 4, flameLen / 2);
        g2d.fillOval(x + w - 16, exhaustY, 4, flameLen / 2);
    }
}
