package it.polimi.ingsw.PSP30.View.Gui;

import it.polimi.ingsw.PSP30.Client.Client;
import it.polimi.ingsw.PSP30.Messages.NumPlayersResponse;
import it.polimi.ingsw.PSP30.Messages.UsernameResponse;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;


/**
 * Controller for LoginScene.fxml and Home.fxml.
 */
public class LoginController {

    @FXML public ImageView usernameNext;
    @FXML public ImageView nextAddress;
    @FXML private TextField addressField,usernameField;

    /**
     * Saves and checks the address in input by the player.
     * @param event mouse clicked of the Next button.
     */
    public void handleAddress(MouseEvent event) {
        event.consume();
        String address=addressField.getText();
        address = address.trim();
        if(address.isEmpty()) showEmptyAddressAlert();
        else {
            nextAddress.setDisable(true);
            Client.setServerAddress(address);
            Client.interruptClientThread();
        }
    }

    /**
     * Alert for empty address.
     */
    private void showEmptyAddressAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Address Null");
        alert.setHeaderText(null);
        alert.setContentText("Insert an address first!");
        alert.showAndWait();
    }

    /**
     * Manages the choice of three players.
     * @param event mouse click on the image of the three players.
     */
    public void handleThreePlayerButton(MouseEvent event) {
        event.consume();
        Client.setNumPlayer(3);
        Client.sendMessageToServer(new NumPlayersResponse(3));
    }

    /**
     * Manages the choice of two players.
     * @param event mouse click on the image of the two players.
     */
    public void handleTwoPlayerButton(MouseEvent event) {
        event.consume();
        Client.setNumPlayer(2);
        Client.sendMessageToServer(new NumPlayersResponse(2));
    }

    /**
     * Manages the insertion of the username.
     * @param event mouse click on next button.
     */
    public void handleUsernameButton(MouseEvent event){
        event.consume();
        String username = usernameField.getText().trim().toUpperCase();
        if(username.isEmpty()) showEmptyAddressAlert();
        else {
            Client.setUsername(username);
            Client.sendMessageToServer(new UsernameResponse(username));
            usernameNext.setDisable(true);
        }
    }
}