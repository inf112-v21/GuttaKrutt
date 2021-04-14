package inf112.app.logic;

import com.badlogic.gdx.utils.Array;
import inf112.app.Card;
import inf112.app.Deck;
import inf112.app.Player;
import inf112.app.Robot;
import inf112.app.networking.GameClient;

import java.util.*;

/** controls the main loop of the game and executes the game rules */
public class GameLogic {
    BoardLogic boardLogic;
    int turn;
    Deck deck;
    Map<UUID, Player> playerList;
    GameClient client;
    UUID uuid;

    Card currentCard;

    public GameLogic() {this(null,null);}

    /** class constructor which sets the initial turn number, builds
    * a deck and initiates the players */
    public GameLogic(BoardLogic boardLogic, GameClient client) {
        this.boardLogic = boardLogic;
        this.client = client;
        turn=0;
        deck = new Deck();
        buildDeck();
        deck.shuffle();
        if (client != null) {
            playerList = client.getPlayerList();
            uuid = client.clientUUID;
            dealCards();
        }
    }

    /** builds a deck of cards according to the game rules
    * (18 move1, 12 move2, 6 move3, 6 backup (reverse), 18 rotate
    * cards both directions, 6 U-turn) */
    public void buildDeck() {
        for (int i=490; i<=660; i+=10) {
            Card c = new Card(Card.CardType.MOVE1, i);
            deck.insert(c);
        }
        for (int i=670; i<=780; i+=10) {
            Card c = new Card(Card.CardType.MOVE2, i);
            deck.insert(c);
        }
        for (int i=790; i<=840; i+=10) {
            Card c = new Card(Card.CardType.MOVE3, i);
            deck.insert(c);
        }
        for (int i=430; i<=480; i+=10) {
            Card c = new Card(Card.CardType.BACKUP, i);
            deck.insert(c);
        }
        for (int i=80; i<=420; i+=20) {
            Card c = new Card(Card.CardType.ROTRIGHT, i);
            deck.insert(c);
        }
        for (int i=70; i<=410; i+=20) {
            Card c = new Card(Card.CardType.ROTLEFT, i);
            deck.insert(c);
        }
        for (int i=10; i<=60; i+=10) {
            Card c = new Card(Card.CardType.UTURN, i);
            deck.insert(c);
        }
    }

    public Deck getDeck() {
        return deck;
    }

    /**
     * the main loop of the game where the player announces that
     * they are ready (after choosing their cards) and wait for
     * other players to press ready as well. Then, the cards are
     * played in the correct order. Players who did not choose
     * to power down receive new cards and a new round starts.
     */
    public void ready() {
        playerList.get(uuid).setReady(true);
        client.updatePlayer(uuid,playerList.get(uuid));

        loopTillOthersAreReady();

        doTurn();

        tryCatchSleep(2000);
        playerList.get(uuid).setReady(false);
        client.updatePlayer(uuid,playerList.get(uuid));
        for (Player player : playerList.values())
            System.out.println("player: " + player.getName() + " rot: " + player.getRobot().getRotation());

        if(checkIfGameConcluded()){
            System.out.println("A player has won");
            client.updatePlayer(uuid, playerList.get(uuid));
            if(!playerList.get(uuid).getRobot().getWon())
                System.out.println("You lost, loser!");
        }
    }

    public void doTurn() {
        //A. Reveal Program Cards
        //B. Robots Move
        //C. Board Elements Move
        //D. Lasers Fire
        //E. Touch Checkpoints

        for (int i=0;i<5;i++) {
            processCards(i); // B. Robots move
            boardElementsMove(); //C
            boardLogic.laserSpawner(); //D
            touchCheckpoints(); //E
        }

        dealCards();

        for (Player player : playerList.values()) {
            if (!player.getRobot().getAlive()) {
                player.getRobot().respawn();
            }
            player.getRobot().setPowerDown(false);
            boardLogic.checkForRepairs(player.getRobot());
        }

        turn++;
    }

