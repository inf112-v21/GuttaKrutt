package inf112.app.networking;

import java.io.IOException;
import java.util.*;

import com.badlogic.gdx.utils.Array;
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
    int seed;
    UUID winner;
    ArrayList<UUID> ready = new ArrayList<>();
    public Map<UUID,String> mapVotes = new HashMap<>();

    public GameServer() throws IOException {
        this(false);
    }

    public GameServer(boolean serverTesting) throws IOException {
        server = new Server();
        server.start();
        server.bind(54555);

        this.serverTesting = serverTesting;
        Network.register(server);

        seed = new Random().nextInt(10000);

        playerList = new HashMap<>();
        connectionList = new HashMap<>();

        //Server listening for connections (clients)
        server.addListener(new Listener() {
            public void received (Connection connection, Object object) {
                if(object instanceof String) {
                    if (run) { connection.close();
                    } else {
                        UUID uuid = UUID.randomUUID();
                        playerList.put(uuid,new Player((String) object));
                        connectionList.put(connection.getID(),uuid);
                        connection.sendTCP(uuid);

                        Network.Seed packet = new Network.Seed();
                        packet.seed = seed;
                        connection.sendTCP(packet);

                        Network.UpdatePlayers players = new Network.UpdatePlayers();
                        players.playerList = playerList;
                        server.sendToAllTCP(players);
                    }
                }

                if(object instanceof Network.UpdatePlayer){
                    Network.UpdatePlayer player = (Network.UpdatePlayer) object;
                    playerList.put(player.uuid, player.player);
                    server.sendToAllExceptTCP(connection.getID(), player);
                }

                if(object instanceof Network.NewWinner){
                    System.out.println("Game won by " + playerList.get(((Network.NewWinner) object).uuid).getName());
                    winner = ((Network.NewWinner) object).uuid;
                    ready = new ArrayList<>();
                    server.sendToAllTCP(object);

                    run = false;
                }

                if(object instanceof Network.Ready) {
                    ready.add(((Network.Ready) object).uuid);
                    server.sendToAllTCP(object);

                    boolean send = true;

                    //Checks for all players if they have pressed ready.
                    for (UUID uuid : playerList.keySet()) {
                        if (!ready.contains(uuid)) {
                            send = false; //If at least one person has not pressed ready, do not start the game.
                            break;
                        }
                    }

                    if (send && winner == null) {
                        System.out.println("All players ready. Starting game.");
                        Network.MapName packet = new Network.MapName();
                        packet.mapName = getVotedMap();
                        server.sendToAllTCP(packet);
                        server.sendToAllTCP(new Network.RunGame());
                    } else if (send) {
                        System.out.println("Setting up new game...");
                        playerList = new HashMap<>();
                        connectionList = new HashMap<>();
                        run = false;
                        ready = new ArrayList<>();
                        mapVotes = new HashMap<>();
                        winner = null;

                        server.sendToAllTCP(new Network.NewGame());
                    }
                }

                if(object instanceof Network.MapVote) {
                    mapVotes.put(connectionList.get(connection.getID()),((Network.MapVote) object).mapName);
                    Network.MapVotes votes = new Network.MapVotes();
                    votes.votes = getVoteCount();
                    server.sendToAllTCP(votes);
                }

                if(object instanceof Network.TestPacket){
                    System.out.println("Server received test packet");
                    Network.TestPacket packet = (Network.TestPacket) object;
                    gotPackage = true;
                    connection.sendTCP(packet);
                }
            }
            public void disconnected(Connection connection) {
                playerList.remove(connectionList.get(connection.getID()));

                Network.RemovePlayer packet = new Network.RemovePlayer();
                packet.uuid = connectionList.get(connection.getID());
                server.sendToAllTCP(packet);

                connectionList.remove(connection.getID());
            }
        });
    }

    public Server getServer() { return server; }

    public void setMap(String mapName) {
        this.mapName = mapName;
    }

    /**
     * @return Map from all voted on maps as keys and vote count as value.
     */
    private Map<String,Integer> getVoteCount() {
        Map<String,Integer> count = new HashMap<>();

        for (String vote : mapVotes.values()) {
            Integer countInt = count.get(vote);
            if (countInt == null) {
                countInt = 0;
            }
            countInt++;
            count.put(vote,countInt);
        }

        return count;
    }

    /**
     * Finds the map with the most votes and returns its String value. If more than one map is tied for most a
     * random map will be returned from the winners.
     * @return Most voted on map.
     */
    private String getVotedMap() {
        Map<String,Integer> count = getVoteCount();

        int high = 0;
        Array<String> highS = new Array<>(new String[]{"Checkmate.tmx","TiledTest.tmx","Risky Exchange.tmx"});
        for (Map.Entry<String,Integer> entry : count.entrySet()) {
            if (entry.getValue() > high) {
                high = entry.getValue();
                highS.clear();
                highS.add(entry.getKey());
            }
            if (entry.getValue() == high) {
                highS.add(entry.getKey());
            }
        }

        return highS.random(); //Selects random from all top maps. If highS only contains one map, it will always be that one.
    }

    static public void main(String[] args) {
        try {
            new GameServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
