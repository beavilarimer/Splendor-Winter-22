import java.util.HashMap;
import java.util.Random;

public class Splendor {
    private Player[] players;
    private Deck[] table;
    private Noble[] nobles; //done
    private int[] chipBank;
    private Deck[] decks; //done

    public static final int LEVELS = 3;
    public static final int OPEN_CARDS = 4;
    public static final int NUM_NOBLES = 10;

    public static final int[][] nobleCosts = { { 4, 4, 0, 0, 0 }, 
    { 0, 4, 4, 0, 0 }, { 0, 4, 0, 4, 0 }, { 4, 0, 0, 0, 4 }, 
    { 0, 0, 0, 4, 4 }, { 3, 3, 3, 0, 0 }, { 3, 3, 0, 0, 3 },
    { 0, 3, 3, 3, 0 }, { 3, 0, 0, 3, 3 }, { 0, 0, 3, 3, 3 } };

    public static final int EASY_SIZE = 40;
    public static final int MEDIUM_SIZE = 30;
    public static final int HARD_SIZE = 20;
    public static final int E_LEVEL = 1;
    public static final int M_LEVEL = 2;
    public static final int H_LEVEL = 3;

    Splendor(int playerCount){
        genNobles(playerCount);
        genAllDecks();
    }

    private void genNobles(int playerCount){
        Noble[] nobles = new Noble[NUM_NOBLES];
        Random random = new Random();
        Noble temp;

        for (int i = 0; i < NUM_NOBLES; i++){
            nobles[i] = new Noble(nobleCosts[i]);
        }
    
        for(int i = 0; i < NUM_NOBLES; i++){
            int newIdx = random.nextInt(NUM_NOBLES - i);
            temp = nobles[i];
            this.nobles[i] = this.nobles[newIdx];
            this.nobles[newIdx] = temp;
        }

    }

    private Deck genEasy(){
        Deck easyDeck = new Deck(EASY_SIZE, E_LEVEL);

        easyDeck.addCard(new Card("diamond", new int[] { 0, 1, 1, 1, 1 }, 0));
        easyDeck.addCard(new Card("diamond", new int[] { 0, 1, 2, 1, 1 }, 0));
        easyDeck.addCard(new Card("diamond", new int[] { 0, 0, 0, 2, 1 }, 0));
        easyDeck.addCard(new Card("diamond", new int[] { 0, 2, 0, 0, 2 }, 0));
        easyDeck.addCard(new Card("diamond", new int[] { 0, 3, 0, 0, 0 }, 0));
        easyDeck.addCard(new Card("diamond", new int[] { 0, 2, 2, 0, 1 }, 0));
        easyDeck.addCard(new Card("diamond", new int[] { 3, 1, 0, 0, 1 }, 0));
        easyDeck.addCard(new Card("diamond", new int[] { 0, 0, 4, 0, 0 }, 1));
        easyDeck.addCard(new Card("sapphire", new int[] { 1, 0, 1, 1, 1 }, 0));
        easyDeck.addCard(new Card("sapphire", new int[] { 1, 0, 1, 2, 1 }, 0));
        easyDeck.addCard(new Card("sapphire", new int[] { 0, 1, 3, 1, 0 }, 0));
        easyDeck.addCard(new Card("sapphire", new int[] { 1, 0, 2, 2, 0 }, 0));
        easyDeck.addCard(new Card("sapphire", new int[] { 0, 0, 2, 0, 2 }, 0));
        easyDeck.addCard(new Card("sapphire", new int[] { 1, 0, 0, 0, 2 }, 0));
        easyDeck.addCard(new Card("sapphire", new int[] { 0, 0, 0, 0, 3 }, 0));
        easyDeck.addCard(new Card("sapphire", new int[] { 0, 0, 0, 4, 0 }, 1));
        easyDeck.addCard(new Card("emerald", new int[] { 1, 1, 0, 1, 1 }, 0));
        easyDeck.addCard(new Card("emerald", new int[] { 1, 1, 0, 1, 2 }, 0));
        easyDeck.addCard(new Card("emerald", new int[] { 1, 3, 1, 0, 0 }, 0));
        easyDeck.addCard(new Card("emerald", new int[] { 0, 1, 0, 2, 2 }, 0));
        easyDeck.addCard(new Card("emerald", new int[] { 0, 2, 0, 2, 0 }, 0));
        easyDeck.addCard(new Card("emerald", new int[] { 2, 1, 0, 0, 0 }, 0));
        easyDeck.addCard(new Card("emerald", new int[] { 0, 0, 0, 3, 0 }, 0));
        easyDeck.addCard(new Card("emerald", new int[] { 0, 0, 0, 0, 4 }, 1));
        easyDeck.addCard(new Card("ruby", new int[] { 1, 1, 1, 0, 1 }, 0));
        easyDeck.addCard(new Card("ruby", new int[] { 2, 1, 1, 0, 1 }, 0));
        easyDeck.addCard(new Card("ruby", new int[] { 1, 0, 0, 1, 3 }, 0));
        easyDeck.addCard(new Card("ruby", new int[] { 2, 0, 0, 2, 0 }, 0));
        easyDeck.addCard(new Card("ruby", new int[] { 0, 2, 1, 0, 0 }, 0));
        easyDeck.addCard(new Card("ruby", new int[] { 3, 0, 0, 0, 0 }, 0));
        easyDeck.addCard(new Card("ruby", new int[] { 2, 0, 1, 0, 2 }, 0));
        easyDeck.addCard(new Card("ruby", new int[] { 4, 0, 0, 0, 0 }, 1));
        easyDeck.addCard(new Card("onyx", new int[] { 1, 2, 1, 1, 0 }, 0));
        easyDeck.addCard(new Card("onyx", new int[] { 1, 1, 1, 1, 0 }, 0));
        easyDeck.addCard(new Card("onyx", new int[] { 0, 0, 1, 3, 1 }, 0));
        easyDeck.addCard(new Card("onyx", new int[] { 2, 2, 0, 1, 0 }, 0));
        easyDeck.addCard(new Card("onyx", new int[] { 2, 0, 2, 0, 0 }, 0));
        easyDeck.addCard(new Card("onyx", new int[] { 0, 0, 2, 1, 0 }, 0));
        easyDeck.addCard(new Card("onyx", new int[] { 0, 0, 3, 0, 0 }, 0));
        easyDeck.addCard(new Card("onyx", new int[] { 0, 4, 0, 0, 0 }, 1));

        return easyDeck;
    }

