package View.GUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import Messages.*;
import Client.NetworkHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class is the controller for LoginScene.fxml and Home.fxml
 */
public class LoginController implements Initializable {


    Stage stage=new Stage();
    @FXML
    private ImageView usernameNext, nextAddress;
    private static final int SOCKET_PORT = 50000;
    private static NetworkHandler networkHandler;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }


    /**
     * This methos is called when the playButton in Home.fxml is clicked
     * @param event indicates the event when mouse clicked on the button
     */
    @FXML
    private void handleButton(MouseEvent event) throws IOException {
        Stage stageMain =MainScene.getStage();
        stageMain.close();
        Parent addressScene = FXMLLoader.load(getClass().getClassLoader().getResource("Fxml/AddressScene.fxml"));
        Scene scene1 = new Scene(addressScene);
        stage.setScene(scene1);
        stage.show();
    }

    @FXML
    public void handleAddress(MouseEvent event) throws IOException {
        stage.close();
        Parent usernameScene = FXMLLoader.load(getClass().getClassLoader().getResource("Fxml/UsernameScene.fxml"));
        Scene scene2 = new Scene(usernameScene);
        stage.setScene(scene2);
        stage.show();
    }

    @FXML
    public void handleUsername(MouseEvent event) throws IOException {
        stage.close();
        Parent playerScene = FXMLLoader.load(getClass().getClassLoader().getResource("Fxml/ChoosePlayerScene.fxml"));
        Scene scene3 = new Scene(playerScene);
        stage.setScene(scene3);
        stage.show();

    }

}





