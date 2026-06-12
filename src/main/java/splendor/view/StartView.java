package splendor.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import splendor.controller.GameController;
import splendor.model.Splendor;

import java.util.ArrayList;
import java.util.List;

public class StartView extends VBox {

    public StartView(Stage stage) {
        setAlignment(Pos.CENTER);
        setSpacing(18);
        setPadding(new Insets(40));
        setStyle("-fx-background-color: #263238;");

        Label title = new Label("Splendor");
        title.setFont(Font.font("Georgia", 48));
        title.setTextFill(Color.web("#FFD700"));

        Label subtitle = new Label("A gem-trading board game");
        subtitle.setStyle("-fx-font-size: 14; -fx-text-fill: #90A4AE;");

        // Player count
        Label countLabel = new Label("Number of players:");
        countLabel.setStyle("-fx-text-fill: #CFD8DC; -fx-font-size: 13;");
        Spinner<Integer> countSpinner = new Spinner<>(2, 4, 2);
        countSpinner.setPrefWidth(80);
        HBox countRow = new HBox(10, countLabel, countSpinner);
        countRow.setAlignment(Pos.CENTER);

        // Name fields (dynamic)
        VBox nameFields = new VBox(8);
        nameFields.setAlignment(Pos.CENTER);
        List<TextField> nameInputs = new ArrayList<>();
        rebuildNameFields(nameFields, nameInputs, 2);

        countSpinner.valueProperty().addListener((obs, old, n) ->
            rebuildNameFields(nameFields, nameInputs, n));

        // Start button
        Button startBtn = new Button("Start Game");
        startBtn.setStyle("-fx-background-color: #FFD700; -fx-text-fill: #263238; " +
                          "-fx-font-weight: bold; -fx-font-size: 14; " +
                          "-fx-padding: 8 28; -fx-background-radius: 20;");
        startBtn.setOnAction(e -> {
            String[] names = nameInputs.stream()
                .map(f -> f.getText().isBlank() ? f.getPromptText() : f.getText().trim())
                .toArray(String[]::new);

            Splendor game       = new Splendor(names);
            GameController ctrl = new GameController(game);
            BoardView board     = new BoardView(ctrl);

            stage.setScene(new Scene(board, 1050, 720));
        });

        getChildren().addAll(title, subtitle, countRow, nameFields, startBtn);
    }

    private void rebuildNameFields(VBox container, List<TextField> inputs, int count) {
        container.getChildren().clear();
        inputs.clear();
        for (int i = 0; i < count; i++) {
            TextField tf = new TextField();
            tf.setPromptText("Player " + (i + 1));
            tf.setMaxWidth(220);
            tf.setStyle("-fx-background-radius: 4;");
            inputs.add(tf);
            container.getChildren().add(tf);
        }
    }
}
