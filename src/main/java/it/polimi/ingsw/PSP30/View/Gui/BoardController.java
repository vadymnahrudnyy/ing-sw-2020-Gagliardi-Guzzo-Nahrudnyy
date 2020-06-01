package it.polimi.ingsw.PSP30.View.Gui;

import it.polimi.ingsw.PSP30.Model.*;
import it.polimi.ingsw.PSP30.View.GUI;
import it.polimi.ingsw.PSP30.Messages.*;


import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.io.IOException;
import java.util.ArrayList;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import it.polimi.ingsw.PSP30.Client.Client;


public class BoardController{


    //Board
    @FXML private StackPane mainPane, playerPane, firstOpponentPane, secondOpponentPane;
    @FXML private Label playerUsername, firstOpponentUsername, secondOpponentUsername;
    @FXML private ImageView playerGod, secondOpponentGod, firstOpponentGod, exitButton, rulesButton;
    @FXML private VBox opponentsPane;


    //startPlayerSelection
    @FXML private StackPane selectPlayerMainPane,firstPlayer, secondPlayer, thirdPlayer ;
    @FXML private BorderPane selectPlayerBorderPane;
    @FXML private ToggleButton firstButton, secondButton, thirdButton;
    @FXML private Label firstLabel, secondLabel, thirdLabel;
    @FXML private HBox hBox;
    @FXML private ImageView selectButton;

    //load startPlayerSelection
    //controllare se è playerUsername o opponent's username e imporre toggle username in base alla divinità


    private static Cell[][] cell = new Cell[5][5];
    private static GridPane gridPane=new GridPane();
    private static Scene boardScene;

    private static String firstOpponent;
    private static String secondOpponent;

    private static ToggleGroup toggleGroup=new ToggleGroup();
    private static final ArrayList<String> selectedGods = new ArrayList<>();

    public void initializeBoard(GameStartNotification message) {
        if (GUI.getGameStage() == null) GUI.setGameStage(new Stage());
        GUI.getGameStage().setResizable(false);
        try{
            mainPane = FXMLLoader.load(BoardController.class.getClassLoader().getResource("Fxml/Board.fxml"));
        }catch (IOException e){
            e.printStackTrace();
        }

        exitButton=(ImageView) mainPane.getChildren().get(1);
        rulesButton=(ImageView) mainPane.getChildren().get(2);
        playerPane= (StackPane) mainPane.getChildren().get(4);
        playerGod=(ImageView) playerPane.getChildren().get(0);
        playerUsername=(Label) playerPane.getChildren().get(3);
        opponentsPane=(VBox) mainPane.getChildren().get(5);
        firstOpponentPane= (StackPane) opponentsPane.getChildren().get(0);
        firstOpponentGod=(ImageView) firstOpponentPane.getChildren().get(0);
        firstOpponentUsername=(Label) firstOpponentPane.getChildren().get(2);
        secondOpponentPane= (StackPane) opponentsPane.getChildren().get(1);
        secondOpponentGod=(ImageView) secondOpponentPane.getChildren().get(0);
        secondOpponentUsername=(Label) secondOpponentPane.getChildren().get(2);
        Player[] playersUsername = message.getGame().getPlayers();


        playerUsername.setText(Client.getUsername());
        for (Player player : playersUsername) {
            if (!Client.getUsername().equals(player.getUsername())) {
                if (firstOpponent == null) firstOpponent = player.getUsername();
                else secondOpponent = player.getUsername();
            }
        }
        firstOpponentUsername.setText(firstOpponent);

        playerGod.setVisible(false);
        firstOpponentGod.setVisible(false);
        secondOpponentGod.setVisible(false);

        if(message.getGame().getNumPlayers() == 3) secondOpponentUsername.setText(secondOpponent);
        else {
            secondOpponentPane.setVisible(false);
            opponentsPane.getChildren().remove(1);
        }


        IslandBoard board = message.getGame().getGameBoard();

        for(int X = 0; X < IslandBoard.TABLE_DIMENSION;X++)
            for (int Y = 0; Y < IslandBoard.TABLE_DIMENSION; Y++){
                Space currentSpace = board.getSpace(X+1,Y+1);
                cell[X][Y]=new Cell(X, Y, currentSpace);
                gridPane.add(cell[X][Y],Y,X);
            }
        mainPane.getChildren().add(gridPane);
        gridPane.setAlignment(Pos.CENTER);
        boardScene = new Scene(mainPane);
        GUI.getGameStage().setScene(boardScene);
        GUI.getGameStage().show();
    }

