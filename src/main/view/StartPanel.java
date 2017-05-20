package main.view;


import sun.applet.Main;

import javax.swing.*;
import java.awt.*;

public class StartPanel extends JPanel {
    private static final String START_GAME_TEXT = "Start";
    private static final String EXIT_GAME_TEXT = "Exit";

    public StartPanel() {
        super();
        setLayout(new GridLayout(10, 5));
        JButton startGameButton = new JButton(START_GAME_TEXT);
        JButton exitGameButton = new JButton(EXIT_GAME_TEXT);

        startGameButton.setFocusable(false);
        exitGameButton.setFocusable(false);

        startGameButton.addActionListener(event -> {
            MainFrame frame = (MainFrame) MainFrame.getParentFrame(this);
            frame.startNewGame();
        });
        exitGameButton.addActionListener(event -> System.exit(0));

        for (int i = 0; i < 17; i++) {
            add(new JLabel(""));
        }
        add(startGameButton);
        for (int i = 0; i < 9; i++) {
            add(new JLabel(""));
        }
        add(exitGameButton);
        for (int i = 0; i < 21; i++) {
            add(new JLabel(""));
        }
    }
}
