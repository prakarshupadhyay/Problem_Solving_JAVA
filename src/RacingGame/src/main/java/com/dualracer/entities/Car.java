package com.dualracer.entities;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public abstract class Car {

    protected double x;
    protected double y;
    protected int width;
    protected int height;
    protected double speed;

    public Car(double x, double y, int width, int height, double speed) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
    }

    public abstract void update(double dt);

    public abstract void render(Graphics2D g2d);

    public Rectangle2D.Double getBounds() {
        // slightly trimmed hitbox for forgiving, enjoyable arcade collision
        double insetX = width * 0.08;
        double insetY = height * 0.06;
        return new Rectangle2D.Double(x + insetX, y + insetY, width - 2 * insetX, height - 2 * insetY);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }
}