    public void updateGameBoard(Game updatedGame){
        GUI.getGameStage().setScene(boardScene);
        Player currentPlayer = updatedGame.getCurrentPlayer();
        IslandBoard currentBoard = updatedGame.getGameBoard();

        //Divinità
        Player[] gamePlayers = updatedGame.getPlayers();
        for (Player gamePlayer : gamePlayers) {
            God currentGod = gamePlayer.getGod();
            if (currentGod != null){
                if (Client.getUsername().equals(gamePlayer.getUsername())) {
                    playerGod.setImage(new Image(getClass().getResourceAsStream(selectGodImage(false, gamePlayer.getGod().getName()))));
                    playerGod.setVisible(true);
                }
                else if (firstOpponent.equals(gamePlayer.getUsername())) {
                firstOpponentGod.setImage(new Image(getClass().getResourceAsStream(selectGodImage(true, gamePlayer.getGod().getName()))));
                firstOpponentGod.setVisible(true);
                }
                else {
                    secondOpponentGod.setImage(new Image(getClass().getResourceAsStream(selectGodImage(true, gamePlayer.getGod().getName()))));
                    secondOpponentGod.setVisible(true);
                }
            }
        }
        //da aggiungere il colore dietro al current player

        GridPane newGridPane = new GridPane();
        for(int X = 0; X < IslandBoard.TABLE_DIMENSION;X++)
            for (int Y = 0; Y < IslandBoard.TABLE_DIMENSION; Y++){
                Space currentSpace = currentBoard.getSpace(X+1,Y+1);
                Cell newCell = new Cell(X, Y, currentSpace);
                newGridPane.add(newCell,Y,X);
            }
        gridPane = newGridPane;

    }

    public void firstPlayerStart(MouseEvent event){

    }


    public void showSelectFirstPlayer(Player[] players) throws IOException {
        selectPlayerMainPane = FXMLLoader.load(BoardController.class.getClassLoader().getResource("Fxml/StartPlayerSelection.fxml"));
        selectPlayerBorderPane=(BorderPane) selectPlayerMainPane.getChildren().get(1);
        hBox=(HBox) selectPlayerBorderPane.getCenter();
        selectButton=(ImageView) selectPlayerBorderPane.getBottom();
        firstPlayer=(StackPane) hBox.getChildren().get(0);
        secondPlayer=(StackPane) hBox.getChildren().get(1);
        thirdPlayer=(StackPane) hBox.getChildren().get(2);
        firstButton=(ToggleButton) firstPlayer.getChildren().get(0);
        firstButton.setToggleGroup(toggleGroup);
        firstLabel=(Label) firstPlayer.getChildren().get(1);
        secondButton=(ToggleButton) secondPlayer.getChildren().get(0);
        secondButton.setToggleGroup(toggleGroup);
        secondLabel=(Label) secondPlayer.getChildren().get(1);
        if(Client.getNumPlayers()==3) {
            thirdButton = (ToggleButton) thirdPlayer.getChildren().get(0);
            thirdButton.setToggleGroup(toggleGroup);
            thirdLabel = (Label) thirdPlayer.getChildren().get(1);
        }
        else hBox.getChildren().remove(2);
        for(Player currentPlayer : players ){
            if(!Client.getUsername().equals(currentPlayer.getUsername())) {
                if(firstOpponent.equals(currentPlayer.getUsername())) {
                    setToggleButtonImages(currentPlayer.getGod().getName(),secondButton);
                    secondLabel.setText(currentPlayer.getUsername());
                }
                else {
                    setToggleButtonImages(currentPlayer.getGod().getName(),thirdButton);
                    thirdLabel.setText(currentPlayer.getUsername());
                }
            }
            else{
                setToggleButtonImages(currentPlayer.getGod().getName(),firstButton);
                firstLabel.setText(currentPlayer.getUsername());
            }
        }
        GUI.getGameStage().setScene(new Scene(selectPlayerMainPane));
    }

