package inf112.app;

import org.junit.Test;

import static org.junit.Assert.*;

public class RobotTest {

    @Test
    public void ShouldAddDamage() {
        Robot robot = new Robot();
        robot.addDamage(1);
        assertEquals(1, robot.getDamage());
    }

    @Test
    public void ShouldDestroyAt10DT() {
        Robot robot = new Robot();
        int lifeTokens = 3;
        for (int i=0; i<3; i++) {
            robot.addDamage(10);
            lifeTokens--;
            assertEquals(lifeTokens, robot.getLifeTokens());
        }
        assertEquals(robot.getAlive(), false);
    }
}
