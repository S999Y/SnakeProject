package gameInterface;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

public class GameWindow {
    private int windowHeight = 613;
    private int windowWidth = 613;
    private JFrame window;
    private GamePanel gamePanel;

    public GameWindow() {
        this.gamePanel = new GamePanel(600, 600);
        createWindow();
        this.window.add(gamePanel, BorderLayout.CENTER);
        this.window.pack();
        this.window.setVisible(true);
        this.gamePanel.requestFocusInWindow();
    }

    public int getWindowHeight() {
        return windowHeight;
    }

    public void setWindowHeight(int windowHeight) {
        this.windowHeight = windowHeight;
    }

    public int getWindowWidth() {
        return windowWidth;
    }

    public void setWindowWidth(int windowWidth) {
        this.windowWidth = windowWidth;
    }

    public void createWindow() {
        this.window = new JFrame();
        this.window.setPreferredSize(new Dimension(this.windowWidth, this.windowHeight));
        this.window.setLocation(400, 150);
        this.window.setResizable(false);
        this.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.window.getContentPane().setBackground(new Color(68, 70, 69));
        this.window.setLayout(new BorderLayout());

    }

}
