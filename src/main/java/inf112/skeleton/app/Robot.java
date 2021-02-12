package inf112.skeleton.app;

import com.badlogic.gdx.math.Vector2;

public class Robot {
    boolean alive = true;
    boolean won = false;

    int damageTokens = 0;
    Vector2 pos = new Vector2(0,0);

    public Robot() {
    }

    public void damage(int dam) {
        int newDT = damageTokens + dam;

        damageTokens = Math.max(Math.min(damageTokens, 10), 0);
    }

    public Vector2 getPos() {
        return pos;
    }

    public boolean getAlive() {
        return alive;
    }

    public boolean getWon() {
        return won;
    }
}
