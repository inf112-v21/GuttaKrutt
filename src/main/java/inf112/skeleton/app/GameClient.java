package inf112.skeleton.app;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;
import org.lwjgl.system.CallbackI;

public class GameClient {
/*
    ChatFrame chatFrame;
    Client client;
    String name;

    public GameClient() {
        client = new Client();
        client.start();


        // For consistency, the classes to be sent over the network are
        // registered by the same method for both the client and server.
        Network.register(client);

        client.addListener(new Listener() {
            public void connected (Connection connection) {
                Network.RegisterName registerName = new Network.RegisterName();
                registerName.name = name;
                client.sendTCP(registerName);
            }

            public void received (Connection connection, Object object) {
                if (object instanceof Network.UpdateNames) {
                    Network.UpdateNames updateNames = (Network.UpdateNames)object;
                    chatFrame.setNames(updateNames.names);
                    return;
                }


            }

            public void disconnected (Connection connection) {
                EventQueue.invokeLater(new Runnable() {
                    public void run () {
                        // Closing the frame calls the close listener which will stop the client's update thread.
                        chatFrame.dispose();
                    }
                });
            }
        });

        // Request the host from the user.
        String input = (String) JOptionPane.showInputDialog(null, "Host:", "Connect to chat server", JOptionPane.QUESTION_MESSAGE,
                null, null, "localhost");
        if (input == null || input.trim().length() == 0) System.exit(1);
        final String host = input.trim();

        // Request the user's name.
        input = (String)JOptionPane.showInputDialog(null, "Name:", "Connect to chat server", JOptionPane.QUESTION_MESSAGE, null,
                null, "Test");
        if (input == null || input.trim().length() == 0) System.exit(1);
        name = input.trim();

        // All the ugly Swing stuff is hidden in ChatFrame so it doesn't clutter the KryoNet example code.
        chatFrame = new ChatFrame(host);
        // This listener is called when the send button is clicked.
        chatFrame.setSendListener(new Runnable() {

        });
        // This listener is called when the chat window is closed.
        chatFrame.setCloseListener(new Runnable() {
            public void run () {
                client.stop();
            }
        });
        chatFrame.setVisible(true);

        // We'll do the connect on a new thread so the ChatFrame can show a progress bar.
        // Connecting to localhost is usually so fast you won't see the progress bar.
        new Thread("Connect") {
            public void run () {
                try {
                    client.connect(5000, host, Network.port);
                    // Server communication after connection can go here, or in Listener#connected().
                } catch (IOException ex) {
                    ex.printStackTrace();
                    System.exit(1);
                }
            }
        }.start();
    }

    static private class ChatFrame extends JFrame {
        CardLayout cardLayout;
        JProgressBar progressBar;
        JList messageList;
        JTextField sendText;
        JButton sendButton;
        JList nameList;

        public ChatFrame (String host) {
            super("Chat Client");
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setSize(640, 200);
            setLocationRelativeTo(null);

            Container contentPane = getContentPane();
            cardLayout = new CardLayout();
            contentPane.setLayout(cardLayout);
            {
                JPanel panel = new JPanel(new BorderLayout());
                contentPane.add(panel, "progress");
                panel.add(new JLabel("Connecting to " + host + "..."));
                {
                    panel.add(progressBar = new JProgressBar(), BorderLayout.SOUTH);
                    progressBar.setIndeterminate(true);
                }
            }
            {
                JPanel panel = new JPanel(new BorderLayout());
                contentPane.add(panel, "chat");
                {
                    JPanel topPanel = new JPanel(new GridLayout(1, 2));
                    panel.add(topPanel);
                    {
                        topPanel.add(new JScrollPane(messageList = new JList()));
                        messageList.setModel(new DefaultListModel());
                    }
                    {
                        topPanel.add(new JScrollPane(nameList = new JList()));
                        nameList.setModel(new DefaultListModel());
                    }
                    DefaultListSelectionModel disableSelections = new DefaultListSelectionModel() {
                        public void setSelectionInterval (int index0, int index1) {
                        }
                    };
                    messageList.setSelectionModel(disableSelections);
                    nameList.setSelectionModel(disableSelections);
                }
                {
                    JPanel bottomPanel = new JPanel(new GridBagLayout());
                    panel.add(bottomPanel, BorderLayout.SOUTH);
                    bottomPanel.add(sendText = new JTextField(), new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.CENTER,
                            GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
                    bottomPanel.add(sendButton = new JButton("Send"), new GridBagConstraints(1, 0, 1, 1, 0, 0,
                            GridBagConstraints.CENTER, 0, new Insets(0, 0, 0, 0), 0, 0));
                }
            }

            sendText.addActionListener(new ActionListener() {
                public void actionPerformed (ActionEvent e) {
                    sendButton.doClick();
                }
            });
        }

        public void setSendListener (final Runnable listener) {
            sendButton.addActionListener(new ActionListener() {
                public void actionPerformed (ActionEvent evt) {
                    if (getSendText().length() == 0) return;
                    listener.run();
                    sendText.setText("");
                    sendText.requestFocus();
                }
            });
        }

        public void setCloseListener (final Runnable listener) {
            addWindowListener(new WindowAdapter() {
                public void windowClosed (WindowEvent evt) {
                    listener.run();
                }

                public void windowActivated (WindowEvent evt) {
                    sendText.requestFocus();
                }
            });
        }

        public String getSendText () {
            return sendText.getText().trim();
        }

        public void setNames (final String[] names) {
            // This listener is run on the client's update thread, which was started by client.start().
            // We must be careful to only interact with Swing components on the Swing event thread.
            EventQueue.invokeLater(new Runnable() {
                public void run () {
                    cardLayout.show(getContentPane(), "chat");
                    DefaultListModel model = (DefaultListModel)nameList.getModel();
                    model.removeAllElements();
                    for (String name : names)
                        model.addElement(name);
                }
            });
        }

        public void addMessage (final String message) {
            EventQueue.invokeLater(new Runnable() {
                public void run () {
                    DefaultListModel model = (DefaultListModel)messageList.getModel();
                    model.addElement(message);
                    messageList.ensureIndexIsVisible(model.size() - 1);
                }
            });
        }
    }
    */

