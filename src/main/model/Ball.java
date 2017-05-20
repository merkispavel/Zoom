package main.model;

import javafx.util.Pair;

import java.awt.*;


class Ball implements Drawable {
    private Color color;
    private double polarRadius;
    private double deltaPolarRadius;
    private double polarAngle;
    private double deltaPolarAngle;

    public static Pair<Integer, Integer> polarToCartesian(double radius, double angle) {
        int x = (int) (Board.CIRCLE_CENTER_X + Math.cos(angle) * radius);
        int y = (int) (Board.CIRCLE_CENTER_Y - Math.sin(angle) * radius);
        return new Pair<>(x, y);
    }

    public Ball(double polarRadius, double deltaPolarRadius, double polarAngle, double deltaPolarAngle) {
        this.polarRadius = polarRadius;
        this.deltaPolarRadius = deltaPolarRadius;
        this.polarAngle = polarAngle;
        this.deltaPolarAngle = deltaPolarAngle;
        color = RandomColorGenerator.getRandomColor();
    }

    public void setPolarRadius(double polarRadius) {
        this.polarRadius = polarRadius;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {

        return color;
    }

    public double getDeltaPolarRadius() {
        return deltaPolarRadius;
    }

    public double getDeltaPolarAngle() {
        return deltaPolarAngle;
    }

    public double getPolarAngle() {

        return polarAngle;

    }

    public void setDeltaPolarRadius(double deltaPolarRadius) {
        this.deltaPolarRadius = deltaPolarRadius;
    }

    public void setDeltaPolarAngle(double deltaPolarAngle) {
        this.deltaPolarAngle = deltaPolarAngle;
    }

    public double getPolarRadius() {
        return polarRadius;
    }

    public void setPolarAngle(double polarAngle) {

        this.polarAngle = polarAngle;
    }

    public double distance(Ball o) {
        Pair<Integer, Integer> a = polarToCartesian(polarRadius, polarAngle);
        Pair<Integer, Integer> b = polarToCartesian(o.polarRadius, o.polarAngle);
        double dx = Math.abs(a.getKey() - b.getKey());
        double dy = Math.abs(a.getValue() - b.getValue());
        return Math.sqrt(dx * dx + dy * dy);
    }

    public boolean overlaps(Ball o) {
        return distance(o) <= 2f * Board.BALL_RADIUS;
    }
    
    public void move() {
        polarRadius += deltaPolarRadius;
        polarAngle += deltaPolarAngle;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        Pair<Integer, Integer> cartesian = polarToCartesian(polarRadius, polarAngle);
        g.fillOval(cartesian.getKey(), cartesian.getValue(),
                2 * Board.BALL_RADIUS, 2 * Board.BALL_RADIUS);
    }
}
