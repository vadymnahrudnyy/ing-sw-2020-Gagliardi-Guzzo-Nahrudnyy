package it.polimi.ingsw.PSP30.View.Gui;

import it.polimi.ingsw.PSP30.Client.Client;
import it.polimi.ingsw.PSP30.View.GUI;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import java.io.IOException;
import java.util.Objects;

/**
 * This class shows the end scene, one for the winner player and one for the other
 */
public class EndSceneController {
    private static final int GAME_FINISH_EXIT_STATUS = 101;
    private static final int OPPONENT_DISCONNECTED_EXIT_STATUS = 102;
    private static final int DISCONNECTED_FROM_SERVER_EXIT_STATUS = 103;

    public static void winner(String winner) throws IOException {
        StackPane pane;
        if(Client.getUsername().equals(winner))
            pane = FXMLLoader.load(Objects.requireNonNull(GodsController.class.getClassLoader().getResource("Fxml/WinnerPopUp.fxml")));
        else pane= FXMLLoader.load(Objects.requireNonNull(GodsController.class.getClassLoader().getResource("Fxml/LoserPopUp.fxml")));

        GUI.getGameStage().setScene(new Scene(pane));
    }

    public void endGame(MouseEvent event){
        event.consume();
        GUI.getGameStage().close();
        System.exit(GAME_FINISH_EXIT_STATUS);
    }

    public static void opponentDisconnection(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Opponent disconnected");
        alert.setHeaderText(null);
        if (Client.getNumPlayers() == 2) alert.setContentText("Your Opponent has left. The game is finished.");
        else alert.setContentText("One of your opponents has left. The game is finished.");
        alert.showAndWait();
        System.exit(OPPONENT_DISCONNECTED_EXIT_STATUS);
    }

    public static void disconnectedFromServer(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("");
        alert.setHeaderText(null);
        alert.setContentText("Disconnected from the server");
        alert.showAndWait();
        System.exit(DISCONNECTED_FROM_SERVER_EXIT_STATUS);
    }
}

