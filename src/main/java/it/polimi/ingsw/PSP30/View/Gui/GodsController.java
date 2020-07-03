package it.polimi.ingsw.PSP30.View.Gui;

import it.polimi.ingsw.PSP30.View.GUI;
import it.polimi.ingsw.PSP30.Model.God;
import it.polimi.ingsw.PSP30.Client.Client;
import it.polimi.ingsw.PSP30.Messages.ChoseGodResponse;
import it.polimi.ingsw.PSP30.Messages.GodsListResponse;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToggleButton;

import java.util.Objects;
import java.io.IOException;
import java.util.ArrayList;
import javafx.scene.layout.*;


/**
 * Manages the Gods.fxml scene (choice of the gods by the first player).
 */
public class GodsController {

    @FXML private BorderPane borderPane;
    @FXML private ToggleButton apollo, artemis, ares;
    @FXML private ToggleButton hestia, hera;
    @FXML private ToggleButton demeter, chronus, zeus;
    @FXML private ToggleButton hephaestus, athena, atlas;
    @FXML private ToggleButton pan, prometheus, minotaur;
    @FXML private ImageView godSelectionButton;

    private static final ArrayList<String> selectedGods = new ArrayList<>();

    @FXML private VBox vBox ;
    @FXML private ToggleButton firstGod,secondGod,thirdGod;
    @FXML private BorderPane singleGodBorderPane;
    @FXML private ImageView selectSingleGod;
    private static String selectedGod;

    private final ToggleGroup toggleGroup=new ToggleGroup();
    private static final double godSelectionCenterWidth = 450.0, godSelectionCenterHeight = 900.0;


