package inf112.skeleton.app;

import org.junit.Test;

import static org.junit.Assert.*;

public class RobotTest {

    @Test
    public void ShouldAddDamage() {
        Robot robot = new Robot();
        robot.addDamage(1);
        assertEquals(1, robot.getDamage());
    }
}
