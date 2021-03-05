package inf112.skeleton.app;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BoardLogicTest {

    int[][][] map;
    Robot[] robots;
    BoardLogic boardLogic;

    public void SetUpEmptyMap(Robot[] robots) {
        this.robots = robots;
        this.map = new int[6][5][5];
        GenerateEmptyMap(map);
        this.boardLogic = new BoardLogic(map, robots);
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
        Robot[] robots = new Robot[1];
        robots[0] = new Robot();
        SetUpEmptyMap(robots);
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
        Robot[] robots = new Robot[1];
        robots[0] = new Robot();
        SetUpEmptyMap(robots);
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

    @Test
    public void RobotsBlockLaserBeams() {
        Robot[] robots = new Robot[4];
        for(int i=0; i<4; i++)
            robots[i] = new Robot();
        SetUpEmptyMap(robots);
        map[5][4][4] = 46;
        map[5][4][0] = 37;
        map[5][0][0] = 38;
        map[5][0][4] = 45;
        robots[0].setPos(2,4);
        robots[1].setPos(4,2);
        robots[2].setPos(2,0);
        robots[3].setPos(0,2);
        boardLogic.laserSpawner();

        assertEquals(0,map[4][1][4]);
        /*assertEquals(0,map[4][4][3]);
        assertEquals(0,map[4][3][0]);
        assertEquals(0,map[4][0][1]);*/
    }

    public void PlayerTakesDamageWhenMovingIntoLaser() {
        Robot[] robots = new Robot[1];
        robots[0] = new Robot();
        SetUpEmptyMap(robots);
        map[5][4][2] = 46;
        robots[0].setPos(2,1);
        boardLogic.movePlayer(robots[0],2,2);

        assertEquals(9,robots[0].getDamage());
    }
}
