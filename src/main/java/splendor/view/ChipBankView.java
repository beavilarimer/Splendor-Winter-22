package splendor.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import splendor.controller.GameController;

import java.util.List;

public class ChipBankView extends HBox {

    private static final String[] COLOR_NAMES = {
        "diamond", "sapphire", "emerald", "ruby", "onyx", "gold"
    };
    private static final String GOLD_HEX = "#FFD700";

    public ChipBankView(GameController controller, int[] chipBank) {
        setSpacing(10);
        setPadding(new Insets(8));
        setAlignment(Pos.CENTER_LEFT);

        List<Integer> selected = controller.getSelectedChips();

        for (int i = 0; i < 6; i++) {
            String hex = (i < 5)
                ? CardView.COLOR_HEX.get(COLOR_NAMES[i])
                : GOLD_HEX;

            boolean isSelected = selected.contains(i);
            boolean isEmpty    = chipBank[i] == 0;

            Circle chip = new Circle(18, Color.web(hex));
            chip.setStroke(isSelected ? Color.WHITE : Color.web("#888"));
            chip.setStrokeWidth(isSelected ? 3 : 1);
            chip.setStrokeType(StrokeType.OUTSIDE);
            if (isEmpty) chip.setOpacity(0.3);

            Label count = new Label(String.valueOf(chipBank[i]));
            count.setStyle("-fx-font-size: 11; -fx-text-fill: #555;");

            VBox slot = new VBox(3, chip, count);
            slot.setAlignment(Pos.CENTER);

            // Gold chips (index 5) are not selectable by the player
            if (i < 5 && !isEmpty) {
                int colorIdx = i;
                slot.setOnMouseClicked(e -> controller.toggleChipSelection(colorIdx));
                slot.setStyle("-fx-cursor: hand;");
            }

            getChildren().add(slot);
        }
    }
}
