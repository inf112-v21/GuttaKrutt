package inf112.skeleton.app;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;

public class GameServer {
    /*
    Server server;

    public GameServer() throws IOException {
        server = new Server() {
            protected Connection newConnection () {
                // By providing our own connection implementation, we can store per
                // connection state without a connection ID to state look up.
                return new ChatConnection();
            }
        };

        // For consistency, the classes to be sent over the network are
        // registered by the same method for both the client and server.
        Network.register(server);

        server.addListener(new Listener() {
            public void received (Connection c, Object object) {
                // We know all connections for this server are actually ChatConnections.
                ChatConnection connection = (ChatConnection)c;

                if (object instanceof Network.RegisterName) {
                    // Ignore the object if a client has already registered a name. This is
                    // impossible with our client, but a hacker could send messages at any time.
                    if (connection.name != null) return;
                    // Ignore the object if the name is invalid.
                    String name = ((Network.RegisterName)object).name;
                    if (name == null) return;
                    name = name.trim();
                    if (name.length() == 0) return;
                    // Store the name on the connection.
                    connection.name = name;
                    // Send a "connected" message to everyone except the new client.
                    Network.ChatMessage chatMessage = new Network.ChatMessage();
                    chatMessage.text = name + " connected.";
                    server.sendToAllExceptTCP(connection.getID(), chatMessage);
                    // Send everyone a new list of connection names.
                    updateNames();
                    return;
                }

                if (object instanceof Network.ChatMessage) {
                    // Ignore the object if a client tries to chat before registering a name.
                    if (connection.name == null) return;
                    Network.ChatMessage chatMessage = (Network.ChatMessage)object;
                    // Ignore the object if the chat message is invalid.
                    String message = chatMessage.text;
                    if (message == null) return;
                    message = message.trim();
                    if (message.length() == 0) return;
                    // Prepend the connection's name and send to everyone.
                    chatMessage.text = connection.name + ": " + message;
                    server.sendToAllTCP(chatMessage);
                    return;
                }
            }

            public void disconnected (Connection c) {
                ChatConnection connection = (ChatConnection)c;
                if (connection.name != null) {
                    // Announce to everyone that someone (with a registered name) has left.
                    Network.ChatMessage chatMessage = new Network.ChatMessage();
                    chatMessage.text = connection.name + " disconnected.";
                    server.sendToAllTCP(chatMessage);
                    updateNames();
                }
            }
        });
        server.bind(Network.port);
        server.start();

        // Open a window to provide an easy way to stop the server.
        JFrame frame = new JFrame("Chat Server");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosed (WindowEvent evt) {
                server.stop();
            }
        });
        frame.getContentPane().add(new JLabel("Close to stop the chat server."));
        frame.setSize(320, 200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    void updateNames () {
        // Collect the names for each connection.
        Connection[] connections = server.getConnections();
        ArrayList names = new ArrayList(connections.length);
        for (int i = connections.length - 1; i >= 0; i--) {
            ChatConnection connection = (ChatConnection)connections[i];
            names.add(connection.name);
        }
        // Send the names to everyone.
        Network.UpdateNames updateNames = new Network.UpdateNames();
        updateNames.names = (String[])names.toArray(new String[names.size()]);
        server.sendToAllTCP(updateNames);
    }

    // This holds per connection state.
    static class ChatConnection extends Connection {
        public String name;
    }

    public static void main (String[] args) throws IOException {
        Log.set(Log.LEVEL_DEBUG);
        new GameServer();
    }*/

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start();
        server.bind(54555, 54777);
        Network.register(server);

        server.addListener(new Listener() {
            public void received (Connection connection, Object object) {
                System.out.println("Client connected");
                if (object instanceof Robot){
                    String answer = "Robot recieved!!";
                    System.out.println(answer);
                    Robot robot2 = new Robot(0,1);
                    connection.sendTCP(robot2);
                }
                if (object instanceof TestPacket){
                    System.out.println("recieved");
                    TestPacket pac = (TestPacket) object;
                    System.out.println(pac.text);
                    TestPacket resend = new TestPacket();
                    resend.text = "You said: " + pac.text;
                    connection.sendTCP(resend);
                }
            }
        });
    }

    /*
    public GameServer() throws IOException {
        Server server = new Server();
        server.start();
        server.bind(54555, 54777);
        Network.register(server);

        server.addListener(new Listener() {
            public void received (Connection connection, Object object) {
                System.out.println("Client connected");
                if (object instanceof Robot){
                    String answer = "Robot recieved!!";
                    System.out.println(answer);
                    Robot robot2 = new Robot(0,1);
                    connection.sendTCP(robot2);
                }
                if (object instanceof TestPacket){
                    System.out.println("recieved");
                    TestPacket pac = (TestPacket) object;
                    System.out.println(pac.text);
                    TestPacket resend = new TestPacket();
                    resend.text = "You said: " + pac;
                    connection.sendTCP(resend);
                }
            }
        });
    }*/
}