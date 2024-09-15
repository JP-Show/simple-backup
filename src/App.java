import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {

        VBox root = FXMLLoader.load(this.getClass().getResource("view/primary.fxml"));
    
        Scene scene = new Scene(root, 800, 480);
        String css = this.getClass().getResource("application.css").toExternalForm();
        scene.getStylesheets().add(css);
        primaryStage.setTitle("Simple Backup");
        primaryStage.setScene(scene);
        primaryStage.show();
        

    }

    @Override
    public void stop() throws Exception {
        super.stop();    
        System.exit(0);
    }
    public static void main(String[] args) {
        launch(args);
    }
}
