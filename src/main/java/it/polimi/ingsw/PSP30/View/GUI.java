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
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.ArrayList;

/**
 * GUI class implements the UI interface and defines all the features for play with graphic user interface
 */
public class GUI implements UI,Runnable{
    private static Stage primaryStage;
    private static Stage gameStage;
    private static Stage rulesStage;
    GodsController godsController = new GodsController();
    BoardController boardController = new BoardController();
    LobbyController lobbyController = new LobbyController();
    AlertsController alertsController=new AlertsController();
    GodPowerController godPowerController = new GodPowerController();

    private static final int GAME_WINDOW_CLOSED = 3005;

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

    /**
     * This method creates a new instance of the CLI class
     */
    public void createCLI() {
        primaryStage.close();
        UI newInterface = new CLI();
        Client.setUI(newInterface);
        Client.interruptClientThread();
    }

    /**
     * This method creates a new instance of the GUI class
     */
    public void createGUI() {
        UI newInterface = new GUI();
        Client.setUI(newInterface);
        Client.interruptClientThread();
    }


    /**
     * This method runs when the user click on the Info&Rules button. It creates a new stages on which it loads the first scene.
     * @throws IOException when an error occurred in loading fxml file
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

    /**
     *This method shows the "Power Rules" of the Info&Rules page and manages mouse click on next button.
     * @param stage stage in which is loaded the scene
     * @throws IOException when an error occurred in loading fxml file
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


    /**
     * This method show the glossary of the Info&Rules page and manages mouse click on next and back button.
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
            }
            catch (IOException e) {
                e.printStackTrace();
            } });
        Scene scene = new Scene(stackPane);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method shows the "How to Play" of the Info&Rules page and manages mouse click on back button.
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
    public void chooseServerAddress() {
        Platform.runLater(this::showServerAddress);
        try {
            Client.getClientThread().sleep(12312312);
        } catch (InterruptedException e) {
            System.out.println("Server address inserted" );
        }
    }

    /**
     * This method shows AddressScene in which the player have to insert the address of the server he wants to connect to
     */
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
      Runnable serverAddressError = () -> { alertsController.showServerAddressError();};
      Platform.runLater(serverAddressError);
    }

    @Override
    public void chooseUsername() {
        Platform.runLater(this::showUsername);
    }

    /**
     * This method shows usernameScene in which the player have to insert his username
     */
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
        Runnable usernameError = () -> { alertsController.showUsernameError();};
        Platform.runLater(usernameError);
    }

    @Override
    public void chooseNumPlayers() {
        Platform.runLater(this::showChooseNumPlayers);
    }

    /**
     * This method shows numPlayerScene where the player have to choose how many players he wants to be in the game (2 or 3)
     */
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
    public void printLobbyStatus(int selectedLobby, int slotsOccupied, ArrayList<String> usernames) {
        Runnable showLobby = () ->{
            try {
                if (primaryStage != null) primaryStage.close();
                if (gameStage == null) {
                    gameStage = new Stage();
                    gameStage.setResizable(false);
                    gameStage.setOnCloseRequest(GUI::closeApp);
                }
                lobbyController.showLobby(usernames);
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        Platform.runLater(showLobby);
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
    public void chooseGameGods() { }

    @Override
    public void printAllPlayers(Player[] players) {
        Runnable firstPlayerSelection = () -> {
            try {
                boardController.showSelectFirstPlayer(players);
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        Platform.runLater(firstPlayerSelection);}

    @Override
    public void playerError() { }

    @Override
    public void chooseFirstPlayer() { }

    @Override
    public void chooseGod(ArrayList<God> godList, ArrayList<God> unavailableList) {
        Runnable singleGodSelection = () -> {
            try {
                godsController.showSingleGodSelector(godList,unavailableList);
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        Platform.runLater(singleGodSelection);
    }

    @Override
    public void godChoiceError() { }

    @Override
    public void showLastGod(ArrayList<God> godList, God lastGod) { }

    @Override
    public void placeWorkerInSpace(int currentWorker, boolean[][] allowedPositions) {
        Runnable workerPositionRequest = () -> boardController.workerPositionRequest(currentWorker);
        Platform.runLater(workerPositionRequest);
    }

    @Override
    public void chooseWorker() {
        Runnable chooseWorker = () -> boardController.selectWorkerRequest();
        Platform.runLater(chooseWorker);
    }

    @Override
    public void workerChosenError() {
        Runnable chooseWorkerError = () -> alertsController.showWorkerSelectedError();
        Platform.runLater(chooseWorkerError);
    }

    @Override
    public boolean confirmChoice() {
        return false;
    }

    @Override
    public void moveWorker(boolean[][] allowedMoves) {
        Runnable moveWorker = () -> boardController.moveRequest(allowedMoves);
        Platform.runLater(moveWorker);
    }

    @Override
    public void otherWorker() {
        Runnable workerCannotMoveError = () -> alertsController.showWorkerCannotMoveError();
        Platform.runLater(workerCannotMoveError);
    }

    @Override
    public void moveOtherWorker(boolean[][] allowedMoves) {
        Runnable moveWorker = () -> boardController.moveRequest(allowedMoves);
        Platform.runLater(moveWorker);
    }

    @Override
    public void printPossibleAction(boolean[][] allowed) { }

    @Override
    public void changeWorker(boolean canChangeWorker) {
        Runnable setChange = () -> boardController.setCanChangeWorker(canChangeWorker);
        Platform.runLater(setChange);
    }

    @Override
    public void buildTower(boolean[][] allowedBuild) {
        Runnable buildTower = () -> boardController.buildRequest(allowedBuild);
        Platform.runLater(buildTower);
    }

    @Override
    public void askPowerUsage() {
        Runnable powerStage = () -> {godPowerController.showScene();};
        Platform.runLater(powerStage);
    }

    @Override
    public void chooseRemoval(boolean[][] allowedToRemove) {
        Runnable blockRemoval = () -> {boardController.blockRemoveRequest(allowedToRemove);};
        Platform.runLater(blockRemoval);
    }

    @Override
    public void noPossibleMoves() {
        Runnable noPossibleMovesError = () -> alertsController.showNoPossibleMovesError();
        Platform.runLater(noPossibleMovesError);
    }

    @Override
    public void invalidMove() {
        Runnable invalidMoveError = () -> {
            alertsController.showInvalidMoveError();
            boardController.setMoveRequest(true);
        };
        Platform.runLater(invalidMoveError);
    }

    @Override
    public void printCurrentStatus(Game updatedGame) {}

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
                System.out.println("Failed to load end game popup");
            }
        };
        Platform.runLater(endGame);

    }

    @Override
    public void opponentDisconnected() {
        Runnable opponentDisconnectedNotification = EndSceneController::opponentDisconnection;
        Platform.runLater(opponentDisconnectedNotification);
    }

    @Override
    public void run() {
        StartScene.main();
    }

    public static void closeApp(WindowEvent t){
        Platform.exit();
        System.exit(GAME_WINDOW_CLOSED);
    }

}