    /**
     * Shows the list of all the Gods usable during the game and allows the first player to choose the Gods he wants for the current match.
     * The number of the cards selected has to be equal to the number of players chosen.
     * @throws IOException when an error loading FXML occurs.
     */
    public void showGodSelector() throws IOException {
        StackPane godSelectionPane = FXMLLoader.load(Objects.requireNonNull(GodsController.class.getClassLoader().getResource("Fxml/Gods.fxml")));
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

    /**
     * Shows the list of the Gods chosen by the first player and allows the current player to choose what card he wants to use during the match.
     * @param gameGods ArrayList of Gods chosen by the first player.
     * @param chosenGods ArrayList of Gods already chosen by others players.
     * @throws IOException when an error occurred in loading fxml file.
     */
    public void showSingleGodSelector(ArrayList<God> gameGods,ArrayList<God> chosenGods) throws IOException {
        God tempGod;
        StackPane singleGodSelectionPane = FXMLLoader.load(Objects.requireNonNull(GodsController.class.getClassLoader().getResource("Fxml/ChooseGod.fxml")));
        singleGodBorderPane=(BorderPane) singleGodSelectionPane.getChildren().get(1);
        vBox=(VBox) singleGodBorderPane.getRight();
        firstGod= (ToggleButton) vBox.getChildren().get(0);
        firstGod.setToggleGroup(toggleGroup);
        secondGod=(ToggleButton) vBox.getChildren().get(1);
        secondGod.setToggleGroup(toggleGroup);
        thirdGod=(ToggleButton) vBox.getChildren().get(2);
        thirdGod.setToggleGroup(toggleGroup);
        singleGodChoiceToggleButtonImage((tempGod = gameGods.get(0)).getName(), firstGod, singleGodBorderPane);
        if (godAlreadyChosen(tempGod.getName(),chosenGods)) {
            firstGod.setOpacity(0.3);
            firstGod.setDisable(true);
        }
        singleGodChoiceToggleButtonImage((tempGod = gameGods.get(1)).getName(), secondGod, singleGodBorderPane);
        if (godAlreadyChosen(tempGod.getName(),chosenGods)) {
            secondGod.setOpacity(0.3);
            secondGod.setDisable(true);
        }
        if (Client.getNumPlayers() == 3){
            singleGodChoiceToggleButtonImage((tempGod = gameGods.get(2)).getName(), thirdGod, singleGodBorderPane);
            if (godAlreadyChosen(tempGod.getName(),chosenGods)) {
                thirdGod.setOpacity(0.3);
                thirdGod.setDisable(true);
            }
        }
        else vBox.getChildren().remove(2);
        GUI.getGameStage().setScene(new Scene(singleGodSelectionPane));

    }

    /**
     * Checks if the selected God has already been chosen by other players.
     * @param godName String that indicates the name of the selected God.
     * @param chosen ArrayList of Gods already chosen by others players.
     * @return true if the selected God has already been chosen by other players, false otherwise.
     */
    public boolean godAlreadyChosen(String godName, ArrayList<God> chosen){
        for (God god : chosen) if (godName.equals(god.getName())) return true;
        return false;
    }

    /**
     * Manages the choice of the God card.
     * @param godName String that indicates the name of the selected God.
     * @param button button that contains the God card.
     * @param pane pane in which arrange the buttons.
     */
    public void singleGodChoiceToggleButtonImage(String godName, ToggleButton button, BorderPane pane){
        selectedGods.add(godName);
        Image image = null, image2 = null;

        switch (godName){
            case "Apollo":
                button.setStyle("-fx-background-image: url(Images/toggleButtonGods/Apollo.png); -fx-background-size: 135px;");
                image = new Image(getClass().getResourceAsStream("/Images/Gods/Apo.png"));
                image2 = new Image(getClass().getResourceAsStream("/Images/Backgrounds/Apollo.png"));
                break;
            case "Artemis":
                button.setStyle("-fx-background-image: url(Images/toggleButtonGods/Artemis.png); -fx-background-size: 135px;");
                image = new Image(getClass().getResourceAsStream("/Images/Gods/Arte.png"));
                image2 = new Image(getClass().getResourceAsStream("/Images/Backgrounds/Artemis.png"));
                break;
            case "Athena":
                button.setStyle("-fx-background-image: url(Images/toggleButtonGods/Athena.png); -fx-background-size: 135px;");
                image = new Image(getClass().getResourceAsStream("/Images/Gods/Athe.png"));
                image2 = new Image(getClass().getResourceAsStream("/Images/Backgrounds/Athena.png"));
                break;
            case "Atlas":
                button.setStyle("-fx-background-image: url(Images/toggleButtonGods/Atlas.png); -fx-background-size: 135px;");
                image = new Image(getClass().getResourceAsStream("/Images/Gods/Atla.png"));
                image2 = new Image(getClass().getResourceAsStream("/Images/Backgrounds/Atlas.png"));
                break;
            case "Demeter":
                button.setStyle("-fx-background-image: url(Images/toggleButtonGods/Demeter.png); -fx-background-size:135px;");
                image = new Image(getClass().getResourceAsStream("/Images/Gods/Demet.png"));
                image2 = new Image(getClass().getResourceAsStream("/Images/Backgrounds/Demeter.png"));
                break;
            case "Hephaestus":
                button.setStyle("-fx-background-image: url(Images/toggleButtonGods/Hephaestus.png); -fx-background-size:135px;");
                image = new Image(getClass().getResourceAsStream("/Images/Gods/Hepha.png"));
                image2 = new Image(getClass().getResourceAsStream("/Images/Backgrounds/Hephaestus.png"));
                break;
            case "Minotaur":
                button.setStyle("-fx-background-image: url(Images/toggleButtonGods/Minotaur.png); -fx-background-size: 135px;");
                image = new Image(getClass().getResourceAsStream("/Images/Gods/Minot.png"));
                image2 = new Image(getClass().getResourceAsStream("/Images/Backgrounds/Minotaur.png"));
                break;
            case "Pan":
                button.setStyle("-fx-background-image: url(Images/toggleButtonGods/Pan.png); -fx-background-size: 135px;");
                image = new Image(getClass().getResourceAsStream("/Images/Gods/Pa.png"));
                image2 = new Image(getClass().getResourceAsStream("/Images/Backgrounds/Pan.png"));
                break;
            case "Prometheus":
                button.setStyle("-fx-background-image: url(Images/toggleButtonGods/Prometheus.png); -fx-background-size: 135px;");
                image = new Image(getClass().getResourceAsStream("/Images/Gods/Promet.png"));
                image2 = new Image(getClass().getResourceAsStream("/Images/Backgrounds/Prometheus.png"));
                break;
            case "Ares":
                button.setStyle("-fx-background-image: url(Images/toggleButtonGods/Ares.png); -fx-background-size: 135px;");
                image = new Image(getClass().getResourceAsStream("/Images/Gods/Are.png"));
                image2 = new Image(getClass().getResourceAsStream("/Images/Backgrounds/Ares.png"));
                break;
            case "Chronus":
                button.setStyle("-fx-background-image: url(Images/toggleButtonGods/Chronus.png); -fx-background-size:135px;");
                image = new Image(getClass().getResourceAsStream("/Images/Gods/Chronos.png"));
                image2 = new Image(getClass().getResourceAsStream("/Images/Backgrounds/Chronus.png"));
                break;
            case "Hera":
                button.setStyle("-fx-background-image: url(Images/toggleButtonGods/Hera.png); -fx-background-size:  135px;");
                image = new Image(getClass().getResourceAsStream("/Images/Gods/Her.png"));
                image2 = new Image(getClass().getResourceAsStream("/Images/Backgrounds/Hera.png"));
                break;
            case "Hestia":
                button.setStyle("-fx-background-image: url(Images/toggleButtonGods/Hestia.png); -fx-background-size:135px;");
                image = new Image(getClass().getResourceAsStream("/Images/Gods/Hest.png"));
                image2 = new Image(getClass().getResourceAsStream("/Images/Backgrounds/Hestia.png"));
                break;
            case "Zeus":
                button.setStyle("-fx-background-image: url(Images/toggleButtonGods/Zeus.png); -fx-background-size:135px;");
                image = new Image(getClass().getResourceAsStream("/Images/Gods/Zeu.png"));
                image2 = new Image(getClass().getResourceAsStream("/Images/Backgrounds/Zeus.png"));
                break;
        }

        Image finalImage = image2;
        Image finalImage1 = image;
        button.addEventHandler(MouseEvent.MOUSE_ENTERED, e-> {
            ImageView img=new ImageView(finalImage1);
            pane.setCenter(img);
            ImageView img2=new ImageView(finalImage);
            img2.setFitWidth(450.0);
            img2.setFitHeight(900.0);
            pane.setLeft(img2);
        });
        button.addEventHandler(MouseEvent.MOUSE_EXITED, e-> flushPane(e, pane));
    }

    /**
     * Does not show the image when the mouse is removed from the button (card).
     * @param event mouse pointer is removed from the card.
     * @param pane pane in which arrange the buttons.
     */
    public void flushPane(MouseEvent event, BorderPane pane) {
        event.consume();
        pane.setCenter(null);
        pane.setLeft(null);
    }

    /**
     * Handles the "On mouse entered" event on a god toggle button during god selection.
     * @param godSelectionCenter the image will be shown in central position of the screen.
     * @param godSelectionLeft the image will be shown in left position of the screen.
     */
    protected void showCard(Image godSelectionCenter, Image godSelectionLeft){
        ImageView leftImage = new ImageView(godSelectionLeft);
        ImageView centerImage = new ImageView(godSelectionCenter);
        leftImage.setFitWidth(godSelectionCenterWidth);
        leftImage.setFitHeight(godSelectionCenterHeight);
        borderPane.setLeft(leftImage);
        borderPane.setCenter(centerImage);
    }

    /**
     * Shows Apollo card when is selected by the mouse.
     * @param event mouse pointer is positioned over Apollo card.
     */
    public void showApollo(MouseEvent event) {
        event.consume();
        Image center = new Image(getClass().getResourceAsStream("/Images/Gods/Apo.png"));
        Image left = new Image(getClass().getResourceAsStream("/Images/Backgrounds/Apollo.png"));
        showCard(center,left);
    }

    /**
     * Shows Artemis card when is selected by the mouse.
     * @param event mouse pointer is positioned over Artemis card.
     */
    public void showArtemis(MouseEvent event) {
        event.consume();
        Image center = new Image(getClass().getResourceAsStream("/Images/Gods/Arte.png"));
        Image left = new Image(getClass().getResourceAsStream("/Images/Backgrounds/Artemis.png"));
        showCard(center,left);
    }

    /**
     * Shows Athena card when is selected by the mouse.
     * @param event mouse pointer is positioned over Athena card.
     */
    public void showAthena(MouseEvent event) {
        event.consume();
        Image center = new Image(getClass().getResourceAsStream("/Images/Gods/Athe.png"));
        Image left = new Image(getClass().getResourceAsStream("/Images/Backgrounds/Athena.png"));
        showCard(center,left);
    }

    /**
     * Shows Atlas card when is selected by the mouse.
     * @param event mouse pointer is positioned over Atlas card.
     */
    public void showAtlas(MouseEvent event) {
        event.consume();
        Image center = new Image(getClass().getResourceAsStream("/Images/Gods/Atlas2.png"));
        Image left = new Image(getClass().getResourceAsStream("/Images/Backgrounds/Atlas.png"));
        showCard(center,left);
    }

    /**
     * Shows Demeter card when is selected by the mouse.
     * @param event mouse pointer is positioned over Demeter card.
     */
    public void showDemeter(MouseEvent event) {
        event.consume();
        Image center = new Image(getClass().getResourceAsStream("/Images/Gods/Demet.png"));
        Image left = new Image(getClass().getResourceAsStream("/Images/Backgrounds/Demeter.png"));
        showCard(center,left);
    }

    /**
     * Shows Hephaestus card when is selected by the mouse.
     * @param event mouse pointer is positioned over Hephaestus card.
     */
    public void showHephaestus(MouseEvent event) {
        event.consume();
        Image center = new Image(getClass().getResourceAsStream("/Images/Gods/Hepha.png"));
        Image left = new Image(getClass().getResourceAsStream("/Images/Backgrounds/Hephaestus.png"));
        showCard(center,left);
    }

    /**
     * Shows Prometheus card when is selected by the mouse.
     * @param event mouse pointer is positioned over Prometheus card.
     */
    public void showPrometheus(MouseEvent event) {
        event.consume();
        Image center = new Image(getClass().getResourceAsStream("/Images/Gods/Promet.png"));
        Image left = new Image(getClass().getResourceAsStream("/Images/Backgrounds/Prometheus.png"));
        showCard(center,left);
    }

    /**
     * Shows Pan card when is selected by the mouse.
     * @param event mouse pointer is positioned over Pan card.
     */
    public void showPan(MouseEvent event) {
        event.consume();
        Image center = new Image(getClass().getResourceAsStream("/Images/Gods/Pa.png"));
        Image left = new Image(getClass().getResourceAsStream("/Images/Backgrounds/Pan.png"));
        showCard(center,left);
    }

    /**
     * Shows Ares card when is selected by the mouse.
     * @param event mouse pointer is positioned over Ares card.
     */
    public void showAres(MouseEvent event) {
        event.consume();
        Image center = new Image(getClass().getResourceAsStream("/Images/Gods/Are.png"));
        Image left = new Image(getClass().getResourceAsStream("/Images/Backgrounds/Ares.png"));
        showCard(center,left);
    }

    /**
     * Shows Chronus card when is selected by the mouse.
     * @param event mouse pointer is positioned over Chronus card.
     */
    public void showChronus(MouseEvent event) {
        event.consume();
        Image center = new Image(getClass().getResourceAsStream("/Images/Gods/Chronos.png"));
        Image left = new Image(getClass().getResourceAsStream("/Images/Backgrounds/Chronus.png"));
        showCard(center,left);
    }

    /**
     * Shows Hestia card when is selected by the mouse.
     * @param event mouse pointer is positioned over Hestia card.
     */
    public void showHestia(MouseEvent event) {
        event.consume();
        Image center = new Image(getClass().getResourceAsStream("/Images/Gods/Hest.png"));
        Image left = new Image(getClass().getResourceAsStream("/Images/Backgrounds/Hestia.png"));
        showCard(center,left);
    }

    /**
     * Shows Hera card when is selected by the mouse.
     * @param event mouse pointer is positioned over Hera card.
     */
    public void showHera(MouseEvent event) {
        event.consume();
        Image center = new Image(getClass().getResourceAsStream("/Images/Gods/Her.png"));
        Image left = new Image(getClass().getResourceAsStream("/Images/Backgrounds/Hera.png"));
        showCard(center,left);
    }

    /**
     * Shows Minotaur card when is selected by the mouse.
     * @param event mouse pointer is positioned over Minotaur card.
     */
    public void showMinotaur(MouseEvent event) {
        event.consume();
        Image center = new Image(getClass().getResourceAsStream("/Images/Gods/Minot.png"));
        Image left = new Image(getClass().getResourceAsStream("/Images/Backgrounds/Minotaur.png"));
        showCard(center,left);
    }

    /**
     * Shows Zeus card when is selected by the mouse.
     * @param event mouse pointer is positioned over Zeus card.
     */
    public void showZeus(MouseEvent event) {
        event.consume();
        Image center = new Image(getClass().getResourceAsStream("/Images/Gods/Zeu.png"));
        Image left = new Image(getClass().getResourceAsStream("/Images/Backgrounds/Zeus.png"));
        showCard(center,left);
    }

    /**
     * Does not show the image when the mouse is removed from the button (card).
     * @param event mouse pointer is removed from the card.
     */
    public void none(MouseEvent event) {
        event.consume();
        borderPane.setCenter(null);
        borderPane.setLeft(null);
    }

    /**
     * Selects the image when the then button is selected.
     * @param event mouse is clicked on the button.
     */
    public void selected(MouseEvent event) {
        event.consume();
    }

    /**
     * Manages the selection of  Apollo card by the mouse, it also verifies the number of cards selected is smaller than the number of players,
     * otherwise no more cards can be selected.
     * @param event mouse pointer have clicked on Apollo card.
     */
    public void selectApollo(MouseEvent event){
        event.consume();
        if (apollo.isSelected()){
            if (selectedGods.size() < Client.getNumPlayers()) {
                selectedGods.add("Apollo");
            }
            else apollo.setSelected(false);
        }
        else selectedGods.remove("Apollo");
    }

    /**
     * Manages the selection of  Artemis card by the mouse, it also verifies the number of cards selected is smaller than the number of players,
     * otherwise no more cards can be selected.
     * @param event mouse pointer have clicked on Artemis card.
     */
    public void selectArtemis(MouseEvent event){
        event.consume();
        if (artemis.isSelected()){
            if (selectedGods.size() < Client.getNumPlayers()) {
                selectedGods.add("Artemis");
            }
            else artemis.setSelected(false);
        }
        else selectedGods.remove("Artemis");
    }

    /**
     * Manages the selection of  Athena card by the mouse, it also verifies the number of cards selected is smaller than the number of players,
     * otherwise no more cards can be selected.
     * @param event mouse pointer have clicked on Athena card.
     */
    public void selectAthena(MouseEvent event){
        event.consume();
        if (athena.isSelected()){
            if (selectedGods.size() < Client.getNumPlayers()) {
                selectedGods.add("Athena");
            }
            else athena.setSelected(false);
        }
        else selectedGods.remove("Athena");
    }

    /**
     * Manages the selection of Atlas card by the mouse, it also verifies the number of cards selected is smaller than the number of players,
     * otherwise no more cards can be selected.
     * @param event mouse pointer have clicked on Atlas card.
     */
    public void selectAtlas(MouseEvent event){
        event.consume();
        if (atlas.isSelected()){
            if (selectedGods.size() < Client.getNumPlayers()) {
                selectedGods.add("Atlas");
            }
            else atlas.setSelected(false);
        }
        else selectedGods.remove("Atlas");
    }

    /**
     * Manages the selection of Demeter card by the mouse, it also verifies the number of cards selected is smaller than the number of players,
     * otherwise no more cards can be selected.
     * @param event mouse pointer have clicked on Demeter card.
     */
    public void selectDemeter(MouseEvent event){
        event.consume();
        if (demeter.isSelected()){
            if (selectedGods.size() < Client.getNumPlayers()) {
                selectedGods.add("Demeter");
            }
            else demeter.setSelected(false);
        }
        else selectedGods.remove("Demeter");
    }

    /**
     * Manages the selection of Hephaestus card by the mouse, it also verifies the number of cards selected is smaller than the number of players,
     * otherwise no more cards can be selected.
     * @param event mouse pointer have clicked on Hephaestus card.
     */
    public void selectHephaestus(MouseEvent event){
        event.consume();
        if (hephaestus.isSelected()){
            if (selectedGods.size() < Client.getNumPlayers()) {
                selectedGods.add("Hephaestus");
            }
            else hephaestus.setSelected(false);
        }
        else selectedGods.remove("Hephaestus");
    }

    /**
     * Manages the selection of Minotaur card by the mouse, it also verifies the number of cards selected is smaller than the number of players,
     * otherwise no more cards can be selected.
     * @param event mouse pointer have clicked on Minotaur card.
     */
    public void selectMinotaur(MouseEvent event){
        event.consume();
        if (minotaur.isSelected()){
            if (selectedGods.size() < Client.getNumPlayers()) {
                selectedGods.add("Minotaur");
            }
            else minotaur.setSelected(false);
        }
        else selectedGods.remove("Minotaur");
    }

    /**
     * Manages the selection of Pan card by the mouse, it also verifies the number of cards selected is smaller than the number of players,
     * otherwise no more cards can be selected.
     * @param event mouse pointer have clicked on Pan card.
     */
    public void selectPan(MouseEvent event){
        event.consume();
        if (pan.isSelected()){
            if (selectedGods.size() < Client.getNumPlayers()) {
                selectedGods.add("Pan");
            }
            else pan.setSelected(false);
        }
        else selectedGods.remove("Pan");
    }

    /**
     * Manages the selection of Prometheus card by the mouse, it also verifies the number of cards selected is smaller than the number of players,
     * otherwise no more cards can be selected.
     * @param event mouse pointer have clicked on Prometheus card.
     */
    public void selectPrometheus(MouseEvent event){
        event.consume();
        if (prometheus.isSelected()){
            if (selectedGods.size() < Client.getNumPlayers()) {
                selectedGods.add("Prometheus");
            }
            else prometheus.setSelected(false);
        }
        else selectedGods.remove("Prometheus");
    }

    /**
     * Manages the selection of Ares card by the mouse, it also verifies the number of cards selected is smaller than the number of players,
     * otherwise no more cards can be selected.
     * @param event mouse pointer have clicked on Ares card.
     */
    public void selectAres(MouseEvent event){
        event.consume();
        if (ares.isSelected()){
            if (selectedGods.size() < Client.getNumPlayers()) {
                selectedGods.add("Ares");
            }
            else ares.setSelected(false);
        }
        else selectedGods.remove("Ares");
    }

    /**
     * Manages the selection of Chronus card by the mouse, it also verifies the number of cards selected is smaller than the number of players,
     * otherwise no more cards can be selected.
     * @param event mouse pointer have clicked on Chronus card.
     */
    public void selectChronus(MouseEvent event){
        event.consume();
        if (chronus.isSelected()){
            if (selectedGods.size() < Client.getNumPlayers()) {
                selectedGods.add("Chronus");
            }
            else chronus.setSelected(false);
        }
        else selectedGods.remove("Chronus");
    }

    /**
     * Manages the selection of Hera card by the mouse, it also verifies the number of cards selected is smaller than the number of players,
     * otherwise no more cards can be selected.
     * @param event mouse pointer have clicked on Hera card.
     */
    public void selectHera(MouseEvent event){
        event.consume();
        if (hera.isSelected()){
            if (selectedGods.size() < Client.getNumPlayers()) {
                selectedGods.add("Hera");

            }
            else hera.setSelected(false);
        }
        else selectedGods.remove("Hera");
    }

    /**
     * Manages the selection of Hestia card by the mouse, it also verifies the number of cards selected is smaller than the number of players,
     * otherwise no more cards can be selected.
     * @param event mouse pointer have clicked on Hestia card.
     */
    public void selectHestia(MouseEvent event){
        event.consume();
        if (hestia.isSelected()){
            if (selectedGods.size() < Client.getNumPlayers()) {
                selectedGods.add("Hestia");
            }
            else hestia.setSelected(false);
        }
        else selectedGods.remove("Hestia");
    }

    /**
     * Manages the selection of Zeus card by the mouse, it also verifies the number of cards selected is smaller than the number of players,
     * otherwise no more cards can be selected.
     * @param event mouse pointer have clicked on Zeus card.
     */
    public void selectZeus(MouseEvent event){
        event.consume();
        if (zeus.isSelected()){
            if (selectedGods.size() < Client.getNumPlayers()) {
                selectedGods.add("Zeus");
            }
            else zeus.setSelected(false);
        }
        else selectedGods.remove("Zeus");
    }

    /**
     * Manages the selection of the firstGod card by the mouse.
     * @param event MouseClick.
     */
    public void firstGodSelected(MouseEvent event){
        event.consume();
        if (firstGod.isSelected()) selectedGod = selectedGods.get(0);
    }

    /**
     * Manages the selection of the secondGod card by the mouse.
     * @param event MouseClick.
     */
    public void secondGodSelected(MouseEvent event){
        event.consume();
        if (secondGod.isSelected()) selectedGod = selectedGods.get(1);
    }

    /**
     * Manages the selection of the thirdGod card by the mouse.
     * @param event MouseClick.
     */
    public void thirdGodSelected(MouseEvent event){
        event.consume();
        if (thirdGod.isSelected()) selectedGod=selectedGods.get(2);
    }

    /**
     * Manages the button clicked at the end of the choice of the God by the first player.
     * @param event mouse click on the godSelectionButton.
     */
    public void handleGodSelectionButton(MouseEvent event){
        event.consume();
        if (selectedGods.size() == Client.getNumPlayers()){
            godSelectionButton.setDisable(true);
            Client.sendMessageToServer(new GodsListResponse(selectedGods));
        }
    }

    /**
     * Manages the button clicked at the end of the choice of the God by the other players.
     * @param event mouse click on the selectSingleGod.
     */
    public void handleSingleGodSelectionButton (MouseEvent event){
        event.consume();
        selectSingleGod.setDisable(true);
        if(firstGod.isSelected()||secondGod.isSelected()|| thirdGod.isSelected()) Client.sendMessageToServer(new ChoseGodResponse(selectedGod));
        else selectSingleGod.setDisable(false);
    }
}