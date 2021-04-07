package inf112.app.logic;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import inf112.app.MapParser;
import inf112.app.Player;
import inf112.app.Robot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BoardLogic extends InputAdapter {
    Map<UUID,Player> players;
    Map<String,int[][]> map;

    public BoardLogic(Map<String,int[][]> map, Map<UUID, Player> players) {
        this.players = players;
        this.map = map;
        for(Player player : players.values()){
            setFlagPositions(player.getRobot());
        }
    }

    public BoardLogic(TiledMap tiledMap, Map<UUID, Player> players) {
        this.players = players;
        MapParser mapParser = new MapParser().fromTiledMap(tiledMap);
        this.map = mapParser.getHashMap();
        int i = 0; Vector2[] startingSpots = getStartingSpots();
        for(Player player : players.values()){
            setFlagPositions(player.getRobot());
            player.getRobot().setPos(startingSpots[i]);
            i++;
        }
    }

    public Map<String,int[][]> getMap() { return map; }

    /**
     * Moves the input robot by old x-position + input x, and old y-position + input y.
     */
    public void movePlayer(Robot robot, int x, int y) {
        Vector2 oldPos = robot.getPos();
        int oldX = (int) oldPos.x;
        int oldY = (int) oldPos.y;
        int newX = oldX + x;
        int newY = oldY + y;
        if (!robot.getAlive() || robot.getWon()) {
            System.out.println("inf112.skeleton.app.Player cannot move, game is over.");
            return;
        }
        int direction;
        switch (x + "" + y) {
            case "-10": direction = 1; break;
            case "0-1": direction = 2; break;
            case "10": direction = 3; break;
            default: direction = 0; break;
        }

        Robot enemy = checkForRobot(newX, newY);
        if(enemy != null){
            movePlayer(enemy, (newX-oldX), (newY-oldY));
        }
        if (!getWall(oldX,oldY)[direction] && !getWall(newX,newY)[(direction + 2) % 4] && checkForRobot(newX, newY) == null) {
            robot.setPos(new Vector2(newX, newY));

            checkTile(robot);
        }
        laserSpawner();
        if(robot.getDamage()==10)
            robot.setAlive(false);
    }

    /**
     * Checks the tile the robot is currently on for environmental objects,
     * such as holes, flags, wrenches etc. and applies the effect of the
     * environmental object on the robot.
     */
    public void checkTile(Robot robot) {
        int x = robot.getX();
        int y = robot.getY();

        boolean outSideBorder = (x >= map.get("board").length || x < 0 || y >= map.get("board")[0].length || y < 0);

        if(outSideBorder) {
            robot.setAlive(false);
            System.out.println("inf112-skeleton.app.Player has died outside the border");
        } else {
            int hole = map.get("hole")[x][y];
            int flag = map.get("flag")[x][y];
            int creenCog = map.get("Green cog")[x][y];
            int redCog = map.get("Red cog")[x][y];

            if (hole != 0) {
                robot.setAlive(false);
                System.out.println("inf112.skeleton.app.Player has died.");
            }
            if (flag != 0 && checkFlags(map.get("flag")[x][y], robot)) {
                robot.getFlagVisits().put(map.get("flag")[x][y], true);
                System.out.println("You got the flag!");
                if(robot.checkWin()){
                    robot.setWon(true);
                    System.out.println("You won!");
                }
            }
            if (creenCog != 0){
                greenCogRotate(robot);
            }
            if (redCog != 0){
                redCogRotate(robot);
            }
        }
    }
    public Robot checkForRobot(int x, int y) {
        for (Player player : players.values()) {
            if (x == player.getRobot().getX() && y == player.getRobot().getY()) {
                return player.getRobot();
            }
        }
        return null;
    }

    public boolean[] getWall(int x, int y) {
        boolean outSideBorder = (x >= map.get("board").length || x < 0 || y >= map.get("board")[0].length || y < 0);
        if (!outSideBorder) {
            int id = map.get("wall")[x][y];
            boolean[] a;
            switch (id) {
                case 8:
                    a = new boolean[]{false, false, true, true};
                    break;
                case 16:
                    a = new boolean[]{true, false, false, true};
                    break;
                case 24:
                    a = new boolean[]{true, true, false, false};
                    break;
                case 32:
                    a = new boolean[]{false, true, true, false};
                    break;
                case 31:
                case 44:
                case 93:
                case 1:
                case 9:
                    a = new boolean[]{true, false, false, false};
                    break;
                case 30:
                case 38:
                case 91:
                case 4:
                case 12:
                    a = new boolean[]{false, true, false, false};
                    break;
                case 29:
                case 37:
                case 87:
                case 3:
                case 11:
                    a = new boolean[]{false, false, true, false};
                    break;
                case 23:
                case 46:
                case 95:
                case 2:
                case 10:
                    a = new boolean[]{false, false, false, true};
                    break;
                default:
                    a = new boolean[]{false, false, false, false};
                    break;
            }
            return a;
        } return new boolean[]{false,false,false,false};
    }

    public void laserSpawner() {
        int dir;
        for(int i=0;i<5;i++){
            for(int j=0;j<5;j++){
                map.get("laser")[i][j]=0;
            }
        }

        for(int i = 0; i<5; i++) {
            for (int j = 0; j < 5; j++) {
                int id = map.get("wall")[i][j];
                switch (id) {
                    case 37:
                        dir = 0;
                        laser(i,j,dir);
                        break;
                    case 38:
                        dir = 3;
                        laser(i,j,dir);
                        break;
                    case 45:
                        dir = 2;
                        laser(i,j,dir);
                        break;
                    case 46:
                        dir = 1;
                        laser(i,j,dir);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    //Denne funksjonen er en laser som rekursivt iterer over brettet
    public void laser(int x, int y, int dir) {
        boolean outSideBorder = (x >= map.get("board").length || x < 0 || y >= map.get("board")[0].length || y < 0);
        Robot robot = checkForRobot(x,y);
        if (robot != null) {
            robot.addDamage(1);
            System.out.println("Hit by laser");
        } else if(!outSideBorder) {
            //dir : 0 = north, 1 = west, 2 = south, 3 = east
            switch (dir) {
                case 0:
                    checkOverLapLaser(x,y,dir);
                    if(!getWall(x,y)[dir] && !getWall(x,y+1)[(dir + 2) % 4])
                        laser(x,y+1,dir);
                    break;
                case 1:
                    checkOverLapLaser(x,y,dir);
                    if(!getWall(x,y)[dir] && !getWall(x-1,y)[(dir + 2) % 4])
                        laser(x-1,y,dir);
                    break;
                case 2:
                    checkOverLapLaser(x,y,dir);
                    if(!getWall(x,y)[dir] && !getWall(x,y-1)[(dir + 2) % 4])
                        laser(x,y-1,dir);
                    break;
                case 3:
                    checkOverLapLaser(x,y,dir);
                    if(!getWall(x,y)[dir] && !getWall(x+1,y)[(dir + 2) % 4])
                        laser(x+1,y,dir);
                    break;
            }
        }
    }

    public void checkOverLapLaser(int x, int y, int dir) {
        //Hvis laseren er horisontal skal ruten ha verdi 1, hvis en er vertikal skal ruten ha 2.
        //Hvis 2 lasere krysser skal ruten ha verdi 3
        if(map.get("laser")[x][y]==1+(dir % 2))
            map.get("laser")[x][y] = 3;
        else if(map.get("laser")[x][y]==0)
            map.get("laser")[x][y] = 2-(dir % 2);
    }


    public void activateBlueConveyorBelt() {
        activateConveyorBelts("Blue conveyor belts");
    }

    public void activateYellowConveyorBelt() {
        activateConveyorBelts("Yellow conveyor belts");
    }

    private void activateConveyorBelts(String conveyorLayer) {
        class PlayerMove {
            Robot specificrobot;
            int x;
            int y;

            PlayerMove(Robot robot, int x, int y) {
                this.specificrobot = robot;
                this.x = x;
                this.y = y;
            }
        }
        ArrayList<PlayerMove> moves = new ArrayList<>();
        for (int i=0; i<map.get("board").length; i++) {
            for (int j=0; j<map.get("board")[0].length; j++) {
                int[][] layer = map.get(conveyorLayer);
                int type = 0;
                if (layer != null) { type = layer[i][j]; }
                Robot movingRobot = checkForRobot(i, j);
                /*if(movingRobot!=null)
                    System.out.println("Robot is not null, position: " + i + j + ", And the type of layer is " + type);
                 */
                switch (type) {
                    case 42:
                    case 43:
                    case 49:
                    case 26:
                    case 27:
                    case 13:
                        if (movingRobot!=null) {
                            PlayerMove move = new PlayerMove(movingRobot,0, 1);
                            moves.add(move);
                            }
                        break;
                    case 34:
                    case 44:
                    case 51:
                    case 18:
                    case 28:
                    case 22:
                        if (movingRobot!=null) {
                            PlayerMove move = new PlayerMove(movingRobot,-1, 0);
                            moves.add(move);
                        }
                        break;
                    case 33:
                    case 36:
                    case 50:
                    case 17:
                    case 20:
                    case 21:
                        if (movingRobot!=null) {
                            PlayerMove move = new PlayerMove(movingRobot,0, -1);
                            moves.add(move);
                        }
                        break;
                    case 35:
                    case 41:
                    case 52:
                    case 19:
                    case 25:
                    case 14:
                        if (movingRobot!=null) {
                            PlayerMove move = new PlayerMove(movingRobot,1, 0);
                            moves.add(move);
                        }
                        break;
                }
            }
        }
        for (PlayerMove move : moves) {
            Robot potentialRobot = checkForRobot(move.specificrobot.getX() + move.x, move.specificrobot.getY() + move.y);
            if (potentialRobot == null) {
                movePlayer(move.specificrobot, move.x, move.y);
            }
        }
    }
    /*
    Yellow:
      rotate:
        33: East -> South
        34: South -> West
        35: South -> East
        36: West -> South
        41: North -> East
        42: West -> North
        43: East -> North
        44: North -> West
      straight:
        49: South -> North
        50: North -> South
        51: East -> West
        52: West -> East

    Blue:
      rotate:
        17: East -> South
        18: South -> West
        19: South -> East
        20: West -> South
        25: North -> East
        26: West -> North
        27: East -> North
        28: North -> West
      straight:
        13: South -> North
        14: West -> East
        21: North -> South
        22: East -> West
*/
    public void move(Robot robot, int distance) {
        distance--;
        int x = 0;
        int y = 0;
        switch (robot.getRotation()){
            case 0: y++; break;
            case 1: x--; break;
            case 2: y--; break;
            case 3: x++; break;
            default: y += 10; break;
        }

        movePlayer(robot, x, y);
        if (distance>0) {
            move(robot, distance);
        }
    }

    public void moveBack(Robot robot) {
        int x = 0;
        int y = 0;
        switch (robot.getRotation()){
            case 0: y--; break;
            case 1: x++; break;
            case 2: y++; break;
            case 3: x--; break;
            default: y += 10; break;
        }

        movePlayer(robot, x, y);
    }

    public void setFlagPositions(Robot robot){
        Map<Integer, Boolean> flagPositions = new HashMap<>();
        for (int i = 0; i < map.get("flag").length; i++){
            for(int j = 0; j < map.get("flag")[i].length; j++){
                if(map.get("flag")[i][j] != 0){
                    flagPositions.put(map.get("flag")[i][j], false);
                }
            }
        }
        robot.setFlagVisits(flagPositions);
    }

    public boolean checkFlags(int x, Robot robot){
        if(x == 55){
            return true;
        }
        else if(x == 63 && robot.getFlagVisits().get(55)){
            return true;
        }
        else if(x == 71 && robot.getFlagVisits().get(63)){
            return true;
        }
        else if(x == 79 && robot.getFlagVisits().get(71)){
            return true;
        } else {
            return false;
        }
    }

    public Vector2[] getStartingSpots() {
        Vector2[] startingSpots = new Vector2[8];

        for (int i = 0; i < 8; i++) {
            int id;
            switch (i) {
                case 0: id = 121; break;
                case 1: id = 122; break;
                case 2: id = 123; break;
                case 3: id = 124; break;
                case 4: id = 129; break;
                case 5: id = 130; break;
                case 6: id = 131; break;
                default: id = 132; break;
            }

            int[][] layer = map.get("starting spots");
            if (layer == null) {
                startingSpots[i] = new Vector2(0,0);
            } else {
                for (int x = 0; x < layer.length; x++) {
                    for (int y = 0; y < layer[0].length; y++) {
                        if (layer[x][y] == id) { startingSpots[i] = new Vector2(x,y); }
                    }
                }
                if (startingSpots[i] == null) { startingSpots[i] = new Vector2(0,0); }
            }
        }
        return startingSpots;
    }

    public void greenCogRotate(Robot robot){
        robot.rotate(-1);
    }

    public void redCogRotate(Robot robot){
        robot.rotate(1);
    }

}
