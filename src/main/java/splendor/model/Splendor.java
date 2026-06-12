package splendor.model;

import java.util.Random;

public class Splendor {
    private Player[] players;
    private Card[][] table;       // [level 0-2][slot 0-3], null = empty slot
    private Noble[] nobles;
    private int[] chipBank;       // [diamond, sapphire, emerald, ruby, onyx, gold]
    private Deck[] decks;         // [easy, medium, hard]
    private int currentPlayer;
    private int playerCount;
    private boolean finalRound;

    public static final int LEVELS = 3;
    public static final int OPEN_CARDS = 4;
    public static final int NUM_NOBLES = 10;
    public static final int WIN_PRESTIGE = 15;

    public static final int[][] nobleCosts = {
        { 4, 4, 0, 0, 0 }, { 0, 4, 4, 0, 0 }, { 0, 4, 0, 4, 0 },
        { 4, 0, 0, 0, 4 }, { 0, 0, 0, 4, 4 }, { 3, 3, 3, 0, 0 },
        { 3, 3, 0, 0, 3 }, { 0, 3, 3, 3, 0 }, { 3, 0, 0, 3, 3 },
        { 0, 0, 3, 3, 3 }
    };

    public static final int EASY_SIZE   = 40;
    public static final int MEDIUM_SIZE = 30;
    public static final int HARD_SIZE   = 20;
    public static final int E_LEVEL = 1;
    public static final int M_LEVEL = 2;
    public static final int H_LEVEL = 3;

    public Splendor(int playerCount) {
        this.playerCount   = playerCount;
        this.currentPlayer = 0;
        this.finalRound    = false;
        this.players = new Player[playerCount];
        for (int i = 0; i < playerCount; i++)
            players[i] = new Player("Player " + (i + 1));
        genNobles(playerCount);
        genAllDecks();
        initChipBank(playerCount);
        initTable();
    }

    public Splendor(String[] names) {
        this.playerCount   = names.length;
        this.currentPlayer = 0;
        this.finalRound    = false;
        this.players = new Player[playerCount];
        for (int i = 0; i < playerCount; i++)
            players[i] = new Player(names[i]);
        genNobles(playerCount);
        genAllDecks();
        initChipBank(playerCount);
        initTable();
    }

    // -------------------------------------------------------------------------
    // Setup
    // -------------------------------------------------------------------------

    private void initChipBank(int playerCount) {
        this.chipBank = new int[6];
        int colorChips = (playerCount == 2) ? 4 : (playerCount == 3) ? 5 : 7;
        for (int i = 0; i < 5; i++)
            chipBank[i] = colorChips;
        chipBank[5] = 5; // gold is always 5
    }

    private void initTable() {
        this.table = new Card[LEVELS][OPEN_CARDS];
        for (int level = 0; level < LEVELS; level++) {
            decks[level].shuffleDeck();
            for (int slot = 0; slot < OPEN_CARDS; slot++)
                table[level][slot] = decks[level].removeCard();
        }
    }

    private void genNobles(int playerCount) {
        Noble[] allNobles = new Noble[NUM_NOBLES];
        Random random = new Random();
        Noble temp;

        for (int i = 0; i < NUM_NOBLES; i++)
            allNobles[i] = new Noble(nobleCosts[i]);

        for (int i = 0; i < NUM_NOBLES - 1; i++) {
            int newIdx = i + random.nextInt(NUM_NOBLES - i);
            temp = allNobles[i];
            allNobles[i] = allNobles[newIdx];
            allNobles[newIdx] = temp;
        }

        int noblesInPlay = playerCount + 1;
        this.nobles = new Noble[noblesInPlay];
        for (int i = 0; i < noblesInPlay; i++)
            this.nobles[i] = allNobles[i];
    }

    private void genAllDecks() {
        this.decks = new Deck[] { genEasy(), genMedium(), genHard() };
    }

    // -------------------------------------------------------------------------
    // Turn actions — each returns false if the move is illegal
    // -------------------------------------------------------------------------

    public boolean takeThreeChips(Player player, int[] colors) {
        if (colors.length != 3) return false;
        if (colors[0] == colors[1] || colors[1] == colors[2] || colors[0] == colors[2])
            return false;
        for (int c : colors)
            if (c < 0 || c > 4 || chipBank[c] < 1) return false;
        if (player.getChipCount() + 3 > 10) return false;

        int[] diff = new int[6];
        for (int c : colors) {
            chipBank[c]--;
            diff[c] = 1;
        }
        player.updateChips(diff);
        player.updateChipCount(3);
        return true;
    }

    public boolean takeTwoChips(Player player, int color) {
        if (color < 0 || color > 4) return false;
        if (chipBank[color] < 4) return false;
        if (player.getChipCount() + 2 > 10) return false;

        int[] diff = new int[6];
        chipBank[color] -= 2;
        diff[color] = 2;
        player.updateChips(diff);
        player.updateChipCount(2);
        return true;
    }

