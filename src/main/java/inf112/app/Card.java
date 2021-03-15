package inf112.app;

/* contains all information about cards */
public class Card {

    int priority;
    CardType type;



    /* every card belongs to one of the following types: */
    public enum CardType {
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

    public int getTypeInt() {
        switch (type) {
            case MOVE1: return 0;
            case MOVE2: return 1;
            case MOVE3: return 2;
            case BACKUP: return 3;
            case ROTRIGHT: return 4;
            case ROTLEFT: return 5;
            case UTURN: return 6;
        }
        return 0;
    }
}