    public void setToggleButtonImages(String godName, ToggleButton button){
        selectedGods.add(godName);
        switch (godName) {
            case "Apollo":
                button.setStyle("-fx-background-image: url(Images/toggleButtonGods/Apollo.png);");
                break;
            case "Artemis":
                button.setStyle("-fx-background-image: url(Images/toggleButtonGods/Artemis.png);");
                break;
            case "Athena":
                button.setStyle("-fx-background-image: url(Images/toggleButtonGods/Athena.png);");
                break;
            case "Atlas":
                button.setStyle("-fx-background-image: url(Images/toggleButtonGods/Atlas.png);");
                break;
            case "Demeter":
                button.setStyle("-fx-background-image: url(Images/toggleButtonGods/Demeter.png);");
                break;
            case "Hephaestus":
                button.setStyle("-fx-background-image: url(Images/toggleButtonGods/Hephaestus.png);");
                break;
            case "Minotaur":
                button.setStyle("-fx-background-image: url(Images/toggleButtonGods/Minotaur.png);");
                break;
            case "Pan":
                button.setStyle("-fx-background-image: url(Images/toggleButtonGods/Pan.png);");
                break;
            case "Prometheus":
                button.setStyle("-fx-background-image: url(Images/toggleButtonGods/Prometheus.png);");
                break;
            case "Ares":
                button.setStyle("-fx-background-image: url(Images/toggleButtonGods/Ares.png);");
                break;
            case "Chronus":
                button.setStyle("-fx-background-image: url(Images/toggleButtonGods/Chronus.png);");
                break;
            case "Hera":
                button.setStyle("-fx-background-image: url(Images/toggleButtonGods/Hera.png);");
                break;
            case "Hestia":
                button.setStyle("-fx-background-image: url(Images/toggleButtonGods/Hestia.png);");
                break;
            case "Zeus":
                button.setStyle("-fx-background-image: url(Images/toggleButtonGods/Zeus.png);");
                break;
        }
    }

    public void handleFirstPlayerSelection(MouseEvent mouseEvent) {
        selectButton.setDisable(true);
        if(firstButton.isSelected()) Client.sendMessageToServer(new StartPlayerResponse(firstLabel.getText()));
        else if(secondButton.isSelected()) Client.sendMessageToServer(new StartPlayerResponse(secondLabel.getText()));
        else if(thirdButton.isSelected()) Client.sendMessageToServer(new StartPlayerResponse(thirdLabel.getText()));
        else selectButton.setDisable(false);
    }

