package inf112.skeleton.app;

public class Card {

    int priority;
    CardType type;

    enum CardType {
        MOVE1,
        MOVE2,
        MOVE3,
        BACKUP,
        ROTRIGHT,
        ROTLEFT,
        UTURN
    }

    public Card(CardType type, int priority) {
        this.priority = priority;
        this.type = type;
    }

    public int getPriority() {
        return priority;
    }

    public CardType getType() {
        return type;
    }

}
