package inf112.skeleton.app;

import java.util.ArrayList;
import java.util.List;

/* contains all information about a player */
public class Player {

    Robot robot;
    Deck cardList = new Deck();

    /* constructor class which initiates a new robot
    * alongside the player */
    public Player() {
        robot = new Robot();
    }

    public Robot getRobot() {
        return robot;
    }

    public Deck getCards() { return cardList; }
}
