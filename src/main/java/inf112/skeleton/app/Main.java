package inf112.skeleton.app;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        /*Lwjgl3ApplicationConfiguration cfg = new Lwjgl3ApplicationConfiguration();
        cfg.setTitle("test");
        cfg.setWindowedMode(500, 500);

/*
        Robot[] robots = new Robot[1];
        for (int i=0;i<1;i++) {
            robots[i] = new Robot();
        }

        new Lwjgl3Application(new GUI(robots), cfg);
        */
        GameClient client = new GameClient();
        //TimeUnit.SECONDS.sleep(8);
        /*while (true) {
            if(client.getNumberOfPlayers().amount != null) {
                System.out.println("Got numberofplayers");
                if (client.getNumberOfPlayers().amount > 0) {
                    Robot[] robots = new Robot[client.getNumberOfPlayers().amount];
                    for (int i = 0; i < client.getNumberOfPlayers().amount; i++) {
                        robots[i] = new Robot();
                    }

                    new Lwjgl3Application(new GUI(robots), cfg);
                }
            }
        }*/
    }
}