package it.polimi.ingsw.PSP30.View.Gui;

import it.polimi.ingsw.PSP30.View.GUI;
import javafx.application.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.stage.*;


import java.io.IOException;
import java.util.Objects;

/**
 * Starts the GUI application.
 */
public class StartScene extends Application implements Runnable {

    /**
     * Launches the application.
     */
    public static void main() {
        launch();
    }

    /**
     * Loads Home.fxml file and shows it into a stage.
     * @param stage indicates the stage on which the scene will be projected.
     * @throws IOException throws an exception for method load.
     **/
    @Override
    public void start(Stage stage) throws Exception {

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("Fxml/Home.fxml")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        GUI.setPrimaryStage(stage);
        stage.setOnCloseRequest(GUI::closeApp);
        stage.show();
    }

    @Override
    public void run() {}
}