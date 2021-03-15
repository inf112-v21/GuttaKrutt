package inf112.app;

import static org.junit.Assert.*;
import org.junit.Test;

public class DeckTest {

    @Test
    public void ShouldInsertCard() {
        Deck deck = new Deck();
        Card c = new Card(Card.CardType.MOVE2, 1);
        deck.insert(c);
        assertEquals(1, deck.size());
    }

    @Test
    public void ShouldTakeCard() {
        Deck deck = new Deck();
        Card c1 = new Card(Card.CardType.MOVE3, 1);
        Card c2 = new Card(Card.CardType.BACKUP, 2);
        deck.insert(c1);
        deck.insert(c2);
        assertEquals(c2, deck.take());
        assertEquals(1, deck.size());
        assertEquals(c1, deck.take());
        assertEquals(0, deck.size());
    }
}
