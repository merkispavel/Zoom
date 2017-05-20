package main.model;

import java.awt.*;


public class BallEmitter implements Drawable {
    public final double DELTA_ROTATE_ANGLE = 0.04;
    private AimLine aimLine;
    private Ball ball;


    public BallEmitter() {
        aimLine = new AimLine();
        spawnInitialBall();
    }

    public void spawnInitialBall() {
        ball = new Ball(0, 0, aimLine.getAngle(), aimLine.getDeltaAngle());
    }


    public Ball throwBall() {
        Ball thrownBall = ball;
        spawnInitialBall();
        return thrownBall;
    }

    private void setDeltaRotateAngle(double rotateAngle) {
        aimLine.setDeltaAngle(rotateAngle);
        ball.setDeltaPolarAngle(rotateAngle);
    }

    public void enableCounterClockwiseRotate() {
        setDeltaRotateAngle(DELTA_ROTATE_ANGLE);
    }

    public void enableClockwiseRotate() {
        setDeltaRotateAngle(-DELTA_ROTATE_ANGLE);
    }

    public void disableRotate() {
        setDeltaRotateAngle(0);
    }

    public void move() {
        aimLine.move();
        ball.move();
    }

    @Override
    public void draw(Graphics g) {
        aimLine.draw(g);
        ball.draw(g);
    }
}
