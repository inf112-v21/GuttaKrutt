package inf112.app;

import inf112.app.logic.BoardLogic;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.*;

public class ConveyorTest {

    Map<String, int[][]> map;
    Map<UUID, Player> players;
    BoardLogic boardLogic;

    @Before
    public void setUp() {
        Map<UUID, Player> players = new HashMap<>();
        players.put(UUID.randomUUID(), new Player());
        SetUpEmptyMap(players);
    }

    public void SetUpEmptyMap(Map<UUID, Player> players) {
        this.players = players;
        this.map = new HashMap<>();
        String[] layers = {"board", "hole", "flag", "laser", "wall", "repair", "Green cog", "Red cog", "Yellow conveyor belts", "Blue conveyor belts"};

        for (String s : layers)
            map.put(s, new int[5][5]);
        GenerateEmptyMap(map);
        this.boardLogic = new BoardLogic(map, players);
    }

    public void GenerateEmptyMap(Map<String, int[][]> map) {
        for (Map.Entry<String, int[][]> layer : map.entrySet()) {
            for (int j = 0; j < 5; j++) {
                for (int k = 0; k < 5; k++) {
                    if (layer.getKey().equals("board"))
                        layer.getValue()[j][k] = 1;
                    else
                        layer.getValue()[j][k] = 0;
                }
            }
        }
    }

    @Test
    public void ConveyorBeltsMoveOneTile() {
        map.get("Yellow conveyor belts")[0][0] = 49;
        for (Player player : players.values()) {
            player.getRobot().setPos(0,0);
            assertEquals(0, player.getRobot().getX());
            assertEquals(0, player.getRobot().getY());
            boardLogic.activateYellowConveyorBelt();
            assertEquals(0, player.getRobot().getX());
            assertEquals(1, player.getRobot().getY());
        }
    }

    @Test
    public void ConveyorBeltsMovesMultipleTiles() {
        map.get("Yellow conveyor belts")[0][0] = 49;
        map.get("Yellow conveyor belts")[0][1] = 49;
        map.get("Yellow conveyor belts")[0][2] = 49;
        map.get("Yellow conveyor belts")[0][3] = 49;

        for (Player player : players.values()) {
            player.getRobot().setPos(0,0);
            assertEquals(0, player.getRobot().getX());
            assertEquals(0, player.getRobot().getY());
            boardLogic.activateYellowConveyorBelt();
            boardLogic.activateYellowConveyorBelt();
            boardLogic.activateYellowConveyorBelt();
            boardLogic.activateYellowConveyorBelt();
            assertEquals(0, player.getRobot().getX());
            assertEquals(4, player.getRobot().getY());
        }
    }

    @Test
    public void ConveyorBeltsMovesRobotThroughComplicatedTrack() {
        map.get("Yellow conveyor belts")[1][1] = 52;
        map.get("Yellow conveyor belts")[2][1] = 52;
        map.get("Yellow conveyor belts")[3][1] = 42;
        map.get("Yellow conveyor belts")[3][2] = 34;
        map.get("Yellow conveyor belts")[2][2] = 43;
        map.get("Yellow conveyor belts")[2][3] = 49;
        map.get("Yellow conveyor belts")[2][4] = 35;
        map.get("Yellow conveyor belts")[3][4] = 52;
        map.get("Yellow conveyor belts")[4][4] = 33;
        map.get("Yellow conveyor belts")[4][3] = 50;


        for (Player player : players.values()) {
            player.getRobot().setPos(1,1);
            assertEquals(1, player.getRobot().getX());
            assertEquals(1, player.getRobot().getY());
            for(int i=0;i<10;i++)
                boardLogic.activateYellowConveyorBelt();
            assertEquals(4, player.getRobot().getX());
            assertEquals(2, player.getRobot().getY());
            assertEquals(1,player.getRobot().getRotation());
        }
    }

