package inf112.skeleton.app;


import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;

import static com.esotericsoftware.jsonbeans.JsonValue.ValueType.object;

public class Multiplayer {


    public static void main(String[] args) throws IOException {


        Server server = new Server();
        server.start();
        server.bind(54555, 54777);

        Listener listener = new Listener() {
            public void received (Connection connection, Object object) {
                if (object instanceof SomeRequest) {
                    SomeRequest request = (SomeRequest)object;
                    System.out.println(request.text);

                    SomeResponse response = new SomeResponse();
                    response.text = "Thanks";
                    connection.sendTCP(response);
                }
            }
        };
        server.addListener(listener);

        Client client = new Client();
        client.start();
        client.connect(5000, "192.168.0.4", 54555, 54777);

        SomeRequest request = new SomeRequest();
        request.text = "Here is the request";
        client.sendTCP(request);

        client.addListener(new Listener() {
            public void received (Connection connection, Object object) {
                if (object instanceof SomeResponse) {
                    SomeResponse response = (SomeResponse)object;
                    System.out.println(response.text);
                }
            }
        });

        Kryo kryo = server.getKryo();
        kryo.register(SomeRequest.class);
        kryo.register(SomeResponse.class);
        Kryo kryo = client.getKryo();
        kryo.register(SomeRequest.class);
        kryo.register(SomeResponse.class);
    }
    public static class SomeRequest {
        public String text;
    }
    public static class SomeResponse {
        public String text;
    }






    //Client client = new Client();




}
