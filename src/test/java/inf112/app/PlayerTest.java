package inf112.app;

import org.junit.Test;

import static org.junit.Assert.*;

public class PlayerTest {

    Player player;

    @Test
    public void nameEqualsBobTest(){
        String name = "Bob";
        player = new Player(name);
        assertEquals("Bob", player.getName());
    }

}
