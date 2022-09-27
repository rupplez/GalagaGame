import java.awt.Image;

public class ShotSprite extends Sprite {
    private GalagaGame game;
    private Sprite owner;

    public ShotSprite(GalagaGame game, Sprite owner, Image image, int x, int y) {
        super(game, image, x, y);
        this.game = game;
        this.owner = owner;
        dy = 3 * owner.shootDir;
    }

    @Override
    public void move() {
        super.move();
        if (y < -100 || y > 700) { //
            game.removeSprite(this);
        }
    }

    @Override
    public void handleCollision(Sprite other) {
        if (!(other.getClass().getName().equals(this.owner.getClass().getName()))&&!(other.getClass().getName().equals("ShotSprite"))) { //
            //System.out.printf("%s %s\n", other.getClass().getName(), this.owner.getClass().getName());
            game.removeSprite(this);
            game.removeSprite(other);

            if(other.getClass().getName().equals("AlienSprite")) {
                game.addScore(100);
            } else {
                game.starShipAttacked();
            }
        }
    }
}
