package inf112.skeleton.app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* controls the main loop of the game and executes the game rules */
public class GameLogic {

    GameScreen game;
    int turn;
    Deck deck;
    List<Player> playerList = new ArrayList<>();

    /* class constructor which sets the initial turn number, builds
    * a deck and initiates the players */
    public GameLogic(int players, GameScreen game) {
        this.game = game;
        turn=0;
        deck = new Deck();
        buildDeck();
        deck.shuffle();
        for (int i=0; i<players; i++) {
            Player player = new Player();
            playerList.add(player);
        }
        dealCards();
    }

    /* builds a deck of cards according to the game rules
    * (18 move1, 12 move2, 6 move3, 6 backup (reverse), 18 rotate
    * cards both directions, 6 U-turn) */
    public void buildDeck() {
        for (int i=490; i<=650; i+=10) {
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
        for (int i=430; i<=470; i+=10) {
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

    /* the main loop of the game which starts a new turn,
    * deals cards to players and asks them to program registers. */
    public void doTurn() {
        processCards();
        turn++;
        dealCards();
    }


    public int getTurn() {
        return turn;
    }

    public List<Player> getPlayers() { return playerList; }

    /* deals cards to each player according to the number
    * of their damage tokens */
    public void dealCards() {
        for (Player p : playerList) {
            for (int i=0; i < (9-p.getRobot().getDamage()); i++) {
                p.cardList.add(deck.take());
            }
        }
    }

    private void processCards() {
        List<List<Card>> cards = new ArrayList<>();
        for (Player player : playerList) {
            List<Card> programRegister = new ArrayList<>();
            for (Card card : player.getRobot().getProgramRegister()) {
                if (card != null) { programRegister.add(card); }
            }
            cards.add(programRegister);
        }

        boolean done = false;
        while(!done) {
            int nextRobot = 0;
            int highPriority = 0;
            for (int i = 0; i < cards.size(); i++) {
                int priority;
                if (cards.get(i).size() == 0) {
                    priority = 0;
                } else {
                    priority = cards.get(i).get(0).priority;
                }

                if (highPriority < priority) {
                    highPriority = priority;
                    nextRobot = i;
                }
            }
            if (highPriority == 0) {
                done = true;
                break;
            }

            Card card = cards.get(nextRobot).remove(0);
            useCard(playerList.get(nextRobot).getRobot(),card);
        }

        for (Player player : playerList) {
            player.getRobot().setProgramRegister(new Card[5]);
        }
    }

    private void useCard(Robot robot, Card card) {
        switch(card.type) {
            case MOVE1: game.boardLogic.move(robot,1); break;
            case MOVE2: game.boardLogic.move(robot,2); break;
            case MOVE3: game.boardLogic.move(robot,3); break;
            case BACKUP: game.boardLogic.moveBack(robot); break;
            case ROTLEFT: robot.rotate(1); break;
            case ROTRIGHT: robot.rotate(-1); break;
            case UTURN: robot.rotate(2); break;
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
