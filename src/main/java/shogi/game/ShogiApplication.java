package shogi.game;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ShogiApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/shogi.fxml"));
        stage.setTitle("Shogi");
        stage.setResizable(false);
        stage.setScene(new Scene(root));
        stage.show();
    }
}
