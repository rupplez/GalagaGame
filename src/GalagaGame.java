import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GalagaGame extends JPanel implements KeyListener {
    private boolean running = true;

    private ArrayList sprites = new ArrayList<>();
    private Sprite starship;

    private BufferedImage shotImage;
    private BufferedImage shipImage;
    private BufferedImage alienImage;

    private int score;
    private JLabel scoreLabel;
    private int life;

    public GalagaGame() {
        JFrame frame = new JFrame("Galaga Game");

        score = 0;
        life = 3;
        scoreLabel = new JLabel("SCORE : ");

        frame.setSize(800,600);
        frame.add(this);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            shotImage = ImageIO.read(new File("images/fire.png"));
            shipImage = ImageIO.read(new File("images/starship.png"));
            alienImage = ImageIO.read(new File("images/alien.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //frame.add(scoreLabel);

        this.requestFocus();
        this.initSprites();
        addKeyListener(this);
    }
    private void initSprites() {
        starship = new StarShipSprite(this, shipImage, 370, 550);
        this.addSprite(starship);
        for(int y=0;y<5;y++) {
            for(int x=0;x<12;x++) {
                Sprite alien = new AlienSprite(this, alienImage, 100+(x*50), 50+y*30);
                this.addSprite(alien);
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
    public void endGame() {
        System.out.println("GAME OVER");
        System.exit(0);
    }
    public void removeSprite(Sprite sprite) {
        sprites.remove(sprite);
    }
    public void starShipAttacked() {
        try{
            if(life>1) {
                Thread.sleep(2000);
                life--;
                startGame();
            } else {
                this.endGame();
            }
        } catch (Exception e) {

        }
    }
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.black);
        g.fillRect(0,0,800,600);
        for(int i=0;i<sprites.size();i++) {
            Sprite sprite = (Sprite)sprites.get(i);
            sprite.draw(g);
        }
    }
    public void addScore(int amount) {
        this.score += amount;
        System.out.printf("SCORE : %d\n",score);
    }
    public void gameLoop() {
        while(running) {
            Random r = new Random(); //
            for(int i=0;i<sprites.size();i++) {
                Sprite sprite = (Sprite)sprites.get(i);
                sprite.move();
                if(sprite instanceof AlienSprite && r.nextInt(1000)==1) {
                    sprite.fire(shotImage);
                }
            }
            for(int p=0;p<sprites.size();p++) {
                for(int s=p+1;s<sprites.size();s++) {
                    Sprite me = (Sprite)sprites.get(p);
                    Sprite other = (Sprite)sprites.get(s);

                    if(me.checkCollision(other)) {
                        me.handleCollision(other);
                        other.handleCollision(me);
                    }
                }
            }
            repaint();
            
            try{
                Thread.sleep(10);
            } catch (Exception e) {

            }
        }
    }
    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_LEFT)
            starship.setDx(-3);
        if(e.getKeyCode()==KeyEvent.VK_RIGHT)
            starship.setDx(+3);
        if(e.getKeyCode()==KeyEvent.VK_UP)
            starship.setDy(-3);
        if(e.getKeyCode()==KeyEvent.VK_DOWN)
            starship.setDy(+3);
        if(e.getKeyCode()==KeyEvent.VK_SPACE)
            starship.fire(shotImage);
        if(e.getKeyCode()==KeyEvent.VK_F5) //
            startGame();
    }
    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_LEFT||e.getKeyCode()==KeyEvent.VK_RIGHT)
            starship.setDx(0);
        if(e.getKeyCode()==KeyEvent.VK_UP||e.getKeyCode()==KeyEvent.VK_DOWN)
            starship.setDy(0);
    }
    @Override 
    public void keyTyped(KeyEvent e){
        
    }
    public static void main(String[] args) {
        GalagaGame g = new GalagaGame();
        g.gameLoop();
    }
}
