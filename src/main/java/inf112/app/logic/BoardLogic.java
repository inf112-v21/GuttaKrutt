package inf112.app.logic;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import inf112.app.Player;
import inf112.app.Robot;

import java.util.Map;
import java.util.UUID;

public class BoardLogic extends InputAdapter {
    Map<UUID,Player> players;
    int[][][] map;
    UUID uuid;

    public BoardLogic(int[][][] map, Map<UUID,Player> players) {
        this(map,players,players.keySet().stream().findFirst().get());
    }

    public BoardLogic(int[][][] map, Map<UUID, Player> players, UUID uuid) {
        this.players = players;
        this.map = map;
        this.uuid = uuid;
    }

    public int[][][] getMap() { return map; }

    public boolean keyUp(int keyCode) {
        Robot robot = players.get(uuid).getRobot();
        if (keyCode == Input.Keys.LEFT || keyCode == Input.Keys.A) {
            robot.rotate(1);
            return true;
        }
        if (keyCode == Input.Keys.RIGHT || keyCode == Input.Keys.D) {
            robot.rotate(-1);
            return true;
        }
        if (keyCode == Input.Keys.DOWN || keyCode == Input.Keys.S) {
            moveBack(robot);
            return true;
        }
        if (keyCode == Input.Keys.UP || keyCode == Input.Keys.W) {
            move(robot,1);
            return true;
        }
        if (keyCode == Input.Keys.Q) {
            robot.rotate(2);
            return true;
        }


        if (keyCode == Input.Keys.R) {
            robot = new Robot();
            return true;
        }

        return false;
    }

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

        if (!getWall(oldX,oldY)[direction] && !getWall(newX,newY)[(direction + 2) % 4]) {
            robot.setPos(new Vector2(newX, newY));

            checkTile(robot);
        }
        laserSpawner();
        if(robot.getDamage()==10)
            robot.setAlive(false);
    }

    public void checkTile(Robot robot) {
        int x = robot.getX();
        int y = robot.getY();

        boolean outSideBorder = (x >= map[0].length || x < 0 || y >= map[0][0].length || y < 0);

        if(outSideBorder) {
            robot.setAlive(false);
            System.out.println("inf112-skeleton.app.Player has died outside the border");
        } else {
            int hole = map[1][x][y];
            int flag = map[2][x][y];

            if (hole != 0) {
                robot.setAlive(false);
                System.out.println("inf112.skeleton.app.Player has died.");
            }
            if (flag != 0) {
                robot.visitsFlag(x, y);
                if(robot.getFlagVisits() == 3){
                    robot.setWon(true);
                    System.out.println("inf112.skeleton.app.Player has won.");
                }
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
        boolean outSideBorder = (x >= map[0].length || x < 0 || y >= map[0][0].length || y < 0);
        if (!outSideBorder) {
            int id = map[5][x][y];
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
                map[4][i][j]=0;
            }
        }

        for(int i = 0; i<5; i++) {
            for (int j = 0; j < 5; j++) {
                int id = map[5][i][j];
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
        boolean outSideBorder = (x >= map[0].length || x < 0 || y >= map[0][0].length || y < 0);
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
        if(map[4][x][y]==1+(dir % 2))
            map[4][x][y] = 3;
        else if(map[4][x][y]==0)
            map[4][x][y] = 2-(dir % 2);
    }

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

}
