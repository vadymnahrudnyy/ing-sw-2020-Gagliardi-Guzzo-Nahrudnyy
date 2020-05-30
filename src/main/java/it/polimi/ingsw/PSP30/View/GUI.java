package it.polimi.ingsw.PSP30.View;

import it.polimi.ingsw.PSP30.Client.Client;
import it.polimi.ingsw.PSP30.Messages.GameStartNotification;
import it.polimi.ingsw.PSP30.Model.Game;
import it.polimi.ingsw.PSP30.Model.God;
import it.polimi.ingsw.PSP30.Model.Player;
import it.polimi.ingsw.PSP30.View.Gui.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class GUI implements UI,Runnable{
    private static Stage primaryStage;
    private static Stage gameStage;
    private static Stage rulesStage;
    GodsController godsController = new GodsController();
    BoardController boardController = new BoardController();

    @FXML ImageView backButton2, backButton1, nextButton1, nextButton2;



    public static Stage getStage() {
        return primaryStage;
    }

    public static Stage getGameStage(){
        return gameStage;
    }

    public static void setGameStage(Stage newGameStage){
        gameStage = newGameStage;
    }

    public static void setPrimaryStage(Stage newPrimaryStage){
        primaryStage = newPrimaryStage;
    }

    public void createCLI() {
        primaryStage.close();
        UI newInterface = new CLI();
        Client.setUI(newInterface);
        Client.interruptClientThread();
    }
    public void createGUI() {
        UI newInterface = new GUI();
        Client.setUI(newInterface);
        Client.interruptClientThread();
    }

    /*
     * Method that runs when the user click on the Info&Rules button. It creates a new stages on which it load the first scene.
     */
    public void showRules() throws IOException {
        rulesStage=new Stage();
        rulesStage.initModality(Modality.APPLICATION_MODAL);
        rulesStage.initOwner(primaryStage);
        StackPane stackPane = FXMLLoader.load(LoginController.class.getClassLoader().getResource("Fxml/RulesScene1.fxml"));
        nextButton1=(ImageView) stackPane.getChildren().get(1);
        nextButton1.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            try {
                rulesScene2(rulesStage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        Scene scene = new Scene(stackPane);
        rulesStage.setScene(scene);
        rulesStage.show();

    }

    /*
    This method show the "Power Rules" of the Info&Rules page and manages mouse click on next button.
    */
    public void rulesScene1(Stage stage) throws IOException {
        StackPane stackPane = FXMLLoader.load(LoginController.class.getClassLoader().getResource("Fxml/RulesScene1.fxml"));
        nextButton1=(ImageView) stackPane.getChildren().get(1);
        nextButton1.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            try {
                rulesScene2(rulesStage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        Scene scene = new Scene(stackPane);
        stage.setScene(scene);
        stage.show();
    }


    /*
    This method show the glossary of the Info&Rules page and manages mouse click on next and back button.
    */
    public void rulesScene2(Stage stage) throws IOException {
        StackPane stackPane = FXMLLoader.load(LoginController.class.getClassLoader().getResource("Fxml/RulesScene2.fxml"));
        nextButton2=(ImageView) stackPane.getChildren().get(1);
        backButton2=(ImageView) stackPane.getChildren().get(2);
        nextButton2.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            try {
                rulesScene3(rulesStage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        backButton2.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            try {
                rulesScene1(rulesStage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        Scene scene = new Scene(stackPane);
        stage.setScene(scene);
        stage.show();
    }

    /*
    This method show the "How to Play" of the Info&Rules page and manages mouse click on back button.
    */
    public void rulesScene3(Stage stage) throws IOException {
        StackPane stackPane = FXMLLoader.load(LoginController.class.getClassLoader().getResource("Fxml/RulesScene3.fxml"));
        backButton1=(ImageView) stackPane.getChildren().get(1);
        backButton1.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            try {
                rulesScene2(rulesStage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        Scene dialogScene = new Scene(stackPane);
        stage.setScene(dialogScene);
        stage.show();
    }

    @Override
    public void gameInfo() {

    }

    @Override
    public void chooseServerAddress() {
        Platform.runLater(this::showServerAddress);
        try {
            Client.getClientThread().sleep(12312312);
        } catch (InterruptedException e) {
            System.out.println("Server address inserted" );
        }
    }

    public void showServerAddress(){
        try{
            Parent addressScene = FXMLLoader.load(getClass().getClassLoader().getResource("Fxml/AddressScene.fxml"));
            Scene scene1 = new Scene(addressScene);
            primaryStage.setScene(scene1);
            primaryStage.show();
        }catch (IOException e){
            System.out.println();
        }
    }

    @Override
    public void errorServerAddress() {

    }

    @Override
    public void chooseUsername() {
        Platform.runLater(this::showUsername);
    }
    public void showUsername(){
        Parent usernameScene = null;
        try {
            usernameScene = FXMLLoader.load(getClass().getClassLoader().getResource("Fxml/UsernameScene.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene2 = new Scene(usernameScene);
        primaryStage.setScene(scene2);
        primaryStage.show();
    }

    @Override
    public void usernameError() {

    }

    @Override
    public void chooseNumPlayers() {
        Platform.runLater(this::showChooseNumPlayers);
    }
    public void showChooseNumPlayers() {
        Parent numPlayerScene = null;
        try {
            numPlayerScene = FXMLLoader.load(getClass().getClassLoader().getResource("Fxml/ChoosePlayerScene.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene2 = new Scene(numPlayerScene);
        primaryStage.setScene(scene2);
    }


    @Override
    public void printLobbyStatus(int selectedLobby, int slotsOccupied) {
    }

    @Override
    public void startNotification(GameStartNotification message) {
        Runnable closePrimaryStage = () -> {if(primaryStage!=null)primaryStage.close();};
        Platform.runLater(closePrimaryStage);
        Runnable initializeBoard = () -> boardController.initializeBoard(message);
        Platform.runLater(initializeBoard);
    }

    @Override
    public void showGodList(ArrayList<God> gods) {
        Runnable showGodSelector = () -> {
            try {
                godsController.showGodSelector(gods);
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        Platform.runLater(showGodSelector);
    }

    @Override
    public void chooseGameGods() {

    }

    @Override
    public void printAllPlayers(Player[] players) {

    }

    @Override
    public void playerError() {

    }

    @Override
    public void chooseFirstPlayer() {

    }

    @Override
    public void chooseGod(ArrayList<God> godList, ArrayList<God> unavailableList) {

    }

    @Override
    public void godChoiceError() {

    }

    @Override
    public void showLastGod(ArrayList<God> godList, God lastGod) {

    }

    @Override
    public void placeWorkerInSpace(int currentWorker, boolean[][] allowedPositions) {

    }

    @Override
    public void chooseWorker() {

    }

    @Override
    public void workerChosenError() {

    }

    @Override
    public boolean confirmChoice() {
        return false;
    }

    @Override
    public void moveWorker(boolean[][] allowedMoves) {

    }

    @Override
    public void otherWorker() {

    }

    @Override
    public void moveOtherWorker(boolean[][] allowedMoves) {

    }

    @Override
    public void printPossibleAction(boolean[][] allowed) {

    }

    @Override
    public void changeWorker(boolean canChangeWorker) {

    }

    @Override
    public void buildTower(boolean[][] allowedBuild) {

    }

    @Override
    public void askPowerUsage() {

    }

    @Override
    public void chooseRemoval(boolean[][] allowedToRemove) {

    }

    @Override
    public void noPossibleMoves() {

    }

    @Override
    public void invalidMove() {

    }

    @Override
    public void printCurrentStatus(Game updatedGame) {

    }

    @Override
    public void printCurrentBoard(Game updatedGame) {
        Runnable updateGameStatus = () -> boardController.updateGameBoard(updatedGame);
        Platform.runLater(updateGameStatus);
    }

    @Override
    public void isWinner(String winner) {
        Runnable endGame = () -> {
            try {
                EndSceneController.winner(winner);
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        Platform.runLater(endGame);

    }

    @Override
    public void run() {
        StartScene.main();
    }
}








