package main.view;

import main.model.Board;
import main.model.SoundManager;
import sun.applet.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class GamePanel extends JPanel implements ActionListener {
    private Board board;
    private Timer frameTimer;


    public GamePanel() {
        super();
        board = new Board(this);
        configureAndStartTimers();
        addKeyListener(board);
        setFocusable(true);
        requestFocusInWindow();
    }

    private void configureAndStartTimers() {
        frameTimer = new Timer(15, event -> {
            repaint();
        });
        frameTimer.start();
    }

    public void onGameFinished(int score) {
        SoundManager.playLoseSound();
        frameTimer.stop();
        Object[] options = new Object[] {
            "Play again",
                "Exit"
        };
        int n = JOptionPane.showOptionDialog(this,
                "Your score is " + Integer.toString(score),
                "",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);
        if (n == 1 || n == JOptionPane.CLOSED_OPTION) { // EXIT
            System.exit(0);
        } else { // Play again
            MainFrame frame = (MainFrame) MainFrame.getParentFrame(this);
            frame.startNewGame();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        board.frameTimerCallback(g);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}
