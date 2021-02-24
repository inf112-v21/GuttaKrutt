package inf112.skeleton.app;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.maps.tiled.*;

public class Controls extends InputAdapter {
    Robot[] robots;
    TiledMap map;
    GUI gui;

    public Controls(TiledMap map, Robot[] robots, GUI gui) {
        this.map = map;
        this.robots = robots;
        this.gui = gui;
    }

    public boolean keyUp(int keyCode) {
        Robot robot = robots[0];
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
            robots[0] = new Robot();
            return true;
        }

        if (keyCode == Input.Keys.F) {
            robots[0].fireLaser = 20;
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
        if (!robot.getAlive() || robots[0].getWon()) {
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
        System.out.println(direction);

        if (!getWall(oldX,oldY)[direction] && !getWall(newX,newY)[(direction + 2) % 4]) {
            //GUI.playerLayer.setCell((int) playerPos.x, (int) playerPos.y, null);
            robot.setPos(new Vector2(newX, newY));

            System.out.println("inf112.skeleton.app.Player moved to " + newX + ", " + newY);

            checkTile(robot);
        }
    }

    public void checkTile(Robot robot) {
        Vector2 pos = robot.getPos();
        TiledMapTileLayer holeLayer = (TiledMapTileLayer) map.getLayers().get("hole");
        TiledMapTileLayer.Cell hole = holeLayer.getCell((int) pos.x,(int) pos.y);

        TiledMapTileLayer flagLayer = (TiledMapTileLayer) map.getLayers().get("flag");
        TiledMapTileLayer.Cell flag = flagLayer.getCell((int) pos.x,(int) pos.y);

        int x = (int) pos.x;
        int y = (int) pos.y;
        boolean outSideBorder = (x >= gui.mapWidth || x < 0 || y >= gui.mapHeight || y < 0);

        if(outSideBorder) {
            robots[0].setAlive(false);
            System.out.println("inf112-skeleton.app.Player has died outside the border");
        }

        if (hole != null) {
            robots[0].setAlive(false);
            System.out.println("inf112.skeleton.app.Player has died.");
            System.out.println(hole.getTile().getId());
        }
        if (flag != null) {
            robot.setWon(true);
            System.out.println("inf112.skeleton.app.Player has won.");
            System.out.println(flag.getTile().getId());
        }

    }

    public boolean[] getWall(int x, int y) {
        TiledMapTileLayer.Cell cell = gui.wallLayer.getCell(x, y);
        int id = 0;
        if (cell != null) {
            id = cell.getTile().getId();
        }
        boolean[] a;
        switch(id) {
            case 8: a = new boolean[]{false,false,true,true}; break;
            case 16: a = new boolean[]{true, false, false, true}; break;
            case 24: a = new boolean[]{true, true, false, false}; break;
            case 32: a = new boolean[]{false, true, true, false}; break;
            case 31: case 44: case 93: case 1: case 9:
                a = new boolean[]{true,false,false,false}; break;
            case 30: case 38: case 91: case 4: case 12:
                a = new boolean[]{false,true,false,false}; break;
            case 29: case 37: case 87: case 3: case 11:
                a = new boolean[]{false,false,true,false}; break;
            case 23: case 46: case 95: case 2: case 10:
                a = new boolean[]{false,false,false,true}; break;
            default: a = new boolean[]{false,false,false,false}; break;
        }
        return a;
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
