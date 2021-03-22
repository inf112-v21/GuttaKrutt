package inf112.app;

import inf112.app.logic.BoardLogic;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BoardLogicTest {

    int[][][] map;
    Map<UUID,Player> players;
    BoardLogic boardLogic;

    @Before
    public void setUp(){
        Map<UUID,Player> players = new HashMap<>();
        players.put(UUID.randomUUID(),new Player());
        SetUpEmptyMap(players);
    }

    public void SetUpEmptyMap(Map<UUID,Player> players) {
        this.players = players;
        this.map = new int[6][5][5];
        GenerateEmptyMap(map);
        this.boardLogic = new BoardLogic(map, players);
    }

    public void setUpMapWithXFlags(Map<UUID,Player> players, int x){
        //Max four flags
        if(x > 4)
            x = 4;
        this.players = players;
        this.map = new int[6][5][5];
        GenerateEmptyMap(map);
        for(int i = 0; i < x; i++){
            if(i==0)
                map[2][1][1] = 55;
            if(i==1)
                map[2][0][1] = 63;
            if(i==2)
                map[2][0][3] = 71;
            if(i==3)
                map[2][1][3] = 79;
        }
        this.boardLogic = new BoardLogic(map, players);
    }

    public void GenerateEmptyMap(int[][][] map) {
        for(int i=0; i<6; i++){
            for(int j=0; j<5; j++){
                for(int k=0; k<5; k++){
                    if(i==0)
                        map[i][j][k]=1;
                    else
                        map[i][j][k]=0;
                }
            }
        }
    }

    @Test
    public void LaserWallGeneratesLaser(){
        map[5][4][4] = 46;

        boardLogic.laserSpawner();

        assertEquals(1,map[4][4][4]);
        assertEquals(1,map[4][3][4]);
        assertEquals(1,map[4][2][4]);
        assertEquals(1,map[4][1][4]);
        assertEquals(1,map[4][0][4]);
    }

    @Test
    public void LaserBeamsCross() {
        map[5][4][2] = 46;
        map[5][2][0] = 37;

        boardLogic.laserSpawner();

        assertEquals(2,map[4][2][0]);
        assertEquals(2,map[4][2][1]);
        assertEquals(2,map[4][2][3]);

        assertEquals(3,map[4][2][2]);

        assertEquals(1,map[4][4][2]);
        assertEquals(1,map[4][3][2]);
        assertEquals(1,map[4][1][2]);
    }

    //@Test
    public void RobotsBlockLaserBeams() {
        Map<UUID,Player> players = new HashMap<>();
        for(int i=0; i<4; i++)
            players.put(UUID.randomUUID(),new Player());
        SetUpEmptyMap(players);
        map[5][4][4] = 46;
        map[5][4][0] = 37;
        map[5][0][0] = 38;
        map[5][0][4] = 45;
        int i = 0;
        for (Player player : players.values()) {
            switch (i) {
                case 0: player.getRobot().setPos(2,4);
                case 1: player.getRobot().setPos(4,2);
                case 2: player.getRobot().setPos(2,0);
                case 3: player.getRobot().setPos(0,2);
            }
            i++;
        }
        boardLogic.laserSpawner();

        assertEquals(0,map[4][1][4]);
        /*assertEquals(0,map[4][4][3]);
        assertEquals(0,map[4][3][0]);
        assertEquals(0,map[4][0][1]);*/
    }

    //@Test
    public void PlayerTakesDamageWhenMovingIntoLaser() {
        Map<UUID,Player> players = new HashMap<>();
        Player player = new Player();
        players.put(UUID.randomUUID(),player);
        SetUpEmptyMap(players);
        map[5][4][2] = 46;
        player.getRobot().setPos(2,1);
        boardLogic.movePlayer(player.getRobot(),2,2);

        assertEquals(9,player.getRobot().getDamage());
    }

    @Test
    public void playerWinsIfReachingFlagTest(){
        setUpMapWithXFlags(players, 1);
        for(Player player : players.values()){
            //Player doesn't win right away
            assertEquals(false, player.getRobot().getWon());

            //Moves player to flag 1
            boardLogic.movePlayer(player.getRobot(), 1,1);

            //Player should have won
            assertTrue(player.getRobot().getWon());
        }
    }

    @Test
    public void playerWinsIfReachingTwoFlagsInCorrectOrderTest(){
        setUpMapWithXFlags(players, 2);
        for(Player player : players.values()){
            //Player first moves on top of flag 2
            boardLogic.movePlayer(player.getRobot(), 0,1);
            assertEquals(63, map[2][0][1]);
            assertEquals(false, player.getRobot().getWon());

            //Then flag 1
            boardLogic.movePlayer(player.getRobot(), 1,0);
            assertEquals(55, map[2][1][1]);
            assertEquals(false, player.getRobot().getWon());

            //Then back to flag 2
            boardLogic.movePlayer(player.getRobot(), -1,0);

            //Now the player should have won
            assertTrue(player.getRobot().getWon());
        }
    }

    @Test
    public void playerWinsIfReachingThreeFlagsInCorrectOrderTest(){
        setUpMapWithXFlags(players, 3);
        for(Player player : players.values()){
            //Player first moves on top of flag 3
            boardLogic.movePlayer(player.getRobot(), 0,3);
            assertEquals(71, map[2][0][3]);
            assertEquals(false, player.getRobot().getWon());

            //Then flag 2
            boardLogic.movePlayer(player.getRobot(), 0,-2);
            assertEquals(63, map[2][0][1]);
            assertEquals(false, player.getRobot().getWon());

            //Then flag 1
            boardLogic.movePlayer(player.getRobot(), 1,0);
            assertEquals(false, player.getRobot().getWon());
            assertEquals(55, map[2][1][1]);

            //Then back to flag 2
            boardLogic.movePlayer(player.getRobot(), -1,0);
            assertEquals(false, player.getRobot().getWon());

            //Then back to flag 3
            boardLogic.movePlayer(player.getRobot(), 0, 2);

            //Now the player should have won
            assertTrue(player.getRobot().getWon());
        }
    }

    @Test
    public void playerWinsIfReachingFourFlagsInCorrectOrderTest(){
        setUpMapWithXFlags(players, 4);
        for(Player player : players.values()){
            //Player first moves on top of flag 4
            boardLogic.movePlayer(player.getRobot(), 1,3);
            assertEquals(79, map[2][1][3]);
            assertEquals(false, player.getRobot().getWon());

            //Then flag 3
            boardLogic.movePlayer(player.getRobot(), -1,0);
            assertEquals(71, map[2][0][3]);
            assertEquals(false, player.getRobot().getWon());

            //Then flag 2
            boardLogic.movePlayer(player.getRobot(), 0,-2);
            assertEquals(63, map[2][0][1]);
            assertEquals(false, player.getRobot().getWon());

            //Then flag 1
            boardLogic.movePlayer(player.getRobot(), 1,0);
            assertEquals(false, player.getRobot().getWon());
            assertEquals(55, map[2][1][1]);

            //Then back to flag 2
            boardLogic.movePlayer(player.getRobot(), -1,0);
            assertEquals(false, player.getRobot().getWon());

            //Then back to flag 3
            boardLogic.movePlayer(player.getRobot(), 0, 2);

            //Then back to flag 4
            boardLogic.movePlayer(player.getRobot(), 1, 0);

            //Now the player should have won
            assertTrue(player.getRobot().getWon());
        }
    }
}
