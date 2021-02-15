package inf112.skeleton.app;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class Main {
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration cfg = new Lwjgl3ApplicationConfiguration();
        cfg.setTitle("test");
        cfg.setWindowedMode(500, 500);


        Robot[] robots = new Robot[7];
        robots[0] = new Robot();

        new Lwjgl3Application(new GUI(robots), cfg);
    }
}