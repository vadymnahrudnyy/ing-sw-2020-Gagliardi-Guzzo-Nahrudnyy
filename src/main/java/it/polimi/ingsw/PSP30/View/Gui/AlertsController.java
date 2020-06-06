package it.polimi.ingsw.PSP30.View.Gui;

import javafx.scene.control.Alert;
public class AlertsController {


    /**
     * Alert showed when the address inserted isn't valid
     */
    public void showServerAddressError(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Server Address Error");
        alert.setHeaderText(null);
        alert.setContentText("The server address is not valid! Choose another server address.");
        alert.showAndWait();
    }

    /**
     * Alert showed when the username inserted isn't valid
     */
    public void showUsernameError() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Wrong Username");
        alert.setHeaderText(null);
        alert.setContentText("The username you chose is already used by another player! Choose a different username.");
        alert.showAndWait();
    }

    /**
     * Alert showed when the player isn't the owner of the worker he has selected
     */
    public void showWorkerSelectedError(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Wrong Worker");
        alert.setHeaderText(null);
        alert.setContentText("You selected another player's worker! Choose a different worker.");
        alert.showAndWait();
    }

    /**
     * Alert showed when the player has selected a worker who has no more moves available
     */
    public void showWorkerCannotMoveError(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Worker cannot be moved");
        alert.setHeaderText(null);
        alert.setContentText("The worker you selected does not have any possible position to move to. Move the other worker.");
        alert.showAndWait();
    }

    /**
     * Alert showed when both the worker of the player can't move anymore
     */
    public void showNoPossibleMovesError(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("No possible moves");
        alert.setHeaderText(null);
        alert.setContentText("Sorry! Your workers do not have any possible position to be moved to.");
        alert.showAndWait();
    }

    /**
     * Alert showed when the selected move isn't allowed
     */
    public void showInvalidMoveError(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Invalid Move");
        alert.setHeaderText(null);
        alert.setContentText("Attention: you cannot move there!");
        alert.showAndWait();
    }

}
