package inf112.app;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class Robot {
    int flagVisits = 0;
    ArrayList<Vector2> visitedFlags = new ArrayList<>(3);

    boolean alive = true;
    boolean won = false;

    int fireLaser = 0;

    Card[] programRegister = new Card[5];

    int damageTokens = 0;
    Vector2 pos = new Vector2(0,0);
    int rotation = 0;

    public Robot() {}
    
    public Robot(int x, int y) {
        this.pos = new Vector2(x, y);
    }

    public void addDamage(int dam) {
        int newDT = damageTokens + dam;

        damageTokens = Math.max(Math.min(newDT, 10), 0);
    }
    
    public int getDamage() {
        return damageTokens;
    }

    public Card[] getProgramRegister() { return programRegister; }

    public void setProgramRegister(Card[] cards) { this.programRegister = cards; }

    public Vector2 getPos() {
        return pos;
    }

    public int getX() { return (int) pos.x; }

    public int getY() { return (int) pos.y; }

    public void setPos(Vector2 newPos) {pos = newPos;}

    public void setPos(int x, int y) { pos = new Vector2(x,y); }

    public void visitsFlag(int x, int y){
        Vector2 flagPos = new Vector2(x, y);
        if(!visitedFlags.contains(flagPos)){
            visitedFlags.add(flagPos);
            flagVisits++;
        } else{
            System.out.println("You already visited this flag!");
        }
    }

    public int getFlagVisits(){
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
}
