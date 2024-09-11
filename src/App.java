import java.io.IOException;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {

        VBox root = FXMLLoader.load(this.getClass().getResource("view/primary.fxml"));
        HBox container = (HBox) root.getChildren().get(0);
        container.setStyle("-fx-background-color: red;");
        VBox menu = (VBox) container.getChildren().get(1);
        menu.setStyle("-fx-background-color: blue;");
        VBox menu_inputs = (VBox) menu.getChildren().get(0);
        menu_inputs.setStyle("-fx-background-color: gray;");
        VBox source_menu_input = (VBox) menu_inputs.getChildren().get(0);
        source_menu_input.setStyle("-fx-background-color: yellow;");
        TextField source_input = (TextField) source_menu_input.getChildren().get(1);
        source_input.setStyle("-fx-background-color: green;");
        // TextField menu_inputs = (TextField) ((VBox) menu.getChildren().get(0)).getChildren().get(1);
        // input_source.setStyle("-fx-background-color: green;");
    
        Scene scene = new Scene(root, 800, 480);
        String css = this.getClass().getResource("application.css").toExternalForm();
        scene.getStylesheets().add(css);
        primaryStage.setTitle("Simple Backup");
        primaryStage.setScene(scene);
        primaryStage.show();

    }
    public static void main(String[] args) {
        launch(args);
    }
}
