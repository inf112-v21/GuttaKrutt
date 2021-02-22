package inf112.skeleton.app;

public class Robot {

    int damage;

    public Robot() {
        damage=0;
    }

    public void addDamage() {
        damage++;
    }

    public int getDamage() {
        return damage;
    }
}
