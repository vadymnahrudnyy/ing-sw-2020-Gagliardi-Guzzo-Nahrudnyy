package View.GUI;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class Controller implements Initializable {
    Stage stage;
    Parent root;

    @FXML
    private AnchorPane mainPane;
    @FXML
    private ImageView playButton;

    @FXML
    private void handlePlayButton(MouseEvent event) throws Exception {

        FxmlHandler object=new FxmlHandler();
        Pane view=object.getHomePage("LoginScene");
        mainPane.getChildren().setAll(view);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TranslateTransition transition=new TranslateTransition(Duration.seconds(5), playButton);
        transition.setByZ(100);
        transition.setAutoReverse(true);
        transition.setCycleCount(TranslateTransition.INDEFINITE);
        transition.play();
    }
}
