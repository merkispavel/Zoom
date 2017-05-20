package main.view;

import javax.swing.*;
import java.awt.*;


public class MainFrame extends JFrame {
    private GamePanel gamePanel;
    private StartPanel startPanel;

    public MainFrame(String title) throws HeadlessException {
        super(title);
        setContentPane(startPanel = new StartPanel());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
    }

    public static JFrame getParentFrame(JComponent component) {
        return (JFrame) SwingUtilities.getWindowAncestor(component);
    }


    public void startNewGame() {
        setContentPane(gamePanel = new GamePanel());
        revalidate();
        gamePanel.requestFocus();
    }
}
