package inf112.skeleton.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

public class Controls {
    Vector2 playerPos;
    boolean playerAlive;
    boolean playerWon;



    public Controls(robots) {

    }

    public void create(){
        playerPos = new Vector2(0,0);

        Gdx.input.setInputProcessor(this);
    }

    public Vector2 get(){
        return playerPos;
    }

    public void set(int x, int y){
        playerPos.x = x;
        playerPos.y = y;
    }

    public boolean keyUp(int keyCode) {
        if (keyCode == Input.Keys.LEFT || keyCode == Input.Keys.A) {
            movePlayer(-1,0);
            return true;
        }
        if (keyCode == Input.Keys.RIGHT || keyCode == Input.Keys.D) {
            movePlayer(1,0);
            return true;
        }
        if (keyCode == Input.Keys.DOWN || keyCode == Input.Keys.S) {
            movePlayer(0,-1);
            return true;
        }
        if (keyCode == Input.Keys.UP || keyCode == Input.Keys.W) {
            movePlayer(0,1);
            return true;
        }

        if (keyCode == Input.Keys.R) {
            create();
        }

        return false;
    }

    public void movePlayer(int x, int y) {
        float newX = playerPos.x + x;
        float newY = playerPos.y + y;
        if (!playerAlive || playerWon) {
            System.out.println("inf112.skeleton.app.Player cannot move, game is over.");
            return;
        }
        if (!(newX<0||newY<0||newX>=GUI.mapWidth||newY>=GUI.mapHeight)) {
            GUI.playerLayer.setCell((int) playerPos.x, (int) playerPos.y, null);
            playerPos.x = newX;
            playerPos.y = newY;

            System.out.println("inf112.skeleton.app.Player moved to " + newX + ", " + newY);

            checkTile((int)newX, (int)newY);
        } else {
            System.out.println("inf112.skeleton.app.Player tried to move to " + newX + ", " + newY + ", but it is outside of the map.");
        }
    }

    public void checkTile(int x, int y) {
        TiledMapTileLayer.Cell hole = GUI.holeLayer.getCell(x, y);
        TiledMapTileLayer.Cell flag = GUI.flagLayer.getCell(x, y);

        if (hole != null) {
            playerAlive = false;
            System.out.println("inf112.skeleton.app.Player has died.");
            System.out.println(hole);
        }
        if (flag != null) {
            playerWon = true;
            System.out.println("inf112.skeleton.app.Player has won.");
            System.out.println(flag);
        }
    }

}
