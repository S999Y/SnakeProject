package gameInterface;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.Timer;
import javax.swing.JPanel;
import gameComponent.Tile;

public class GamePanel extends JPanel implements ActionListener, KeyListener {

    private int boardWidth;
    private int boardHeight;
    private Tile snakeHead;
    private Tile foodTile;
    private final int tileSize = 25;
    private final Color snakeColor = Color.GREEN;
    private final Color foodColor = Color.RED;
    private Random random;
    private Timer gameLoop;
    private int velocityX;
    private int velocityY;
    private ArrayList<Tile> snakeBody;
    private int gameDelay = 100;
    private boolean gameOver;

    public GamePanel(int boardWidth, int boardHeight) {
        this.setDoubleBuffered(true);
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        createGamePanel();
        placeFood();

        // set no initial movement
        this.velocityX = 0;
        this.velocityY = 0;

        this.addKeyListener(this);
        this.setFocusable(true);

        this.gameLoop = new Timer(this.gameDelay, this);
        this.gameLoop.start();
    }

    public int getBoardWidth() {
        return boardWidth;
    }

    public void setBoardWidth(int boardWidth) {
        this.boardWidth = boardWidth;
    }

    public int getBoardHeight() {
        return boardHeight;
    }

    public void setBoardHeight(int boardHeight) {
        this.boardHeight = boardHeight;
    }

    public int getTileSize() {
        return tileSize;
    }

    private void createGamePanel() {
        this.setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        this.setBackground(Color.BLACK);

        this.snakeHead = new Tile(6, 6);
        this.foodTile = new Tile(0, 0);
        this.snakeBody = new ArrayList<>();
        this.random = new Random();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        // draw grid (x1,y1,x2,y2)
        int lines = this.boardHeight / this.tileSize;
        g.setColor(Color.DARK_GRAY);

        for (int i = 0; i <= lines; i++) {
            g.drawLine(i * tileSize, 0, i * tileSize, this.boardHeight);
            g.drawLine(0, i * tileSize, boardWidth, i * tileSize);
        }

        // square snake head
        g.setColor(this.snakeColor);
        g.fillRect(snakeHead.getX() * tileSize, snakeHead.getY() * tileSize, tileSize, tileSize);

        // snake body
        int bodySize = this.snakeBody.size();
        for (int i = 0; i < bodySize; i++) {
            Tile snakeBodyTile = this.snakeBody.get(i);
            g.fillRect(snakeBodyTile.getX() * this.tileSize, snakeBodyTile.getY() * this.tileSize, this.tileSize,
                    this.tileSize);
        }

        // food pain
        g.setColor(this.foodColor);
        g.fillRect(foodTile.getX() * tileSize, foodTile.getY() * tileSize, tileSize, tileSize);

        // score
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        g.setColor(Color.GREEN);
        if (this.gameOver) {
            g.setColor(Color.red);
            g.setFont(new Font("Arial", Font.BOLD, 38));
            g.drawString("Game Over! Score: " + this.snakeBody.size(), this.tileSize * 4,
                    this.tileSize * 9);
        } else {

            g.drawString("Score: " + String.valueOf(this.snakeBody.size()), this.tileSize - 16, this.tileSize);
        }

    }

    public void placeFood() {
        this.foodTile.setX(this.random.nextInt(this.boardWidth / this.tileSize));
        this.foodTile.setY(this.random.nextInt(this.boardHeight / this.tileSize));

        repaint();
    }

    public void move() {

        if (this.gameOver) {
            return;
        }
        // eat food
        if (collision(this.snakeHead, this.foodTile)) {

            this.snakeBody.add(new Tile(this.foodTile.getX(), this.foodTile.getY()));

            placeFood();
        }

        // snake body increase
        int bodySize = this.snakeBody.size();
        for (int i = bodySize - 1; i >= 0; i--) {
            Tile snakePart = this.snakeBody.get(i);

            if (i == 0) {
                snakePart.setX(this.snakeHead.getX());
                snakePart.setY(this.snakeHead.getY());
            } else {
                Tile prevSnakePart = this.snakeBody.get(i - 1);
                snakePart.setX(prevSnakePart.getX());
                snakePart.setY(prevSnakePart.getY());
            }
        }

        // snakeHead
        snakeHead.setX(snakeHead.getX() + this.velocityX);
        snakeHead.setY(snakeHead.getY() + this.velocityY);

        // game over logics
        int snakeSize = this.snakeBody.size();
        for (int i = 0; i < snakeSize; i++) {
            Tile snakePart = this.snakeBody.get(i);

            if (collision(snakePart, this.snakeHead)) {
                this.gameOver = true;
            }
        }

        if (this.snakeHead.getX() * this.tileSize < 0 || this.snakeHead.getX() * this.tileSize > this.boardWidth
                || this.snakeHead.getY() * this.tileSize < 0
                || this.snakeHead.getY() * this.tileSize > this.boardHeight) {
            this.gameOver = true;
        }

        // score
    }

    public boolean collision(Tile tile1, Tile tile2) {

        return tile1.getX() == tile2.getX() && tile1.getY() == tile2.getY();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();

        if (this.gameOver) {
            gameLoop.stop();
        }
    }

    public void restartGame() {
        this.snakeHead = new Tile(6, 6);
        this.snakeBody.clear();
        this.velocityX = 0;
        this.velocityY = 0;
        this.gameOver = false;
        placeFood();
        this.gameLoop.start();
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (this.gameOver && e.getKeyCode() == KeyEvent.VK_SPACE) {
            restartGame();
            return;
        }

        if (e.getKeyCode() == KeyEvent.VK_W && velocityY != 1) {
            this.velocityX = 0;
            this.velocityY = -1;
        } else if (e.getKeyCode() == KeyEvent.VK_S && velocityY != -1) {
            this.velocityX = 0;
            this.velocityY = 1;
        } else if (e.getKeyCode() == KeyEvent.VK_A && velocityX != 1) {
            this.velocityX = -1;
            this.velocityY = 0;
        } else if (e.getKeyCode() == KeyEvent.VK_D && velocityX != -1) {
            this.velocityX = 1;
            this.velocityY = 0;
        }
    }

    // for now we don't need bottom two
    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

}
