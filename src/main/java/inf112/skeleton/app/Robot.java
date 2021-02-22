package inf112.skeleton.app;

import com.badlogic.gdx.math.Vector2;

public class Robot {
    boolean alive = true;
    boolean won = false;

    int fireLaser = 0;

    int damageTokens = 0;
    Vector2 pos = new Vector2(0,0);
    int rotation = 0;

    public Robot() {
    }

    public void damage(int dam) {
        int newDT = damageTokens + dam;

        damageTokens = Math.max(Math.min(newDT, 10), 0);
    }

    public Vector2 getPos() {
        return pos;
    }

    public int getX() { return (int) pos.x; }

    public int getY() { return (int) pos.y; }

    public void setPos(Vector2 newPos) {pos = newPos;}

    public void setPos(int x, int y) { pos = new Vector2(x,y); }

    public boolean getAlive() { return alive; }

    public void setAlive(boolean newAlive) {
        alive = newAlive;
    }

    public boolean getWon() {
        return won;
    }

    public void setWon(boolean newWon) {
        won = newWon;
    }
    public int getRotation() { return rotation; }

    public void rotate(int rot) {
        rotation = Math.floorMod(rotation + rot,4);
    }
}
