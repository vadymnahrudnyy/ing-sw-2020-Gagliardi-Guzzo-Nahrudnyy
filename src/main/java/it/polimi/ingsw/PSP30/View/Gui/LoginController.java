package it.polimi.ingsw.PSP30.View.Gui;

import it.polimi.ingsw.PSP30.Client.Client;
import it.polimi.ingsw.PSP30.Messages.NumPlayersResponse;
import it.polimi.ingsw.PSP30.Messages.UsernameResponse;
import it.polimi.ingsw.PSP30.View.GUI;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class is the controller for LoginScene.fxml and Home.fxml
 */
public class LoginController {

    @FXML private static StackPane lobbyPane;
    @FXML public ImageView usernameNext;
    @FXML private TextField addressField,usernameField;
    private static final int SOCKET_PORT = 50000;

    /**
     * This method displays the lobby scene
     */
    public static void displayLobby() {
        GUI.setGameStage(new Stage());
        try{
            lobbyPane = FXMLLoader.load(BoardController.class.getClassLoader().getResource("Fxml/Lobby.fxml"));
        }catch (IOException e){
            e.printStackTrace();
        }
        Scene scene = new Scene(lobbyPane);
        GUI.getGameStage().setScene(scene);
        GUI.getGameStage().show();
    }


    @FXML
    public void handleAddress(MouseEvent event) throws IOException {
        String address=addressField.getText();
        address = address.trim();
        if(address.isEmpty()) showEmptyAddressAlert();
        else {
            Client.setServerAddress(address);
            Client.interruptClientThread();
        }
    }

    /**
     * Alert for empty address
     */
    private void showEmptyAddressAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Address Null");
        alert.setHeaderText(null);
        alert.setContentText("Insert an address first!");
        alert.showAndWait();
    }

    /**
     * This method manages the choice of three players
     * @param event mouse click on the image of the three players
     */
    public void handlethreePlayer(MouseEvent event) {
        Client.setNumPlayer(3);
        Client.sendMessageToServer(new NumPlayersResponse(3));
    }

    /**
     * This method manages the choice of two players
     * @param event mouse click on the image of the two players
     */
    public void handletwoPlayer(MouseEvent event) {
      Client.setNumPlayer(2);
        Client.sendMessageToServer(new NumPlayersResponse(2));
    }

    /**
     * his method manages the insertion of the username
     * @param event mouse click on next button
     */
    public void handleUsernameButton(MouseEvent event){
        String username = usernameField.getText();
        username = username.trim();
        if(username.isEmpty()) showEmptyAddressAlert();
        else {
            Client.setUsername(username);
            Client.sendMessageToServer(new UsernameResponse(username));
            usernameNext.setDisable(true);
        }
    }
}