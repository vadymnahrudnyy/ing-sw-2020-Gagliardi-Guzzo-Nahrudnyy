package it.polimi.ingsw.PSP30.View;

import it.polimi.ingsw.PSP30.Client.Client;
import it.polimi.ingsw.PSP30.Model.Game;
import it.polimi.ingsw.PSP30.Model.God;
import it.polimi.ingsw.PSP30.Model.Player;
import it.polimi.ingsw.PSP30.View.Gui.StartScene;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class GUI implements UI,Runnable{
    private static Stage primaryStage;
    private static Stage gameStage;


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
        primaryStage.show();
    }

    @Override
    public void printLobbyStatus(int selectedLobby, int slotsOccupied) {

    }

    @Override
    public void startNotification() {

    }

    @Override
    public void showGodList(ArrayList<God> gods) {

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

    }

    @Override
    public void isWinner(String winner) {

    }

    @Override
    public void run() {
        StartScene.main();
    }
}
