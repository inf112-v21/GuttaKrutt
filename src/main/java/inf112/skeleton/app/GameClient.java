package inf112.skeleton.app;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;

public class GameClient {
    Client client;
    String name;

    public GameClient () {
        client = new Client();
        client.start();
    }
}
