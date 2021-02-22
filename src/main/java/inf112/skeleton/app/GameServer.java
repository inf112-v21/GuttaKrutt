package inf112.skeleton.app;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.kryo.Kryo;

import java.io.IOException;

public class GameServer {
    Server server;

    public GameServer () throws IOException {
        server = new Server() {
            protected Connection newConnection () {
                // By providing our own connection implementation, we can store per
                // connection state without a connection ID to state look up.
                return new GameConnection();
            }
        };
    }

    static class GameConnection extends Connection {
        public String name;
    }

}