    @Test
    public void SouthToWestYellowConveyorRotatesAntiClockwise() {
        map.get("Yellow conveyor belts")[2][1] = 49;
        map.get("Yellow conveyor belts")[2][2] = 34;

        for (Player player : players.values()) {
            player.getRobot().setPos(2,1);
            assertEquals(0, player.getRobot().getRotation());
            boardLogic.activateYellowConveyorBelt();
            assertEquals(1, player.getRobot().getRotation());
        }
    }

    @Test
    public void EastToSouthYellowConveyorRotatesAntiClockwise() {
        map.get("Yellow conveyor belts")[3][2] = 51;
        map.get("Yellow conveyor belts")[2][2] = 33;

        for (Player player : players.values()) {
            player.getRobot().setPos(3,2);
            player.getRobot().rotate(1);
            assertEquals(1, player.getRobot().getRotation());
            boardLogic.activateYellowConveyorBelt();
            assertEquals(2, player.getRobot().getRotation());
        }
    }

    @Test
    public void NorthToEastYellowConveyorRotatesAntiClockwise() {
        map.get("Yellow conveyor belts")[2][3] = 50;
        map.get("Yellow conveyor belts")[2][2] = 41;

        for (Player player : players.values()) {
            player.getRobot().setPos(2,3);
            player.getRobot().rotate(2);
            assertEquals(2, player.getRobot().getRotation());
            boardLogic.activateYellowConveyorBelt();
            assertEquals(3, player.getRobot().getRotation());
        }
    }

    @Test
    public void WestToNorthYellowConveyorRotatesAntiClockwise() {
        map.get("Yellow conveyor belts")[1][2] = 52;
        map.get("Yellow conveyor belts")[2][2] = 42;

        for (Player player : players.values()) {
            player.getRobot().setPos(1,2);
            player.getRobot().rotate(3);
            assertEquals(3, player.getRobot().getRotation());
            boardLogic.activateYellowConveyorBelt();
            assertEquals(0, player.getRobot().getRotation());
        }
    }

    @Test
    public void SouthToWestBlueConveyorRotatesAntiClockwise() {
        map.get("Blue conveyor belts")[2][1] = 13;
        map.get("Blue conveyor belts")[2][2] = 18;

        for (Player player : players.values()) {
            player.getRobot().setPos(2,1);
            assertEquals(0, player.getRobot().getRotation());
            boardLogic.activateBlueConveyorBelt();
            assertEquals(1, player.getRobot().getRotation());
        }
    }

    @Test
    public void EastToSouthBlueConveyorRotatesAntiClockwise() {
        map.get("Blue conveyor belts")[3][2] = 22;
        map.get("Blue conveyor belts")[2][2] = 17;

        for (Player player : players.values()) {
            player.getRobot().setPos(3,2);
            player.getRobot().rotate(1);
            assertEquals(1, player.getRobot().getRotation());
            boardLogic.activateBlueConveyorBelt();
            assertEquals(2, player.getRobot().getRotation());
        }
    }

    @Test
    public void NorthToEastBlueConveyorRotatesAntiClockwise() {
        map.get("Blue conveyor belts")[2][3] = 21;
        map.get("Blue conveyor belts")[2][2] = 25;

        for (Player player : players.values()) {
            player.getRobot().setPos(2,3);
            player.getRobot().rotate(2);
            assertEquals(2, player.getRobot().getRotation());
            boardLogic.activateBlueConveyorBelt();
            assertEquals(3, player.getRobot().getRotation());
        }
    }

    @Test
    public void WestToNorthConveyorRotatesAntiClockwise() {
        map.get("Blue conveyor belts")[1][2] = 14;
        map.get("Blue conveyor belts")[2][2] = 26;

        for (Player player : players.values()) {
            player.getRobot().setPos(1,2);
            player.getRobot().rotate(3);
            assertEquals(3, player.getRobot().getRotation());
            boardLogic.activateBlueConveyorBelt();
            assertEquals(0, player.getRobot().getRotation());
        }
    }

    // høye

