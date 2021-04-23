package inf112.app.networking;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryonet.EndPoint;
import inf112.app.Card;
import inf112.app.Deck;
import inf112.app.Player;
import inf112.app.Robot;
import inf112.app.logic.BoardLogic;
import inf112.app.logic.GameLogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static inf112.app.Card.CardType.*;

/**
 * This class registers each class that is sent over the internet
 */
public class Network {

    // This registers objects that are going to be sent over the network.
    static public void register (EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(Robot.class);
        kryo.register(Player.class);
        kryo.register(BoardLogic.class);
        kryo.register(GameLogic.class);
        kryo.register(HashMap.class);
        kryo.register(UUID.class, new UUIDSerializer());
        kryo.register(Deck.class);
        kryo.register(Card.class);
        kryo.register(Card.CardType.class);
        kryo.register(Card[].class);
        kryo.register(int[].class);
        kryo.register(com.badlogic.gdx.math.Vector2.class);
        kryo.register(RegisterName.class);
        kryo.register(UpdatePlayer.class);
        kryo.register(UpdatePlayers.class);
        kryo.register(RemovePlayer.class);
        kryo.register(NewWinner.class);
        kryo.register(RunGame.class);
        kryo.register(Ready.class);
        kryo.register(NewGame.class);
        kryo.register(MapName.class);
        kryo.register(MapVote.class);
        kryo.register(MapVotes.class);
        kryo.register(Seed.class);
        kryo.register(TestPacket.class);
        kryo.register(ArrayList.class);
    }

    //Field for registering the name of a client
    static public class RegisterName {
        public String name;
    }

    //Field for updating the player
    static public class UpdatePlayer {
        public UUID uuid;
        public Player player;
    }

    static public class MapName {
        public String mapName;
    }

    //Field for updating all players
    static public class UpdatePlayers {
        public Map<UUID,Player> playerList;
    }

    static public class RemovePlayer {
        public UUID uuid;
    }

    static public class NewWinner {
        public UUID uuid;
    }

    static public class Ready {
        public UUID uuid;
    }
    static public class RunGame {}
    static public class NewGame {}

    static public class MapVote {
        public String mapName;
    }
    static public class MapVotes {
        public Map<String,Integer> votes;
    }

    static public class Seed {
        public int seed;
    }

    static public  class TestPacket{
        public String packet;
    }

    public static class UUIDSerializer extends Serializer<UUID> {

        @Override
        public void write(Kryo kryo, Output output, UUID uuid) {
            output.writeString(uuid.toString());
        }

        @Override
        public UUID read(Kryo kryo, Input input, Class<UUID> aClass) {
            return UUID.fromString(input.readString());

        }
    }
}
