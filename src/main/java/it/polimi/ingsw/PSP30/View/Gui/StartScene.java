package it.polimi.ingsw.PSP30.View.Gui;

import it.polimi.ingsw.PSP30.View.GUI;
import javafx.application.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.*;


import java.io.IOException;

/**
 * This class start the GUI application
 */
public class StartScene extends Application implements Runnable {


    public static void main() {
        launch();
    }

    /**
     * This method load Home.fxml file and shows it into a stage
     * @param stage indicates the stage on which the scene will be projected.
     * @throws IOException throws an exception for method load
     **/
    @Override
    public void start(Stage stage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("Fxml/Home.fxml"));
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