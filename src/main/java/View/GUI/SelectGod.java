package View.GUI;


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SelectGod implements Initializable {

    @FXML StackPane rootPane;
    Stage stage=new Stage();

    @FXML
    private void FirstGod(MouseEvent event) throws IOException {
        Parent nextroot= FXMLLoader.load(getClass().getClassLoader().getResource("Fxml/Apollo.fxml"));
        Scene nextscene= new Scene(nextroot);
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(nextscene);
        stage.setFullScreen(true);
        stage.show();
    }

    @FXML
    public void Artemis(MouseEvent event) throws IOException {
        FxmlHandler object=new FxmlHandler();
        Pane view=object.getHomePage("Artemis");
        rootPane.getChildren().setAll(view);


    }

    @FXML
    private void Apollo(MouseEvent event) throws IOException {
        FxmlHandler object=new FxmlHandler();
        Pane view=object.getHomePage("Apollo");
        rootPane.getChildren().setAll(view);

    }

    @FXML
    private void Ares(MouseEvent event) throws IOException {
        FxmlHandler object=new FxmlHandler();
        Pane view=object.getHomePage("Ares");
        rootPane.getChildren().setAll(view);

    }

    @FXML
    private void Athena(MouseEvent event) throws IOException {
        FxmlHandler object=new FxmlHandler();
        Pane view=object.getHomePage("Athena");
        rootPane.getChildren().setAll(view);

    }


    @FXML
    private void Atlas(MouseEvent event) throws IOException {
        FxmlHandler object=new FxmlHandler();
        Pane view=object.getHomePage("Atlas");
        rootPane.getChildren().setAll(view);

    }
    @FXML
    private void Chronus(MouseEvent event) throws IOException {
        FxmlHandler object=new FxmlHandler();
        Pane view=object.getHomePage("Chronus");
        rootPane.getChildren().setAll(view);

    }

    @FXML
    private void Demeter(MouseEvent event) throws IOException {
        FxmlHandler object=new FxmlHandler();
        Pane view=object.getHomePage("Demeter");
        rootPane.getChildren().setAll(view);

    }
    @FXML
    private void Hephaestus(MouseEvent event) throws IOException {
        FxmlHandler object=new FxmlHandler();
        Pane view=object.getHomePage("Hephaestus");
        rootPane.getChildren().setAll(view);

    }
    @FXML
    private void Hermes(MouseEvent event) throws IOException {
        FxmlHandler object=new FxmlHandler();
        Pane view=object.getHomePage("Hermes");
        rootPane.getChildren().setAll(view);

    }

    @FXML
    private void Hestia(MouseEvent event) throws IOException {
        FxmlHandler object=new FxmlHandler();
        Pane view=object.getHomePage("Hestia");
        rootPane.getChildren().setAll(view);

    }
    @FXML
    private void Minotaur(MouseEvent event) throws IOException {
        FxmlHandler object=new FxmlHandler();
        Pane view=object.getHomePage("Minotaur");
        rootPane.getChildren().setAll(view);

    }
    @FXML
    private void Pan(MouseEvent event) throws IOException {
        FxmlHandler object=new FxmlHandler();
        Pane view=object.getHomePage("Pan");
        rootPane.getChildren().setAll(view);

    }
    @FXML
    private void Hera(MouseEvent event) throws IOException {
        FxmlHandler object=new FxmlHandler();
        Pane view=object.getHomePage("Hera");
        rootPane.getChildren().setAll(view);

    }
    @FXML
    private void Prometheus(MouseEvent event) throws IOException {
        FxmlHandler object=new FxmlHandler();
        Pane view=object.getHomePage("Prometheus");
        rootPane.getChildren().setAll(view);

    }
    @FXML
    private void Zeus(MouseEvent event) throws IOException {
        FxmlHandler object=new FxmlHandler();
        Pane view=object.getHomePage("Zeus");
        rootPane.getChildren().setAll(view);

        }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}