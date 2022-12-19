
public class Noble {
    private int[] cardCost;
    private boolean inPlay;

    Noble(int [] cardCost){
        this.cardCost = cardCost;
        this.inPlay = true;
    }

    // setters
    public void updateInPlay(){
        this.inPlay = false;
    }

    // getters
    public int[] getCardCost(){
        return cardCost;
    }
    public boolean getInPlay(){
        return inPlay;
    }
}


