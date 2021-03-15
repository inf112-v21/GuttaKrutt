package inf112.app.networking;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import inf112.app.Player;

/**
 * This class contains the logic of the kryonet client and decides how the client respond to packages it
 * receives.
 */
public class GameClient {

    private static Scanner scanner = new Scanner(System.in);
    public UUID clientUUID;
    private Client client;
    private Network.NumberOfPlayers playersInGame = new Network.NumberOfPlayers();
    //private Player player;
    private static Integer expectedPlayers = 1;
    public boolean gotPackage = false;
    public boolean clientTesting;
    private String host;
    Map<UUID,Player> playerList = new HashMap<>();
    public boolean run = false;
    public String mapName;

    //Run this if you are hosting the server
    public GameClient() throws IOException {
        this("127.0.0.1", false);
    }

    //Run this if you are not hosting the server
    public GameClient(String host, boolean clientTesting)throws  IOException{
        //Initiating client
        this.client = new Client();
        client.start();
        this.clientTesting = clientTesting;
        this.host = host;
        client.connect(5000, host, 54555);

        Network.register(client);

        client.addListener(new Listener() {
            public void received(Connection connection, Object object) {
                System.out.println("connected to server");
                if (object instanceof Network.RunGame) {
                    run = true;
                }
                if (object instanceof Network.MapName) {
                    mapName = ((Network.MapName) object).mapName;
                }
                if (object instanceof UUID) {
                    clientUUID = (UUID) object;
                }
                if (object instanceof Network.NumberOfPlayers) {
                    System.out.println("received playeramount");
                    Network.NumberOfPlayers numberOfPlayers = (Network.NumberOfPlayers) object;
                    setNumberOfPlayers(numberOfPlayers.amount);
                    System.out.println(playersInGame.amount);
                }
                if(object instanceof Network.UpdatePlayer){
                    System.out.println("received player robot");
                    Network.UpdatePlayer player = (Network.UpdatePlayer) object;
                    playerList.put(player.uuid,player.player);
                }
                if(object instanceof Network.UpdatePlayers){
                    System.out.println("received player robots");
                    Network.UpdatePlayers players = (Network.UpdatePlayers) object;
                    playerList = players.playerList;
                }
                if(object instanceof Network.TestPacket){
                    System.out.println("Client received test packet");
                    Network.TestPacket packet = (Network.TestPacket) object;
                    gotPackage = true;
                }
            }
        });

        //Starting the game if enough players joined
        while (client.isConnected()) {
            if(clientTesting) {
                break;
            }
            else if (playersInGame.amount != null) {
                if (playersInGame.amount == expectedPlayers) {
                    System.out.println("using playeramount");
                    break;
                }
            }
        }
    }

    public void updatePlayer(UUID uuid, Player player){
        if(!client.isConnected()){
            System.out.println("You are not connected to a server!");
        } else {
            Network.UpdatePlayer packet = new Network.UpdatePlayer();
            packet.uuid = uuid;
            packet.player = player;
            client.sendTCP(packet);
        }
    }

    public String inputHost(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter host: (Blank for localhost)");
        String input = scanner.nextLine();
        if(input == null){
            return "127.0.0.1";
        } else {
            return input;
        }
    }

    public Client getClient(){
        return client;
    }

    public Network.NumberOfPlayers getNumberOfPlayers () {
        return playersInGame;
    }

    public Integer setNumberOfPlayers (Integer x){
        return playersInGame.amount = x;
    }

    public Map<UUID,Player> getPlayerList() { return playerList; }
}

