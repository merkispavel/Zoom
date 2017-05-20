package main.model;

import com.sun.org.glassfish.gmbal.GmbalException;
import main.view.GamePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.security.Key;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Board implements Drawable, KeyListener{
    public static final int BALL_RADIUS = 20;
    public static final int CIRCLE_RADIUS = 300;
    public static final int VERTICAL_OFFSET = 50;
    public static final int HORIZONTAL_OFFSET = 400;
    public static final int CIRCLE_CENTER_X = CIRCLE_RADIUS + HORIZONTAL_OFFSET;
    public static final int CIRCLE_CENTER_Y = CIRCLE_RADIUS + VERTICAL_OFFSET;
    public static final int LIMIT_TIMER_COUNT = 66;
    public static final int LIMIT_BALL_RAW_SIZE = 3;
    public static final double ANGLE_BALL_FORWARD_SPEED = 0.002;
    public static final double ANGLE_BALL_SHIFTBACK_SPEED = -2 * ANGLE_BALL_FORWARD_SPEED;
    public static final double THROWN_BALL_RADIUS_SPEED = 15;
    public static final double CENTRAL_BALL_ANGLE = 2f * Math.asin((double) BALL_RADIUS / CIRCLE_RADIUS);
    public static final double EPS = 1e-6;
    private List<Ball> circleBalls = new ArrayList<>();
    private List<Ball> emittedBalls = new ArrayList<>();
    private BallEmitter ballEmitter;
    private int timerCount;
    private int score;
    private GamePanel gamePanel;
    private boolean nowShiftingBack;

    public Board(GamePanel panel) {
        gamePanel = panel;
        ballEmitter = new BallEmitter();
        spawnNewCircleBall();
    }

    private void spawnNewCircleBall() {
        circleBalls.add(new Ball(CIRCLE_RADIUS, 0,
                0, ANGLE_BALL_FORWARD_SPEED));
    }

    private boolean newCircleBallTime() {
        timerCount++;
        if (timerCount % LIMIT_TIMER_COUNT == 0) {
            timerCount %= LIMIT_TIMER_COUNT;
            return true;
        }
        return false;
    }

    private int getClosestCircleBallIndex(Ball ball) {
        int minDistanceIndex = 0;
        double minDistance = ball.distance(circleBalls.get(0));
        for (int i = 0; i < circleBalls.size(); i++) {
            double curDistance = ball.distance(circleBalls.get(i));
            if (curDistance < minDistance) {
                minDistanceIndex = i;
                minDistance = curDistance;
            }
        }
        return minDistanceIndex;
    }

    private void shiftBallsByAngle(List<Ball> list, int beginIndex,
                                 int endIndex, double delta) {
        if (beginIndex < 0 || endIndex >= list.size())
            throw new IndexOutOfBoundsException(
                    String.format("[%d, %d), size = %d", beginIndex, endIndex, list.size()));
        for (int i = beginIndex; i < endIndex; i++) {
            Ball ball = list.get(i);
            ball.setPolarAngle(ball.getPolarAngle() + delta);
        }
    }

    private void configureNewCircleBall(Ball newBall, int insertPosition) {
        newBall.setDeltaPolarRadius(0);
        newBall.setDeltaPolarAngle(ANGLE_BALL_FORWARD_SPEED);
        newBall.setPolarRadius(CIRCLE_RADIUS);
        if (insertPosition == circleBalls.size()) {
            newBall.setPolarAngle(
                    circleBalls.get(circleBalls.size() - 1).getPolarAngle() - CENTRAL_BALL_ANGLE);
        } else {
            newBall.setPolarAngle(circleBalls.get(insertPosition).getPolarAngle() + CENTRAL_BALL_ANGLE);
        }
    }

    private void removeCircleBallRaw(int beginIndex, int endIndex) {
        for (int i = endIndex - 1; i >= beginIndex; i--) {
            circleBalls.remove(i);
        }
    }

    private void setShiftBackSpeed(int endIndex) {
        for (int i = 0; i < endIndex; i++) {
            circleBalls.get(i).setDeltaPolarAngle(ANGLE_BALL_SHIFTBACK_SPEED);
        }
    }

    private void checkAndResolveCollisions() {
        if (emittedBalls.isEmpty())
            return;
        Ball lastEmittedBall = emittedBalls.get(emittedBalls.size() - 1);
        int closestCircleBallIndex = getClosestCircleBallIndex(lastEmittedBall);
        if (lastEmittedBall.distance(circleBalls.get(closestCircleBallIndex)) > 2.0f * BALL_RADIUS)
            return;
        emittedBalls.remove(emittedBalls.size() - 1);
        int insertPosition;
        if (lastEmittedBall.getPolarAngle() >
                circleBalls.get(closestCircleBallIndex).getPolarAngle()) {
            insertPosition = closestCircleBallIndex;
        } else {
            insertPosition = closestCircleBallIndex + 1;
        }
        shiftBallsByAngle(circleBalls, 0, insertPosition, CENTRAL_BALL_ANGLE);
        configureNewCircleBall(lastEmittedBall, insertPosition);
        circleBalls.add(insertPosition, lastEmittedBall);

        int l = insertPosition, r = insertPosition;
        Color curColor = circleBalls.get(insertPosition).getColor();
        while (l > 0 && circleBalls.get(l - 1).getColor().equals(curColor)) {
            --l;
        }
        while (r < circleBalls.size() - 1 && circleBalls.get(r + 1).getColor().equals(curColor)) {
            ++r;
        }
        if (r - l + 1 > LIMIT_BALL_RAW_SIZE) {
            score += r - l + 1;
            SoundManager.playShiftBackSound();
            removeCircleBallRaw(l, r + 1);
            if (l != 0) {
                nowShiftingBack = true;
                setShiftBackSpeed(l);
            }
        }
    }

    private boolean isBallShiftingBack(Ball ball) {
        return Math.abs(ball.getDeltaPolarAngle() - ANGLE_BALL_SHIFTBACK_SPEED) < EPS;
    }


    private int getMaxShiftingBackBallIndex() {
        for (int i = 0; i < circleBalls.size(); i++) {
            if (!isBallShiftingBack(circleBalls.get(i))) {
                return i - 1;
            }
        }
        return circleBalls.size() - 1;
    }

    private void setForwardSpeed(int endIndex) {
        for (int i = 0; i < endIndex; i++) {
            circleBalls.get(i).setDeltaPolarAngle(ANGLE_BALL_FORWARD_SPEED);
        }
    }


    private void checkShiftBackCollision() {
        int maxShiftingBackBallIndex = getMaxShiftingBackBallIndex();
        if (maxShiftingBackBallIndex == -1)
            return;
        if (maxShiftingBackBallIndex == circleBalls.size() - 1
                || circleBalls.get(maxShiftingBackBallIndex)
                .distance(circleBalls.get(maxShiftingBackBallIndex + 1)) > 2 * BALL_RADIUS) {
            return;
        }
        int l = maxShiftingBackBallIndex, r = maxShiftingBackBallIndex;
        Color curColor = circleBalls.get(maxShiftingBackBallIndex).getColor();
        while (l > 0 && circleBalls.get(l - 1).getColor().equals(curColor)) {
            --l;
        }
        while (r < circleBalls.size() - 1 && circleBalls.get(r + 1).getColor().equals(curColor)) {
            ++r;
        }
        if (r - l + 1 > LIMIT_BALL_RAW_SIZE && maxShiftingBackBallIndex != circleBalls.size() - 1) {
            score += r - l + 1;
            SoundManager.playShiftBackSound();
            removeCircleBallRaw(l, r + 1);
            if (l != 0) {
                nowShiftingBack = true;
                setShiftBackSpeed(l);
            } else {
                nowShiftingBack = false;
            }
        } else {
            setForwardSpeed(maxShiftingBackBallIndex + 1);
            nowShiftingBack = false;
        }
    }

    private void checkGameFinish() {
        Ball leadBall = circleBalls.get(0);
        if (leadBall.getPolarAngle() + CENTRAL_BALL_ANGLE / 2 >= 2 * Math.PI) {
            gamePanel.onGameFinished(score);
        }
    }

    public void frameTimerCallback(Graphics g) {
        checkGameFinish();
        checkShiftBackCollision();
        checkAndResolveCollisions();
        if (newCircleBallTime()) {
            spawnNewCircleBall();
        }
        draw(g);
    }

    private void throwBall() {
        Ball thrownBall = ballEmitter.throwBall();
        thrownBall.setDeltaPolarRadius(THROWN_BALL_RADIUS_SPEED);
        thrownBall.setDeltaPolarAngle(0);
        emittedBalls.add(thrownBall);
        SoundManager.playShootSound();
    }


    @Override
    public void draw(Graphics g) {
        ballEmitter.move();
        ballEmitter.draw(g);

        circleBalls.forEach(ball -> {
            ball.move();
            ball.draw(g);
        });

        emittedBalls.forEach(ball -> {
            ball.move();
            ball.draw(g);
        });
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            ballEmitter.enableCounterClockwiseRotate();
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            ballEmitter.enableClockwiseRotate();
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            throwBall();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT) {
            ballEmitter.disableRotate();
        }
    }
}
