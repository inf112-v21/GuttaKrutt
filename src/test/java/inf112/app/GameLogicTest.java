package inf112.app;

import com.badlogic.gdx.Game;
import inf112.app.logic.BoardLogic;
import inf112.app.logic.GameLogic;
import inf112.app.networking.GameClient;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.*;

public class GameLogicTest {

    @Test
    public void shouldBuildDeck() {
        GameLogic game = new GameLogic();
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
    public void cardRunOutTest() {
        GameClient client = new GameClient(true);

        Map<UUID, Player> playerList = new HashMap<>();
        UUID uuid = UUID.randomUUID();
        playerList.put(uuid, new Player());
        playerList.put(UUID.randomUUID(), new Player());
        client.playerList = playerList;

        client.clientUUID = uuid;

        Map<String,int[][]> map = new HashMap<>();
        String[] layers = {"board","hole","flag","laser","wall","repair","Green cog","Red cog"};

        for (String s : layers)
            map.put(s,new int[5][5]);

        GameLogic gameLogic = new GameLogic(new BoardLogic(map,playerList),client);

        assertEquals(66,gameLogic.getDeck().size());
        gameLogic.doTurn();
        assertEquals(66,gameLogic.getDeck().size());
        gameLogic.doTurn();
        assertEquals(66,gameLogic.getDeck().size());
    }
}
