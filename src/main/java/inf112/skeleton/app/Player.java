package inf112.skeleton.app;

import java.util.ArrayList;
import java.util.List;

public class Player {

    Robot robot;
    List<Card> cardList = new ArrayList<Card>();

    public Player() {
        robot = new Robot();
    }

    public Robot getRobot() {
        return robot;
    }

    public List<Card> getCards() { return cardList; }
}
