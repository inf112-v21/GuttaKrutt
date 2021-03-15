package inf112.app.logic;

import inf112.app.Card;
import inf112.app.Deck;
import inf112.app.Player;
import inf112.app.Robot;
import inf112.app.networking.GameClient;
import inf112.app.screens.GameScreen;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.HashMap;
import java.util.Map;

/* controls the main loop of the game and executes the game rules */
public class GameLogic {

    GameScreen game;
    int turn;
    Deck deck;
    Map<UUID, Player> playerList;
    GameClient client;
    UUID uuid;

    public GameLogic() {this(null,null);}

    /* class constructor which sets the initial turn number, builds
    * a deck and initiates the players */
    public GameLogic(GameScreen game, GameClient client) {
        this.game = game;
        this.client = client;
        turn=0;
        deck = new Deck();
        buildDeck();
        deck.shuffle();
        playerList = client.getPlayerList();
        uuid = client.clientUUID;
        dealCards();
    }

    /* builds a deck of cards according to the game rules
    * (18 move1, 12 move2, 6 move3, 6 backup (reverse), 18 rotate
    * cards both directions, 6 U-turn) */
    public void buildDeck() {
        for (int i=490; i<=650; i+=10) {
            Card c = new Card(Card.CardType.MOVE1, i);
            deck.insert(c);
        }
        for (int i=670; i<=780; i+=10) {
            Card c = new Card(Card.CardType.MOVE2, i);
            deck.insert(c);
        }
        for (int i=790; i<=840; i+=10) {
            Card c = new Card(Card.CardType.MOVE3, i);
            deck.insert(c);
        }
        for (int i=430; i<=470; i+=10) {
            Card c = new Card(Card.CardType.BACKUP, i);
            deck.insert(c);
        }
        for (int i=80; i<=420; i+=20) {
            Card c = new Card(Card.CardType.ROTRIGHT, i);
            deck.insert(c);
        }
        for (int i=70; i<=410; i+=20) {
            Card c = new Card(Card.CardType.ROTLEFT, i);
            deck.insert(c);
        }
        for (int i=10; i<=60; i+=10) {
            Card c = new Card(Card.CardType.UTURN, i);
            deck.insert(c);
        }
    }

    public Deck getDeck() {
        return deck;
    }

    public void submit() {
        client.updatePlayer(uuid,playerList.get(uuid));
        Card[] cards = playerList.get(uuid).getRobot().getProgramRegister();
        for (Card card : cards) {
            if (card == null) {
                System.out.println("null");
            } else {
                System.out.println(card.getType());
            }
        }
    }

    /* the main loop of the game which starts a new turn,
    * deals cards to players and asks them to program registers. */
    public void doTurn() {
        playerList = client.getPlayerList();
        for (Player player : playerList.values()) {
            Card[] cards = player.getRobot().getProgramRegister();
            for (Card card : cards) {
                if (card == null) {
                    System.out.println("null");
                } else {
                    System.out.println(card.getType());
                }
            }
            System.out.println();
        }
        processCards();
        turn++;
        dealCards();
    }

    public int getTurn() {
        return turn;
    }

    public Map<UUID,Player> getPlayers() { return playerList; }

    /* deals cards to each player according to the number
    * of their damage tokens */
    public void dealCards() {
        for (Player p : playerList.values()) {
            for (int i=0; i < (9-p.getRobot().getDamage()); i++) {
                p.getCards().add(deck.take());
            }
        }
    }

    private void processCards() {
        Map<UUID,List<Card>> cards = new HashMap<>();
        for (Map.Entry<UUID,Player> entry : playerList.entrySet()) {
            List<Card> programRegister = new ArrayList<>();
            for (Card card : entry.getValue().getRobot().getProgramRegister()) {
                if (card != null) { programRegister.add(card); }
            }
            cards.put(entry.getKey(),programRegister);
        }

        while(true) {
            UUID nextRobot = UUID.randomUUID();
            int highPriority = 0;
            for (Map.Entry<UUID,List<Card>> entry : cards.entrySet()) {
                int priority;
                if (entry.getValue().size() == 0) {
                    priority = 0;
                } else {
                    priority = entry.getValue().get(0).getPriority();
                }

                if (highPriority < priority) {
                    highPriority = priority;
                    nextRobot = entry.getKey();
                }
            }
            if (highPriority == 0) {
                break;
            }

            Card card = cards.get(nextRobot).remove(0);
            useCard(playerList.get(nextRobot).getRobot(),card);
        }

        for (Player player : playerList.values()) {
            player.setCards(new Deck());
        }
    }

    private void useCard(Robot robot, Card card) {
        switch(card.getType()) {
            case MOVE1: game.boardLogic.move(robot,1); break;
            case MOVE2: game.boardLogic.move(robot,2); break;
            case MOVE3: game.boardLogic.move(robot,3); break;
            case BACKUP: game.boardLogic.moveBack(robot); break;
            case ROTLEFT: robot.rotate(1); break;
            case ROTRIGHT: robot.rotate(-1); break;
            case UTURN: robot.rotate(2); break;
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}