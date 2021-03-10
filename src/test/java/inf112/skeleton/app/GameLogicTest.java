package inf112.skeleton.app;

import org.junit.Test;

import static org.junit.Assert.*;

public class GameLogicTest {

    @Test
    public void ShouldBuildDeck() {
        GameLogic game = new GameLogic(4, null);
        Deck deck = game.getDeck();
        int move1=0, move2=0, move3=0, backup=0, rotright=0, rotleft=0, uturn=0;
        for (Card c : deck) {
            switch(c.getType()) {
                case MOVE1:
                    move1++;
                    break;
                case MOVE2:
                    move2++;
                    break;
                case MOVE3:
                    move3++;
                    break;
                case BACKUP:
                    backup++;
                    break;
                case ROTRIGHT:
                    rotright++;
                    break;
                case ROTLEFT:
                    rotleft++;
                    break;
                case UTURN:
                    uturn++;
                    break;
            }
        }
        assertEquals(18, move1);
        assertEquals(12, move2);
        assertEquals(6, move3);
        assertEquals(6, backup);
        assertEquals(18, rotright);
        assertEquals(18, rotleft);
        assertEquals(6, uturn);
    }

    @Test
    public void ShouldDoTurn() {
        GameLogic game = new GameLogic(4, null);
        game.doTurn();
        assertEquals(1, game.getTurn());
    }

    @Test
    public void ShouldDealCards() {
        GameLogic game = new GameLogic(4, null);
        game.doTurn();
        for (int i=0; i<game.getPlayers().size(); i++){
            assertEquals(9, game.getPlayers().get(i).getCards().size());
        }
    }

    @Test
    public void ShouldAskPowerDown() { }

    @Test
    public void ShouldCompleteRegisters() { }
}
