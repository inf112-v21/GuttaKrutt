package inf112.skeleton.app;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;

public class Controls extends InputAdapter {
    Robot[] robots;
    int[][][] map;

    public Controls(int[][][] map, Robot[] robots) {
        this.map = map;
        this.robots = robots;
    }

    public boolean keyUp(int keyCode) {
        Robot robot = robots[0];
        if (keyCode == Input.Keys.LEFT || keyCode == Input.Keys.A) {
            movePlayer(robot,-1,0);
            return true;
        }
        if (keyCode == Input.Keys.RIGHT || keyCode == Input.Keys.D) {
            movePlayer(robot,1,0);
            return true;
        }
        if (keyCode == Input.Keys.DOWN || keyCode == Input.Keys.S) {
            movePlayer(robot,0,-1);
            return true;
        }
        if (keyCode == Input.Keys.UP || keyCode == Input.Keys.W) {
            movePlayer(robot,0,1);
            return true;
        }

        /*
        if (keyCode == Input.Keys.R) {
            create();
        }
        */

        return false;
    }

    public void movePlayer(Robot robot, int x, int y) {
        Vector2 oldPos = robot.getPos();
        float newX = oldPos.x + x;
        float newY = oldPos.y + y;
        if (!robot.getAlive() || robots[0].getWon()) {
            System.out.println("inf112.skeleton.app.Player cannot move, game is over.");
            return;
        }
        if (!(newX<0||newY<0||newX>5||newY>5)) {
            //GUI.playerLayer.setCell((int) playerPos.x, (int) playerPos.y, null);
            robot.setPos(new Vector2(newX, newY));

            System.out.println("inf112.skeleton.app.Player moved to " + newX + ", " + newY);

            checkTile(robot);
        } else {
            System.out.println("inf112.skeleton.app.Player tried to move to " + newX + ", " + newY + ", but it is outside of the map.");
        }
    }

    public void checkTile(Robot robot) {
        Vector2 pos = robot.getPos();
        int hole = map[1][(int) pos.x][(int) pos.y];
        int flag = map[2][(int) pos.x][(int) pos.y];

        if (hole != 0) {
            robots[0].setAlive(false);
            System.out.println("inf112.skeleton.app.Player has died.");
            System.out.println(hole);
        }
        if (flag != 0) {
            robot.setWon(true);
            System.out.println("inf112.skeleton.app.Player has won.");
            System.out.println(flag);
        }
    }

}
