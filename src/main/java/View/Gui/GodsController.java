package View.Gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class GodsController implements Initializable {

    @FXML private BorderPane borderPane;
    @FXML private ToggleButton apollo, artemis, ares;
    @FXML private ToggleButton hermes, hestia, hera;
    @FXML private ToggleButton demeter, chronus, zeus;
    @FXML private ToggleButton hephaestus, athena, atlas;
    @FXML private ToggleButton pan, prometheus, minotaur;


    private ToggleGroup buttons=new ToggleGroup();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        apollo.setToggleGroup(buttons);
        artemis.setToggleGroup(buttons);
        ares.setToggleGroup(buttons);
        hermes.setToggleGroup(buttons);
        hera.setToggleGroup(buttons);
        hestia.setToggleGroup(buttons);
        demeter.setToggleGroup(buttons);
        chronus.setToggleGroup(buttons);
        zeus.setToggleGroup(buttons);
        hephaestus.setToggleGroup(buttons);
        athena.setToggleGroup(buttons);
        atlas.setToggleGroup(buttons);
        pan.setToggleGroup(buttons);
        prometheus.setToggleGroup(buttons);
        minotaur.setToggleGroup(buttons);



    }




    public void showArtemis(MouseEvent event) {
        Image image = new Image(getClass().getResourceAsStream("/Images/Gods/Arte.png"));
        ImageView img=new ImageView(image);
        borderPane.setCenter(img);
        Image image2 = new Image(getClass().getResourceAsStream("/Images/Backgrounds/Artemis.png"));
        ImageView img2=new ImageView(image2);
        img2.setFitWidth(450.0);
        img2.setFitHeight(900.0);
        borderPane.setLeft(img2);

    }

    public void showApollo(MouseEvent event) {

        Image image = new Image(getClass().getResourceAsStream("/Images/Gods/Apo.png"));
        ImageView img=new ImageView(image);
        borderPane.setCenter(img);
        Image image2 = new Image(getClass().getResourceAsStream("/Images/Backgrounds/Apollo.png"));
        ImageView img2=new ImageView(image2);
        img2.setFitWidth(450.0);
        img2.setFitHeight(900.0);
        borderPane.setLeft(img2);
    }

    public void showAthena(MouseEvent event) {
        Image image = new Image(getClass().getResourceAsStream("/Images/Gods/Athe.png"));
        ImageView img=new ImageView(image);
        borderPane.setCenter(img);
        Image image2 = new Image(getClass().getResourceAsStream("/Images/Backgrounds/Athena.png"));
        ImageView img2=new ImageView(image2);
        img2.setFitWidth(450.0);
        img2.setFitHeight(900.0);
        borderPane.setLeft(img2);
    }

    public void showAtlas(MouseEvent event) {
        Image image = new Image(getClass().getResourceAsStream("/Images/Gods/Atlas2.png"));
        ImageView img=new ImageView(image);
        borderPane.setCenter(img);
        Image image2 = new Image(getClass().getResourceAsStream("/Images/Backgrounds/Atlas.png"));
        ImageView img2=new ImageView(image2);
        img2.setFitWidth(450.0);
        img2.setFitHeight(900.0);
        borderPane.setLeft(img2);
    }

    public void showDemeter(MouseEvent event) {
        Image image = new Image(getClass().getResourceAsStream("/Images/Gods/Demet.png"));
        ImageView img=new ImageView(image);
        borderPane.setCenter(img);
        Image image2 = new Image(getClass().getResourceAsStream("/Images/Backgrounds/Demeter.png"));
        ImageView img2=new ImageView(image2);
        img2.setFitWidth(450.0);
        img2.setFitHeight(900.0);
        borderPane.setLeft(img2);
    }

    public void showHephaestus(MouseEvent event) {
        Image image = new Image(getClass().getResourceAsStream("/Images/Gods/Hepha.png"));
        ImageView img=new ImageView(image);
        borderPane.setCenter(img);
        Image image2 = new Image(getClass().getResourceAsStream("/Images/Backgrounds/Hephaestus.png"));
        ImageView img2=new ImageView(image2);
        img2.setFitWidth(450.0);
        img2.setFitHeight(900.0);
        borderPane.setLeft(img2);
    }

    public void showHermes(MouseEvent event) {
        Image image = new Image(getClass().getResourceAsStream("/Images/Gods/Herm.png"));
        ImageView img=new ImageView(image);
        borderPane.setCenter(img);
        Image image2 = new Image(getClass().getResourceAsStream("/Images/Backgrounds/Hermes.png"));
        ImageView img2=new ImageView(image2);
        img2.setFitWidth(450.0);
        img2.setFitHeight(900.0);
        borderPane.setLeft(img2);
    }

    public void showPrometheus(MouseEvent event) {
        Image image = new Image(getClass().getResourceAsStream("/Images/Gods/Promet.png"));
        ImageView img=new ImageView(image);
        borderPane.setCenter(img);
        Image image2 = new Image(getClass().getResourceAsStream("/Images/Backgrounds/Prometheus.png"));
        ImageView img2=new ImageView(image2);
        img2.setFitWidth(450.0);
        img2.setFitHeight(900.0);
        borderPane.setLeft(img2);
    }

    public void showPan(MouseEvent event) {
        Image image = new Image(getClass().getResourceAsStream("/Images/Gods/Pa.png"));
        ImageView img=new ImageView(image);
        borderPane.setCenter(img);
        Image image2 = new Image(getClass().getResourceAsStream("/Images/Backgrounds/Pan.png"));
        ImageView img2=new ImageView(image2);
        img2.setFitWidth(450.0);
        img2.setFitHeight(900.0);
        borderPane.setLeft(img2);
    }

    public void showAres(MouseEvent event) {
        Image image = new Image(getClass().getResourceAsStream("/Images/Gods/Are.png"));
        ImageView img=new ImageView(image);
        borderPane.setCenter(img);
        Image image2 = new Image(getClass().getResourceAsStream("/Images/Backgrounds/Ares.png"));
        ImageView img2=new ImageView(image2);
        img2.setFitWidth(450.0);
        img2.setFitHeight(900.0);
        borderPane.setLeft(img2);
    }

    public void showChronus(MouseEvent event) {
        Image image = new Image(getClass().getResourceAsStream("/Images/Gods/Chronos.png"));
        ImageView img=new ImageView(image);
        borderPane.setCenter(img);
        Image image2 = new Image(getClass().getResourceAsStream("/Images/Backgrounds/Chronus.png"));
        ImageView img2=new ImageView(image2);
        img2.setFitWidth(450.0);
        img2.setFitHeight(900.0);
        borderPane.setLeft(img2);
    }

    public void showHestia(MouseEvent event) {
        Image image = new Image(getClass().getResourceAsStream("/Images/Gods/Hest.png"));
        ImageView img=new ImageView(image);
        borderPane.setCenter(img);
        Image image2 = new Image(getClass().getResourceAsStream("/Images/Backgrounds/Hestia.png"));
        ImageView img2=new ImageView(image2);
        img2.setFitWidth(450.0);
        img2.setFitHeight(900.0);
        borderPane.setLeft(img2);
    }

    public void showHera(MouseEvent event) {
        Image image = new Image(getClass().getResourceAsStream("/Images/Gods/Her.png"));
        ImageView img=new ImageView(image);
        borderPane.setCenter(img);
        Image image2 = new Image(getClass().getResourceAsStream("/Images/Backgrounds/Hera.png"));
        ImageView img2=new ImageView(image2);
        img2.setFitWidth(450.0);
        img2.setFitHeight(900.0);
        borderPane.setLeft(img2);
    }

    public void showMinotaur(MouseEvent event) {
        Image image = new Image(getClass().getResourceAsStream("/Images/Gods/Minot.png"));
        ImageView img=new ImageView(image);
        borderPane.setCenter(img);
        Image image2 = new Image(getClass().getResourceAsStream("/Images/Backgrounds/Minotaur.png"));
        ImageView img2=new ImageView(image2);
        img2.setFitWidth(450.0);
        img2.setFitHeight(900.0);
        borderPane.setLeft(img2);
    }

    public void showZeus(MouseEvent event) {
        Image image = new Image(getClass().getResourceAsStream("/Images/Gods/Zeu.png"));
        ImageView img=new ImageView(image);
        borderPane.setCenter(img);
        Image image2 = new Image(getClass().getResourceAsStream("/Images/Backgrounds/Zeus.png"));
        ImageView img2=new ImageView(image2);
        img2.setFitWidth(450.0);
        img2.setFitHeight(900.0);
        borderPane.setLeft(img2);
    }

    public void none(MouseEvent event) {
        borderPane.setCenter(null);
        borderPane.setLeft(null);
    }

    public void selected(MouseEvent event) {

    }
}

