package splendor.model;

public class Card {
    private Player reservedBy;
    private int prestige;
    private String color;
    private int colorIdx;
    private int[] chipCost;

    Card(String color, int[] chipCost, int prestige) {
        this.reservedBy = null;
        this.prestige = prestige;
        this.color = color;
        this.colorIdx = colorToIndex(color);
        this.chipCost = chipCost;
    }

    private static int colorToIndex(String color) {
        switch (color) {
            case "diamond":  return 0;
            case "sapphire": return 1;
            case "emerald":  return 2;
            case "ruby":     return 3;
            case "onyx":     return 4;
            default: throw new IllegalArgumentException("Unknown card color: " + color);
        }
    }

    public void setReservedBy(Player player) {
        this.reservedBy = player;
    }

    public void clearReservedBy() {
        this.reservedBy = null;
    }

    public Player getReservedBy() {
        return reservedBy;
    }

    public int getPrestige() {
        return prestige;
    }

    public String getColor() {
        return color;
    }

    public int getCardColor() {
        return colorIdx;
    }

    public int[] getChipCost() {
        return chipCost;
    }
}