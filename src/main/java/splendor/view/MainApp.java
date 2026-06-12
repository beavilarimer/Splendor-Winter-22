package splendor.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {
        StartView startView = new StartView(stage);
        stage.setScene(new Scene(startView, 560, 480));
        stage.setTitle("Splendor");
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
