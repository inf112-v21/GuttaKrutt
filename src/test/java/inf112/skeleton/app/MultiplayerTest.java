package inf112.skeleton.app;

import static org.junit.Assert.assertTrue;


import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class MultiplayerTest {

    GameServer server = new GameServer();
    GameClient client = new GameClient();

    public MultiplayerTest() throws IOException {
    }

    @Test
    public void test(){
        Assert.assertTrue(true);

    }
    @Test
    public void clientServerCommunicationTest() throws InterruptedException {
        if(client.getClient().isConnected()){
            Network.TestPacket packet = new Network.TestPacket();
            packet.packet = "test";
            client.getClient().sendTCP(packet);
            TimeUnit.SECONDS.sleep(6);
            Assert.assertTrue(client.gotPackage);
        }

    }
}
