package inf112.skeleton.app;

import java.util.ArrayList;
import java.util.List;

/* contains all information about a player */
public class Player {

    Robot robot;
    List<Card> cardList = new ArrayList<>();
    List<Card> registers = new ArrayList<>();

    /* constructor class which initiates a new robot
    * alongside the player */
    public Player() {
        robot = new Robot();
    }

    public Robot getRobot() {
        return robot;
    }

    public List<Card> getCards() { return cardList; }

    public List<Card> getRegisters() { return registers; }

    /* programs one register at a time */
    public void addRegister(Card c) { registers.add(c); }
}