    @Test
    public void SouthToEastYellowConveyorRotatesClockwise() {
        map.get("Yellow conveyor belts")[2][1] = 49;
        map.get("Yellow conveyor belts")[2][2] = 35;

        for (Player player : players.values()) {
            player.getRobot().setPos(2,1);
            assertEquals(0, player.getRobot().getRotation());
            boardLogic.activateYellowConveyorBelt();
            assertEquals(3, player.getRobot().getRotation());
        }
    }

    @Test
    public void EastToNorthYellowConveyorRotatesClockwise() {
        map.get("Yellow conveyor belts")[3][2] = 51;
        map.get("Yellow conveyor belts")[2][2] = 43;

        for (Player player : players.values()) {
            player.getRobot().setPos(3,2);
            player.getRobot().rotate(-1);
            assertEquals(3, player.getRobot().getRotation());
            boardLogic.activateYellowConveyorBelt();
            assertEquals(2, player.getRobot().getRotation());
        }
    }

    @Test
    public void NorthToWestYellowConveyorRotatesClockwise() {
        map.get("Yellow conveyor belts")[2][3] = 50;
        map.get("Yellow conveyor belts")[2][2] = 44;

        for (Player player : players.values()) {
            player.getRobot().setPos(2,3);
            player.getRobot().rotate(-2);
            assertEquals(2, player.getRobot().getRotation());
            boardLogic.activateYellowConveyorBelt();
            assertEquals(1, player.getRobot().getRotation());
        }
    }

    @Test
    public void WestToSouthYellowConveyorRotatesClockwise() {
        map.get("Yellow conveyor belts")[1][2] = 52;
        map.get("Yellow conveyor belts")[2][2] = 36;

        for (Player player : players.values()) {
            player.getRobot().setPos(1,2);
            player.getRobot().rotate(-3);
            assertEquals(1, player.getRobot().getRotation());
            boardLogic.activateYellowConveyorBelt();
            assertEquals(0, player.getRobot().getRotation());
        }
    }

    @Test
    public void SouthToEastBlueConveyorRotatesClockwise() {
        map.get("Blue conveyor belts")[2][1] = 13;
        map.get("Blue conveyor belts")[2][2] = 19;

        for (Player player : players.values()) {
            player.getRobot().setPos(2,1);
            assertEquals(0, player.getRobot().getRotation());
            boardLogic.activateBlueConveyorBelt();
            assertEquals(3, player.getRobot().getRotation());
        }
    }

    @Test
    public void EastToNorthBlueConveyorRotatesClockwise() {
        map.get("Blue conveyor belts")[3][2] = 22;
        map.get("Blue conveyor belts")[2][2] = 27;

        for (Player player : players.values()) {
            player.getRobot().setPos(3,2);
            player.getRobot().rotate(-1);
            assertEquals(3, player.getRobot().getRotation());
            boardLogic.activateBlueConveyorBelt();
            assertEquals(2, player.getRobot().getRotation());
        }
    }

    @Test
    public void NorthToWestBlueConveyorRotatesClockwise() {
        map.get("Blue conveyor belts")[2][3] = 21;
        map.get("Blue conveyor belts")[2][2] = 28;

        for (Player player : players.values()) {
            player.getRobot().setPos(2,3);
            player.getRobot().rotate(-2);
            assertEquals(2, player.getRobot().getRotation());
            boardLogic.activateBlueConveyorBelt();
            assertEquals(1, player.getRobot().getRotation());
        }
    }

    @Test
    public void WestToSouthConveyorRotatesClockwise() {
        map.get("Blue conveyor belts")[1][2] = 14;
        map.get("Blue conveyor belts")[2][2] = 20;

        for (Player player : players.values()) {
            player.getRobot().setPos(1,2);
            player.getRobot().rotate(-3);
            assertEquals(1, player.getRobot().getRotation());
            boardLogic.activateBlueConveyorBelt();
            assertEquals(0, player.getRobot().getRotation());
        }
    }
}