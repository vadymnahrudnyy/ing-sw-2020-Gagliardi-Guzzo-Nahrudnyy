package View.GUI;

import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class GodsController implements Initializable {

    @FXML private ImageView apollo, artemis, athena;
    @FXML private ImageView demeter, minotaur, pan;
    @FXML private ImageView hestia, hera, zeus;
    @FXML private ImageView prometheus, hephaestus, ares;
    @FXML private ImageView hermes, atlas, chronus;
    @FXML private BorderPane borderPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void showArtemis(MouseEvent event) {
        Image image = new Image(getClass().getResourceAsStream("/Images/Backgrounds/ArtemisoPopUp.png"));
        ImageView img=new ImageView(image);
        borderPane.setCenter(img);
    }

    public void showApollo(MouseEvent event) {

        Image image = new Image(getClass().getResourceAsStream("/Images/Backgrounds/ApolloPopUp.png"));
        ImageView img=new ImageView(image);
        borderPane.setCenter(img);
    }

    public void showAthena(MouseEvent event) {
        Image image = new Image(getClass().getResourceAsStream("/Images/Backgrounds/AthenaPopUp.png"));
        ImageView img=new ImageView(image);
        borderPane.setCenter(img);
    }

    public void showAtlas(MouseEvent event) {
        Image image = new Image(getClass().getResourceAsStream("/Images/Backgrounds/AtlasPopUp.png"));
        ImageView img=new ImageView(image);
        borderPane.setCenter(img);
    }

    public void showDemeter(MouseEvent event) {
        Image image = new Image(getClass().getResourceAsStream("/Images/Backgrounds/DemeterPopUp.png"));
        ImageView img=new ImageView(image);
        borderPane.setCenter(img);
    }

    public void showHephaestus(MouseEvent event) {
        Image image = new Image(getClass().getResourceAsStream("/Images/Backgrounds/HephestusPopUp.png"));
        ImageView img=new ImageView(image);
        borderPane.setCenter(img);
    }

    public void showHermes(MouseEvent event) {
        Image image = new Image(getClass().getResourceAsStream("/Images/Backgrounds/HermesPopUp.png"));
        ImageView img=new ImageView(image);
        borderPane.setCenter(img);
    }

    public void showPrometheus(MouseEvent event) {
        Image image = new Image(getClass().getResourceAsStream("/Images/Backgrounds/PrometheusPopUp.png"));
        ImageView img=new ImageView(image);
        borderPane.setCenter(img);
    }

    public void showPan(MouseEvent event) {
        Image image = new Image(getClass().getResourceAsStream("/Images/Backgrounds/PanPopUp.png"));
        ImageView img=new ImageView(image);
        borderPane.setCenter(img);
    }

    public void showAres(MouseEvent event) {
        Image image = new Image(getClass().getResourceAsStream("/Images/Backgrounds/AresPopUp.png"));
        ImageView img=new ImageView(image);
        borderPane.setCenter(img);
    }

    public void showChronus(MouseEvent event) {
        Image image = new Image(getClass().getResourceAsStream("/Images/Backgrounds/ChronusPopUp.png"));
        ImageView img=new ImageView(image);
        borderPane.setCenter(img);
    }

    public void showHestia(MouseEvent event) {
        Image image = new Image(getClass().getResourceAsStream("/Images/Backgrounds/HestiaPopUp.png"));
        ImageView img=new ImageView(image);
        borderPane.setCenter(img);
    }

    public void showHera(MouseEvent event) {
        Image image = new Image(getClass().getResourceAsStream("/Images/Backgrounds/HeraPopUp.png"));
        ImageView img=new ImageView(image);
        borderPane.setCenter(img);
    }

    public void showMinotaur(MouseEvent event) {
        Image image = new Image(getClass().getResourceAsStream("/Images/Backgrounds/MinotaurPopUp.png"));
        ImageView img=new ImageView(image);
        borderPane.setCenter(img);
    }

    public void showZeus(MouseEvent event) {
        Image image = new Image(getClass().getResourceAsStream("/Images/Backgrounds/ZeusPopUp.png"));
        ImageView img=new ImageView(image);
        borderPane.setCenter(img);
    }

    public void none(MouseEvent event) {
        borderPane.setCenter(null);
    }
}
