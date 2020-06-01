package it.polimi.ingsw.PSP30.View.Gui;

import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;

public class AlertsController {


    public void showPlayerError() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Player Error");
        alert.setHeaderText(null);
        alert.setContentText("The player chosen is not valid");
        alert.showAndWait();
    }

    public void showWorkerChosenError() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Wrong Worker Chosen");
        alert.setHeaderText(null);
        alert.setContentText("You selected another player's worker!");
        alert.showAndWait();
    }

    public void showUserError() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Wrong Username");
        alert.setHeaderText(null);
        alert.setContentText("The username you chose is already used by another player! Choose a different username.");
        alert.showAndWait();
    }


}
