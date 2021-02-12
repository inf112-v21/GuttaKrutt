package inf112.skeleton.app;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class Main {
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration cfg = new Lwjgl3ApplicationConfiguration();
        cfg.setTitle("test");
        cfg.setWindowedMode(500, 500);

        boolean[][][] matrixMap;
        Robot[] robots;

        robots[0] = new Robot();

        new Controls(matrixMap, robots);

        new Lwjgl3Application(new GUI(robots), cfg);
    }
}