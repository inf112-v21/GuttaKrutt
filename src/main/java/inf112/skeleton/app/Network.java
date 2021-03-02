package inf112.skeleton.app;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

// This class is a convenient place to keep things common to both the client and server.
public class Network {
    static public final int port = 54555;

    // This registers objects that are going to be sent over the network.
    static public void register (EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(Robot.class);
        kryo.register(Player.class);
        kryo.register(Controls.class);
        kryo.register(GameLogic.class);
        kryo.register(com.badlogic.gdx.math.Vector2.class);
        kryo.register(TestPacket.class);
    }



}