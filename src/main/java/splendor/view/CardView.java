package splendor.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.Map;

public class CardView extends VBox {

    public static final Map<String, String> COLOR_HEX = Map.of(
        "diamond",  "#E0E0E0",
        "sapphire", "#1E88E5",
        "emerald",  "#43A047",
        "ruby",     "#E53935",
        "onyx",     "#37474F"
    );

    private static final Map<String, String> GEM_SYMBOL = Map.of(
        "diamond",  "◆",
        "sapphire", "●",
        "emerald",  "▲",
        "ruby",     "■",
        "onyx",     "★"
    );

    private static final String[] COLOR_NAMES = {
        "diamond", "sapphire", "emerald", "ruby", "onyx"
    };

    private final String baseStyle;
    private final String hoverStyle;
    private final DropShadow glow = new DropShadow(8, Color.web("#1E88E5"));
    private boolean selected = false;

    public CardView(splendor.model.Card card, String reservedBy, boolean clickable, Runnable onClick) {
        setMinSize(108, 148);
        setMaxSize(108, 148);

        boolean isReserved = reservedBy != null;
        String borderColor = isReserved ? "#FF9800" : "#999";
        String bgColor     = isReserved ? "#FFF8E1" : "#FAFAFA";
        String cursor      = clickable  ? "hand"    : "default";
        String opacity     = !clickable ? " -fx-opacity: 0.75;" : "";
        baseStyle  = "-fx-border-color: " + borderColor + "; -fx-border-radius: 6; -fx-background-radius: 6; " +
                     "-fx-background-color: " + bgColor + "; -fx-cursor: " + cursor + ";" + opacity;
        String hoverBorder = isReserved ? "#FF9800" : "#1E88E5";
        hoverStyle = "-fx-border-color: " + hoverBorder + "; -fx-border-width: 2; -fx-border-radius: 6; " +
                     "-fx-background-radius: 6; -fx-background-color: " + bgColor + "; -fx-cursor: " + cursor + ";" + opacity;
        setStyle(baseStyle);

        if (clickable) {
            setOnMouseEntered(e -> { if (!selected) { setStyle(hoverStyle); setEffect(glow); } });
            setOnMouseExited(e ->  { if (!selected) { setStyle(baseStyle);  setEffect(null); } });
        }

        String hex    = COLOR_HEX.getOrDefault(card.getColor(), "#CCC");
        String symbol = GEM_SYMBOL.getOrDefault(card.getColor(), "?");
        boolean darkBg = card.getColor().equals("sapphire") || card.getColor().equals("onyx");

        // Header strip
        HBox header = new HBox();
        header.setPrefHeight(36);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(4, 8, 4, 8));
        header.setStyle("-fx-background-color: " + hex + "; -fx-background-radius: 6 6 0 0;");

        Label gemLabel = new Label(symbol);
        gemLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: " +
                          (darkBg ? "white" : "#333") + ";");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label prestigeLabel = new Label(card.getPrestige() > 0 ? String.valueOf(card.getPrestige()) : "");
        prestigeLabel.setStyle("-fx-font-size: 15; -fx-font-weight: bold; -fx-text-fill: " +
                               (darkBg ? "white" : "#333") + ";");

        header.getChildren().addAll(gemLabel, spacer, prestigeLabel);

        // Cost body
        VBox body = new VBox(3);
        body.setPadding(new Insets(6, 8, 6, 8));
        body.setAlignment(Pos.TOP_LEFT);

        int[] costs = card.getChipCost();
        for (int i = 0; i < 5; i++) {
            if (costs[i] == 0) continue;
            HBox row = new HBox(4);
            row.setAlignment(Pos.CENTER_LEFT);
            Circle dot = new Circle(5, Color.web(COLOR_HEX.get(COLOR_NAMES[i])));
            dot.setStroke(Color.web("#888"));
            dot.setStrokeWidth(0.5);
            Label num = new Label(String.valueOf(costs[i]));
            num.setStyle("-fx-font-size: 12;");
            row.getChildren().addAll(dot, num);
            body.getChildren().add(row);
        }

        getChildren().addAll(header, body);
        VBox.setVgrow(body, Priority.ALWAYS);

        if (reservedBy != null) {
            Label tag = new Label("🔒 " + reservedBy);
            tag.setMaxWidth(Double.MAX_VALUE);
            tag.setStyle("-fx-font-size: 9; -fx-text-fill: #E65100; -fx-background-color: #FFE0B2; " +
                         "-fx-padding: 2 4 2 4; -fx-background-radius: 0 0 6 6;");
            getChildren().add(tag);
        }

        if (onClick != null && clickable) {
            setOnMouseClicked(e -> onClick.run());
        }
    }

    public void select() {
        selected = true;
        setStyle(hoverStyle);
        setEffect(glow);
    }

    public void deselect() {
        selected = false;
        setStyle(baseStyle);
        setEffect(null);
    }

    // Gray placeholder for empty table slot
    public static StackPane emptySlot() {
        StackPane p = new StackPane();
        p.setMinSize(108, 148);
        p.setMaxSize(108, 148);
        p.setStyle("-fx-border-color: #CCC; -fx-border-radius: 6; -fx-background-radius: 6; " +
                   "-fx-background-color: #F0F0F0;");
        return p;
    }
}
