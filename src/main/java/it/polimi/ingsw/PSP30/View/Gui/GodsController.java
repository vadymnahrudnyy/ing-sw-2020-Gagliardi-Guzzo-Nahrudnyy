package it.polimi.ingsw.PSP30.View.Gui;

import it.polimi.ingsw.PSP30.Client.Client;
import it.polimi.ingsw.PSP30.Messages.GodsListResponse;
import it.polimi.ingsw.PSP30.Model.God;
import it.polimi.ingsw.PSP30.View.GUI;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GodsController implements Initializable {

    @FXML private BorderPane borderPane;
    @FXML private ToggleButton apollo, artemis, ares;
    @FXML private ToggleButton hestia, hera;
    @FXML private ToggleButton demeter, chronus, zeus;
    @FXML private ToggleButton hephaestus, athena, atlas;
    @FXML private ToggleButton pan, prometheus, minotaur;
    @FXML private Button godSelectionButton;

    private final ArrayList<String> selectedGods = new ArrayList<>();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void showGodSelector(ArrayList<God> godList) throws IOException {
        StackPane godSelectionPane = FXMLLoader.load(GodsController.class.getClassLoader().getResource("Fxml/Gods.fxml"));
        if(Client.getNumPlayers() == 3){
            BorderPane godsBorderPane = (BorderPane) godSelectionPane.getChildren().get(1);
            StackPane godIconsStackPane = (StackPane)godsBorderPane.getRight();
            VBox godsVBox = (VBox) godIconsStackPane.getChildren().get(0);
            HBox CronusHBox = (HBox)godsVBox.getChildren().get(3);
            ToggleButton CronusButton = (ToggleButton)CronusHBox.getChildren().get(1);
            CronusButton.setDisable(true);
        }
        GUI.getGameStage().setScene(new Scene(godSelectionPane));
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

    private void selectedGod() {

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

    public void selectApollo(MouseEvent event){
        if (apollo.isSelected()){
            if (selectedGods.size() < Client.getNumPlayers()) {
                selectedGods.add("Apollo");
            }
            else apollo.setSelected(false);
        }
        else selectedGods.remove("Apollo");
    }

    public void selectArtemis(MouseEvent event){
        if (artemis.isSelected()){
            if (selectedGods.size() < Client.getNumPlayers()) {
                selectedGods.add("Artemis");
            }
            else artemis.setSelected(false);
        }
        else selectedGods.remove("Artemis");
    }

    public void selectAthena(MouseEvent event){
        if (athena.isSelected()){
            if (selectedGods.size() < Client.getNumPlayers()) {
                selectedGods.add("Athena");
            }
            else athena.setSelected(false);
        }
        else selectedGods.remove("Athena");
    }

    public void selectAtlas(MouseEvent event){
        if (atlas.isSelected()){
            if (selectedGods.size() < Client.getNumPlayers()) {
                selectedGods.add("Atlas");
            }
            else atlas.setSelected(false);
        }
        else selectedGods.remove("Atlas");
    }

    public void selectDemeter(MouseEvent event){
        if (demeter.isSelected()){
            if (selectedGods.size() < Client.getNumPlayers()) {
                selectedGods.add("Demeter");
            }
            else demeter.setSelected(false);
        }
        else selectedGods.remove("Demeter");
    }

    public void selectHephaestus(MouseEvent event){
        if (hephaestus.isSelected()){
            if (selectedGods.size() < Client.getNumPlayers()) {
                selectedGods.add("Hephaestus");
            }
            else hephaestus.setSelected(false);
        }
        else selectedGods.remove("Hephaestus");
    }

    public void selectMinotaur(MouseEvent event){
        if (minotaur.isSelected()){
            if (selectedGods.size() < Client.getNumPlayers()) {
                selectedGods.add("Minotaur");
            }
            else minotaur.setSelected(false);
        }
        else selectedGods.remove("Minotaur");
    }

    public void selectPan(MouseEvent event){
        if (pan.isSelected()){
            if (selectedGods.size() < Client.getNumPlayers()) {
                selectedGods.add("Pan");
            }
            else pan.setSelected(false);
        }
        else selectedGods.remove("Pan");
    }

    public void selectPrometheus(MouseEvent event){
        if (prometheus.isSelected()){
            if (selectedGods.size() < Client.getNumPlayers()) {
                selectedGods.add("Prometheus");
            }
            else prometheus.setSelected(false);
        }
        else selectedGods.remove("Prometheus");
    }

    public void selectAres(MouseEvent event){
        if (ares.isSelected()){
            if (selectedGods.size() < Client.getNumPlayers()) {
                selectedGods.add("Ares");
            }
            else ares.setSelected(false);
        }
        else selectedGods.remove("Ares");
    }

    public void selectChronus(MouseEvent event){
        if (chronus.isSelected()){
            if (selectedGods.size() < Client.getNumPlayers()) {
                selectedGods.add("Chronus");
            }
            else chronus.setSelected(false);
        }
        else selectedGods.remove("Chronus");
    }

    public void selectHera(MouseEvent event){
        if (hera.isSelected()){
            if (selectedGods.size() < Client.getNumPlayers()) {
                selectedGods.add("Hera");
            }
            else hera.setSelected(false);
        }
        else selectedGods.remove("Hera");
    }

    public void selectHestia(MouseEvent event){
        if (hestia.isSelected()){
            if (selectedGods.size() < Client.getNumPlayers()) {
                selectedGods.add("Hestia");
            }
            else hestia.setSelected(false);
        }
        else selectedGods.remove("Hestia");
    }

    public void selectZeus(MouseEvent event){
        if (zeus.isSelected()){
            if (selectedGods.size() < Client.getNumPlayers()) {
                selectedGods.add("Zeus");
            }
            else zeus.setSelected(false);
        }
        else selectedGods.remove("Zeus");
    }

    public void handleGodSelectionButton(MouseEvent event){
        if (selectedGods.size() == Client.getNumPlayers()){
            godSelectionButton.setDisable(true);
            Client.sendMessageToServer(new GodsListResponse(selectedGods));
        }

    }


}

