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
}