package it.polimi.ingsw.PSP30.View.Gui;

import it.polimi.ingsw.PSP30.Client.Client;
import it.polimi.ingsw.PSP30.View.GUI;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import java.io.IOException;

public class EndSceneController {
    public static void winner(String winner)throws IOException {
        if(Client.getUsername().equals(winner)){
            StackPane pane = FXMLLoader.load(GodsController.class.getClassLoader().getResource("Fxml/WinnerPopUp.fxml"));
            GUI.getGameStage().setScene(new Scene(pane));
        }
        else {
            StackPane pane= FXMLLoader.load(GodsController.class.getClassLoader().getResource("Fxml/LoserPopUp.fxml"));
            GUI.getGameStage().setScene(new Scene(pane));
        }
    }

    public void endGame(MouseEvent event){
        GUI.getGameStage().close();
        //Client.sendMessageToServer(new Disconnection());
    }
}

