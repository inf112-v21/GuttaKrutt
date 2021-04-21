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

    @Test
    public void shuffleWithSeed() {
        Deck deck1 = new Deck(25);
        Deck deck2 = new Deck(25);
        Card c1 = new Card(Card.CardType.MOVE3, 1);
        Card c2 = new Card(Card.CardType.BACKUP, 2);
        Card c3 = new Card(Card.CardType.MOVE2, 1);
        Card c4 = new Card(Card.CardType.ROTRIGHT, 2);
        Card c5 = new Card(Card.CardType.MOVE1, 1);
        Card c6 = new Card(Card.CardType.ROTLEFT, 2);
        deck1.add(c1);deck1.add(c2);deck1.add(c3);deck1.add(c4);deck1.add(c5);deck1.add(c6);
        deck2.add(c1);deck2.add(c2);deck2.add(c3);deck2.add(c4);deck2.add(c5);deck2.add(c6);
        deck1.shuffle();
        deck2.shuffle();
        assertEquals(deck1.take(),deck2.take());
        assertEquals(deck1.take(),deck2.take());
        assertEquals(deck1.take(),deck2.take());
        assertEquals(deck1.take(),deck2.take());
        assertEquals(deck1.take(),deck2.take());
        assertEquals(deck1.take(),deck2.take());
    }
}
