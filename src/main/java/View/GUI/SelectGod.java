package View.GUI;


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;

public class SelectGod extends Application{
    private Welcome start;

    @FXML
    private ImageView backButton;
    @FXML
    private ImageView selectButton;
    @FXML
    private ImageView previousButton;

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(new Pane()));
        start.setLayout(stage.getScene(), "Fxml/Apollo.fxml");
        stage.setResizable(false);
        stage.show();
    }
}