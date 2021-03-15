package inf112.skeleton.app;

import java.io.IOException;
import java.util.Scanner;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

/**
 * This class contains the logic of the kryonet client and decides how the client respond to packages it
 * receives.
 */
public class GameClient {

    private static Scanner scanner = new Scanner(System.in);
    private Client client;
    private Network.NumberOfPlayers playersInGame = new Network.NumberOfPlayers();
    private static Integer expectedPlayers = 1;
    public boolean gotPackage = false;
    public boolean clientTesting;
    private String host;

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
        client.connect(5000, host, 54555, 54777);

        Network.register(client);


        client.addListener(new Listener() {
            public void received(Connection connection, Object object) {
                System.out.println("connected to server");
                if (object instanceof Network.NumberOfPlayers) {
                    System.out.println("received playeramount");
                    Network.NumberOfPlayers numberOfPlayers = (Network.NumberOfPlayers) object;
                    setNumberOfPlayers(numberOfPlayers.amount);
                    System.out.println(playersInGame.amount);
                }
                if(object instanceof Network.UpdatePlayer){
                    System.out.println("received player robot");
                    Network.UpdatePlayer player = (Network.UpdatePlayer) object;
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
                    run();
                    break;
                }
            }

        }
    }


    public void updatePlayer(Robot robot){
        if(!client.isConnected()){
            System.out.println("You are not connected to a server!");
        } else {
            Network.UpdatePlayer packet = new Network.UpdatePlayer();
            packet.playerRobot = robot;
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

    public void run () {
        /*Lwjgl3ApplicationConfiguration cfg = new Lwjgl3ApplicationConfiguration();
        cfg.setTitle("test");
        cfg.setWindowedMode(500, 500);

        Robot[] robots = new Robot[playersInGame.amount];
        for (int i = 0; i < playersInGame.amount; i++) {
            robots[i] = new Robot();
        }

        new Lwjgl3Application(new GUI(robots), cfg);*/
    }

    public void sendMove () {

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
}

