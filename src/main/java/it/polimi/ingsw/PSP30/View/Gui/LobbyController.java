package it.polimi.ingsw.PSP30.View.Gui;


import it.polimi.ingsw.PSP30.Client.Client;
import it.polimi.ingsw.PSP30.Messages.LobbyStatusNotification;
import it.polimi.ingsw.PSP30.Messages.NumPlayersResponse;
import it.polimi.ingsw.PSP30.Messages.UsernameResponse;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;


public class LobbyController {

    @FXML private StackPane lobbyPane, playerStackPane, firstOpponentPane, secondOpponentPane;
    @FXML private Label playerUsernameLabel, firstOpponentLabel, secondOpponentLabel;
    @FXML private BorderPane borderPane;
    @FXML private VBox opponentsVBox;
    @FXML private ImageView exitButton;



    public void showLobby(Stage stage, LobbyStatusNotification message) throws Exception {
        String firstOpponentUsername = null, secondOpponentUsername=null;
        ArrayList<String> players= message.getPlayersInLobby();
        lobbyPane= FXMLLoader.load(GodsController.class.getClassLoader().getResource("Fxml/Lobby.fxml"));
        borderPane=(BorderPane) lobbyPane.getChildren().get(1);
        exitButton=(ImageView) borderPane.getBottom();
        playerStackPane=(StackPane) borderPane.getLeft();
        playerUsernameLabel=(Label) playerStackPane.getChildren().get(1);
        opponentsVBox=(VBox) borderPane.getRight();
        firstOpponentPane=(StackPane) opponentsVBox.getChildren().get(0);
        secondOpponentPane=(StackPane) opponentsVBox.getChildren().get(1);
        firstOpponentLabel=(Label) firstOpponentPane.getChildren().get(2);
        secondOpponentLabel=(Label) secondOpponentPane.getChildren().get(2);
        if(Client.getNumPlayers()==2) opponentsVBox.getChildren().remove(1);
        playerUsernameLabel.setText(Client.getUsername());
       for(String username :players ){
           if(!Client.getUsername().equals(username)) {
               if(firstOpponentUsername==null) {
                   firstOpponentLabel.setText(firstOpponentUsername=username);
               }
               else secondOpponentLabel.setText(secondOpponentUsername=username);
           }
       }
    }
}
