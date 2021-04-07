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

    public UUID clientUUID;
    private Client client;
    public boolean gotPackage = false;
    public boolean clientTesting;
    private String host;
    public Map<UUID,Player> playerList = new HashMap<>();
    public boolean run = false;
    public String mapName;

    //Run this if you are hosting the server
    public GameClient() throws IOException {
        this("127.0.0.1", false);
    }

    public GameClient(boolean clientTesting) {
        if (clientTesting) {
            this.client = new Client();
        }
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
    }

    public void updatePlayer() {
        updatePlayer(clientUUID,playerList.get(clientUUID));
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

    public Client getClient(){
        return client;
    }

    public Map<UUID,Player> getPlayerList() {
        return playerList;
    }
}

