package inf112.app.logic;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import inf112.app.MapParser;
import inf112.app.Player;
import inf112.app.Robot;
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
            player.getRobot().setCheckpoint(startingSpots[i]);
            player.getRobot().respawn(true);
            i++;
        }
    }

    public Map<String,int[][]> getMap() { return map; }

    /**
     * Moves the input robot by old x-position + input x, and old y-position + input y.
     */
    public void movePlayer(Robot robot, int x, int y, boolean playerCollision) {
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
        if(enemy != null && playerCollision){
            movePlayer(enemy, (newX-oldX), (newY-oldY), true);
        }
        enemy = checkForRobot(newX, newY);
        if (!getWall(oldX,oldY)[direction] && !getWall(newX,newY)[(direction + 2) % 4] && (enemy == null) || !playerCollision) {
            robot.setPos(new Vector2(newX, newY));

            checkForDangers(robot);
        }
      
        if(robot.getDamage()==10)
            robot.setAlive(false);
    }

    /**
     * This method checks for holes and if the robot has exited the map.
     * This method is called while the robot is moving. (And not at the end of the round. If the robot is over a hole,
     * it can't move away before the round is over.)
     * @param robot Robot that we should check tile for
     */
    public void checkForDangers(Robot robot) {
        int x = robot.getX();
        int y = robot.getY();

        if(!inBorder(x,y)) {
            robot.setAlive(false);
            System.out.println("inf112-skeleton.app.Player has died outside the border");
        } else {
            int hole = map.get("hole")[x][y];

            if (hole != 0) {
                robot.setAlive(false);
                System.out.println("inf112.skeleton.app.Player has died.");
            }
        }
    }

    /**
     * This method checks for flags and repair sites. Sets checkpoints.
     * This method is part of the last section of the register round, and is therefore played /after/ all cards have been played.
     * @param robot Robot that we should check tile for
     */
    public void checkForCheckpoints(Robot robot) {
        int x = robot.getX();
        int y = robot.getY();

        if (!inBorder(x,y)) { return; }

        int[][] flag = map.get("flag");
        int[][] repair = map.get("repair");


        if (flag != null && (flag[x][y] != 0 && checkFlags(flag[x][y], robot))) {
            robot.getFlagVisits().put(map.get("flag")[x][y], true);
            System.out.println("You got the flag!");
            if (robot.checkWin()) {
                robot.setWon(true);
                System.out.println("You won!");
            }
            robot.setCheckpoint(new Vector2(x,y));
        }

        if (repair != null && (repair[x][y] == 7 || repair[x][y] == 15)) {
            robot.setCheckpoint(new Vector2(x,y));
        }
    }

    /**
     * Checks if the robot stands on a repair tile and removes one damage token if so.
     * @param robot Robot being repaired
     */
    public void checkForRepairs(Robot robot) {
        int x = robot.getX();
        int y = robot.getY();

        if (!inBorder(x,y)) { return; }

        if (layerIsNull("repair")) return;
        int repair = map.get("repair")[x][y];

        if (repair == 7 || repair == 15) {
            robot.addDamage(-1);
        }
    }

    /**
     * Checks if there is a robot on the tile at position x,y
     * @param x horisontal position
     * @param y vertical position
     * @return If there is a robot on the tile, returns robot, else null
     */
    public Robot checkForRobot(int x, int y) {
        for (Player player : players.values()) {
            if (x == player.getRobot().getX() && y == player.getRobot().getY()) {
                return player.getRobot();
            }
        }
        return null;
    }

    /**
     * Checks if there are walls on any of the sides of the tile at position x,y.
     * @param x horisontal position
     * @param y vertical position
     * @return Returns boolean list of length = 4, where true implies wall and false implies no wall
     */
    public boolean[] getWall(int x, int y) {
        if (inBorder(x,y)) {
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

    /**
     * Checks if position x,y is inside the map borders.
     * @return True if inside the border, else false
     */
    public boolean inBorder(int x,int y) {
        return (x < map.get("board").length && x >= 0 && y < map.get("board")[0].length && y >= 0);
    }

    /**
     * Cleans the board of all lasers.
     */
    public void laserCleaner(){
        for(int i=0;i<map.get("laser").length;i++){
            for(int j=0;j<map.get("laser")[0].length;j++){
                map.get("laser")[i][j]=0;
            }
        }
    }
    /**
     * Spawns lasers from wall laser spawner map object.
     */
    public void laserSpawner() {
        int dir;
        for(int i = 0; i<map.get("laser").length; i++) {
            for (int j = 0; j < map.get("laser")[0].length; j++) {
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

    /**
     * Checks for walls between position (x,y) and position (a,b) in direction dir from (x,y).
     * dir : 0 = north, 1 = west, 2 = south, 3 = east
     * @param x current x pos
     * @param y current y pos
     * @param dir direction
     * @return True if no wall, else false
     */
    public boolean checkForWall(int x, int y, int dir){
        switch (dir){
            case 0:
                return !getWall(x,y)[dir] && !getWall(x,y+1)[(dir + 2) % 4];
            case 1:
                return !getWall(x,y)[dir] && !getWall(x-1,y)[(dir + 2) % 4];
            case 2:
                return !getWall(x,y)[dir] && !getWall(x,y-1)[(dir + 2) % 4];
            case 3:
                return !getWall(x,y)[dir] && !getWall(x+1,y)[(dir + 2) % 4];
            default:
                return false;
        }
    }

    /**
     * Recursively iterates a laser across the board.
     * @param x initial x pos
     * @param y initial y pos
     * @param dir travel direction
     */
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
                    if(checkForWall(x,y,dir))
                        laser(x,y+1,dir);
                    break;
                case 1:
                    checkOverLapLaser(x,y,dir);
                    if(checkForWall(x,y,dir))
                        laser(x-1,y,dir);
                    break;
                case 2:
                    checkOverLapLaser(x,y,dir);
                    if(checkForWall(x,y,dir))
                        laser(x,y-1,dir);
                    break;
                case 3:
                    checkOverLapLaser(x,y,dir);
                    if(checkForWall(x,y,dir))
                        laser(x+1,y,dir);
                    break;
            }
        }
    }

    /**
     * Shoots lasers from every robot in the direction the robot is standing.
     */
    public void robotsShootsLasers(){
        for(Player player : players.values()) {
            Robot robot = player.getRobot();
            if(checkForWall(robot.getX(), robot.getY(), robot.getRotation())) {
                int rotation = robot.getRotation();
                switch (rotation) {
                    case 0:
                        laser(robot.getX(), robot.getY() + 1, rotation);
                        break;
                    case 1:
                        laser(robot.getX() - 1, robot.getY(), rotation);
                        break;
                    case 2:
                        laser(robot.getX(), robot.getY() - 1, rotation);
                        break;
                    default:
                        laser(robot.getX() + 1, robot.getY(), rotation);
                        break;
                }
            }
        }
    }

    /**
     * Hvis laseren er horisontal skal ruten ha verdi 1, hvis en er vertikal skal ruten ha 2.
     * Hvis 2 lasere krysser skal ruten ha verdi 3.
     * @param x
     * @param y
     * @param dir
     */
    public void checkOverLapLaser(int x, int y, int dir) {

        if(map.get("laser")[x][y]==1+(dir % 2))
            map.get("laser")[x][y] = 3;
        else if(map.get("laser")[x][y]==0)
            map.get("laser")[x][y] = 2-(dir % 2);
    }

    /***
     * Activates all the blue conveyorbelts present on the map
     */
    public void activateBlueConveyorBelt() {
        activateConveyorBelts("Blue conveyor belts");
    }

    /***
     * Activates all the yellow conveyorbelts present on the map
     */
    public void activateYellowConveyorBelt() {
        activateConveyorBelts("Yellow conveyor belts");
    }

    private void activateConveyorBelts(String conveyorLayer) {

        if (layerIsNull(conveyorLayer)) return;

        for(Player player : players.values()) {
            Robot movingRobot = player.getRobot();
            if (map.get(conveyorLayer) == null) return;
            if (movingRobot.getAlive()) {
                if (map.get(conveyorLayer)[movingRobot.getX()][movingRobot.getY()] != 0) {
                    int type = map.get(conveyorLayer)[movingRobot.getX()][movingRobot.getY()];
                    switch (type) {
                        case 42: //west -> north
                        case 43: //east -> north
                        case 49:
                        case 26: //west -> north
                        case 27: //east -> north
                        case 13:
                            conveyorMoveCheck(movingRobot, 0, 1);
                            break;
                        case 34: //south -> west
                        case 44: //north -> west
                        case 51:
                        case 18: //south -> west
                        case 28: //north -> west
                        case 22:
                            conveyorMoveCheck(movingRobot, -1, 0);
                            break;
                        case 33: //east -> south
                        case 36: //west -> south
                        case 50:
                        case 17: //east -> south
                        case 20: //west -> south
                        case 21:
                            conveyorMoveCheck(movingRobot, 0, -1);
                            break;
                        case 35: //south -> east
                        case 41: //north -> east
                        case 52:
                        case 19: //south -> east
                        case 25: //north -> east
                        case 14:
                            conveyorMoveCheck(movingRobot, 1, 0);
                            break;
                    }
                }
            }
        }
    }

    /***
     * Checks if the conveyorbelt should move the robot. If the robot is blocked by another robot not on a conveyorbelt
     * the movement should halt.
     * The conveyorbelt also rotate the robot if the conveyorbelt moves the robot into a turn.
     * @param movingRobot
     * @param addedX
     * @param addedY
     */
    private void conveyorMoveCheck(Robot movingRobot, int addedX, int addedY) {
        Robot roadBlockRobot = checkForRobot(movingRobot.getX()+addedX,movingRobot.getY()+addedY);
        boolean halt = false;
        if (roadBlockRobot!=null)
            halt = ((map.get("Yellow conveyor belts")[roadBlockRobot.getX()][roadBlockRobot.getY()])==0
                    && (map.get("Blue conveyor belts")[roadBlockRobot.getX()][roadBlockRobot.getY()])==0);
        if(!halt) {
            movePlayer(movingRobot, addedX, addedY, false);
            conveyorRotation(movingRobot);
        }
    }

    /***
     * Rotates the given robot either left or right depending on the conveyorbelt it stands on.
     * @param rotatingRobot
     */

    private void conveyorRotation(Robot rotatingRobot) {
        if(inBorder(rotatingRobot.getX(),rotatingRobot.getY())) {
            int currentConveyor = map.get("Yellow conveyor belts")[rotatingRobot.getX()][rotatingRobot.getY()];
            if (currentConveyor==0)
                currentConveyor = map.get("Blue conveyor belts")[rotatingRobot.getX()][rotatingRobot.getY()];
            switch (currentConveyor) {
                //rotate left
                case 42: //west -> north
                case 26: //west -> north
                case 34: //south -> west
                case 18: //south -> west
                case 33: //east -> south
                case 17: //east -> south
                case 41: //north -> east
                case 25: //north -> east
                    rotatingRobot.rotate(1);
                    break;
                //rotate right
                case 43: //east -> north
                case 27: //east -> north
                case 44: //north -> west
                case 28: //north -> west
                case 36: //west -> south
                case 20: //west -> south
                case 35: //south -> east
                case 19: //south -> east
                    rotatingRobot.rotate(-1);
                    break;
            }
        }
    }
    /*
    Reference list for directions of conveyorbelts
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

    /**
     * Iterates map for cogs and rotates robots on cogs.
     * Left rotation for red cog. Right rotation for green cog.
     */
    public void activateGears() {
        if (layerIsNull("Green cog") && layerIsNull("Red cog")) return;
        int[][] greens = map.get("Green cog");
        int[][] reds = map.get("Red cog");

        for (int i=0; i<map.get("board").length; i++) {
            for (int j = 0; j < map.get("board")[0].length; j++) {
                int type = greens[i][j] + reds[i][j];

                Robot robot = checkForRobot(i,j);
                if(robot != null) {
                    switch (type) {
                        case 53:
                            robot.rotate(1);
                            break;
                        case 54:
                            robot.rotate(-1);
                            break;
                    }
                }
            }
        }
    }

    /**
     * Moves robot from initial position to new position
     * @param robot Robot being moved
     * @param distance Number of tiles the robot moves
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

        movePlayer(robot, x, y,true);
        if (distance>0) {
            move(robot, distance);
        }
    }

    /**
     * Moves the robot one step back.
     */
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

        movePlayer(robot, x, y,true);
    }

    /**
     * Initiates a robots list of flags visited
     * @param robot
     */
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

    /**
     * Checks if the robot can acquire the flag it stands on
     * @param x
     * @param robot
     * @return
     */
    public boolean checkFlags(int x, Robot robot){
        if(x == 55) return true;
        else if(x == 63 && robot.getFlagVisits().get(55)) return true;
        else if(x == 71 && robot.getFlagVisits().get(63)) return true;
        else return x == 79 && robot.getFlagVisits().get(71);
    }

    /**
     * Places every robot on their designated starting spot.
     * If there are no starting spots on the map, then every robot is places in position (0,0)
     * @return
     */
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

            if (layerIsNull("starting spots")) {
                startingSpots[i] = new Vector2(0,0);
            } else {
                int[][] layer = map.get("starting spots");
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

    private boolean layerIsNull(String layerName) {
        int[][] layer = map.get(layerName);
        if(layer==null)
            return true;
        return false;
    }

}
