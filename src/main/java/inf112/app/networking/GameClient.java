package inf112.app.networking;

import java.io.IOException;
import java.util.*;

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
    public Map<UUID,Player> playerList = new HashMap<>();
    public boolean run = false;
    public String mapName;
    public String name;
    public UUID winner;
    public int seed;
    public ArrayList<UUID> ready = new ArrayList<>();
    public Map<String,Integer> mapVotes = new HashMap<>();
    public ArrayList<UUID> remove = new ArrayList<>();

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
        client.connect(5000, host, 54555);

        Network.register(client);

        client.addListener(new Listener() {
            public void received(Connection connection, Object object) {
                if (object instanceof Network.RunGame) {
                    ready = new ArrayList<>();
                    run = true;
                }
                if (object instanceof Network.MapName) {
                    mapName = ((Network.MapName) object).mapName;
                }
                if (object instanceof Network.Seed) {
                    seed = ((Network.Seed) object).seed;
                }
                if (object instanceof UUID) {
                    clientUUID = (UUID) object;
                }
                if(object instanceof Network.UpdatePlayer){
                    Network.UpdatePlayer player = (Network.UpdatePlayer) object;
                    playerList.put(player.uuid,player.player);
                }
                if(object instanceof Network.UpdatePlayers){
                    Network.UpdatePlayers players = (Network.UpdatePlayers) object;
                    playerList = players.playerList;
                }
                if(object instanceof Network.NewWinner){
                    winner = ((Network.NewWinner) object).uuid;

                    run = false;
                }
                if(object instanceof Network.NewGame){
                    clientUUID = null;
                    playerList = new HashMap<>();
                    run = false;
                    winner = null;
                    ready = new ArrayList<>();
                    mapVotes = new HashMap<>();

                    client.sendTCP(name);
                }
                if(object instanceof Network.Ready) {
                    ready.add(((Network.Ready) object).uuid);
                }
                if(object instanceof Network.MapVotes) {
                    mapVotes = ((Network.MapVotes) object).votes;
                }
                if(object instanceof Network.RemovePlayer) {
                    remove.add(((Network.RemovePlayer) object).uuid);
                }
                if(object instanceof Network.TestPacket){
                    System.out.println("Client received test packet");
                    gotPackage = true;
                }
            }
        });
    }

    public void updatePlayer() {
        updatePlayer(clientUUID,playerList.get(clientUUID));
    }

    /**
     * Creates an UpdatePlayer packet and sends it to the server
     * @param uuid UUID of player.
     * @param player Player object.
     */
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

    public void readyForGame() {
        if(!client.isConnected()){
            System.out.println("You are not connected to a server!");
        } else {
            Network.Ready packet = new Network.Ready();
            packet.uuid = clientUUID;
            client.sendTCP(packet);
        }
    }

    public void declareVictory() {
        if (!client.isConnected()){
            System.out.println("You are not connected to a server!");
        } else {
            Network.NewWinner packet = new Network.NewWinner();
            packet.uuid = clientUUID;
            client.sendTCP(packet);
        }
    }

    public Client getClient(){
        return client;
    }

    public Map<UUID,Player> getPlayerList() {
        return playerList;
    }
    public Map<String,Integer> getMapVotes() { return mapVotes; }
}

