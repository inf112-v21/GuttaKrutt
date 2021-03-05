package inf112.skeleton.app;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        GameServer server = new GameServer();
        GameClient client = new GameClient();
        server.server.close();

    }
}