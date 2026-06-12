package splendor.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class NobleTileView extends VBox {

    private static final String[] COLOR_NAMES = {
        "diamond", "sapphire", "emerald", "ruby", "onyx"
    };

    public NobleTileView(splendor.model.Noble noble) {
        setMinSize(80, 80);
        setMaxSize(80, 80);
        setAlignment(Pos.TOP_CENTER);
        setPadding(new Insets(4));
        setSpacing(2);

        boolean claimed = !noble.getInPlay();
        String borderColor = claimed ? "#CCC" : "#FFD700";
        String bgColor     = claimed ? "#EEE" : "#FFFDE7";
        setStyle("-fx-border-color: " + borderColor + "; -fx-border-width: 2; " +
                 "-fx-border-radius: 6; -fx-background-radius: 6; " +
                 "-fx-background-color: " + bgColor + ";");

        Label prestigeLabel = new Label("♛ 3");
        prestigeLabel.setStyle("-fx-font-size: 13; -fx-font-weight: bold; " +
                               "-fx-text-fill: " + (claimed ? "#AAA" : "#7B5900") + ";");
        getChildren().add(prestigeLabel);

        int[] cost = noble.getCardCost();
        HBox dots = new HBox(3);
        dots.setAlignment(Pos.CENTER);
        for (int i = 0; i < 5; i++) {
            if (cost[i] == 0) continue;
            VBox dotGroup = new VBox(1);
            dotGroup.setAlignment(Pos.CENTER);
            Circle dot = new Circle(5, Color.web(CardView.COLOR_HEX.get(COLOR_NAMES[i])));
            if (claimed) dot.setOpacity(0.3);
            Label num = new Label(String.valueOf(cost[i]));
            num.setStyle("-fx-font-size: 9; -fx-text-fill: " + (claimed ? "#AAA" : "#333") + ";");
            dotGroup.getChildren().addAll(dot, num);
            dots.getChildren().add(dotGroup);
        }
        getChildren().add(dots);
    }
}
