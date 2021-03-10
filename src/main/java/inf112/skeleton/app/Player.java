package inf112.skeleton.app;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/* contains all information about a player */
public class Player {

    Robot robot;
    Deck cardList = new Deck();
    UUID uuid = UUID.randomUUID();

    /* constructor class which initiates a new robot
    * alongside the player */
    public Player() {
        robot = new Robot();
    }

    public Robot getRobot() {
        return robot;
    }

    public Deck getCards() { return cardList; }

    public UUID getID() { return uuid; }
}
