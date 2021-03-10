package inf112.skeleton.app;

/* contains all information about cards */
public class Card {

    int priority;
    CardType type;

    /* every card belongs to one of the following types: */
    enum CardType {
        MOVE1,
        MOVE2,
        MOVE3,
        BACKUP,
        ROTRIGHT,
        ROTLEFT,
        UTURN
    }

    /* constructor class which sets the priority and
    * type of the card (two necessary input values) */
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
