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

public class SelectGod {

    @FXML StackPane rootPane;
    Stage stage=new Stage();

    public void Apollo(MouseEvent event) throws IOException{
        Parent nextroot= FXMLLoader.load(getClass().getClassLoader().getResource("Fxml/Apollo.fxml"));
        Scene nextscene= new Scene(nextroot);
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(nextscene);
        stage.show();
    }
    public void Ares(MouseEvent event) throws IOException {
        Parent nextroot= FXMLLoader.load(getClass().getClassLoader().getResource("Fxml/Ares.fxml"));
        Scene nextscene= new Scene(nextroot);
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(nextscene);
        stage.show();

    }
  public void Artemis(MouseEvent event) throws IOException {
      stage.setFullScreen(true);
      Parent nextroot= FXMLLoader.load(getClass().getClassLoader().getResource("Fxml/Artemis.fxml"));
      Scene nextscene= new Scene(nextroot);
      stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
      stage.setScene(nextscene);
      stage.show();

    }
    public void Athena(MouseEvent event) throws IOException {
        Parent nextroot= FXMLLoader.load(getClass().getClassLoader().getResource("Fxml/Athena.fxml"));
        Scene nextscene= new Scene(nextroot);
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(nextscene);
        stage.show();
    }
    public void Atlas(MouseEvent event) throws IOException {
        Parent nextRoot= FXMLLoader.load(getClass().getClassLoader().getResource("Fxml/Atlas.fxml"));
        Scene nextScene= new Scene(nextRoot);
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(nextScene);
        stage.show();

    }
    public void Chronus(MouseEvent event) throws IOException {
        Parent nextRoot= FXMLLoader.load(getClass().getClassLoader().getResource("Fxml/Chronus.fxml"));
        Scene nextScene= new Scene(nextRoot);
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setFullScreen(true);
        stage.setScene(nextScene);
        stage.show();

    }
    public void Demeter(MouseEvent event) throws IOException {
        Parent nextRoot= FXMLLoader.load(getClass().getClassLoader().getResource("Fxml/Demeter.fxml"));
        Scene nextScene= new Scene(nextRoot);
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setFullScreen(true);
        stage.setScene(nextScene);
        stage.show();

    }
    public void Hephaestus(MouseEvent event) throws IOException {
        Parent nextRoot= FXMLLoader.load(getClass().getClassLoader().getResource("Fxml/Hephaestus.fxml"));
        Scene nextScene= new Scene(nextRoot);
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(nextScene);
        stage.show();

    }
    public void Hermes(MouseEvent event) throws IOException {
        Parent nextRoot= FXMLLoader.load(getClass().getClassLoader().getResource("Fxml/Hermes.fxml"));
        Scene nextScene= new Scene(nextRoot);
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(nextScene);
        stage.show();

    }
    public void Hestia(MouseEvent event) throws IOException {
        Parent nextRoot= FXMLLoader.load(getClass().getClassLoader().getResource("Fxml/Hestia.fxml"));
        Scene nextScene= new Scene(nextRoot);
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(nextScene);
        stage.show();

    }
    public void Minotaur(MouseEvent event) throws IOException {
        Parent nextRoot= FXMLLoader.load(getClass().getClassLoader().getResource("Fxml/Minotaur.fxml"));
        Scene nextScene= new Scene(nextRoot);
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(nextScene);
        stage.show();

    }
    public void Pan(MouseEvent event) throws IOException {
        Parent nextRoot= FXMLLoader.load(getClass().getClassLoader().getResource("Fxml/Pan.fxml"));
        Scene nextScene= new Scene(nextRoot);
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(nextScene);
        stage.show();

    }
    public void Hera(MouseEvent event) throws IOException {
        Parent nextRoot= FXMLLoader.load(getClass().getClassLoader().getResource("Fxml/Hera.fxml"));
        Scene nextScene= new Scene(nextRoot);
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(nextScene);
        stage.show();

    }
    public void Prometheus(MouseEvent event) throws IOException {
        Parent nextRoot= FXMLLoader.load(getClass().getClassLoader().getResource("Fxml/Prometheus.fxml"));
        Scene nextScene= new Scene(nextRoot);
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(nextScene);
        stage.show();

    }
    public void Zeus(MouseEvent event) throws IOException {
        stage.setFullScreen(true);
        Parent nextRoot= FXMLLoader.load(getClass().getClassLoader().getResource("Fxml/Zeus.fxml"));
        Scene nextScene= new Scene(nextRoot);
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(nextScene);
        stage.show();

        }


}