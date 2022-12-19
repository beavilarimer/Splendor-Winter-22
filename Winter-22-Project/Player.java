//String[] typesOfGems = { "diamond", "sapphire", "emerald", "ruby", "onyx" };
//String[] typesOfGems = { "white", "blue", "green", "red", "black" };


public class Player{
    private boolean reserved;    
    private int chipCount;
    private int prestige;

    private int[] chipTracker;
    private int[] cardTracker;
    
    public static int playerCount;

    Player(String name){
        this.reserved = false;
        this.chipCount = 0;
        this.prestige = 0;
        this.chipTracker = new int[6];
        this.cardTracker = new int[5];
        playerCount++;
    }

    /* settters 
     * can be used for all chip exchange just make sure to update the signs elsewhere
    */
    public void updateChips(int[] difference){
        for(int i = 0; i < chipTracker.length; i++){
            this.chipTracker[i] += difference[i];
        }
    }

    public void updateCards(Card card){
        int color = card.getCardColor();
        this.cardTracker[color] += 1;
    }

    public void updateReserved(){
        this.reserved = true;
    }

    public void updateGold(int difference){
        this.chipTracker[5] += difference;
    }

    public void updateChipCount(int difference){
        this.chipCount += difference;
    }

    public void updatePrestige(int prestige){
        this.prestige += prestige;
    }

    //getters
    public boolean getReserved(){
        return reserved;
    }

    public int getGold(){
        return chipTracker[5];
    }

    public int getChipCount(){
        return chipCount;
    }

    public int getPrestige(){
        return prestige;
    }

    public int[] getChipTracker(){
        return chipTracker;
    }

    public int[] getCardTracker(){
        return cardTracker;
    }

}