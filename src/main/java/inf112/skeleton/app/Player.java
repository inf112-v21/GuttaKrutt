package inf112.skeleton.app;

import java.util.List;

public class Player {

    Robot robot;
    List<Card> cardList;

    public Player() {
        robot = new Robot();
    }

    public Robot getRobot() {
        return robot;
    }

    public List<Card> getCardList() { return cardList; }
}
