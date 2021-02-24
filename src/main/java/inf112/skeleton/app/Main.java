package inf112.skeleton.app;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class Main {
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration cfg = new Lwjgl3ApplicationConfiguration();
        cfg.setTitle("RoboRally");
        cfg.setWindowedMode(500, 500);


        Robot[] robots = new Robot[1];
        for (int i=0;i<1;i++) {
            robots[i] = new Robot();
        }

        new Lwjgl3Application(new GUI(robots), cfg);
    }
}