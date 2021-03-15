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
    Integer expectedNumberOfPlayers;
    boolean playerCountSent = false;
    boolean serverTesting;
    public boolean gotPackage = false;
    public Map<UUID,Player> playerList;
    Map<Integer,UUID> connectionList;
    String mapName;
    public boolean run = false;

    public GameServer() throws IOException {
        this(false,1);
    }

    public GameServer(boolean serverTesting, int numPlayers) throws IOException {
        server = new Server();
        server.start();
        server.bind(54555, 54777);

        this.serverTesting = serverTesting;
        Network.register(server);

        playerList = new HashMap<>();
        connectionList = new HashMap<>();
        //expectedNumberOfPlayers = inputExpectedPlayers();
        //While developing
        expectedNumberOfPlayers = numPlayers;
        Network.NumberOfPlayers numberOfPlayers = new Network.NumberOfPlayers();

        //Server listening for connections (clients)
        server.addListener(new Listener() {
            public void received (Connection connection, Object object) {
                System.out.println("Client connected");

                if(object instanceof String) {
                    if (run) { connection.close();
                    } else {
                        UUID uuid = UUID.randomUUID();
                        playerList.put(uuid,new Player((String) object));
                        connectionList.put(connection.getID(),uuid);
                        server.sendToTCP(connection.getID(),uuid);

                        Network.UpdatePlayers players = new Network.UpdatePlayers();
                        players.playerList = playerList;
                        server.sendToAllTCP(players);
                        Network.MapName map = new Network.MapName();
                        map.mapName = mapName;
                        server.sendToTCP(connection.getID(), map);
                    }
                }

                if(server.getConnections().length == expectedNumberOfPlayers && !playerCountSent) {
                    playerCountSent = true;
                    numberOfPlayers.amount = expectedNumberOfPlayers;
                    server.sendToAllTCP(numberOfPlayers);
                    System.out.println("packet sent");
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

    public Integer inputExpectedPlayers(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter expected number of players:");
        return scanner.nextInt();
    }

    public static void main(String[] args) throws IOException {
        GameServer server = new GameServer();
    }

    public void setMap(String mapName) {
        this.mapName = mapName;
    }
}
