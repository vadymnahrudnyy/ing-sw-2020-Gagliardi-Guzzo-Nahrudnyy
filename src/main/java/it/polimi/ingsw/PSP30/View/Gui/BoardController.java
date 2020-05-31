package it.polimi.ingsw.PSP30.View.Gui;

import it.polimi.ingsw.PSP30.Client.Client;
import it.polimi.ingsw.PSP30.Messages.GameStartNotification;
import it.polimi.ingsw.PSP30.Model.*;
import it.polimi.ingsw.PSP30.View.GUI;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Cell;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.w3c.dom.events.MouseEvent;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Stack;


public class BoardController{

    @FXML private StackPane mainPane, playerPane, firstOpponentPane, secondOpponentPane;
    @FXML private Label playerUsername, firstOpponentUsername, secondOpponentUsername;
    @FXML private ImageView playerGod, secondOpponentGod, firstOpponentGod, exitButton, rulesButton;
    @FXML private VBox opponentsPane;


    private static Cell[][] cell = new Cell[5][5];
    private static GridPane gridPane=new GridPane();
    private static Scene boardScene;

    private static String firstOpponent;
    private static String secondOpponent;

    public void initializeBoard(GameStartNotification message) {
        GUI.setGameStage(new Stage());
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
                    playerGod.setImage(new Image(selectGodImage(false, gamePlayer.getGod().getName())));
                    playerGod.setVisible(true);
                }
                else if (firstOpponent.equals(gamePlayer.getUsername())) {
                firstOpponentGod.setImage(new Image(selectGodImage(true, gamePlayer.getGod().getName())));
                firstOpponentGod.setVisible(true);
                }
                else {
                    secondOpponentGod.setImage(new Image(selectGodImage(true, gamePlayer.getGod().getName())));
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