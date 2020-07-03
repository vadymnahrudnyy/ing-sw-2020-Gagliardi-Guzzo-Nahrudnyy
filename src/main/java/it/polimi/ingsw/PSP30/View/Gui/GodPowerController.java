package it.polimi.ingsw.PSP30.View.Gui;

import it.polimi.ingsw.PSP30.Client.Client;
import it.polimi.ingsw.PSP30.Messages.UsePowerResponse;
import it.polimi.ingsw.PSP30.View.GUI;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * Creates a stage in order to ask to the player if he wants use the god's power.
 */
public class GodPowerController {

    @FXML StackPane stackPane;
    @FXML ImageView yesButton, noButton;
    private static Stage powerStage;

    /**
     * Loads the scene for the selection of the god.
     */
    public void showScene(){
        powerStage = new Stage();
        powerStage.initModality(Modality.APPLICATION_MODAL);
        powerStage.initOwner(GUI.getGameStage());
        try {
            stackPane = FXMLLoader.load(Objects.requireNonNull(LoginController.class.getClassLoader().getResource("Fxml/PowerUsage.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(stackPane);
        powerStage.setScene(scene);
        powerStage.show();

    }

    /**
     * Selects the god when the god card is clicked.
     * @param event click occurs on the god card.
     */
    public void usePower(MouseEvent event){
        event.consume();
        Client.sendMessageToServer(new UsePowerResponse(true));
        powerStage.close();
    }

    /**
     * Deselects an already selected god when the god card is clicked on again.
     * @param event click occurs on the god card.
     */
    public void noPower(MouseEvent event){
        event.consume();
        Client.sendMessageToServer(new UsePowerResponse(false));
        powerStage.close();
    }

}
