package it.polimi.ingsw.PSP30.View.Gui;

import it.polimi.ingsw.PSP30.Model.*;
import it.polimi.ingsw.PSP30.View.GUI;
import it.polimi.ingsw.PSP30.Messages.*;


import javafx.fxml.FXML;
import javafx.stage.Modality;
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


    @FXML private StackPane mainPane, playerPane, firstOpponentPane, secondOpponentPane;
    @FXML private Label playerUsername, firstOpponentUsername, secondOpponentUsername, messagesTag;
    @FXML private ImageView playerGod, secondOpponentGod, firstOpponentGod, exitButton, rulesButton;
    @FXML private VBox opponentsPane;

    @FXML private StackPane selectPlayerMainPane,firstPlayer, secondPlayer, thirdPlayer ;
    @FXML private BorderPane selectPlayerBorderPane;
    @FXML private ToggleButton firstButton, secondButton, thirdButton;
    @FXML private Label firstLabel, secondLabel, thirdLabel;
    @FXML private HBox hBox;
    @FXML private ImageView selectButton;

    @FXML ImageView backButton2, backButton1, nextButton1, nextButton2;
    private static Stage rulesStage;

    private static Cell[][] cell = new Cell[5][5];
    private static GridPane gridPane=new GridPane();
    private static Scene boardScene;

    private static String firstOpponent;
    private static String secondOpponent;

    private static ToggleGroup toggleGroup=new ToggleGroup();
    private static final ArrayList<String> selectedGods = new ArrayList<>();

    private static boolean[][] allowedMoves;
    private static boolean workerPositionRequest = false, selectWorkerRequest = false, moveRequest = false, buildRequest = false, removeRequest = false;

    /**
     * This method initializes the Board scene with all the elements of the GameStage
     * @param message GameStartNotification which contains the current status of the game
     */
    public void initializeBoard(GameStartNotification message) {
        if (GUI.getGameStage() == null) GUI.setGameStage(new Stage());
        GUI.getGameStage().setResizable(false);
        try{
            mainPane = FXMLLoader.load(BoardController.class.getClassLoader().getResource("Fxml/Board.fxml"));
        }catch (IOException e){
            e.printStackTrace();
        }

        messagesTag= (Label) mainPane.getChildren().get(6);
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
        gridPane.setMaxSize(550,550);
        mainPane.getChildren().add(gridPane);
        gridPane.setAlignment(Pos.CENTER);

        boardScene = new Scene(mainPane);
        GUI.getGameStage().setScene(boardScene);
        GUI.getGameStage().show();
    }

    /**
     * This method updates the Board scene with all the elements belonging to the current status of the game (workers, buildings and info about the players)
     * @param updatedGame contains the current status of the game
     */
    public void updateGameBoard(Game updatedGame){
        messagesTag.setText("");
        GUI.getGameStage().setScene(boardScene);
        Player currentPlayer = updatedGame.getCurrentPlayer();
        IslandBoard currentBoard = updatedGame.getGameBoard();
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

        resetCurrentPlayerDropshadow();
        if (Client.getUsername().equals(currentPlayer.getUsername())) playerGod.setStyle("-fx-effect: dropshadow(gaussian, #d40027, 30, 0.5, 0, 0)");
        else if (firstOpponent.equals(currentPlayer.getUsername())) firstOpponentGod.setStyle("-fx-effect: dropshadow(gaussian, #d40027, 30, 0.5, 0, 0)");
        else if (secondOpponent.equals(currentPlayer.getUsername())) secondOpponentGod.setStyle("-fx-effect: dropshadow(gaussian, #d40027, 30, 0.5, 0, 0)");

        GridPane newGridPane = new GridPane();
        for(int X = 0; X < IslandBoard.TABLE_DIMENSION;X++)
            for (int Y = 0; Y < IslandBoard.TABLE_DIMENSION; Y++){
                Space currentSpace = currentBoard.getSpace(X+1,Y+1);
                cell[X][Y] = new Cell(X, Y, currentSpace);
                newGridPane.add(cell[X][Y],Y,X);
            }
        newGridPane.setMaxSize(550,550);
        mainPane.getChildren().remove(gridPane);
        mainPane.getChildren().add(newGridPane);
        newGridPane.setAlignment(Pos.CENTER);
        gridPane = newGridPane;
    }

    /**
     * This method resets the dropshadow effect when isn't anymore the player's turn
     */
    public void resetCurrentPlayerDropshadow(){
        playerGod.setStyle("");
        firstOpponentGod.setStyle("");
        secondOpponentGod.setStyle("");
    }

    /**
     * This method is used to set the value of moveRequest variable
     */
    public void setMoveRequest(boolean value){
        moveRequest = value;
    }

    /**
     * This method shows the pane for the choice of the beginning player
     * @param players array which contains the names of the players
     * @throws IOException when an error occurred in loading fxml file
     */
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

    /**
     * This method manages to show the correct image for every god
     * @param godName god's name
     * @param button in which the god's image will be added
     */
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

    /**
     * This method handles the selection of the first player
     * @param mouseEvent mouse click over one of the cards
     */
    public void handleFirstPlayerSelection(MouseEvent mouseEvent) {
        selectButton.setDisable(true);
        if(firstButton.isSelected()) Client.sendMessageToServer(new StartPlayerResponse(firstLabel.getText()));
        else if(secondButton.isSelected()) Client.sendMessageToServer(new StartPlayerResponse(secondLabel.getText()));
        else if(thirdButton.isSelected()) Client.sendMessageToServer(new StartPlayerResponse(thirdLabel.getText()));
        else selectButton.setDisable(false);
    }

    /**
     * This method sets the images of the God cards around the board
     * @param isOpponent true if the god belongs to the opponent of the current player, otherwise false
     * @param godName god's name
     * @return the path of the god's image to set in the scene
     */
    public String selectGodImage(boolean isOpponent, String godName) {
        switch(godName){
            case "Apollo":
                if (!isOpponent)return ("/Images/toggleButtonGods/Apollo.png");
                else return ("/Images/SmallGods/Apollo.png");
            case "Artemis":
                if (!isOpponent) return ("/Images/toggleButtonGods/Artemis.png");
                else return ("/Images/SmallGods/Artemis.png");
            case "Athena":
                if (!isOpponent) return ("/Images/toggleButtonGods/Athena.png");
                else return ("/Images/SmallGods/Athena.png");
            case "Atlas":
                if (!isOpponent) return ("/Images/toggleButtonGods/Atlas.png");
                else return ("/Images/SmallGods/Atlas.png");
            case "Chronus":
                if (!isOpponent) return ("/Images/toggleButtonGods/Chronus.png");
                else return ("/Images/SmallGods/Chronos.png");
            case "Demeter":
                if (!isOpponent) return ("/Images/toggleButtonGods/Demeter.png");
                else return ("/Images/SmallGods/Demeter.png");
            case "Hephaestus":
                if (!isOpponent) return ("/Images/toggleButtonGods/Hephaestus.png");
                else return ("/Images/SmallGods/Hephaestus.png");
            case "Hera":
                if (!isOpponent) return ("/Images/toggleButtonGods/Hera.png");
                else return ("/Images/SmallGods/Hera.png");
            case "Hestia":
                if (!isOpponent) return ("/Images/toggleButtonGods/Hestia.png");
                else return ("/Images/SmallGods/Hestia.png");
            case "Minotaur":
                if (!isOpponent) return ("/Images/toggleButtonGods/Minotaur.png");
                else return ("/Images/SmallGods/Minotaur.png");
            case "Pan":
                if (!isOpponent) return ("/Images/toggleButtonGods/Pan.png");
                else return ("/Images/SmallGods/Pan.png");
            case "Prometheus":
                if (!isOpponent) return ("/Images/toggleButtonGods/Prometheus.png");
                else return ("/Images/SmallGods/Prometheus.png");
            case "Zeus":
                if (!isOpponent) return ("/Images/toggleButtonGods/Zeus.png");
                else return ("/Images/SmallGods/Zeus.png");
            case "Ares":
                if (!isOpponent) return ("/Images/toggleButtonGods/Ares.png");
                else return ("/Images/SmallGods/Ares.png");

        }
        return null;
    }

    /**
     * This method shows the request to select where to place the indicated worker (during the game setup)
     * @param workerIndex integer index of the worker
     */
    public void workerPositionRequest(int workerIndex){
        if (workerIndex == 1) messagesTag.setText("Select first worker's position");
        else messagesTag.setText("Select second worker's position");
        workerPositionRequest = true;
    }

    /**
     * This method shows the request to select the worker to move
     */
    public void selectWorkerRequest(){
        selectWorkerRequest = true;
        messagesTag.setText("Select the worker you want to move");
    }

    /**
     * This method shows the request to select where to move the worker previously selected
     * @param allowedMove the boolean matrix of the allowed moves
     */
    public void moveRequest(boolean[][] allowedMove){
        moveRequest = true;
        allowedMoves = allowedMove;
        for(int X = 0; X < IslandBoard.TABLE_DIMENSION;X++)
            for (int Y = 0; Y < IslandBoard.TABLE_DIMENSION; Y++){
                if(allowedMove[X][Y])cell[X][Y].setAllowed();
            }
        messagesTag.setText("Select the position you want to move to");
    }

    /**
     * This method shows the request to select where to build
     * @param allowedMove the boolean matrix of the spaces where the player is allowed to build
     */
    public void buildRequest(boolean[][] allowedMove){
        buildRequest = true;
        allowedMoves = allowedMove;
        for(int X = 0; X < IslandBoard.TABLE_DIMENSION;X++)
            for (int Y = 0; Y < IslandBoard.TABLE_DIMENSION; Y++){
                if(allowedMove[X][Y])cell[X][Y].setAllowed();
            }
        messagesTag.setText("Select the space you want to build in");
    }

    /**
     * This method shows the request to select where to remove a building
     * @param allowedRemovals the boolean matrix of the spaces where the player is allowed to remove a building
     */
    public void blockRemoveRequest(boolean[][] allowedRemovals){
        removeRequest = true;
        for(int X = 0; X < IslandBoard.TABLE_DIMENSION;X++)
            for (int Y = 0; Y < IslandBoard.TABLE_DIMENSION; Y++){
                if(allowedRemovals[X][Y])cell[X][Y].setAllowed();
            }
        messagesTag.setText("Select the block you want to remove");
    }

    /**
     * This method shows the "Using God Power" of the Info&Rules page and manages mouse click on next button
     * @param mouseEvent mouse click on the Info&Rules button
     * @throws IOException when an error occurred in loading fxml file
     */
    public void showInfoPane(MouseEvent mouseEvent) throws IOException {
        rulesStage=new Stage();
        rulesStage.initModality(Modality.APPLICATION_MODAL);
        rulesStage.initOwner(GUI.getGameStage());
        StackPane stackPane = FXMLLoader.load(LoginController.class.getClassLoader().getResource("Fxml/RulesBoard1.fxml"));
        nextButton1=(ImageView) stackPane.getChildren().get(1);
        nextButton1.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            try {
                rulesScene2();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        Scene scene = new Scene(stackPane);
        rulesStage.setScene(scene);
        rulesStage.show();
    }

    /**
     * This method shows the "Power Rules" of the Info&Rules page and manages mouse click on next button
     * @throws IOException when an error occurred in loading fxml file
     */
    public void rulesScene1() throws IOException {
        StackPane stackPane = FXMLLoader.load(LoginController.class.getClassLoader().getResource("Fxml/RulesBoard1.fxml"));
        nextButton1=(ImageView) stackPane.getChildren().get(1);
        nextButton1.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            try {
                rulesScene2();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        Scene scene = new Scene(stackPane);
        rulesStage.setScene(scene);
    }

    /**
     * This method shows the glossary of the Info&Rules page and manages mouse click on next and back button
     * @throws IOException when an error occurred in loading fxml file
     */
    public void rulesScene2() throws IOException {
        StackPane stackPane = FXMLLoader.load(LoginController.class.getClassLoader().getResource("Fxml/RulesBoard2.fxml"));
        nextButton2=(ImageView) stackPane.getChildren().get(1);
        backButton2=(ImageView) stackPane.getChildren().get(2);
        nextButton2.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            try {
                rulesScene3();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        backButton2.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            try {
                rulesScene1();
            }
            catch (IOException e) {
                e.printStackTrace();
            } });
        Scene scene = new Scene(stackPane);
        rulesStage.setScene(scene);
    }

    /**
     * This method shows the "How to Play" of the Info&Rules page and manages mouse click on back button
     * @throws IOException when an error occurred in loading fxml file
     */
    public void rulesScene3() throws IOException {
        StackPane stackPane = FXMLLoader.load(LoginController.class.getClassLoader().getResource("Fxml/RulesBoard3.fxml"));
        backButton1=(ImageView) stackPane.getChildren().get(1);
        backButton1.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            try {
                rulesScene2();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        Scene dialogScene = new Scene(stackPane);
        rulesStage.setScene(dialogScene);
    }

    /**
     *
     * @param event mouse click on the exit button
     */
    public void disconnectButton(MouseEvent event){
        Client.sendMessageToServer(new Disconnection());
        System.exit(0);
    }

    /**
     * This method manages the content of the cells of the board (workers and buildings) and shows the requests during the player's turn
     */
    public static class Cell extends StackPane {
        protected int coordinateX, coordinateY;
        Pane allowedMovePane = new Pane();

        public Cell(int X, int Y, Space space) {
            this.setMaxSize(107,107);
            coordinateX = X+1;
            coordinateY = Y+1;
            this.setPrefSize(107, 107);
            Pane buildingPane = new Pane(), workerPane = new Pane(), selectedCellPane = new Pane();
            StackPane cellStack = new StackPane();
            cellStack.getChildren().addAll(buildingPane,workerPane,allowedMovePane,selectedCellPane);
            this.getChildren().add(cellStack);
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
                        if (gender == 'f')workerPane.setStyle("-fx-background-image: url(Images/Backgrounds/purpleFemale.png); -fx-background-size: 107px 107px");
                        else workerPane.setStyle("-fx-background-image: url(Images/Backgrounds/purpleMale.png); -fx-background-size: 107px 107px");
                    }
                    else {
                        if (gender == 'f')workerPane.setStyle("-fx-background-image: url(Images/Backgrounds/yellowFemale.png); -fx-background-size: 107px 107px");
                        else workerPane.setStyle("-fx-background-image: url(Images/Backgrounds/yellowMale.png); -fx-background-size: 107px 107px");
                    }
                }
            }
            this.addEventHandler(MouseEvent.MOUSE_ENTERED, e->{
                selectedCellPane.setStyle("-fx-background-color: #ffad00; -fx-opacity: 0.5");
            });
            this.addEventHandler(MouseEvent.MOUSE_EXITED, e->{
                selectedCellPane.setStyle("-fx-background-color: transparent");
            });
            this.addEventHandler(MouseEvent.MOUSE_CLICKED, e->{
                System.out.println("click on X: "+coordinateX +"  Y: " + coordinateY);
                if (workerPositionRequest) {
                    workerPositionRequest = false;

                    Client.sendMessageToServer(new WorkerPositionResponse(coordinateX,coordinateY));
                }
                if (selectWorkerRequest){
                    selectWorkerRequest = false;
                    Client.sendMessageToServer(new SelectWorkerResponse(coordinateX,coordinateY));
                }
                if (moveRequest){
                    moveRequest = false;
                    Client.sendMessageToServer(new MoveResponse(coordinateX,coordinateY));
                }
                if (buildRequest){
                    buildRequest = false;
                    Client.sendMessageToServer(new BuildResponse(coordinateX,coordinateY));
                }
                if (removeRequest){
                    removeRequest = false;
                    Client.sendMessageToServer(new BlockRemovalResponse(coordinateX,coordinateY));
                }
            });
        }

        /**
         * This method sets the color to the cells in which the player can do the action
         */
        public void setAllowed(){
            allowedMovePane.setStyle("-fx-background-color: #49eeff; -fx-opacity: 0.5");
        }
    }

}
