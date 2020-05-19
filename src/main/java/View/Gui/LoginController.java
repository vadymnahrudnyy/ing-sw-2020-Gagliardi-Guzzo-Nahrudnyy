package View.Gui;

import View.GUI;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import Client.NetworkHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.security.Guard;
import java.util.ResourceBundle;

/**
 * This class is the controller for LoginScene.fxml and Home.fxml
 */
public class LoginController implements Initializable {


    @FXML
    private TextField addressField;
    private static final int SOCKET_PORT = 50000;
    private static NetworkHandler networkHandler;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }



    @FXML
    public void handleAddress(MouseEvent event) throws IOException {
        Parent usernameScene = FXMLLoader.load(getClass().getClassLoader().getResource("Fxml/UsernameScene.fxml"));
        Scene scene2 = new Scene(usernameScene);
        GUI.getStage().setScene(scene2);
        GUI.getStage().show();

        String address=addressField.getText();
        if(address!=null){
            System.out.println(address);
           // networkHandler.
        }
        if(address==null) showAlertWithoutHeaderText();


    }

    private void showAlertWithoutHeaderText() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Address Null");
        alert.setHeaderText(null);
        alert.setContentText("Insert an address first!");
        alert.showAndWait();
    }



    public void handlethreePlayer(MouseEvent event) throws IOException {
        GUI.getStage().close();
        Stage stage=new Stage();
        GUI.setGameStage(stage);
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("Fxml/ThreePlayersLobby.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    public void handletwoPlayer(MouseEvent event) throws IOException {
        GUI.getStage().close();
        Stage stage=new Stage();
        GUI.setGameStage(stage);
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("Fxml/TwoPlayersLobby.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
}





