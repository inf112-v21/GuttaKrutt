package inf112.app.networking;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import inf112.app.Player;

/**
 * This class contains the logic of the kryonet server. It decides how to respond to incoming information
 * from clients.
 */
public class GameServer {

    Server server;
    boolean serverTesting;
    public boolean gotPackage = false;
    public Map<UUID,Player> playerList;
    Map<Integer,UUID> connectionList;
    String mapName;
    public boolean run = false;

    public GameServer() throws IOException {
        this(false);
    }

    public GameServer(boolean serverTesting) throws IOException {
        server = new Server();
        server.start();
        server.bind(54555);

        this.serverTesting = serverTesting;
        Network.register(server);

        playerList = new HashMap<>();
        connectionList = new HashMap<>();

        //Server listening for connections (clients)
        server.addListener(new Listener() {
            public void received (Connection connection, Object object) {
                //System.out.println("Client connected");

                if(object instanceof String) {
                    if (run) { connection.close();
                    } else {
                        UUID uuid = UUID.randomUUID();
                        playerList.put(uuid,new Player((String) object));
                        connectionList.put(connection.getID(),uuid);
                        connection.sendTCP(uuid);

                        Network.UpdatePlayers players = new Network.UpdatePlayers();
                        players.playerList = playerList;
                        server.sendToAllTCP(players);
                        Network.MapName map = new Network.MapName();
                        map.mapName = mapName;
                        connection.sendTCP(map);
                    }
                }

                if(object instanceof Network.UpdatePlayer){
                    System.out.println("Received player update");
                    Network.UpdatePlayer player = (Network.UpdatePlayer) object;
                    playerList.put(player.uuid, player.player);
                    server.sendToAllExceptTCP(connection.getID(), player);
                }

                if(object instanceof Network.TestPacket){
                    System.out.println("Server received test packet");
                    Network.TestPacket packet = (Network.TestPacket) object;
                    gotPackage = true;
                    connection.sendTCP(packet);
                }
            }
        });
    }

    public Server getServer() { return server; }

    public void setMap(String mapName) {
        this.mapName = mapName;
    }
}
