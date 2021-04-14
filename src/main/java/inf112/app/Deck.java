package inf112.app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/* an arraylist of cards which has three necessary functions:
* - take: get and remove the first card from the pile
* - insert: add a card to the pile
* - shuffle: change the order of all cards in the pile */
public class Deck extends ArrayList<Card>{
    Random rnd;
    List<Card> usedCards = new ArrayList<Card>();

    public Deck() { rnd = new Random(); }

    public Deck(int seed) {rnd = new Random(seed); }

    public Card take() {
        usedCards.add(super.get(super.size() - 1));
        return super.remove(super.size()-1);
    }

    public void insert(Card c) {
        super.add(c);
    }

    public void shuffle() { Collections.shuffle(this,rnd); }

    public void restock() {
        for (Card c : usedCards) {
            insert(c);
        }
        shuffle();
    }

}
