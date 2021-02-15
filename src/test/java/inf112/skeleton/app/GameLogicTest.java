package inf112.skeleton.app;

import org.junit.Test;

import static org.junit.Assert.*;

public class GameLogicTest {

    @Test
    public void ShouldDoTurn() {
        GameLogic game = new GameLogic();
        game.doTurn();
        assertEquals(1, game.getTurn());
    }

    @Test
    public void ShouldDealCards() { }

    @Test
    public void ShouldProgramRegisters() { }

    @Test
    public void ShouldAskPowerDown() { }

    @Test
    public void ShouldCompleteRegisters() { }
}
