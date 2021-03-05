package inf112.skeleton.app;

import java.io.IOException;
import java.util.Scanner;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

/**
 * This class
 */
public class GameServer {

    Server server;
    Integer expectedNumberOfPlayers;
    boolean playerCountSent = false;
    boolean serverTesting;
    boolean gotPackage = false;

    public GameServer() throws IOException {
        this(false);
    }

    public GameServer(boolean serverTesting) throws IOException {
        server = new Server();
        server.start();
        server.bind(54555, 54777);

        this.serverTesting = serverTesting;
        Network.register(server);
        //expectedNumberOfPlayers = inputExpectedPlayers();
        //While developing
        expectedNumberOfPlayers = 1;
        Network.NumberOfPlayers numberOfPlayers = new Network.NumberOfPlayers();

        //Server listening for connections (clients)
        server.addListener(new Listener() {
            public void received (Connection connection, Object object) {
                System.out.println("Client connected");

                if(server.getConnections().length == expectedNumberOfPlayers && !playerCountSent) {
                    playerCountSent = true;
                    numberOfPlayers.amount = expectedNumberOfPlayers;
                    server.sendToAllTCP(numberOfPlayers);
                    System.out.println("packet sent");
                }
                if(object instanceof Network.UpdatePlayer){
                    System.out.println("Received player update");
                    Network.UpdatePlayer player = (Network.UpdatePlayer) object;
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

    public Integer inputExpectedPlayers(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter expected number of players:");
        Integer input = scanner.nextInt();
        return input;
    }

    public static void main(String[] args) throws IOException {
        GameServer server = new GameServer();
    }
}
