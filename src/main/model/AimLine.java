package main.model;

import javafx.util.Pair;

import java.awt.*;

public class AimLine implements Drawable {
    public void setDeltaAngle(double deltaAngle) {
        this.deltaAngle = deltaAngle;
    }

    private double angle = 0;
    private double deltaAngle;

    public AimLine() {
    }

    public double getDeltaAngle() {
        return deltaAngle;
    }

    public double getAngle() {
        return angle;

    }

    public AimLine(double angle) {
        this.angle = angle;
    }


    public void move() {
        angle += deltaAngle;
    }


    @Override
    public void draw(Graphics g) {
        Pair<Integer, Integer> cartesian = Ball.polarToCartesian(Board.CIRCLE_RADIUS, angle);
        g.drawLine(Board.CIRCLE_CENTER_X + Board.BALL_RADIUS,
                Board.CIRCLE_CENTER_Y + Board.BALL_RADIUS,
                cartesian.getKey(), cartesian.getValue());
    }
}