    public String selectGodImage(boolean isOpponent, String godName) {
        switch(godName){
            case "Apollo":
                if (!isOpponent)return ("/Images/Gods/Apollo.png");
                else return ("/Images/SmallGods/Apollo.png");
            case "Artemis":
                if (!isOpponent) return ("/Images/Gods/Artemis.png");
                else return ("/Images/SmallGods/Artemis.png");
            case "Athena":
                if (!isOpponent) return ("/Images/Gods/Athena.png");
                else return ("/Images/SmallGods/Athena.png");
            case "Atlas":
                if (!isOpponent) return ("/Images/Gods/Atlas.png");
                else return ("/Images/SmallGods/Atlas.png");
            case "Chronus":
                if (!isOpponent) return ("/Images/Gods/Chronus.png");
                else return ("/Images/SmallGods/Chronus.png");
            case "Demeter":
                if (!isOpponent) return ("/Images/Gods/Demeter.png");
                else return ("/Images/SmallGods/Demeter.png");
            case "Hephaestus":
                if (!isOpponent) return ("/Images/Gods/Hephaestus.png");
                else return ("/Images/SmallGods/Hephaestus.png");
            case "Hera":
                if (!isOpponent) return ("/Images/Gods/Hera.png");
                else return ("/Images/SmallGods/Hera.png");
            case "Hestia":
                if (!isOpponent) return ("/Images/Gods/Hestia.png");
                else return ("/Images/SmallGods/Hestia.png");
            case "Minotaur":
                if (!isOpponent) return ("/Images/Gods/Minotaur.png");
                else return ("/Images/SmallGods/Minotaur.png");
            case "Pan":
                if (!isOpponent) return ("/Images/Gods/Pan.png");
                else return ("/Images/SmallGods/Pan.png");
            case "Prometheus":
                if (!isOpponent) return ("/Images/Gods/Prometheus.png");
                else return ("/Images/SmallGods/Prometheus.png");
            case "Zeus":
                if (!isOpponent) return ("/Images/Gods/Zeus.png");
                else return ("/Images/SmallGods/Zeus.png");
            case "Ares":
                if (!isOpponent) return ("/Images/Gods/Ares.png");
                else return ("/Images/SmallGods/Ares.png");

        }
        return null;
    }



    public static class Cell extends StackPane {
        protected int coordinateX, coordinateY;

        public Cell(int X, int Y, Space space) {
            coordinateX = X;
            coordinateY = Y;
            this.setStyle("-fx-border-color: black");
            this.setPrefSize(107, 107);
            Pane buildingPane = new Pane(), workerPane = new Pane();
            StackPane cellStack = new StackPane();
            cellStack.getChildren().addAll(buildingPane,workerPane);
            int height = space.getHeight();
            if (space.getHasDome()) {
                if (height == 1)buildingPane.setStyle("-fx-background-image: url(Images/Backgrounds/OnlyDome.png); -fx-background-size: 107px 107px");
                else if (height == 2)
                    buildingPane.setStyle("-fx-background-image: url(Images/Backgrounds/Height1WithDome.png); -fx-background-size: 107px 107px");
                else if (height == 3)
                    buildingPane.setStyle("-fx-background-image: url(Images/Backgrounds/Height2WithDome.png); -fx-background-size: 107px 107px");
                else
                    buildingPane.setStyle("-fx-background-image: url(Images/Backgrounds/CompleteTower.png); -fx-background-size: 107px 107px");
            }
            else {
                if (height == 1)
                    buildingPane.setStyle("-fx-background-image: url(Images/Backgrounds/Height1.png); -fx-background-size: 107px 107px");
                else if (height == 2)
                    buildingPane.setStyle("-fx-background-image: url(Images/Backgrounds/Height2.png); -fx-background-size: 107px 107px");
                else if (height == 3)
                    buildingPane.setStyle("-fx-background-image: url(Images/Backgrounds/Height3.png); -fx-background-size: 107px 107px");

                Worker worker = space.getWorkerInPlace();
                if (worker != null) {
                    int Color = worker.getColor();
                    char gender = worker.getGender();
                    if( Color == 1){
                        if (gender == 'f')workerPane.setStyle("-fx-background-image: url(Images/Backgrounds/blueFemale.png); -fx-background-size: 107px 107px");
                        else workerPane.setStyle("-fx-background-image: url(Images/Backgrounds/blueMale.png); -fx-background-size: 107px 107px");
                    }
                    else if (Color == 2){
                        if (gender == 'f')workerPane.setStyle("-fx-background-image: url(Images/Backgrounds/greenFemale.png); -fx-background-size: 107px 107px");
                        else workerPane.setStyle("-fx-background-image: url(Images/Backgrounds/greenMale.png); -fx-background-size: 107px 107px");
                    }
                    else {
                        if (gender == 'f')workerPane.setStyle("-fx-background-image: url(Images/Backgrounds/yellowFemale.png); -fx-background-size: 107px 107px");
                        else workerPane.setStyle("-fx-background-image: url(Images/Backgrounds/yellowMale.png); -fx-background-size: 107px 107px");
                    }
                }
            }
        }
    }
}