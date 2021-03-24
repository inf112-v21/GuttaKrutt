package inf112.app;

import static org.junit.Assert.*;
import org.junit.Test;

public class CardTest {

    @Test
    public void ShouldGetPriority() {
        Card c = new Card(Card.CardType.MOVE1, 1);
        assertEquals(1, c.getPriority());
    }

    @Test
    public void ShouldGetType() {
        Card c = new Card(Card.CardType.MOVE1, 1);
        assertEquals(Card.CardType.MOVE1, c.getType());
    }

    @Test
    public void cardPriorityCorrectTest(){
        Card card1 = new Card(Card.CardType.MOVE1, 1);
        Card card2 = new Card(Card.CardType.MOVE1, 2);
        assertTrue(card2.getPriority() > card1.getPriority());
    }
}
