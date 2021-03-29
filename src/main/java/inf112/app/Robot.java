package inf112.app;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.Map;

public class Robot {
    Map<Integer, Boolean> flagVisits;

    boolean alive = true;
    boolean won = false;
    boolean powerDown = false;

    int fireLaser = 0;

    Card[] programRegister = new Card[5];

    int damageTokens = 0;
    Vector2 pos = new Vector2(0,0);
    int rotation = 0;

    int[] textureRegionIndex = new int[]{0,0};

    public Robot() {}
    
    public Robot(int x, int y) {
        this.pos = new Vector2(x, y);
    }

    public void addDamage(int dam) {
        int newDT = damageTokens + dam;

        damageTokens = Math.max(Math.min(newDT, 10), 0);
    }

    public void setPowerDown(boolean bool) {
        powerDown=bool;
    }

    public boolean getPowerDown() { return powerDown; }
    
    public int getDamage() {
        return damageTokens;
    }

    public void discardDamage() { damageTokens=0; }

    public Card[] getProgramRegister() { return programRegister; }

    public void setProgramRegister(Card[] cards) { this.programRegister = cards; }

    public Vector2 getPos() {
        return pos;
    }

    public int getX() { return (int) pos.x; }

    public int getY() { return (int) pos.y; }

    public void setPos(Vector2 newPos) {pos = newPos;}

    public void setPos(int x, int y) { pos = new Vector2(x,y); }

    public boolean checkWin(){
        return !flagVisits.containsValue(false);
    }

    public void setFlagVisits(Map<Integer, Boolean> map){
        flagVisits = map;
    }

    public Map<Integer, Boolean> getFlagVisits(){
        return flagVisits;
    }

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

    public void setTexture(int[] indexes) { textureRegionIndex = indexes; }

    public int[] getTexture() {
        return textureRegionIndex;
    }
}
