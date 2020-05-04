package View.GUI;


import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;


public class Welcome extends Application {

    @FXML
    private ImageView playButton;
    @FXML
    private ImageView textField;
    /*@FXML
    public void initialize() {
        eventPlayGame();
    }*/



    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().
                getClassLoader().getResource("WelcomeScene.fxml"));
        AnchorPane root = (AnchorPane) loader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void eventPlayGame(){
        playButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e->startGame());
    }

    private void startGame() {
        System.out.println("-");
    }
}
