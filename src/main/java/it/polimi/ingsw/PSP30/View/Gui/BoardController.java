package it.polimi.ingsw.PSP30.View.Gui;

import it.polimi.ingsw.PSP30.Messages.GameStartNotification;
import it.polimi.ingsw.PSP30.Model.IslandBoard;
import it.polimi.ingsw.PSP30.Model.Player;
import it.polimi.ingsw.PSP30.Model.Space;
import it.polimi.ingsw.PSP30.Model.Worker;
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
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Stack;


public class BoardController{



    @FXML private static BorderPane mainPane;
    @FXML private static StackPane leftPane, centerPane;
    @FXML private static Label label1, label2, label3;
    @FXML private static VBox vBox;

    private static Cell[][] cell = new Cell[5][5];
    private static GridPane gridPane=new GridPane();

    public static void initializeBoard(GameStartNotification message) {
        GUI.setGameStage(new Stage());
        try{
            mainPane = FXMLLoader.load(BoardController.class.getClassLoader().getResource("Fxml/Board.fxml"));
        }catch (IOException e){
            e.printStackTrace();
        }
        centerPane=(StackPane) mainPane.getCenter();
        leftPane = (StackPane) mainPane.getLeft();
        vBox = (VBox)leftPane.getChildren().get(1);
        label1 = (Label) vBox.getChildren().get(0);
        label2 = (Label) vBox.getChildren().get(1);
        label3 = (Label) vBox.getChildren().get(2);
        Player[] playersUsername = message.getGame().getPlayers();
        label1.setText(playersUsername[0].getUsername());
        label2.setText(playersUsername[1].getUsername());
        if(message.getGame().getNumPlayers() == 3) label3.setText(playersUsername[2].getUsername());
        else label3.setVisible(false);
        IslandBoard board = message.getGame().getGameBoard();

        for(int X = 0; X < IslandBoard.TABLE_DIMENSION;X++)
            for (int Y = 0; Y < IslandBoard.TABLE_DIMENSION; Y++){
                Space currentSpace = board.getSpace(X+1,Y+1);
                cell[X][Y]=new Cell(X, Y, currentSpace);
                gridPane.add(cell[X][Y],Y,X);
            }
        centerPane.getChildren().add(gridPane);
        gridPane.setAlignment(Pos.CENTER);
        Scene scene = new Scene(mainPane);
        GUI.getGameStage().setScene(scene);
        GUI.getGameStage().show();
    }

public static class Cell extends StackPane {
        protected int coordinateX, coordinateY;

        public Cell(int X, int Y, Space space) {
            coordinateX = X;
            coordinateY = Y;
            this.setPrefSize(107, 107);
            Pane buildingPane = new Pane(), workerPane = new Pane();
            StackPane cellStack = new StackPane();
            cellStack.getChildren().addAll(buildingPane,workerPane);
            int height = space.getHeight();
            if (space.getHasDome()) {
                if (height == 1)
                    buildingPane.setStyle("-fx-background-image: url(Images/Backgrounds/OnlyDome.png); -fx-background-size: 107px 107px");
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
