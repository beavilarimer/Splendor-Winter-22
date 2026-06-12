import java.util.Random;

public class Deck {
    private Card[] deck;
    private int size;
    private int level;

    Deck(int deckSize, int level) {
        this.size = 0;
        this.deck = new Card[deckSize];
        this.level = level;
    }

    public void addCard(Card card) {
        this.deck[size] = card;
        this.size++;
    }

    // Removes and returns the top card (last added after shuffle).
    public Card removeCard() {
        this.size--;
        return this.deck[this.size];
    }

    // Returns the card at position i from the top without removing it.
    public Card peekCard(int i) {
        return this.deck[this.size - 1 - i];
    }

    public void shuffleDeck() {
        Random random = new Random();
        Card temp;
        // Fisher-Yates: pick a random remaining element to swap into position i
        for (int i = 0; i < this.size - 1; i++) {
            int newIdx = i + random.nextInt(this.size - i);
            temp = this.deck[i];
            this.deck[i] = this.deck[newIdx];
            this.deck[newIdx] = temp;
        }
    }

    public int getLevel() {
        return level;
    }

    public int getSize() {
        return size;
    }
}