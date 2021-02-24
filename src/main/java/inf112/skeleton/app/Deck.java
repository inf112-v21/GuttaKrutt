package inf112.skeleton.app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck extends ArrayList<Card>{

    public Card take() {
        return (Card) super.remove(super.size()-1);
    }

    public void insert(Card c) {
        super.add(c);
    }

    public void shuffle() {
        Collections.shuffle(this);
    }

}
