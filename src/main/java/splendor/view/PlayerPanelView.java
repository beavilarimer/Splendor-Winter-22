package splendor.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import splendor.model.Card;
import splendor.model.Player;
import splendor.model.Splendor;

public class PlayerPanelView extends VBox {

    private static final String[] COLOR_NAMES = {
        "diamond", "sapphire", "emerald", "ruby", "onyx"
    };
    private static final String[] GEM_SYMBOLS = { "◆", "●", "▲", "■", "★", "⬡" };

    public PlayerPanelView(Splendor game) {
        setSpacing(8);
        setPadding(new Insets(12));
        setPrefWidth(210);
        setStyle("-fx-background-color: #F5F5F5; -fx-border-color: #DDD; -fx-border-width: 0 0 0 1;");

        Player[] players = game.getPlayers();
        int      current = game.getCurrentPlayer();

        for (int i = 0; i < players.length; i++) {
            Player p    = players[i];
            boolean active = (i == current);
            getChildren().add(buildPlayerCard(p, active));
            if (i < players.length - 1) getChildren().add(new Separator());
        }
    }

    private VBox buildPlayerCard(Player player, boolean active) {
        VBox card = new VBox(5);
        card.setPadding(new Insets(6));
        if (active) {
            card.setStyle("-fx-background-color: #E3F2FD; -fx-border-color: #1E88E5; " +
                          "-fx-border-width: 1; -fx-border-radius: 6; -fx-background-radius: 6;");
        }

        // Name + prestige
        HBox header = new HBox(6);
        header.setAlignment(Pos.CENTER_LEFT);
        Label nameLabel = new Label((active ? "▶ " : "") + player.getName());
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: " + (active ? "14" : "12") + ";");
        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);
        Label prestigeLabel = new Label("★ " + player.getPrestige());
        prestigeLabel.setStyle("-fx-font-size: 13; -fx-text-fill: #D4A017;");
        header.getChildren().addAll(nameLabel, sp, prestigeLabel);
        card.getChildren().add(header);

        // Chips row
        HBox chips = new HBox(5);
        chips.setAlignment(Pos.CENTER_LEFT);
        int[] chipTracker = player.getChipTracker();
        for (int i = 0; i < 6; i++) {
            String hex = (i < 5) ? CardView.COLOR_HEX.get(COLOR_NAMES[i]) : "#FFD700";
            Circle dot = new Circle(7, Color.web(hex));
            dot.setStroke(Color.web("#888"));
            dot.setStrokeWidth(0.5);
            Label num = new Label(String.valueOf(chipTracker[i]));
            num.setStyle("-fx-font-size: 10;");
            VBox slot = new VBox(1, dot, num);
            slot.setAlignment(Pos.CENTER);
            chips.getChildren().add(slot);
        }
        Label chipsTitle = new Label("Chips:");
        chipsTitle.setStyle("-fx-font-size: 10; -fx-text-fill: #666;");
        card.getChildren().addAll(chipsTitle, chips);

        // Cards row
        HBox cards = new HBox(5);
        cards.setAlignment(Pos.CENTER_LEFT);
        int[] cardTracker = player.getCardTracker();
        for (int i = 0; i < 5; i++) {
            if (cardTracker[i] == 0) continue;
            String hex = CardView.COLOR_HEX.get(COLOR_NAMES[i]);
            Circle dot = new Circle(7, Color.web(hex));
            dot.setStroke(Color.web("#888"));
            dot.setStrokeWidth(0.5);
            Label num = new Label("×" + cardTracker[i]);
            num.setStyle("-fx-font-size: 10;");
            HBox pair = new HBox(2, dot, num);
            pair.setAlignment(Pos.CENTER_LEFT);
            cards.getChildren().add(pair);
        }
        if (!cards.getChildren().isEmpty()) {
            Label cardsTitle = new Label("Cards:");
            cardsTitle.setStyle("-fx-font-size: 10; -fx-text-fill: #666;");
            card.getChildren().addAll(cardsTitle, cards);
        }

        // Reserved card preview
        if (player.hasReserved()) {
            Label resTitle = new Label("Reserved:");
            resTitle.setStyle("-fx-font-size: 10; -fx-text-fill: #666;");
            card.getChildren().add(resTitle);
            Card reserved = player.getReservedCard();
            String hex = CardView.COLOR_HEX.getOrDefault(reserved.getColor(), "#CCC");
            Label resLabel = new Label(reserved.getColor() + (reserved.getPrestige() > 0
                ? " (" + reserved.getPrestige() + "★)" : ""));
            resLabel.setStyle("-fx-background-color: " + hex + "; -fx-padding: 2 6; " +
                              "-fx-background-radius: 4; -fx-font-size: 11;");
            card.getChildren().add(resLabel);
        }

        return card;
    }
}
