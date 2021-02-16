package inf112.skeleton.app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import org.junit.Test;

import java.awt.*;

/**
 * Unit test for simple App.
 */
public class AppTest {

    public Game setup () {
        Lwjgl3ApplicationConfiguration cfg = new Lwjgl3ApplicationConfiguration();
        cfg.setTitle("RoboRally");
        cfg.setWindowedMode(500, 500);

        Game testGame = new Game();
        Lwjgl3Application app = new Lwjgl3Application(testGame, cfg);
        app.exit();
        return testGame;
    }

    @Test
    public void shouldAnswerWithTrue() {

        Game a = setup();
        Vector2 truePos = new Vector2(0,0);

        assertEquals(a.playerPos,truePos);
        //assertEquals(0,1);
    }
}
