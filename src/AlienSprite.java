import java.awt.Image;

public class AlienSprite extends Sprite {
    private GalagaGame game;

    public AlienSprite(GalagaGame game, Image image, int x, int y) {
        super(game, image, x, y);
        this.game = game;
        dx = -9;
        shootDir = 1;
    }

    @Override
    public void move() {
            if (((dx < 0) && (x < 10)) || ((dx > 0) && (x > 800))) {
                dx = -dx;
                if(dy==0) y += 10;
                if (y > 600) {
                    //game.minusScore(100);
                    game.endGame();
                }
            }
        super.move();
    }
}
