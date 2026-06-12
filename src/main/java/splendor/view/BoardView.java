package splendor.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Popup;
import splendor.controller.GameController;
import splendor.model.Card;
import splendor.model.Noble;
import splendor.model.Player;
import splendor.model.Splendor;

public class BoardView extends BorderPane {

    private final GameController controller;
    private Button takeChipsBtn;

    public BoardView(GameController controller) {
        this.controller = controller;
        controller.setOnRefresh(this::refresh);
        controller.setOnGameOver(this::showGameOver);
        refresh();
    }

    public void refresh() {
        Splendor game   = controller.getGame();
        Player  current = controller.currentPlayer();

        setTop(buildTop(game));
        setCenter(buildCenter(game));
        setRight(new PlayerPanelView(game));
        setBottom(buildBottom(game, current));
    }

    // -------------------------------------------------------------------------
    // Top — nobles + turn label
    // -------------------------------------------------------------------------

    private HBox buildTop(Splendor game) {
        HBox bar = new HBox(10);
        bar.setPadding(new Insets(10));
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setStyle("-fx-background-color: #ECEFF1;");

        for (Noble n : game.getNobles())
            bar.getChildren().add(new NobleTileView(n));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label turnLabel = new Label(controller.currentPlayer().getName() + "'s turn");
        turnLabel.setStyle("-fx-font-size: 15; -fx-font-weight: bold; -fx-text-fill: #1E88E5;");
        bar.getChildren().addAll(spacer, turnLabel);

        return bar;
    }

    // -------------------------------------------------------------------------
    // Center — 3 rows of cards (level 3 on top, level 1 on bottom)
    // -------------------------------------------------------------------------

    private VBox buildCenter(Splendor game) {
        VBox grid = new VBox(10);
        grid.setPadding(new Insets(10));
        Card[][] table = game.getTable();

        for (int lvl = Splendor.LEVELS - 1; lvl >= 0; lvl--) {
            HBox row = new HBox(8);
            row.setAlignment(Pos.CENTER_LEFT);

            // Deck button
            int remaining = game.getDeckSize(lvl);
            Button deckBtn = new Button("L" + (lvl + 1) + "\n" + remaining);
            deckBtn.setMinSize(60, 148);
            deckBtn.setStyle("-fx-background-color: #CFD8DC; -fx-border-radius: 6; " +
                             "-fx-background-radius: 6; -fx-font-size: 11; -fx-font-weight: bold;");
            row.getChildren().add(deckBtn);

            // 4 card slots
            final int level = lvl;
            for (int slot = 0; slot < Splendor.OPEN_CARDS; slot++) {
                Card card = table[lvl][slot];
                final int s = slot;
                if (card != null) {
                    splendor.model.Player reservingPlayer = card.getReservedBy();
                    String reservedByName = reservingPlayer != null ? reservingPlayer.getName() : null;
                    boolean clickable = reservingPlayer == null || reservingPlayer == controller.currentPlayer();
                    CardView cv = new CardView(card, reservedByName, clickable, () -> showCardPopup(card, level, s));
                    row.getChildren().add(cv);
                } else {
                    row.getChildren().add(CardView.emptySlot());
                }
            }

            grid.getChildren().add(row);
        }

        return grid;
    }

    // -------------------------------------------------------------------------
    // Bottom — chip bank + action buttons
    // -------------------------------------------------------------------------

    private HBox buildBottom(Splendor game, Player current) {
        HBox bar = new HBox(16);
        bar.setPadding(new Insets(10));
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setStyle("-fx-background-color: #ECEFF1; -fx-border-color: #DDD; -fx-border-width: 1 0 0 0;");

        bar.getChildren().add(new ChipBankView(controller, game.getChipBank()));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        int sel = controller.getSelectedChips().size();
        takeChipsBtn = new Button("Take Chips" + (sel > 0 ? " (" + sel + ")" : ""));
        takeChipsBtn.setDisable(sel == 0);
        takeChipsBtn.setOnAction(e -> {
            if (!controller.confirmTakeChips()) {
                showError("Invalid chip selection.\nTake 3 different colors, or 2 of the same (needs 4+ in bank).");
            }
        });

        if (current.hasReserved()) {
            Card reserved = current.getReservedCard();
            Button buyResBtn = new Button("Buy Reserved");
            buyResBtn.setDisable(!controller.canAfford(reserved));
            buyResBtn.setOnAction(e -> controller.buyCard(reserved, -1, -1));
            bar.getChildren().add(buyResBtn);
        }

        bar.getChildren().addAll(spacer, takeChipsBtn);
        return bar;
    }

    // -------------------------------------------------------------------------
    // Card action popup
    // -------------------------------------------------------------------------

    private void showCardPopup(Card card, int level, int slot) {
        Popup popup = new Popup();
        VBox box = new VBox(8);
        box.setPadding(new Insets(10));
        box.setStyle("-fx-background-color: white; -fx-border-color: #CCC; -fx-border-radius: 6; " +
                     "-fx-background-radius: 6; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 8, 0, 0, 2);");

        Label title = new Label(card.getColor() + (card.getPrestige() > 0 ? " — " + card.getPrestige() + " ★" : ""));
        title.setStyle("-fx-font-weight: bold;");

        Player current    = controller.currentPlayer();
        Player reservedBy = card.getReservedBy();

        if (reservedBy != null && reservedBy != current) {
            Label info = new Label("Reserved by " + reservedBy.getName());
            info.setStyle("-fx-text-fill: #E65100;");
            box.getChildren().addAll(title, info);
        } else if (reservedBy == current) {
            Button buyResBtn = new Button("Buy (your reserve)");
            buyResBtn.setDisable(!controller.canAfford(card));
            buyResBtn.setOnAction(e -> { popup.hide(); controller.buyCard(card, -1, -1); });
            box.getChildren().addAll(title, buyResBtn);
        } else {
            Button buyBtn = new Button("Buy");
            buyBtn.setDisable(!controller.canAfford(card));
            buyBtn.setOnAction(e -> { popup.hide(); controller.buyCard(card, level, slot); });

            Button reserveBtn = new Button("Reserve");
            reserveBtn.setDisable(current.hasReserved());
            reserveBtn.setOnAction(e -> { popup.hide(); controller.reserveCard(level, slot); });

            box.getChildren().addAll(title, buyBtn, reserveBtn);
        }

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setOnAction(e -> popup.hide());
        box.getChildren().add(cancelBtn);

        popup.getContent().add(box);
        popup.setAutoHide(true);
        popup.show(getScene().getWindow());
    }

    // -------------------------------------------------------------------------
    // Game over
    // -------------------------------------------------------------------------

    private void showGameOver() {
        splendor.model.Player winner = controller.getGame().resolveWinner();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText("🏆 " + winner.getName() + " wins!");
        alert.setContentText("Final prestige: " + winner.getPrestige());
        alert.showAndWait();
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Invalid Move");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
