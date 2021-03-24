package inf112.app;

import inf112.app.logic.BoardLogic;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.*;

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

        assertEquals(0, map[3][2][4]);
        assertEquals(0,map[4][1][4]);
        /*assertEquals(0,map[4][4][3]);
        assertEquals(0,map[4][3][0]);
        assertEquals(0,map[4][0][1]);*/
    }

    @Test
    public void PlayerTakesDamageWhenMovingIntoLaser() {
        for(Player player : players.values()) {
            //Setting up laserwall and spawns laser
            map[5][4][2] = 46;
            boardLogic.laserSpawner();

            //Placing player one tile below laser and checking that there is zero damage
            player.getRobot().setPos(2, 1);
            assertEquals(0, player.getRobot().getDamage());

            //Moving into laser and checking if robot took damage
            boardLogic.movePlayer(player.getRobot(), 0, 1);
            assertEquals(1, player.getRobot().getDamage());
        }
    }

    @Test
    public void playerDiesIfMovingIntoLaserTenTimesTest(){
        for(Player player : players.values()) {
            //Setting up laserwall and spawns laser
            map[5][4][2] = 46;
            boardLogic.laserSpawner();

            //Placing player one tile below laser and checking that there is zero damage
            player.getRobot().setPos(2, 1);
            assertEquals(0, player.getRobot().getDamage());
            int playerDamage = 0;

            //Moving into laser ten times and asserting that the player has expected number
            //of damage tokens
            for(int i = 0; i < 20; i++) {
                if(i % 2 == 0){
                    playerDamage++;
                    boardLogic.movePlayer(player.getRobot(), 0, 1);
                    assertEquals(playerDamage, player.getRobot().getDamage());
                }
                else
                    boardLogic.movePlayer(player.getRobot(), 0, -1);
            }
            //Checking if player has max damage tokens, in which case the player should be dead
            assertEquals(10, player.getRobot().getDamage());
            assertFalse(player.getRobot().getAlive());
        }
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

    @Test
    public void playerCollisionTest(){
        Map<UUID,Player> players = new HashMap<>();
        for(int i=0; i<2; i++)
            players.put(UUID.randomUUID(),new Player());
        SetUpEmptyMap(players);

        int i = 0;
        //Placing player1 at pos = (1,2) and player2 at pos = (2,2)
        for(Player player : players.values()){
            if(i==0){
                player.getRobot().setPos(1,2);
            }
            if(i==1) {
                player.getRobot().setPos(2, 2);
            }
            i++;
        }

        i = 0;
        //Player1 moves into the tile player2 is in (2,2), which should push player2
        //from pos (2,2) to (3,2)
        for(Player player : players.values()){
            if(i==0){
                boardLogic.movePlayer(player.getRobot(), 1,0);
            }
            i++;
        }

        //Expected position for player1
        int expected1X = 2;
        int expected1Y = 2;
        //Expected position for player2
        int expected2X = 3;
        int expected2Y = 2;

        i=0;
        for(Player player : players.values()){
            if(i==0) {
                //Checking X value before collision is wrong
                assertEquals(false, player.getRobot().getX() == 1);
                //Checking new X value after collision is right
                assertEquals(expected1X, player.getRobot().getX());
                assertEquals(expected1Y, player.getRobot().getY());
            }
            if(i==1) {
                //Checking X value before collision is wrong
                assertEquals(false, player.getRobot().getX() == 2);
                //Checking new X value after collision is right
                assertEquals(expected2X, player.getRobot().getX());
                assertEquals(expected2Y, player.getRobot().getY());
            }
            i++;
        }
    }

    @Test
    public void playerTryingToPushOtherPlayerIntoWallTest(){
        Map<UUID,Player> players = new HashMap<>();
        for(int i=0; i<2; i++)
            players.put(UUID.randomUUID(),new Player());
        SetUpEmptyMap(players);

        map[5][2][2] = 16;

        int i = 0;
        //Placing player1 at pos = (1,2) and player2 at pos = (2,2)
        for(Player player : players.values()){
            if(i==0){
                player.getRobot().setPos(1,2);
            }
            if(i==1) {
                player.getRobot().setPos(2, 2);
            }
            i++;
        }

        //Player1 tries to move into the tile player2 is in (2,2), but the wall stops player1
        //from pushing player2, positions of both player should be the same as starting positions
        for(Player player : players.values()){
            if(i==0){
                boardLogic.movePlayer(player.getRobot(), 1,0);
            }
            i++;
        }

        //Expected position for player1
        int expected1X = 1;
        int expected1Y = 2;
        //Expected position for player2
        int expected2X = 2;
        int expected2Y = 2;

        i=0;
        for(Player player : players.values()){
            if(i==0) {
                //Checking X position player1 would be standing on if there was no wall is false
                assertEquals(false, player.getRobot().getX() == 2);
                //Checking X value after tried collision is right
                assertEquals(expected1X, player.getRobot().getX());
                assertEquals(expected1Y, player.getRobot().getY());
            }
            if(i==1) {
                //Checking X position player2 would be standing on if there was no wall is false
                assertEquals(false, player.getRobot().getX() == 3);
                //Checking X value after collision is right
                assertEquals(expected2X, player.getRobot().getX());
                assertEquals(expected2Y, player.getRobot().getY());
            }
            i++;
        }
    }
}
