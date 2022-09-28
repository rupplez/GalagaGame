import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GalagaGame extends JPanel implements KeyListener {
    private boolean running = true;
    private static boolean start = false;
    private static boolean end = false;
    private static int clear = 0;

    public int aliens = 0;

    private ArrayList sprites = new ArrayList<>();
    private Sprite starship;

    private BufferedImage shotImage;
    private BufferedImage shipImage;
    private BufferedImage alienImage;
    private BufferedImage mainImage;
    private BufferedImage endImage;
    private BufferedImage clearImage;

    private int score;
    private JLabel scoreLabel;
    private int life;

    public GalagaGame() {
        JFrame frame = new JFrame("Galaga Game");
        score = 0;
        life = 3;
        scoreLabel = new JLabel("SCORE : ");

        frame.setSize(800, 600);
        frame.add(this);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            mainImage = ImageIO.read(new File("images/mainImage.png"));
            clearImage = ImageIO.read(new File("images/clear.png"));
            endImage = ImageIO.read(new File("images/gameover.jpg"));
            shotImage = ImageIO.read(new File("images/fire.png"));
            shipImage = ImageIO.read(new File("images/starship02.jpg"));
            alienImage = ImageIO.read(new File("images/alien.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // frame.add(scoreLabel);

        this.requestFocus();
        this.initSprites();
        addKeyListener(this);
    }

    private void initSprites() {
        starship = new StarShipSprite(this, shipImage, 370, 550);
        this.addSprite(starship);
        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 12; x++) {
                Sprite alien = new AlienSprite(this, alienImage, 100 + (x * 50), 50 + y * 30);
                this.addSprite(alien);
                aliens += 1;
            }
        }
    }

    public void addSprite(Sprite s) {
        sprites.add(s);
    }

    private void startGame() {
        sprites.clear();
        initSprites();
    }

    private void resetGame() {
        life = 3;
        score = 0;
        aliens = 0;
        sprites.clear();
        initSprites();
    }

    public void endGame() {
        System.out.println("GAME OVER");
        clear = 3;
        try {
            Thread.sleep(3000);
            clear = 0;
        } catch (Exception ex) {
        }
    }

    public void clearGame() {
        System.out.println("GAME CLEAR");
        clear = 2;
        try {
            Thread.sleep(3000);
            clear = 0;
        } catch (Exception ex) {
        }
    }

    public void removeSprite(Sprite sprite) {
        sprites.remove(sprite);
    }

    public void starShipAttacked() {
        try {
            if (life > 1) {
                Thread.sleep(2000);
                life--;
                aliens = 0;
                startGame();
            } else {
                endGame();
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void paint(Graphics g) {
        switch (clear) {
            case 0:
                super.paint(g);
                g.drawImage(mainImage, 0, 0, null);
                break;
            case 1:
                repaint();
                super.paint(g);
                g.setColor(Color.black);
                g.fillRect(0, 0, 800, 600);
                for (int i = 0; i < sprites.size(); i++) {
                    Sprite sprite = (Sprite) sprites.get(i);
                    sprite.draw(g);
                }
                break;
            case 2:
                repaint();
                super.paint(g);
                g.fillRect(0, 0, 800, 600);
                g.drawImage(clearImage, 0, 0, null);
                break;
            case 3:
                repaint();
                super.paint(g);
                g.fillRect(0, 0, 800, 600);
                g.drawImage(endImage, 0, 0, null);
                break;
        }
    }

    public void addScore(int amount) {
        this.score += amount;
        System.out.printf("SCORE : %d\n", score);
        System.out.printf("Aliens : %d\n", aliens);
    }

    public void gameLoop() {
        while (running) {
            Random r = new Random(); //
            for (int i = 0; i < sprites.size(); i++) {
                Sprite sprite = (Sprite) sprites.get(i);
                sprite.move();
                if (sprite instanceof AlienSprite && r.nextInt(1000) == 1) {
                    sprite.fire(shotImage);
                }

            }
            for (int p = 0; p < sprites.size(); p++) {
                for (int s = p + 1; s < sprites.size(); s++) {
                    Sprite me = (Sprite) sprites.get(p);
                    Sprite other = (Sprite) sprites.get(s);

                    if (me.checkCollision(other)) {
                        me.handleCollision(other);
                        other.handleCollision(me);
                    }
                }
            }

            repaint();

            try {
                Thread.sleep(10);
            } catch (Exception e) {

            }
            if (aliens == 0 && clear == 1) {
                clearGame();
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (clear == 0 && (e.getKeyCode() == KeyEvent.VK_SPACE)) {
            clear = 1;
            resetGame();
        } else if (clear == 1 && (e.getKeyCode() == KeyEvent.VK_SPACE)) {
            starship.fire(shotImage);
        }
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT -> starship.setDx(-3);
            case KeyEvent.VK_RIGHT -> starship.setDx(+3);
            case KeyEvent.VK_UP -> starship.setDy(-3);
            case KeyEvent.VK_DOWN -> starship.setDy(+3);
            case KeyEvent.VK_R -> resetGame();
            case KeyEvent.VK_ESCAPE -> System.exit(0);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT)
            starship.setDx(0);
        if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN)
            starship.setDy(0);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    public static void main(String[] args) {
        GalagaGame g = new GalagaGame();
        g.gameLoop();
    }
}
