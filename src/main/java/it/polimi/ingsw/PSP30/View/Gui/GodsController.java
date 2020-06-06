package it.polimi.ingsw.PSP30.View.Gui;

import it.polimi.ingsw.PSP30.Client.Client;
import it.polimi.ingsw.PSP30.Messages.ChoseGodResponse;
import it.polimi.ingsw.PSP30.Messages.GodsListResponse;
import it.polimi.ingsw.PSP30.Model.God;
import it.polimi.ingsw.PSP30.View.GUI;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import java.io.IOException;
import java.util.ArrayList;


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

    private ToggleGroup toggleGroup=new ToggleGroup();


    /**
     * This method shows the list of all the Gods usable during the game and allows the first player to choose the Gods he wants for the current match.
     * The number of the cards selected has to be equal to the number of players chosen.
     */
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

    /**
     * This method shows the list of the Gods chosen by the first player and allows the current player to choose what card he wants to use during the match
     * @param gameGods ArrayList of Gods chosen by the first player
     * @param chosenGods ArrayList of Gods already chosen by others players
     * @throws IOException when an error occurred in loading fxml file
     */
    public void showSingleGodSelector(ArrayList<God> gameGods,ArrayList<God> chosenGods) throws IOException {
        God tempGod;
        //buttonsSingleGod = new ArrayList<>();
        StackPane singleGodSelectionPane = FXMLLoader.load(GodsController.class.getClassLoader().getResource("Fxml/ChooseGod.fxml"));
        singleGodBorderPane=(BorderPane) singleGodSelectionPane.getChildren().get(1);
        vBox=(VBox) singleGodBorderPane.getRight();
        firstGod= (ToggleButton) vBox.getChildren().get(0);
        firstGod.setToggleGroup(toggleGroup);
        secondGod=(ToggleButton) vBox.getChildren().get(1);
        secondGod.setToggleGroup(toggleGroup);
        thirdGod=(ToggleButton) vBox.getChildren().get(2);
        thirdGod.setToggleGroup(toggleGroup);
        singleGodChoiceToggleButtonImage((tempGod = gameGods.get(0)).getName(), firstGod, singleGodBorderPane);
        if (godAlreadyChosen(tempGod.getName(),chosenGods)) firstGod.setOpacity(0.3);
        singleGodChoiceToggleButtonImage((tempGod = gameGods.get(1)).getName(), secondGod, singleGodBorderPane);
        if (godAlreadyChosen(tempGod.getName(),chosenGods)) secondGod.setOpacity(0.3);
        if (Client.getNumPlayers() == 3){
            singleGodChoiceToggleButtonImage((tempGod = gameGods.get(2)).getName(), thirdGod, singleGodBorderPane);
            if (godAlreadyChosen(gameGods.get(2).getName(),chosenGods)) thirdGod.setOpacity(0.3);
        }
        else vBox.getChildren().remove(2);
        GUI.getGameStage().setScene(new Scene(singleGodSelectionPane));

    }

    /**
     * This method checks if the selected God has already been chosen by other players
     * @param godName String that indicates the name of the selected God
     * @param chosen ArrayList of Gods already chosen by others players
     * @return true if the selected God has already been chosen by other players, false otherwise
     */
    public boolean godAlreadyChosen(String godName, ArrayList<God> chosen){
        for (int i= 0; i < chosen.size();i++) if (godName.equals(chosen.get(i).getName())) return true;
        return false;
    }


    /**
     * This method manages the choice of the God card
     * @param godName String that indicates the name of the selected God
     * @param button button that contains the God card
     * @param pane pane in which arrange the buttons
     */
    public void singleGodChoiceToggleButtonImage(String godName, ToggleButton button, BorderPane pane){
        selectedGods.add(godName);
        switch (godName){
            case "Apollo":
                button.setStyle("-fx-background-image: url(Images/toggleButtonGods/Apollo.png); -fx-background-size: 150px;");
                button.addEventHandler(MouseEvent.MOUSE_ENTERED, e-> {
                    Image image = new Image(getClass().getResourceAsStream("/Images/Gods/Apo.png"));
                    ImageView img=new ImageView(image);
                    pane.setCenter(img);
                    Image image2 = new Image(getClass().getResourceAsStream("/Images/Backgrounds/Apollo.png"));
                    ImageView img2=new ImageView(image2);
                    img2.setFitWidth(450.0);
                    img2.setFitHeight(900.0);
                    pane.setLeft(img2);
                });
                button.addEventHandler(MouseEvent.MOUSE_EXITED, e-> flushPane(e, pane));
                break;
            case "Artemis":
                button.setStyle("-fx-background-image: url(Images/toggleButtonGods/Artemis.png); -fx-background-size: 150px;");
                button.addEventHandler(MouseEvent.MOUSE_ENTERED, e-> {
                    Image image = new Image(getClass().getResourceAsStream("/Images/Gods/Arte.png"));
                    ImageView img=new ImageView(image);
                    pane.setCenter(img);
                    Image image2 = new Image(getClass().getResourceAsStream("/Images/Backgrounds/Artemis.png"));
                    ImageView img2=new ImageView(image2);
                    img2.setFitWidth(450.0);
                    img2.setFitHeight(900.0);
                    pane.setLeft(img2);
                });
                button.addEventHandler(MouseEvent.MOUSE_EXITED, e-> flushPane(e, pane));
                break;
            case "Athena":
                button.setStyle("-fx-background-image: url(Images/toggleButtonGods/Athena.png); -fx-background-size: 150px;");
                button.addEventHandler(MouseEvent.MOUSE_ENTERED, e-> {
                    Image image = new Image(getClass().getResourceAsStream("/Images/Gods/Athe.png"));
                    ImageView img=new ImageView(image);
                    pane.setCenter(img);
                    Image image2 = new Image(getClass().getResourceAsStream("/Images/Backgrounds/Athena.png"));
                    ImageView img2=new ImageView(image2);
                    img2.setFitWidth(450.0);
                    img2.setFitHeight(900.0);
                    pane.setLeft(img2);
                });
                button.addEventHandler(MouseEvent.MOUSE_EXITED, e-> flushPane(e, pane));
                break;
            case "Atlas":
                button.setStyle("-fx-background-image: url(Images/toggleButtonGods/Atlas.png); -fx-background-size: 150px;");
                button.addEventHandler(MouseEvent.MOUSE_ENTERED, e-> {
                    Image image = new Image(getClass().getResourceAsStream("/Images/Gods/Atla.png"));
                    ImageView img=new ImageView(image);
                    pane.setCenter(img);
                    Image image2 = new Image(getClass().getResourceAsStream("/Images/Backgrounds/Atlas.png"));
                    ImageView img2=new ImageView(image2);
                    img2.setFitWidth(450.0);
                    img2.setFitHeight(900.0);
                    pane.setLeft(img2);
                });
                button.addEventHandler(MouseEvent.MOUSE_EXITED, e-> flushPane(e, pane));
                break;
            case "Demeter":
                button.setStyle("-fx-background-image: url(Images/toggleButtonGods/Demeter.png); -fx-background-size:150px;");
                button.addEventHandler(MouseEvent.MOUSE_ENTERED, e-> {
                    Image image = new Image(getClass().getResourceAsStream("/Images/Gods/Demet.png"));
                    ImageView img=new ImageView(image);
                    pane.setCenter(img);
                    Image image2 = new Image(getClass().getResourceAsStream("/Images/Backgrounds/Demeter.png"));
                    ImageView img2=new ImageView(image2);
                    img2.setFitWidth(450.0);
                    img2.setFitHeight(900.0);
                    pane.setLeft(img2);
                });
                button.addEventHandler(MouseEvent.MOUSE_EXITED, e-> flushPane(e, pane));
                break;
            case "Hephaestus":
                button.setStyle("-fx-background-image: url(Images/toggleButtonGods/Hephaestus.png); -fx-background-size:150px;");
                button.addEventHandler(MouseEvent.MOUSE_ENTERED, e-> {
                    Image image = new Image(getClass().getResourceAsStream("/Images/Gods/Hepha.png"));
                    ImageView img=new ImageView(image);
                    pane.setCenter(img);
                    Image image2 = new Image(getClass().getResourceAsStream("/Images/Backgrounds/Hephaestus.png"));
                    ImageView img2=new ImageView(image2);
                    img2.setFitWidth(450.0);
                    img2.setFitHeight(900.0);
                    pane.setLeft(img2);
                });
                button.addEventHandler(MouseEvent.MOUSE_EXITED, e-> flushPane(e, pane));
                break;
            case "Minotaur":
                button.setStyle("-fx-background-image: url(Images/toggleButtonGods/Minotaur.png); -fx-background-size: 150px;");
                button.addEventHandler(MouseEvent.MOUSE_ENTERED, e-> {
                    Image image = new Image(getClass().getResourceAsStream("/Images/Gods/Minot.png"));
                    ImageView img=new ImageView(image);
                    pane.setCenter(img);
                    Image image2 = new Image(getClass().getResourceAsStream("/Images/Backgrounds/Minotaur.png"));
                    ImageView img2=new ImageView(image2);
                    img2.setFitWidth(450.0);
                    img2.setFitHeight(900.0);
                    pane.setLeft(img2);
                });
                button.addEventHandler(MouseEvent.MOUSE_EXITED, e-> flushPane(e, pane));
                break;
            case "Pan":
                button.setStyle("-fx-background-image: url(Images/toggleButtonGods/Pan.png); -fx-background-size: 150px;");
                button.addEventHandler(MouseEvent.MOUSE_ENTERED, e-> {
                    Image image = new Image(getClass().getResourceAsStream("/Images/Gods/Pa.png"));
                    ImageView img=new ImageView(image);
                    pane.setCenter(img);
                    Image image2 = new Image(getClass().getResourceAsStream("/Images/Backgrounds/Pan.png"));
                    ImageView img2=new ImageView(image2);
                    img2.setFitWidth(450.0);
                    img2.setFitHeight(900.0);
                    pane.setLeft(img2);
                });
                button.addEventHandler(MouseEvent.MOUSE_EXITED, e-> flushPane(e, pane));
                break;
            case "Prometheus":
                button.setStyle("-fx-background-image: url(Images/toggleButtonGods/Prometheus.png); -fx-background-size: 150px;");
                button.addEventHandler(MouseEvent.MOUSE_ENTERED, e-> {
                    Image image = new Image(getClass().getResourceAsStream("/Images/Gods/Promet.png"));
                    ImageView img=new ImageView(image);
                    pane.setCenter(img);
                    Image image2 = new Image(getClass().getResourceAsStream("/Images/Backgrounds/Prometheus.png"));
                    ImageView img2=new ImageView(image2);
                    img2.setFitWidth(450.0);
                    img2.setFitHeight(900.0);
                    pane.setLeft(img2);
                });
                button.addEventHandler(MouseEvent.MOUSE_EXITED, e-> flushPane(e, pane));
                break;
            case "Ares":
                button.setStyle("-fx-background-image: url(Images/toggleButtonGods/Ares.png); -fx-background-size: 150px;");
                button.addEventHandler(MouseEvent.MOUSE_ENTERED, e-> {
                    Image image = new Image(getClass().getResourceAsStream("/Images/Gods/Are.png"));
                    ImageView img=new ImageView(image);
                    pane.setCenter(img);
                    Image image2 = new Image(getClass().getResourceAsStream("/Images/Backgrounds/Ares.png"));
                    ImageView img2=new ImageView(image2);
                    img2.setFitWidth(450.0);
                    img2.setFitHeight(900.0);
                    pane.setLeft(img2);
                });
                button.addEventHandler(MouseEvent.MOUSE_EXITED, e-> flushPane(e, pane));
                break;
            case "Chronus":
                button.setStyle("-fx-background-image: url(Images/toggleButtonGods/Chronus.png); -fx-background-size:150px;");
                button.addEventHandler(MouseEvent.MOUSE_ENTERED, e-> {
                    Image image = new Image(getClass().getResourceAsStream("/Images/Gods/Chronos.png"));
                    ImageView img=new ImageView(image);
                    pane.setCenter(img);
                    Image image2 = new Image(getClass().getResourceAsStream("/Images/Backgrounds/Chronus.png"));
                    ImageView img2=new ImageView(image2);
                    img2.setFitWidth(450.0);
                    img2.setFitHeight(900.0);
                    pane.setLeft(img2);
                });
                button.addEventHandler(MouseEvent.MOUSE_EXITED, e-> flushPane(e, pane));
                break;
            case "Hera":
                button.setStyle("-fx-background-image: url(Images/toggleButtonGods/Hera.png); -fx-background-size:  150px;");
                button.addEventHandler(MouseEvent.MOUSE_ENTERED, e-> {
                    Image image = new Image(getClass().getResourceAsStream("/Images/Gods/Her.png"));
                    ImageView img=new ImageView(image);
                    pane.setCenter(img);
                    Image image2 = new Image(getClass().getResourceAsStream("/Images/Backgrounds/Hera.png"));
                    ImageView img2=new ImageView(image2);
                    img2.setFitWidth(450.0);
                    img2.setFitHeight(900.0);
                    pane.setLeft(img2);
                });
                button.addEventHandler(MouseEvent.MOUSE_EXITED, e-> flushPane(e, pane));
                break;
            case "Hestia":
                button.setStyle("-fx-background-image: url(Images/toggleButtonGods/Hestia.png); -fx-background-size:150px;");
                button.addEventHandler(MouseEvent.MOUSE_ENTERED, e-> {
                    Image image = new Image(getClass().getResourceAsStream("/Images/Gods/Hest.png"));
                    ImageView img=new ImageView(image);
                    pane.setCenter(img);
                    Image image2 = new Image(getClass().getResourceAsStream("/Images/Backgrounds/Hestia.png"));
                    ImageView img2=new ImageView(image2);
                    img2.setFitWidth(450.0);
                    img2.setFitHeight(900.0);
                    pane.setLeft(img2);
                });
                button.addEventHandler(MouseEvent.MOUSE_EXITED, e-> flushPane(e, pane));
                break;
            case "Zeus":
                button.setStyle("-fx-background-image: url(Images/toggleButtonGods/Zeus.png); -fx-background-size:150px;");
                button.addEventHandler(MouseEvent.MOUSE_ENTERED, e-> {
                    Image image = new Image(getClass().getResourceAsStream("/Images/Gods/Zeu.png"));
                    ImageView img=new ImageView(image);
                    pane.setCenter(img);
                    Image image2 = new Image(getClass().getResourceAsStream("/Images/Backgrounds/Zeus.png"));
                    ImageView img2=new ImageView(image2);
                    img2.setFitWidth(450.0);
                    img2.setFitHeight(900.0);
                    pane.setLeft(img2);
                });
                button.addEventHandler(MouseEvent.MOUSE_EXITED, e-> flushPane(e, pane));
                break;
        }

    }

    /**
     * This method don't show the image when the mouse is removed from the button (card)
     * @param event mouse pointer is removed from the card
     * @param pane pane in which arrange the buttons
     */
    public void flushPane(MouseEvent event, BorderPane pane) {
        pane.setCenter(null);
        pane.setLeft(null);
    }


    /**
     * This method shows Artemis card when is selected by the mouse
     * @param event mouse pointer is positioned over Artemis card
     */
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

    /**
     * This method shows Apollo card when is selected by the mouse
     * @param event mouse pointer is positioned over Apollo card
     */
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

    /**
     * This method shows Athena card when is selected by the mouse
     * @param event mouse pointer is positioned over Athena card
     */
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

    /**
     * This method shows Atlas card when is selected by the mouse
     * @param event mouse pointer is positioned over Atlas card
     */
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

    /**
     * This method shows Demeter card when is selected by the mouse
     * @param event mouse pointer is positioned over Demeter card
     */
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

    /**
     * This method shows Hephaestus card when is selected by the mouse
     * @param event mouse pointer is positioned over Hephaestus card
     */
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

    /**
     * This method shows Prometheus card when is selected by the mouse
     * @param event mouse pointer is positioned over Prometheus card
     */
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

    /**
     * This method shows Pan card when is selected by the mouse
     * @param event mouse pointer is positioned over Pan card
     */
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

    /**
     * This method shows Ares card when is selected by the mouse
     * @param event mouse pointer is positioned over Ares card
     */
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

    /**
     * This method shows Chronus card when is selected by the mouse
     * @param event mouse pointer is positioned over Chronus card
     */
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

    /**
     * This method shows Hestia card when is selected by the mouse
     * @param event mouse pointer is positioned over Hestia card
     */
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

    /**
     * This method shows Hera card when is selected by the mouse
     * @param event mouse pointer is positioned over Hera card
     */
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

    /**
     * This method shows Minotaur card when is selected by the mouse
     * @param event mouse pointer is positioned over Minotaur card
     */
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

    /**
     * This method shows Zeus card when is selected by the mouse
     * @param event mouse pointer is positioned over Zeus card
     */
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


    /**
     * This method don't show the image when the mouse is removed from the button (card)
     * @param event mouse pointer is removed from the card
     */
    public void none(MouseEvent event) {
        borderPane.setCenter(null);
        borderPane.setLeft(null);
    }

    public void selected(MouseEvent event) {

    }

    /**
     * This method manages the selection of  Apollo card by the mouse, it also verifies the number of cards selected is smaller than the number of players,
     * otherwise no more cards can be selected
     * @param event mouse pointer have clicked on Apollo card
     */
    public void selectApollo(MouseEvent event){
        if (apollo.isSelected()){
            if (selectedGods.size() < Client.getNumPlayers()) {
                selectedGods.add("Apollo");
            }
            else apollo.setSelected(false);
        }
        else selectedGods.remove("Apollo");
    }

    /**
     * This method manages the selection of  Artemis card by the mouse, it also verifies the number of cards selected is smaller than the number of players,
     * otherwise no more cards can be selected
     * @param event mouse pointer have clicked on Artemis card
     */
    public void selectArtemis(MouseEvent event){
        if (artemis.isSelected()){
            if (selectedGods.size() < Client.getNumPlayers()) {
                selectedGods.add("Artemis");
            }
            else artemis.setSelected(false);
        }
        else selectedGods.remove("Artemis");
    }

    /**
     * This method manages the selection of  Athena card by the mouse, it also verifies the number of cards selected is smaller than the number of players,
     * otherwise no more cards can be selected
     * @param event mouse pointer have clicked on Athena card
     */
    public void selectAthena(MouseEvent event){
        if (athena.isSelected()){
            if (selectedGods.size() < Client.getNumPlayers()) {
                selectedGods.add("Athena");
            }
            else athena.setSelected(false);
        }
        else selectedGods.remove("Athena");
    }

    /**
     * This method manages the selection of Atlas card by the mouse, it also verifies the number of cards selected is smaller than the number of players,
     * otherwise no more cards can be selected
     * @param event mouse pointer have clicked on Atlas card
     */
    public void selectAtlas(MouseEvent event){
        if (atlas.isSelected()){
            if (selectedGods.size() < Client.getNumPlayers()) {
                selectedGods.add("Atlas");
            }
            else atlas.setSelected(false);
        }
        else selectedGods.remove("Atlas");
    }

    /**
     * This method manages the selection of Demeter card by the mouse, it also verifies the number of cards selected is smaller than the number of players,
     * otherwise no more cards can be selected
     * @param event mouse pointer have clicked on Demeter card
     */
    public void selectDemeter(MouseEvent event){
        if (demeter.isSelected()){
            if (selectedGods.size() < Client.getNumPlayers()) {
                selectedGods.add("Demeter");
            }
            else demeter.setSelected(false);
        }
        else selectedGods.remove("Demeter");
    }

    /**
     * This method manages the selection of Hephaestus card by the mouse, it also verifies the number of cards selected is smaller than the number of players,
     * otherwise no more cards can be selected
     * @param event mouse pointer have clicked on Hephaestus card
     */
    public void selectHephaestus(MouseEvent event){
        if (hephaestus.isSelected()){
            if (selectedGods.size() < Client.getNumPlayers()) {
                selectedGods.add("Hephaestus");
            }
            else hephaestus.setSelected(false);
        }
        else selectedGods.remove("Hephaestus");
    }

    /**
     * This method manages the selection of Minotaur card by the mouse, it also verifies the number of cards selected is smaller than the number of players,
     * otherwise no more cards can be selected
     * @param event mouse pointer have clicked on Minotaur card
     */
    public void selectMinotaur(MouseEvent event){
        if (minotaur.isSelected()){
            if (selectedGods.size() < Client.getNumPlayers()) {
                selectedGods.add("Minotaur");
            }
            else minotaur.setSelected(false);
        }
        else selectedGods.remove("Minotaur");
    }

    /**
     * This method manages the selection of Pan card by the mouse, it also verifies the number of cards selected is smaller than the number of players,
     * otherwise no more cards can be selected
     * @param event mouse pointer have clicked on Pan card
     */
    public void selectPan(MouseEvent event){
        if (pan.isSelected()){
            if (selectedGods.size() < Client.getNumPlayers()) {
                selectedGods.add("Pan");
            }
            else pan.setSelected(false);
        }
        else selectedGods.remove("Pan");
    }

    /**
     * This method manages the selection of Prometheus card by the mouse, it also verifies the number of cards selected is smaller than the number of players,
     * otherwise no more cards can be selected
     * @param event mouse pointer have clicked on Prometheus card
     */
    public void selectPrometheus(MouseEvent event){
        if (prometheus.isSelected()){
            if (selectedGods.size() < Client.getNumPlayers()) {
                selectedGods.add("Prometheus");
            }
            else prometheus.setSelected(false);
        }
        else selectedGods.remove("Prometheus");
    }

    /**
     * This method manages the selection of Ares card by the mouse, it also verifies the number of cards selected is smaller than the number of players,
     * otherwise no more cards can be selected
     * @param event mouse pointer have clicked on Ares card
     */
    public void selectAres(MouseEvent event){
        if (ares.isSelected()){
            if (selectedGods.size() < Client.getNumPlayers()) {
                selectedGods.add("Ares");
            }
            else ares.setSelected(false);
        }
        else selectedGods.remove("Ares");
    }

    /**
     * This method manages the selection of Chronus card by the mouse, it also verifies the number of cards selected is smaller than the number of players,
     * otherwise no more cards can be selected
     * @param event mouse pointer have clicked on Chronus card
     */
    public void selectChronus(MouseEvent event){
        if (chronus.isSelected()){
            if (selectedGods.size() < Client.getNumPlayers()) {
                selectedGods.add("Chronus");
            }
            else chronus.setSelected(false);
        }
        else selectedGods.remove("Chronus");
    }

    /**
     * This method manages the selection of Hera card by the mouse, it also verifies the number of cards selected is smaller than the number of players,
     * otherwise no more cards can be selected
     * @param event mouse pointer have clicked on Hera card
     */
    public void selectHera(MouseEvent event){
        if (hera.isSelected()){
            if (selectedGods.size() < Client.getNumPlayers()) {
                selectedGods.add("Hera");

            }
            else hera.setSelected(false);
        }
        else selectedGods.remove("Hera");
    }

    /**
     * This method manages the selection of Hestia card by the mouse, it also verifies the number of cards selected is smaller than the number of players,
     * otherwise no more cards can be selected
     * @param event mouse pointer have clicked on Hestia card
     */
    public void selectHestia(MouseEvent event){
        if (hestia.isSelected()){
            if (selectedGods.size() < Client.getNumPlayers()) {
                selectedGods.add("Hestia");
            }
            else hestia.setSelected(false);
        }
        else selectedGods.remove("Hestia");
    }

    /**
     * This method manages the selection of Zeus card by the mouse, it also verifies the number of cards selected is smaller than the number of players,
     * otherwise no more cards can be selected
     * @param event mouse pointer have clicked on Zeus card
     */
    public void selectZeus(MouseEvent event){
        if (zeus.isSelected()){
            if (selectedGods.size() < Client.getNumPlayers()) {
                selectedGods.add("Zeus");
            }
            else zeus.setSelected(false);
        }
        else selectedGods.remove("Zeus");
    }

    /**
     * This method manages the selection of the firstGod card by the mouse
     */
    public void firstGodSelected(MouseEvent event){
        if (firstGod.isSelected()) selectedGod = selectedGods.get(0);
    }

    /**
     * This method manages the selection of the secondGod card by the mouse
     */
    public void secondGodSelected(MouseEvent event){
        if (secondGod.isSelected()) selectedGod = selectedGods.get(1);
    }

    /**
     * This method manages the selection of the thirdGod card by the mouse
     */
    public void thirdGodSelected(MouseEvent event){
        if (thirdGod.isSelected()) selectedGod=selectedGods.get(2);
    }


    /**
     * This method manages the button clicked at the end of the choice of the God by the first player
     * @param event mouse click on the godSelectionButton
     */
    public void handleGodSelectionButton(MouseEvent event){
        if (selectedGods.size() == Client.getNumPlayers()){
            godSelectionButton.setDisable(true);
            Client.sendMessageToServer(new GodsListResponse(selectedGods));
        }
    }

    /**
     * This method manages the button clicked at the end of the choice of the God by the other players
     * @param event mouse click on the selectSingleGod
     */
    public void handleSingleGodSelectionButton (MouseEvent event){
        selectSingleGod.setDisable(true);
        if(firstGod.isSelected()||secondGod.isSelected()|| thirdGod.isSelected()) Client.sendMessageToServer(new ChoseGodResponse(selectedGod));
        else selectSingleGod.setDisable(false);
    }
}