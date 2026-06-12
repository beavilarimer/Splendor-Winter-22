public class Noble {
    public static final int PRESTIGE = 3;

    private int[] cardCost;
    private boolean inPlay;

    Noble(int[] cardCost) {
        this.cardCost = cardCost;
        this.inPlay = true;
    }

    public void updateInPlay() {
        this.inPlay = false;
    }

    public int[] getCardCost() {
        return cardCost;
    }

    public boolean getInPlay() {
        return inPlay;
    }
}