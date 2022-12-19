
import java.util.Random;

public class Deck {
    private Card[] deck;
    private int size;
    private int level;

    Deck(int deckSize, int level){
        this.size = 0;
        this.deck = new Card[deckSize];
        this.level = level;
    }

    public void addCard(Card card){
        this.deck[size] = card;
        this.size++;
    }

    public Card removeCard(){
        this.size--;
        return this.deck[this.size];
    }

    public void shuffleDeck(){
        Random random = new Random();
        Card temp;
        for(int i = 0; i < this.size; i++){
            int newIdx = random.nextInt(this.size - i);
            temp = this.deck[i];
            this.deck[i] = this.deck[newIdx];
            this.deck[newIdx] = temp;
        }
    }

    public int getLevel(){
        return level;
    }

}
