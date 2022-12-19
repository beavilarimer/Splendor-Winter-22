
public class Card{
    private boolean reserved;
    private int prestige;
    private String color;
    private int colorIdx;

    private int[] chipCost;

    Card(String color, int[] chipCost, int prestige){
        this.reserved = false;
        this.prestige = prestige;
        this.color = color;
        //this.colorIdx = colorIdx;
        this.chipCost = chipCost;
    }
    
    /* settters 
     * can be used for all chip exchange just make sure to update the signs elsewhere
    */
    public void updateReserved(){
        this.reserved = true;
    }

    //getters
    public boolean getReserved(){
        return reserved;
    }
    public int getPrestige(){
        return prestige;
    }
    public int getCardColor(){
        return colorIdx;
    }
    public int[] getChipCost(){
        return chipCost;
    }
}