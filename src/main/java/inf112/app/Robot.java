package inf112.app;

import com.badlogic.gdx.math.Vector2;

import java.util.Map;

public class Robot {
    Map<Integer, Boolean> flagVisits;

    boolean alive = true;
    boolean won = false;
    boolean powerDown = false;
    boolean powerDownNext = false;

    Card[] programRegister = new Card[5];

    int damageTokens = 0;
    int lifeTokens = 3;
    Vector2 pos = new Vector2(0,0);
    Vector2 checkpoint = new Vector2(0,0);
    int rotation = 0;

    int textureRegionIndex = 0;
    int red = 0;
    int green = 0;
    int blue = 0;

    public Robot() {}
    
    public Robot(int x, int y) {
        this.pos = new Vector2(x, y);
    }

    public void addDamage(int dam) {
        int newDT = damageTokens + dam;

        damageTokens = Math.max(Math.min(newDT, 10), 0);

        /* take away a life token if robot has taken 10 DT
        * if no life tokens left, then robot is destroyed*/
        if (damageTokens == 10) {
            setAlive(false);
        }
    }

    public void setPowerDown(boolean bool) {
        powerDown=bool;
    }

    public boolean getPowerDown() { return powerDown; }
    
    public int getDamage() {
        return damageTokens;
    }

    public int getLifeTokens() {
        return lifeTokens;
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
        if (!newAlive) {
            lifeTokens--;
        }
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

    public void setTexture(int index) { textureRegionIndex = index; }

    public int getTexture() {
        return textureRegionIndex;
    }

    public void respawn(boolean first) {
        if (lifeTokens > 0) {
            alive = true;
            pos = checkpoint;
            if (first) { damageTokens = 0; }
            else { damageTokens = 2; }
        }
    }

    public void respawn() { respawn(false); }

    public void setCheckpoint(Vector2 pos) { checkpoint = pos; }

    public void setRed(int red) { this.red = red; }
    public void setGreen(int green) { this.green = green; }
    public void setBlue(int blue) { this.blue = blue; }

    public int getRed() { return red; }
    public int getGreen() { return green; }
    public int getBlue() { return blue; }

    public void setPowerDownNext(boolean b) {
        powerDownNext = b;
    }
    public boolean getPowerDownNext() { return powerDownNext; }
}
