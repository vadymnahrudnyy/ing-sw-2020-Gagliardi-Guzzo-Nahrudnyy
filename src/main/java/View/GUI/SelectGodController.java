package View.GUI;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.w3c.dom.events.MouseEvent;

public class SelectGodController extends Application {

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage stage) throws Exception {
        Parent root= FXMLLoader.load(getClass().getClassLoader().getResource("Fxml/Apollo.fxml"));
        Scene scene= new Scene(root);
        stage.setFullScreen(true);
        stage.setScene(scene);
        stage.show();
    }

}
