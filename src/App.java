import javafx.application.Application;

import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox();
        // Button btn = new Button();
        // btn.setId("btn");
        HBox hb = new HBox();
        hb.setMinSize(200, 300);
        hb.setId("hboxis");
        root.getChildren().add(hb);

        Scene scene = new Scene(root, 800, 480);
        String css = this.getClass().getResource("application.css").toExternalForm();
        scene.getStylesheets().add(css);
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();

    }
    public static void main(String[] args) {
        launch(args);
    }
}
