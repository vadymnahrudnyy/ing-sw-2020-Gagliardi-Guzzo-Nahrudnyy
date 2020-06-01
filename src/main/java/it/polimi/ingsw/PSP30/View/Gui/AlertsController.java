package it.polimi.ingsw.PSP30.View.Gui;

import javafx.scene.control.Alert;
public class AlertsController {


    public void showServerAddressError(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Server Address Error");
        alert.setHeaderText(null);
        alert.setContentText("The server address is not valid! Choose another server address.");
        alert.showAndWait();
    }

    public void showUsernameError() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Wrong Username");
        alert.setHeaderText(null);
        alert.setContentText("The username you chose is already used by another player! Choose a different username.");
        alert.showAndWait();
    }

    public void showWorkerSelectedError(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Wrong Worker");
        alert.setHeaderText(null);
        alert.setContentText("You selected another player's worker! Choose a different worker.");
        alert.showAndWait();
    }

    public void showWorkerCannotMoveError(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Worker cannot be moved");
        alert.setHeaderText(null);
        alert.setContentText("The worker you selected does not have any possible position to move to. Move the other worker.");
        alert.showAndWait();
    }

    public void showNoPossibleMovesError(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("No possible moves");
        alert.setHeaderText(null);
        alert.setContentText("Sorry! Your workers do not have any possible position to be moved to.");
        alert.showAndWait();
    }

    public void showInvalidMoveError(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Invalid Move");
        alert.setHeaderText(null);
        alert.setContentText("Attention: you cannot move there!");
        alert.showAndWait();
    }

}
