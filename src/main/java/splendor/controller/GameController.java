package splendor.controller;

import splendor.model.Card;
import splendor.model.Player;
import splendor.model.Splendor;

import java.util.ArrayList;
import java.util.List;

public class GameController {

    private final Splendor game;
    private final List<Integer> selectedChips = new ArrayList<>();
    private Runnable onRefresh;
    private Runnable onGameOver;

    public GameController(Splendor game) {
        this.game = game;
    }

    public void setOnRefresh(Runnable r)  { this.onRefresh  = r; }
    public void setOnGameOver(Runnable r) { this.onGameOver = r; }

    // -------------------------------------------------------------------------
    // Chip selection (accumulates across clicks before confirmation)
    // -------------------------------------------------------------------------

    public void toggleChipSelection(int colorIdx) {
        if (selectedChips.contains(colorIdx)) {
            selectedChips.remove((Integer) colorIdx);
        } else {
            if (selectedChips.size() < 3) selectedChips.add(colorIdx);
        }
        if (onRefresh != null) onRefresh.run();
    }

    public List<Integer> getSelectedChips() {
        return selectedChips;
    }

    public boolean confirmTakeChips() {
        Player current = currentPlayer();
        boolean ok = false;

        if (selectedChips.size() == 3) {
            int[] colors = selectedChips.stream().mapToInt(i -> i).toArray();
            ok = game.takeThreeChips(current, colors);
        } else if (selectedChips.size() == 1) {
            ok = game.takeTwoChips(current, selectedChips.get(0));
        } else if (selectedChips.size() == 2 && selectedChips.get(0).equals(selectedChips.get(1))) {
            ok = game.takeTwoChips(current, selectedChips.get(0));
        }

        if (ok) {
            selectedChips.clear();
            if (onRefresh != null) onRefresh.run();
        }
        return ok;
    }

    // -------------------------------------------------------------------------
    // Card actions
    // -------------------------------------------------------------------------

    public boolean canAfford(Card card) {
        return game.canAfford(currentPlayer(), card);
    }

    public boolean buyCard(Card card, int level, int slot) {
        boolean ok = game.buyCard(currentPlayer(), card, level, slot);
        if (ok && onRefresh != null) onRefresh.run();
        return ok;
    }

    public boolean reserveCard(int level, int slot) {
        boolean ok = game.reserveCard(currentPlayer(), level, slot);
        if (ok && onRefresh != null) onRefresh.run();
        return ok;
    }

    // -------------------------------------------------------------------------
    // Turn flow
    // -------------------------------------------------------------------------

    public boolean endTurn() {
        selectedChips.clear();
        boolean gameOver = game.advanceTurn();
        if (gameOver && onGameOver != null) {
            onGameOver.run();
        } else if (onRefresh != null) {
            onRefresh.run();
        }
        return gameOver;
    }

    // -------------------------------------------------------------------------
    // Convenience accessors for the view
    // -------------------------------------------------------------------------

    public Splendor getGame()         { return game; }
    public Player   currentPlayer()   { return game.getPlayers()[game.getCurrentPlayer()]; }
}