    private Deck genMedium(){
        Deck mediumDeck = new Deck(MEDIUM_SIZE, M_LEVEL);

        mediumDeck.addCard(new Card("diamond", new int[] { 0, 0, 3, 2, 2 }, 1));
        mediumDeck.addCard(new Card("diamond", new int[] { 2, 3, 0, 3, 0 }, 1));
        mediumDeck.addCard(new Card("diamond", new int[] { 0, 0, 1, 4, 2 }, 2));
        mediumDeck.addCard(new Card("diamond", new int[] { 0, 0, 0, 5, 3 }, 2));
        mediumDeck.addCard(new Card("diamond", new int[] { 0, 0, 0, 5, 0 }, 2));
        mediumDeck.addCard(new Card("diamond", new int[] { 6, 0, 0, 0, 0 }, 3));
        mediumDeck.addCard(new Card("sapphire", new int[] { 0, 2, 2, 3, 0 }, 1));
        mediumDeck.addCard(new Card("sapphire", new int[] { 0, 2, 3, 0, 3 }, 1));
        mediumDeck.addCard(new Card("sapphire", new int[] { 2, 0, 0, 1, 4 }, 2));
        mediumDeck.addCard(new Card("sapphire", new int[] { 5, 3, 0, 0, 0 }, 2));
        mediumDeck.addCard(new Card("sapphire", new int[] { 0, 5, 0, 0, 0 }, 2));
        mediumDeck.addCard(new Card("sapphire", new int[] { 0, 6, 0, 0, 0 }, 3));
        mediumDeck.addCard(new Card("emerald", new int[] { 2, 3, 0, 0, 2 }, 1));
        mediumDeck.addCard(new Card("emerald", new int[] { 3, 0, 2, 3, 0 }, 1));
        mediumDeck.addCard(new Card("emerald", new int[] { 4, 2, 0, 0, 1 }, 2));
        mediumDeck.addCard(new Card("emerald", new int[] { 0, 5, 3, 0, 0 }, 2));
        mediumDeck.addCard(new Card("emerald", new int[] { 0, 5, 0, 0, 0 }, 2));
        mediumDeck.addCard(new Card("emerald", new int[] { 0, 0, 6, 0, 0 }, 3));
        mediumDeck.addCard(new Card("ruby", new int[] { 2, 0, 0, 2, 3 }, 1));
        mediumDeck.addCard(new Card("ruby", new int[] { 0, 3, 0, 2, 3 }, 1));
        mediumDeck.addCard(new Card("ruby", new int[] { 1, 4, 2, 0, 0 }, 2));
        mediumDeck.addCard(new Card("ruby", new int[] { 3, 0, 0, 0, 5 }, 2));
        mediumDeck.addCard(new Card("ruby", new int[] { 0, 0, 0, 0, 5 }, 2));
        mediumDeck.addCard(new Card("ruby", new int[] { 0, 0, 0, 6, 0 }, 3));
        mediumDeck.addCard(new Card("onyx", new int[] { 3, 2, 2, 0, 0 }, 1));
        mediumDeck.addCard(new Card("onyx", new int[] { 3, 0, 3, 0, 2 }, 1));
        mediumDeck.addCard(new Card("onyx", new int[] { 0, 1, 4, 2, 0 }, 2));
        mediumDeck.addCard(new Card("onyx", new int[] { 0, 0, 5, 3, 0 }, 2));
        mediumDeck.addCard(new Card("onyx", new int[] { 5, 0, 0, 0, 0 }, 2));
        mediumDeck.addCard(new Card("onyx", new int[] { 0, 0, 0, 0, 6 }, 3));

        return mediumDeck;
    }

