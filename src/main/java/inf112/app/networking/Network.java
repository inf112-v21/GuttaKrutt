package inf112.app.networking;

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
        kryo.register(Card.class, new CardSerializer());
        kryo.register(Card.CardType.class);
        kryo.register(Card[].class);
        kryo.register(com.badlogic.gdx.math.Vector2.class);
        kryo.register(RegisterName.class);
        kryo.register(UpdateNames.class);
        kryo.register(NumberOfPlayers.class);
        kryo.register(UpdatePlayer.class);
        kryo.register(UpdatePlayers.class);
        kryo.register(NewConnection.class);
        kryo.register(RunGame.class);
        kryo.register(MapName.class);
        kryo.register(TestPacket.class);
    }

    //Field for registering the name of a client
    static public class RegisterName {
        public String name;
    }

    //Field for list of all client names
    static public class UpdateNames {
        public String[] names;
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

    static public class NewConnection {}
    static public class RunGame {}

    //Field for number of players
    static public class NumberOfPlayers {
        public Integer amount;
    }

    static public  class TestPacket{
        public String packet;
    }

    public static class UUIDSerializer extends Serializer<UUID> {

        @Override
        public void write(Kryo kryo, Output output, UUID uuid) {
            output.writeInt((int) uuid.getLeastSignificantBits());
            output.writeInt((int) uuid.getMostSignificantBits());
        }

        @Override
        public UUID read(Kryo kryo, Input input, Class<UUID> aClass) {
            return new UUID(input.readInt(),input.readInt());
        }
    }

    public static class CardSerializer extends Serializer<Card> {

        @Override
        public void write(Kryo kryo, Output output, Card card) {
            output.write(card.getPriority());
            output.write(card.getTypeInt());
        }

        @Override
        public Card read(Kryo kryo, Input input, Class<Card> aClass) {
            Card.CardType cardType;
            int type = input.read();
            switch (type) {
                case 0: cardType = MOVE1;
                case 1: cardType = MOVE2;
                case 2: cardType = MOVE3;
                case 3: cardType = BACKUP;
                case 4: cardType = ROTRIGHT;
                case 5: cardType = ROTLEFT;
                default: cardType = UTURN;
            }

            return new Card(cardType,input.read());
        }
    }
}
