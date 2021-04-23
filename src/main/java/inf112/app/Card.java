package inf112.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

/* contains all information about cards */
public class Card {

    int priority;
    CardType type;



    /* every card belongs to one of the following types: */
    public enum CardType {
        MOVE1,
        MOVE2,
        MOVE3,
        BACKUP,
        ROTRIGHT,
        ROTLEFT,
        UTURN
    }

    public Card() {}

    /* constructor class which sets the priority and
    * type of the card (two necessary input values) */
    public Card(CardType type, int priority) {
        this.priority = priority;
        this.type = type;
    }

    public int getPriority() {
        return priority;
    }

    public CardType getType() {
        return type;
    }

    /**
     * Draws the current card and returns it as a Texture.
     */
    public Texture draw() {
        Pixmap pixmap;
        pixmap = new Pixmap(Gdx.files.internal("Card_" + type + ".png"));

        pixmap.drawPixmap(drawNumber(priority / 100), 44, 14);
        pixmap.drawPixmap(drawNumber((priority / 10) % 100), 52, 14);
        pixmap.drawPixmap(drawNumber(priority % 10), 60, 14);
        return new Texture(pixmap);
    }

    public static Pixmap drawNumber(int i) {
        i = i % 10;

        Pixmap pixmap = new Pixmap(6,9,Pixmap.Format.RGBA4444);
        pixmap.setColor(0,1,0,1);

        switch(i) {
            case(1):
                pixmap.drawLine(5,0,5,8);
                break;
            case(2):
                pixmap.drawLine(0,0,5,0);
                pixmap.drawLine(5,0,5,4);
                pixmap.drawLine(5,4,0,4);
                pixmap.drawLine(0,4,0,8);
                pixmap.drawLine(0,8,5,8);
                break;
            case(3):
                pixmap.drawLine(5,0,5,8);
                pixmap.drawLine(0,0,5,0);
                pixmap.drawLine(1,4,5,4);
                pixmap.drawLine(0,8,5,8);
                break;
            case(4):
                pixmap.drawLine(0,0,0,4);
                pixmap.drawLine(5,0,5,8);
                pixmap.drawLine(0,4,5,4);
                break;
            case(5):
                pixmap.drawLine(5,0,0,0);
                pixmap.drawLine(0,0,0,4);
                pixmap.drawLine(0,4,5,4);
                pixmap.drawLine(5,4,5,8);
                pixmap.drawLine(5,8,0,8);
                break;
            case(6):
                pixmap.drawLine(0,0,5,0);
                pixmap.drawLine(0,0,0,8);
                pixmap.drawLine(0,4,5,4);
                pixmap.drawLine(0,8,5,8);
                pixmap.drawLine(5,4,5,8);
                break;
            case(7):
                pixmap.drawLine(0,0,5,0);
                pixmap.drawLine(5,0,5,8);
                break;
            case(8):
                pixmap.drawLine(0,0,5,0);
                pixmap.drawLine(5,0,5,8);
                pixmap.drawLine(5,4,0,4);
                pixmap.drawLine(0,0,0,8);
                pixmap.drawLine(0,8,5,8);
                break;
            case(9):
                pixmap.drawLine(0,0,5,0);
                pixmap.drawLine(0,0,0,4);
                pixmap.drawLine(0,4,5,4);
                pixmap.drawLine(5,0,5,8);
                break;
            default:
                pixmap.drawLine(0,0,5,0);
                pixmap.drawLine(0,0,0,8);
                pixmap.drawLine(5,0,5,8);
                pixmap.drawLine(0,8,5,8);
                break;
        }

        return pixmap;
    }
}
