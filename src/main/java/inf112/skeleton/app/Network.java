package inf112.skeleton.app;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

/**
 * This class registers each class that is sent over the internet
 */
public class Network {
    static public final int port = 54555;

    // This registers objects that are going to be sent over the network.
    static public void register (EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(Robot.class);
        kryo.register(Player.class);
        kryo.register(BoardLogic.class);
        kryo.register(GameLogic.class);
        kryo.register(com.badlogic.gdx.math.Vector2.class);
        kryo.register(RegisterName.class);
        kryo.register(UpdateNames.class);
        kryo.register(NumberOfPlayers.class);
        kryo.register(UpdatePlayer.class);
        kryo.register(TestPacket.class);
    }

    //Field for registering the name of a client
    static public class RegisterName {
        public String name;
    }

    //Field for list of all client names
    static public class UpdateNames {
        public String[] names;
    }

    //Field for updating the player
    static public class UpdatePlayer {
        public Robot playerRobot;
    }

    //Field for number of players
    static public class NumberOfPlayers {
        public Integer amount;
    }

    static public  class TestPacket{
        public String packet;
    }
}
