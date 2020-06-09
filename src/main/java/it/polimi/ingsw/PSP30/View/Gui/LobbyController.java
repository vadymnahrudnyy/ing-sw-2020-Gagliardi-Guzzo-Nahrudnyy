package it.polimi.ingsw.PSP30.View.Gui;

import it.polimi.ingsw.PSP30.Messages.Disconnection;
import it.polimi.ingsw.PSP30.View.GUI;
import it.polimi.ingsw.PSP30.Client.Client;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;


public class LobbyController {

    @FXML private StackPane lobbyPane, playerStackPane, firstOpponentPane, secondOpponentPane;
    @FXML private Label playerUsernameLabel, firstOpponentLabel, secondOpponentLabel;
    @FXML private BorderPane borderPane;
    @FXML private VBox opponentsVBox;
    @FXML private ImageView exitButton;

    private static final int APP_CLOSED_BY_LOBBY_EXIT_BUTTON = 3006;


    /**
     * This method manages the Lobby scene
     * @param players names of all the players
     * @throws IOException when an error occurred in loading fxml file
     */
    public void showLobby(ArrayList<String> players) throws IOException {
        String firstOpponentUsername = null, secondOpponentUsername;
        lobbyPane= FXMLLoader.load(GodsController.class.getClassLoader().getResource("Fxml/Lobby.fxml"));
        borderPane=(BorderPane) lobbyPane.getChildren().get(1);
        exitButton=(ImageView) borderPane.getBottom();
        playerStackPane=(StackPane) borderPane.getLeft();
        playerUsernameLabel=(Label) playerStackPane.getChildren().get(2);
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
               else secondOpponentLabel.setText(secondOpponentUsername = username);
           }
       }
       GUI.getGameStage().setScene(new Scene(lobbyPane));
       GUI.getGameStage().show();
    }

    public void lobbyExitButton(MouseEvent event){
        Client.sendMessageToServer(new Disconnection());
        Platform.exit();
        System.exit(APP_CLOSED_BY_LOBBY_EXIT_BUTTON);
    }
}
