package View.GUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


public class Controller {
    Stage stage;
    Parent root;

    @FXML
    private ImageView playButton;
    @FXML
    private void handleButton(MouseEvent event) throws Exception {
        Stage stage;
        Parent root;
        stage = (Stage) playButton.getScene().getWindow();
        root = FXMLLoader.load(getClass().getClassLoader().getResource("Fxml/WelcomeScene.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
