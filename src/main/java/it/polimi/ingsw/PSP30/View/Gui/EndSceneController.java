package it.polimi.ingsw.PSP30.View.Gui;

import it.polimi.ingsw.PSP30.Client.Client;
import it.polimi.ingsw.PSP30.View.GUI;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class EndSceneController {

    public static void winner(String winner)throws IOException {
        StackPane endPane=new StackPane();
        if(Client.getUsername().equals(winner)){
            endPane = FXMLLoader.load(GodsController.class.getClassLoader().getResource("Fxml/WinnerPopUp.fxml"));
            GUI.getGameStage().setScene(new Scene(endPane));
        }
        else {
            endPane= FXMLLoader.load(GodsController.class.getClassLoader().getResource("Fxml/LoserPopUp.fxml"));
            GUI.getGameStage().setScene(new Scene(endPane));
        }
    }
}