    private static Scanner scanner = new Scanner(System.in);
    private Client client;
    private Network.NumberOfPlayers playersInGame = new Network.NumberOfPlayers();
    private Player player;
    private static Integer expectedPlayers = 1;
    boolean gotPackage = false;

    public GameClient() throws IOException {
        //Initiating client
        this.client = new Client();
        client.start();
        String host = inputHost();
        client.connect(5000, host, 54555, 54777);

        Network.register(client);


        client.addListener(new Listener() {
            public void received(Connection connection, Object object) {
                //System.out.println("connected to server");
                if (object instanceof Robot) {
                    System.out.println("Thanks");
                    connection.sendTCP(new Robot(0, 0));

                }
                if (object instanceof Network.NumberOfPlayers) {
                    System.out.println("recieved playeramount");
                    Network.NumberOfPlayers numberOfPlayers = (Network.NumberOfPlayers) object;
                    setNumberOfPlayers(numberOfPlayers.amount);
                    System.out.println(playersInGame.amount);
                }
                if(object instanceof Network.UpdatePlayer){
                    System.out.println("recieved player robot");
                    Network.UpdatePlayer player = (Network.UpdatePlayer) object;
                }
                if(object instanceof Network.TestPacket){
                    System.out.println("Recieved test packet");
                    Network.TestPacket packet = (Network.TestPacket) object;
                    gotPackage = true;
                }
            }
        });

        //Starting the game if enough players joined
        while (client.isConnected()) {
            if (playersInGame.amount != null) {
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
        Lwjgl3ApplicationConfiguration cfg = new Lwjgl3ApplicationConfiguration();
        cfg.setTitle("test");
        cfg.setWindowedMode(500, 500);

        Robot[] robots = new Robot[playersInGame.amount];
        for (int i = 0; i < playersInGame.amount; i++) {
            robots[i] = new Robot();
        }

        new Lwjgl3Application(new GUI(robots), cfg);
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
    /*
    public void setGotPackage(boolean bool){
        gotPackage = bool;
    }*/
}

