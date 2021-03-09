package inf112.skeleton.app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* an arraylist of cards which has three necessary functions:
* - take: get and remove the first card from the pile
* - insert: add a card to the pile
* - shuffle: change the order of all cards in the pile */
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
