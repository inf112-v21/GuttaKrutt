package inf112.skeleton.app;

import java.util.Collections;
import java.util.List;

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

    public GameLogic() {
        turn=0;
        deck = new Deck();
        buildDeck();
    }

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

    public void doTurn() {
        turn++;
    }

    public int getTurn() {
        return turn;
    }
}
