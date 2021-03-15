package inf112.app;

/* contains all information about a player */
public class Player {

    Robot robot;
    Deck cardList = new Deck();
    String name;

    /* constructor class which initiates a new robot
    * alongside the player */
    public Player() {
        robot = new Robot();
    }

    public Player(String name) {
        robot = new Robot();
        this.name = name;
    }

    public Robot getRobot() {
        return robot;
    }

    public Deck getCards() { return cardList; }

    public void setCards(Deck deck) { cardList = deck; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }
}