    // level = -1, slot = -1 signals buying from the player's reserved card
    public boolean buyCard(Player player, Card card, int level, int slot) {
        if (!canAfford(player, card)) return false;

        payForCard(player, card);
        player.updateCards(card);
        player.updatePrestige(card.getPrestige());

        if (slot == -1) {
            player.clearReserved();
        } else {
            table[level][slot] = (decks[level].getSize() > 0)
                ? decks[level].removeCard()
                : null;
        }

        checkNobles(player);
        return true;
    }

    public boolean reserveCard(Player player, int level, int slot) {
        if (player.hasReserved()) return false;
        if (table[level][slot] == null) return false;

        player.reserveCard(table[level][slot]);

        table[level][slot] = (decks[level].getSize() > 0)
            ? decks[level].removeCard()
            : null;

        if (chipBank[5] > 0 && player.getChipCount() < 10) {
            chipBank[5]--;
            player.updateGold(1);
            player.updateChipCount(1);
        }
        return true;
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    public boolean canAfford(Player player, Card card) {
        int[] chipCost    = card.getChipCost();
        int[] chips       = player.getChipTracker();
        int[] cards       = player.getCardTracker();
        int   goldNeeded  = 0;

        for (int i = 0; i < 5; i++) {
            int effectiveCost = Math.max(0, chipCost[i] - cards[i]);
            int shortage      = Math.max(0, effectiveCost - chips[i]);
            goldNeeded += shortage;
        }
        return goldNeeded <= chips[5];
    }

    private void payForCard(Player player, Card card) {
        int[] chipCost = card.getChipCost();
        int[] chips    = player.getChipTracker();
        int[] cards    = player.getCardTracker();
        int[] diff     = new int[6];
        int   totalPaid = 0;

        for (int i = 0; i < 5; i++) {
            int effectiveCost = Math.max(0, chipCost[i] - cards[i]);
            int paid          = Math.min(effectiveCost, chips[i]);
            int gold          = effectiveCost - paid;
            diff[i]  = -paid;
            diff[5] -= gold;
            chipBank[i] += paid;
            chipBank[5] += gold;
            totalPaid   += paid + gold;
        }

        player.updateChips(diff);
        player.updateChipCount(-totalPaid);
    }

    // Nobles auto-visit after a card purchase; at most one per turn.
    private void checkNobles(Player player) {
        int[] cards = player.getCardTracker();
        for (Noble noble : nobles) {
            if (!noble.getInPlay()) continue;
            int[] cost      = noble.getCardCost();
            boolean qualifies = true;
            for (int j = 0; j < 5; j++) {
                if (cards[j] < cost[j]) { qualifies = false; break; }
            }
            if (qualifies) {
                noble.updateInPlay();
                player.updatePrestige(Noble.PRESTIGE);
                break;
            }
        }
    }

    // -------------------------------------------------------------------------
    // Game flow
    // -------------------------------------------------------------------------

    // Returns true if the game is over (final round completed).
    public boolean advanceTurn() {
        if (checkWin()) finalRound = true;
        currentPlayer = (currentPlayer + 1) % playerCount;
        return finalRound && currentPlayer == 0;
    }

    public Player resolveWinner() {
        Player winner   = players[0];
        int    minCards = totalCards(players[0]);

        for (int i = 1; i < players.length; i++) {
            int p = players[i].getPrestige();
            int c = totalCards(players[i]);
            if (p > winner.getPrestige() || (p == winner.getPrestige() && c < minCards)) {
                winner   = players[i];
                minCards = c;
            }
        }
        return winner;
    }

    private boolean checkWin() {
        for (Player p : players)
            if (p.getPrestige() >= WIN_PRESTIGE) return true;
        return false;
    }

    private int totalCards(Player player) {
        int total = 0;
        for (int c : player.getCardTracker()) total += c;
        return total;
    }

    // -------------------------------------------------------------------------
    // Getters for the view layer
    // -------------------------------------------------------------------------

    public Card[][] getTable()         { return table; }
    public Noble[]  getNobles()        { return nobles; }
    public int[]    getChipBank()      { return chipBank; }
    public Player[] getPlayers()       { return players; }
    public int      getCurrentPlayer() { return currentPlayer; }
    public boolean  isFinalRound()     { return finalRound; }
    public int      getDeckSize(int level) { return decks[level].getSize(); }

    // -------------------------------------------------------------------------
    // Card data
    // -------------------------------------------------------------------------

    private Deck genEasy() {
        Deck d = new Deck(EASY_SIZE, E_LEVEL);

        d.addCard(new Card("diamond", new int[] { 0, 1, 1, 1, 1 }, 0));
        d.addCard(new Card("diamond", new int[] { 0, 1, 2, 1, 1 }, 0));
        d.addCard(new Card("diamond", new int[] { 0, 0, 0, 2, 1 }, 0));
        d.addCard(new Card("diamond", new int[] { 0, 2, 0, 0, 2 }, 0));
        d.addCard(new Card("diamond", new int[] { 0, 3, 0, 0, 0 }, 0));
        d.addCard(new Card("diamond", new int[] { 0, 2, 2, 0, 1 }, 0));
        d.addCard(new Card("diamond", new int[] { 3, 1, 0, 0, 1 }, 0));
        d.addCard(new Card("diamond", new int[] { 0, 0, 4, 0, 0 }, 1));

        d.addCard(new Card("sapphire", new int[] { 1, 0, 1, 1, 1 }, 0));
        d.addCard(new Card("sapphire", new int[] { 1, 0, 1, 2, 1 }, 0));
        d.addCard(new Card("sapphire", new int[] { 0, 1, 3, 1, 0 }, 0));
        d.addCard(new Card("sapphire", new int[] { 1, 0, 2, 2, 0 }, 0));
        d.addCard(new Card("sapphire", new int[] { 0, 0, 2, 0, 2 }, 0));
        d.addCard(new Card("sapphire", new int[] { 1, 0, 0, 0, 2 }, 0));
        d.addCard(new Card("sapphire", new int[] { 0, 0, 0, 0, 3 }, 0));
        d.addCard(new Card("sapphire", new int[] { 0, 0, 0, 4, 0 }, 1));

        d.addCard(new Card("emerald", new int[] { 1, 1, 0, 1, 1 }, 0));
        d.addCard(new Card("emerald", new int[] { 1, 1, 0, 1, 2 }, 0));
        d.addCard(new Card("emerald", new int[] { 1, 3, 1, 0, 0 }, 0));
        d.addCard(new Card("emerald", new int[] { 0, 1, 0, 2, 2 }, 0));
        d.addCard(new Card("emerald", new int[] { 0, 2, 0, 2, 0 }, 0));
        d.addCard(new Card("emerald", new int[] { 2, 1, 0, 0, 0 }, 0));
        d.addCard(new Card("emerald", new int[] { 0, 0, 0, 3, 0 }, 0));
        d.addCard(new Card("emerald", new int[] { 0, 0, 0, 0, 4 }, 1));

        d.addCard(new Card("ruby", new int[] { 1, 1, 1, 0, 1 }, 0));
        d.addCard(new Card("ruby", new int[] { 2, 1, 1, 0, 1 }, 0));
        d.addCard(new Card("ruby", new int[] { 1, 0, 0, 1, 3 }, 0));
        d.addCard(new Card("ruby", new int[] { 2, 0, 0, 2, 0 }, 0));
        d.addCard(new Card("ruby", new int[] { 0, 2, 1, 0, 0 }, 0));
        d.addCard(new Card("ruby", new int[] { 3, 0, 0, 0, 0 }, 0));
        d.addCard(new Card("ruby", new int[] { 2, 0, 1, 0, 2 }, 0));
        d.addCard(new Card("ruby", new int[] { 4, 0, 0, 0, 0 }, 1));

        d.addCard(new Card("onyx", new int[] { 1, 2, 1, 1, 0 }, 0));
        d.addCard(new Card("onyx", new int[] { 1, 1, 1, 1, 0 }, 0));
        d.addCard(new Card("onyx", new int[] { 0, 0, 1, 3, 1 }, 0));
        d.addCard(new Card("onyx", new int[] { 2, 2, 0, 1, 0 }, 0));
        d.addCard(new Card("onyx", new int[] { 2, 0, 2, 0, 0 }, 0));
        d.addCard(new Card("onyx", new int[] { 0, 0, 2, 1, 0 }, 0));
        d.addCard(new Card("onyx", new int[] { 0, 0, 3, 0, 0 }, 0));
        d.addCard(new Card("onyx", new int[] { 0, 4, 0, 0, 0 }, 1));

        return d;
    }

    private Deck genMedium() {
        Deck d = new Deck(MEDIUM_SIZE, M_LEVEL);

        d.addCard(new Card("diamond", new int[] { 0, 0, 3, 2, 2 }, 1));
        d.addCard(new Card("diamond", new int[] { 2, 3, 0, 3, 0 }, 1));
        d.addCard(new Card("diamond", new int[] { 0, 0, 1, 4, 2 }, 2));
        d.addCard(new Card("diamond", new int[] { 0, 0, 0, 5, 3 }, 2));
        d.addCard(new Card("diamond", new int[] { 0, 0, 0, 5, 0 }, 2));
        d.addCard(new Card("diamond", new int[] { 6, 0, 0, 0, 0 }, 3));

        d.addCard(new Card("sapphire", new int[] { 0, 2, 2, 3, 0 }, 1));
        d.addCard(new Card("sapphire", new int[] { 0, 2, 3, 0, 3 }, 1));
        d.addCard(new Card("sapphire", new int[] { 2, 0, 0, 1, 4 }, 2));
        d.addCard(new Card("sapphire", new int[] { 5, 3, 0, 0, 0 }, 2));
        d.addCard(new Card("sapphire", new int[] { 0, 5, 0, 0, 0 }, 2));
        d.addCard(new Card("sapphire", new int[] { 0, 6, 0, 0, 0 }, 3));

        d.addCard(new Card("emerald", new int[] { 2, 3, 0, 0, 2 }, 1));
        d.addCard(new Card("emerald", new int[] { 3, 0, 2, 3, 0 }, 1));
        d.addCard(new Card("emerald", new int[] { 4, 2, 0, 0, 1 }, 2));
        d.addCard(new Card("emerald", new int[] { 0, 5, 3, 0, 0 }, 2));
        d.addCard(new Card("emerald", new int[] { 0, 5, 0, 0, 0 }, 2));
        d.addCard(new Card("emerald", new int[] { 0, 0, 6, 0, 0 }, 3));

        d.addCard(new Card("ruby", new int[] { 2, 0, 0, 2, 3 }, 1));
        d.addCard(new Card("ruby", new int[] { 0, 3, 0, 2, 3 }, 1));
        d.addCard(new Card("ruby", new int[] { 1, 4, 2, 0, 0 }, 2));
        d.addCard(new Card("ruby", new int[] { 3, 0, 0, 0, 5 }, 2));
        d.addCard(new Card("ruby", new int[] { 0, 0, 0, 0, 5 }, 2));
        d.addCard(new Card("ruby", new int[] { 0, 0, 0, 6, 0 }, 3));

        d.addCard(new Card("onyx", new int[] { 3, 2, 2, 0, 0 }, 1));
        d.addCard(new Card("onyx", new int[] { 3, 0, 3, 0, 2 }, 1));
        d.addCard(new Card("onyx", new int[] { 0, 1, 4, 2, 0 }, 2));
        d.addCard(new Card("onyx", new int[] { 0, 0, 5, 3, 0 }, 2));
        d.addCard(new Card("onyx", new int[] { 5, 0, 0, 0, 0 }, 2));
        d.addCard(new Card("onyx", new int[] { 0, 0, 0, 0, 6 }, 3));

        return d;
    }

    private Deck genHard() {
        Deck d = new Deck(HARD_SIZE, H_LEVEL);

        d.addCard(new Card("diamond", new int[] { 0, 3, 3, 5, 3 }, 3));
        d.addCard(new Card("diamond", new int[] { 3, 0, 0, 3, 6 }, 4));
        d.addCard(new Card("diamond", new int[] { 0, 0, 0, 0, 7 }, 4));
        d.addCard(new Card("diamond", new int[] { 3, 0, 0, 0, 7 }, 5));

        d.addCard(new Card("sapphire", new int[] { 3, 0, 3, 3, 5 }, 3));
        d.addCard(new Card("sapphire", new int[] { 6, 3, 0, 0, 3 }, 4));
        d.addCard(new Card("sapphire", new int[] { 7, 0, 0, 0, 0 }, 4));
        d.addCard(new Card("sapphire", new int[] { 7, 3, 0, 0, 0 }, 5));

        d.addCard(new Card("emerald", new int[] { 5, 3, 0, 3, 3 }, 3));
        d.addCard(new Card("emerald", new int[] { 3, 6, 3, 0, 0 }, 4));
        d.addCard(new Card("emerald", new int[] { 0, 7, 0, 0, 0 }, 4));
        d.addCard(new Card("emerald", new int[] { 0, 7, 3, 0, 0 }, 5));

        d.addCard(new Card("ruby", new int[] { 3, 5, 3, 0, 3 }, 3));
        d.addCard(new Card("ruby", new int[] { 0, 3, 6, 3, 0 }, 4));
        d.addCard(new Card("ruby", new int[] { 0, 0, 7, 0, 0 }, 4));
        d.addCard(new Card("ruby", new int[] { 0, 0, 7, 3, 0 }, 5));

        d.addCard(new Card("onyx", new int[] { 3, 3, 5, 3, 0 }, 3));
        d.addCard(new Card("onyx", new int[] { 0, 3, 0, 6, 3 }, 4));
        d.addCard(new Card("onyx", new int[] { 0, 0, 0, 7, 0 }, 4));
        d.addCard(new Card("onyx", new int[] { 0, 0, 0, 7, 3 }, 5));

        return d;
    }
}
