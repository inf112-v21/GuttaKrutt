package inf112.skeleton.app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/* controls the main loop of the game and executes the game rules */
public class GameLogic {

    int turn;
    Deck deck;
    List<Integer> move1_priority = Collections.nCopies(18, 1);
    List<Integer> move2_priority = Collections.nCopies(12, 1);
    List<Integer> move3_priority = Collections.nCopies(6, 1);
    List<Integer> backup_priority = Collections.nCopies(6, 1);
    List<Integer> rotright_priority = Collections.nCopies(18, 1);
    List<Integer> rotleft_priority = Collections.nCopies(18, 1);
    List<Integer> uturn_priority = Collections.nCopies(6, 1);
    List<Player> playerList = new ArrayList<>();

    /* class constructor which sets the initial turn number, builds
    * a deck and initiates the players */
    public GameLogic() {
        turn=0;
        deck = new Deck();
        buildDeck();
        for (int i=0; i<4; i++) {
            Player player = new Player();
            playerList.add(player);
        }
    }

    /* builds a deck of cards according to the game rules
    * (18 move1, 12 move2, 6 move3, 6 backup (reverse), 18 rotate
    * cards both directions, 6 U-turn) */
    public void buildDeck() {
        for (int i=0; i<18; i++) {
            Card c = new Card(Card.CardType.MOVE1, move1_priority.get(i));
            deck.insert(c);
        }
        for (int i=0; i<12; i++) {
            Card c = new Card(Card.CardType.MOVE2, move2_priority.get(i));
            deck.insert(c);
        }
        for (int i=0; i<6; i++) {
            Card c = new Card(Card.CardType.MOVE3, move3_priority.get(i));
            deck.insert(c);
        }
        for (int i=0; i<6; i++) {
            Card c = new Card(Card.CardType.BACKUP, backup_priority.get(i));
            deck.insert(c);
        }
        for (int i=0; i<18; i++) {
            Card c = new Card(Card.CardType.ROTRIGHT, rotright_priority.get(i));
            deck.insert(c);
        }
        for (int i=0; i<18; i++) {
            Card c = new Card(Card.CardType.ROTLEFT, rotleft_priority.get(i));
            deck.insert(c);
        }
        for (int i=0; i<6; i++) {
            Card c = new Card(Card.CardType.UTURN, uturn_priority.get(i));
            deck.insert(c);
        }
    }

    public Deck getDeck() {
        return deck;
    }

    /* the main loop of the game which starts a new turn,
    * deals cards to players and asks them to program registers. */
    public void doTurn() {
        turn++;
        dealCards();
        programRegisters();
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

    /* asks the players to choose five cards from the ones
    * dealt to them and put them in a specific order.
    * (at the moment, the players randomly choose the five cards
    * and their order) */
    public void programRegisters() {
        for (Player p : playerList) {
            List<Integer> registers = new ArrayList<>();
            int n = 5;
            for (int i=0; i<n; i++) {
                int randomNum = ThreadLocalRandom.current().nextInt(0,
                        p.getCards().size());
                if (!registers.contains(randomNum)) {
                    registers.add(randomNum);
                }
                else { n++; }
            }
            for (int i : registers) {
                p.addRegister(p.getCards().get(i));
            }
        }
    }
}
