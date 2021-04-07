package inf112.app;

import com.badlogic.gdx.math.Vector2;
import inf112.app.logic.BoardLogic;
import inf112.app.logic.GameLogic;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CheckpointTest {
    /**
     * Checks if a destroyed player will be resurrected by respawn();
     */
    @Test
    public void respawnTest() {
        Robot robot = new Robot();
        robot.setAlive(false);
        robot.respawn();

        assert(robot.getAlive());
    }

    /**
     * Sets a checkpoint manually, and checks if robot respawns there.
     */
    @Test
    public void checkpointTest1() {
        Robot robot = new Robot();
        robot.setPos(0,0);
        robot.setCheckpoint(new Vector2(4,4));
        robot.respawn();

        assert(robot.getPos().equals(new Vector2(4,4)));
    }

    /**
     * Moves a player to a checkpoint and checks if the checkpoint is set and robot respawns there.
     */
    @Test
    public void checkpointTest2() {
        Map<String, int[][]> hashMap = new MapParser().fromFile("assets/Risky Exchange.tmx").getHashMap();

        Map<UUID,Player> playerList = new HashMap<>();
        UUID pUUID = UUID.randomUUID();
        playerList.put(pUUID, new Player());

        BoardLogic boardLogic = new BoardLogic(hashMap,playerList);

        Robot robot = playerList.get(pUUID).getRobot();

        robot.setPos(7,14);
        boardLogic.checkForFlagAndRepair(robot);
        robot.setPos(0,0);

        robot.setAlive(false);

        robot.respawn();
        assert(robot.getPos().equals(new Vector2(7,14)));
    }
}
