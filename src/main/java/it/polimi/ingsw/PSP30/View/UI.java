package it.polimi.ingsw.PSP30.View;

import it.polimi.ingsw.PSP30.Model.Game;
import it.polimi.ingsw.PSP30.Model.God;
import it.polimi.ingsw.PSP30.Model.Player;

import java.util.ArrayList;


/**
 * Interface for standardize the behaviors of the various UI (CLI and GUI)
 */

public interface UI {

    /**
     * This method shows all the significant information about the game
     */
    void gameInfo();


    /**
     * This method asks the player the address of the server
     */
    void chooseServerAddress();

    /**
     * This method  manages not valid username error
     */
    void errorServerAddress();


    /**
     * This method asks to choose the username
     */
    void chooseUsername();


    /**
     * This method manages not valid username error
     */
     void usernameError();

    /**
     * This method asks to choose how many players are wanted in the game
     */
    void chooseNumPlayers();

    /**
     * This method shows the lobby status
     * @param selectedLobby number of the lobby, it can be 2 or 3 depending on the selected players
     * @param slotsOccupied how many players are in the lobby at the moment
     */
    void printLobbyStatus(int selectedLobby, int slotsOccupied);

    /**
     *  This method notifies the game can start (the lobby is full)
     */
    void startNotification();

    /**
     * This method shows all the God cards of the deck
     * @param gods ArrayList of Gods
     */
    void showGodList(ArrayList<God> gods);


    /**
     * This method manages the choice of the Gods that will be used in the game
     */
    void chooseGameGods();

    /**
     * This method shows all the players of the lobby
     * @param players array of Players
     */
    void printAllPlayers(Player[] players);


    /**
     * This method manages not valid first player choice
     */
    void playerError();


    /**
     * This method manages the choice of the first player
     */
    void chooseFirstPlayer();

    /**
     * This method shows the God cards used in the game and those already chosen
     * @param godList ArrayList of all the Gods chosen for the game
     * @param unavailableList ArrayList of Gods already chosen, so not available
     */
    void chooseGod(ArrayList<God> godList, ArrayList<God> unavailableList);


    /**
     * This method manages not valid god choice
     */
    void godChoiceError();


    /**
     * This method shows the God cards used in the game and the last God available
     * @param godList ArrayList of all the Gods chosen for the game
     * @param lastGod last God available
     */
    void showLastGod(ArrayList<God> godList, God lastGod);


    /**
     * This method shows where the player can put the selected worker and then he inserts the chosen coordinate
     */
    void placeWorkerInSpace(int currentWorker, boolean[][] allowedPositions);


    /**
     * This method asks the player of the position in which wants to move the selected worker
     */
    void chooseWorker();


    /**
     * This method manages not valid worker selection
     */
    void workerChosenError();

    /**
     * This method asks the player if he wants to choose another worker
     * @return true if the player chooses to confirm the same worker, false if he wants to change it
     */
    boolean confirmChoice();

    /**
     * This method asks the player to choose where he wants to move the selected worker
     */
    void moveWorker(boolean[][] allowedMoves);

    /**
     * This method warns the player that the worker chosen could't be moved, so he will move the other worker
     */
    void otherWorker();

    /**
     * This method asks the player to choose where he wants to move the other(selected) worker
     */
    void moveOtherWorker(boolean[][] allowedMoves);

    /**
     * This method shows all the possible moves of the respective worker
     * @param allowed the boolean matrix of the allowed moves
     */
    void printPossibleAction(boolean[][] allowed);

    /**
     * This method gives to the player the chance to change the worker selected
     * and allows the player to choose where he wants to move his worker
     */
    void changeWorker(boolean canChangeWorker);

    /**
     * This method asks the player in which position he wants to build
     */
    void buildTower(boolean[][] allowedBuild);

    /**
     * This method asks the player if wants to use the power
     */
    void askPowerUsage();

    /**
     * This method asks the player in which position he wants to remove a building
     */
    void chooseRemoval(boolean[][] allowedToRemove);

    /**
     * This method warns there aren't more possible moves for the player
     */
    void noPossibleMoves();

    /**
     * This method warns the chosen move is not valid
     */
    void invalidMove();

    /**
     * This method shows the legend of the players in the game: username, color of the workers and God chosen
     * @param updatedGame is the current instance of the Game class
     */
    void printCurrentStatus(Game updatedGame);

    /**
     * This method shows the current status of the game board
     * @param updatedGame is the current instance of the Game class
     */
    void printCurrentBoard(Game updatedGame);

    /**
     * This method shows the name of the winner
     * @param winner winner's name
     */
    void isWinner(String winner);

}



