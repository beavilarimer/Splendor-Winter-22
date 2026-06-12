public class Card {
    private boolean reserved;
    private int prestige;
    private String color;
    private int colorIdx;
    private int[] chipCost;

    Card(String color, int[] chipCost, int prestige) {
        this.reserved = false;
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

    public void updateReserved() {
        this.reserved = true;
    }

    public boolean getReserved() {
        return reserved;
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