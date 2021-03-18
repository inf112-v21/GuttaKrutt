package inf112.app;

/* contains all information about a player */
public class Player {

    Robot robot;
    Deck cardList = new Deck();
    String name;
    Boolean ready;

    /* constructor class which initiates a new robot
    * alongside the player */
    public Player() {
        robot = new Robot();
    }

    public Player(String name) {
        robot = new Robot();
        this.name = name;
        this.ready = false;
    }

    public Boolean getReady() {return ready; }

    public void setReady(Boolean newReady) {ready = newReady; }

    public Robot getRobot() {
        return robot;
    }

    public void setRobot(Robot robot) { this.robot = robot; }

    public Deck getCards() { return cardList; }

    public void setCards(Deck deck) { cardList = deck; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }
}
