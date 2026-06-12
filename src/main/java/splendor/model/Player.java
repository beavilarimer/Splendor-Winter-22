package splendor.model;

public class Player {
    private String name;
    private Card reservedCard;
    private int chipCount;
    private int prestige;

    private int[] chipTracker;
    private int[] cardTracker;

    public static int playerCount;

    Player(String name) {
        this.name = name;
        this.reservedCard = null;
        this.chipCount = 0;
        this.prestige = 0;
        this.chipTracker = new int[6];
        this.cardTracker = new int[5];
        playerCount++;
    }

    public void updateChips(int[] difference) {
        for (int i = 0; i < chipTracker.length; i++) {
            this.chipTracker[i] += difference[i];
        }
    }

    public void updateCards(Card card) {
        int colorIdx = card.getCardColor();
        this.cardTracker[colorIdx] += 1;
    }

    public void reserveCard(Card card) {
        this.reservedCard = card;
        card.updateReserved();
    }

    public void clearReserved() {
        this.reservedCard = null;
    }

    public void updateGold(int difference) {
        this.chipTracker[5] += difference;
    }

    public void updateChipCount(int difference) {
        this.chipCount += difference;
    }

    public void updatePrestige(int prestige) {
        this.prestige += prestige;
    }

    public boolean hasReserved() {
        return reservedCard != null;
    }

    public Card getReservedCard() {
        return reservedCard;
    }

    public String getName() {
        return name;
    }

    public int getGold() {
        return chipTracker[5];
    }

    public int getChipCount() {
        return chipCount;
    }

    public int getPrestige() {
        return prestige;
    }

    public int[] getChipTracker() {
        return chipTracker;
    }

    public int[] getCardTracker() {
        return cardTracker;
    }
}