    public void boardElementsMove() {
        tryCatchSleep(500);
        boardLogic.activateBlueConveyorBelt();
        tryCatchSleep(500);
        boardLogic.activateBlueConveyorBelt();
        boardLogic.activateYellowConveyorBelt();
        tryCatchSleep(500);
        boardLogic.activateGears();
    }

    public void touchCheckpoints() {
        for (Player player : playerList.values()) {
            boardLogic.checkForCheckpoints(player.getRobot());
        }
    }

    /**
     * runs after the player chooses to power down. Their next
     * round is then skipped and the robot's damage tokens are
     * discarded. Only works if the robot has one or more damage
     * tokens.
     */
    public void powerDown() {
        Robot robot = playerList.get(uuid).getRobot();
        if (robot.getDamage() > 0) {
            robot.setPowerDown(true);
            robot.discardDamage();
            client.updatePlayer(uuid,playerList.get(uuid));
        }
    }

    /**
     * Checks if any player has won the game, if yes, then every other player is killed.
     */
    public boolean checkIfGameConcluded() {
        for(Player player : playerList.values()){
            if(player.getRobot().checkWin()){
                for(Player p : playerList.values()){
                    if(p != player)
                        p.getRobot().setAlive(false);
                }
                return true;
            }
        }
        return false;
    }

    public void loopTillOthersAreReady() {
        boolean allPlayerReady = false;
        while(!allPlayerReady) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            allPlayerReady = true;
            for (Player player : playerList.values()) {
                if (!player.getReady()) {
                    allPlayerReady = false;
                    break;
                }
            }
        }
    }

    public int getTurn() {
        return turn;
    }

    public Map<UUID,Player> getPlayers() { return playerList; }

    /** deals cards to each player according to the number
    * of their damage tokens */
    public void dealCards() {
        for (Player p : playerList.values()) {
            if (!p.getRobot().getPowerDown()) {
                for (int i = 0; i < (9 - p.getRobot().getDamage()); i++) {
                    p.getCards().add(deck.take());
                }
            }
        }
    }

    public Card getCurrentCard() { return currentCard; }

    /**
     * When used in a sort, sorts a list of players by the priority of a card in their program register, starting with the biggest.
     * Which program registers to compare is inputted in the constructor.
     */
    private class CardComparator implements Comparator<Player> {
        int i;

        public CardComparator(int i) { this.i = i; }

        @Override
        public int compare(Player p1, Player p2) {
            Card card1 = p1.getRobot().getProgramRegister()[i];
            Card card2 = p2.getRobot().getProgramRegister()[i];
            if (card1 == null || card2 == null) {
                if (card1 != null) {
                    return 1;
                }
                if (card2 != null) {
                    return -1;
                }
                return 0;
            }
            return card2.getPriority() - card1.getPriority();
        }
    }

    /**
     * Executes the cards in the program register
     */
    public void processCards(int i) {
        Array<Player> queue = new Array<>();
        for (Player player : playerList.values()) {
        queue.add(player);
            deck.addAll(player.getCards());
            player.setCards(new Deck());
        }
        queue.sort(new CardComparator(i));
        for (Player player : queue) {
            Card card = player.getRobot().getProgramRegister()[i];
            if (card != null) {
                useCard(player.getRobot(), card);
                if (i < 9 - player.getRobot().getDamage()) {
                    deck.insert(card);
                    player.getRobot().getProgramRegister()[i] = null;
                }
            }
        }
        currentCard = null;
    }

    /**
     * Uses a specific card.
     * @param robot Robot that the card should be used on
     * @param card Card to be executed
     */
    private void useCard(Robot robot, Card card) {
        currentCard = card;
        switch(card.getType()) {
            case MOVE1: boardLogic.move(robot,1); break;
            case MOVE2: boardLogic.move(robot,2); break;
            case MOVE3: boardLogic.move(robot,3); break;
            case BACKUP: boardLogic.moveBack(robot); break;
            case ROTLEFT: robot.rotate(1); break;
            case ROTRIGHT: robot.rotate(-1); break;
            case UTURN: robot.rotate(2); break;
        }
        tryCatchSleep(500);
    }

    private void tryCatchSleep(int milliseconds) {
        try {
            Thread.sleep( milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
