package inf112.skeleton.app;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * This class contains tests for both GameClient and GameServer, because all the testing involves communication
 * between the GameServer and the GameClient.
 */
public class GameServerGameClientTest {

    GameServer server = new GameServer();
    GameClient client = new GameClient("127.0.0.1", true);
    private final Network.TestPacket packet = new Network.TestPacket();

    public GameServerGameClientTest() throws IOException {
    }

    @Before
    public void setUp(){
        packet.packet = "test";
    }
    @After
    public void afterTest(){
        server.server.close();
        client.getClient().close();
    }

    @Test
    public void serverSendsPacketToClientAndClientReceivesPacketTest() throws InterruptedException {
        server.server.sendToAllTCP(packet);
        System.out.println("TestPacket sent");

        //Timeout to compensate for transmission delay
        TimeUnit.SECONDS.sleep(1);
        Assert.assertTrue(client.gotPackage);
    }

    @Test
    public void clientSendsPacketToServerAndServerReceivesPacketTest() throws InterruptedException {
        client.getClient().sendTCP(packet);
        System.out.println("TestPacket sent");

        //Timeout to compensate for transmission delay
        TimeUnit.SECONDS.sleep(1);
        Assert.assertTrue(server.gotPackage);
    }

    @Test
    public void clientSendsPacketServerReceivesPacketAndSendsPacketBackToClientTest() throws InterruptedException {
        client.getClient().sendTCP(packet);
        System.out.println("TestPacket sent");

        //Timeout to compensate for transmission delay
        TimeUnit.SECONDS.sleep(1);
        Assert.assertTrue(client.gotPackage);

        }
}