    private Deck genHard(){
        Deck hardDeck = new Deck(HARD_SIZE, H_LEVEL);

        hardDeck.addCard(new Card("diamond", new int[] { 0, 3, 3, 5, 3 }, 3));
        hardDeck.addCard(new Card("diamond", new int[] { 3, 0, 0, 3, 6 }, 4));
        hardDeck.addCard(new Card("diamond", new int[] { 0, 0, 0, 0, 7 }, 4));
        hardDeck.addCard(new Card("diamond", new int[] { 3, 0, 0, 0, 7 }, 5));
        hardDeck.addCard(new Card("sapphire", new int[] { 3, 0, 3, 3, 5 }, 3));
        hardDeck.addCard(new Card("sapphire", new int[] { 6, 3, 0, 0, 3 }, 4));
        hardDeck.addCard(new Card("sapphire", new int[] { 7, 0, 0, 0, 0 }, 4));
        hardDeck.addCard(new Card("sapphire", new int[] { 7, 3, 0, 0, 0 }, 5));
        hardDeck.addCard(new Card("emerald", new int[] { 5, 3, 0, 3, 3 }, 3));
        hardDeck.addCard(new Card("emerald", new int[] { 3, 6, 3, 0, 0 }, 4));
        hardDeck.addCard(new Card("emerald", new int[] { 0, 7, 0, 0, 0 }, 4));
        hardDeck.addCard(new Card("emerald", new int[] { 0, 7, 3, 0, 0 }, 5));
        hardDeck.addCard(new Card("ruby", new int[] { 3, 5, 3, 0, 3 }, 3));
        hardDeck.addCard(new Card("ruby", new int[] { 0, 3, 6, 3, 0 }, 4));
        hardDeck.addCard(new Card("ruby", new int[] { 0, 0, 7, 0, 0 }, 4));
        hardDeck.addCard(new Card("ruby", new int[] { 0, 0, 7, 3, 0 }, 5));
        hardDeck.addCard(new Card("onyx", new int[] { 3, 3, 5, 3, 0 }, 3));
        hardDeck.addCard(new Card("onyx", new int[] { 0, 3, 0, 6, 3 }, 4));
        hardDeck.addCard(new Card("onyx", new int[] { 0, 0, 0, 7, 0 }, 4));
        hardDeck.addCard(new Card("onyx", new int[] { 0, 0, 0, 7, 3 }, 5));

        return hardDeck;
    }

    private void genAllDecks(){
        Deck[] allDecks = new Deck[] {genEasy(), genMedium(), genHard()};
        this.decks = allDecks;
    }
}
