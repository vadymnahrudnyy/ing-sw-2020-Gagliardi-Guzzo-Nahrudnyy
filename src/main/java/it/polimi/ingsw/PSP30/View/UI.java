package it.polimi.ingsw.PSP30.View;

import it.polimi.ingsw.PSP30.Messages.GameStartNotification;
import it.polimi.ingsw.PSP30.Model.Game;
import it.polimi.ingsw.PSP30.Model.God;
import it.polimi.ingsw.PSP30.Model.Player;

import java.util.ArrayList;


/**
 * Interface for standardize the behaviors of the various UI (CLI and GUI).
 */
public interface UI {

    /**
     * Asks the player the address of the server.
     */
    void chooseServerAddress();

    /**
     * Manages not valid username error.
     */
    void errorServerAddress();

    /**
     * Asks to choose the username.
     */
    void chooseUsername();

    /**
     * Manages not valid username error.
     */
     void usernameError();

    /**
     * Asks to choose how many players are wanted in the game.
     */
    void chooseNumPlayers();

    /**
     * Shows the lobby status.
     * @param selectedLobby number of the lobby, it can be 2 or 3 depending on the selected players.
     * @param slotsOccupied Number of players in the lobby at the moment.
     * @param usernames Usernames of the player in lobby.
     */
    void printLobbyStatus(int selectedLobby, int slotsOccupied, ArrayList<String> usernames);

    /**
     * Notifies the game can start (the lobby is full).
     * @param message Notification of game start.
     */
    void startNotification(GameStartNotification message);

    /**
     * Shows all the God cards of the deck.
     * @param gods ArrayList of Gods.
     */
    void showGodList(ArrayList<God> gods);

    /**
     * Manages the choice of the Gods that will be used in the game.
     */
    void chooseGameGods();

    /**
     * Shows all the players of the lobby.
     * @param players array of Players.
     */
    void printAllPlayers(Player[] players);

    /**
     * Manages not valid first player choice.
     */
    void playerError();

    /**
     * Manages the choice of the first player.
     */
    void chooseFirstPlayer();

    /**
     * Shows the God cards used in the game and those already chosen.
     * @param godList ArrayList of all the Gods chosen for the game.
     * @param unavailableList ArrayList of Gods already chosen, so not available.
     */
    void chooseGod(ArrayList<God> godList, ArrayList<God> unavailableList);

    /**
     * Manages not valid god choice.
     */
    void godChoiceError();

    /**
     * Shows the God cards used in the game and the last God available.
     * @param godList ArrayList of all the Gods chosen for the game.
     * @param lastGod last God available.
     */
    void showLastGod(ArrayList<God> godList, God lastGod);

    /**
     * Shows where the player can put the selected worker and then he inserts the chosen coordinate.
     * @param currentWorker integer that identifies the  selected current worker.
     * @param allowedPositions the boolean matrix of the allowed spaces where the player can position his worker.
     */
    void placeWorkerInSpace(int currentWorker, boolean[][] allowedPositions);

    /**
     * Asks the player of the position in which wants to move the selected worker.
     */
    void chooseWorker();

    /**
     * Manages not valid worker selection.
     */
    void workerChosenError();

    /**
     * Asks the player if he wants to choose another worker.
     * @return true if the player chooses to confirm the same worker, false if he wants to change it.
     */
    boolean confirmChoice();

    /**
     * Asks the player to choose where he wants to move the selected worker.
     * @param allowedMoves the boolean matrix of the allowed moves.
     */
    void moveWorker(boolean[][] allowedMoves);

    /**
     * Warns the player that the worker chosen could't be moved, so he will move the other worker.
     */
    void otherWorker();

    /**
     * Asks the player to choose where he wants to move the other(selected) worker.
     * @param allowedMoves the boolean matrix of the allowed moves.
     */
    void moveOtherWorker(boolean[][] allowedMoves);

    /**
     * Shows all the possible moves of the respective worker.
     * @param allowed the boolean matrix of the allowed moves.
     */
    void printPossibleAction(boolean[][] allowed);

    /**
     * Gives to the player the chance to change the worker selected.
     * and allows the player to choose where he wants to move his worker.
     * @param canChangeWorker Boolean value indicating the possibility of the player to change worker.
     */
    void changeWorker(boolean canChangeWorker);

    /**
     * Asks the player in which position he wants to build.
     * @param allowedBuild the boolean matrix of the spaces where the player is allowed to build.
     */
    void buildTower(boolean[][] allowedBuild);

    /**
     * Asks the player if wants to use the power.
     */
    void askPowerUsage();

    /**
     * Asks the player in which position he wants to remove a building.
     * @param allowedToRemove the boolean matrix of the spaces where the player is allowed to remove a building.
     */
    void chooseRemoval(boolean[][] allowedToRemove);

    /**
     * Warns there aren't more possible moves for the player.
     */
    void noPossibleMoves();

    /**
     * Warns the chosen move is not valid.
     */
    void invalidMove();

    /**
     * Shows the legend of the players in the game: username, color of the workers and God chosen.
     * @param updatedGame is the current instance of the Game class.
     */
    void printCurrentStatus(Game updatedGame);

    /**
     * Shows the current status of the game board.
     * @param updatedGame is the current instance of the Game class.
     */
    void printCurrentBoard(Game updatedGame);

    /**
     * Shows the name of the winner.
     * @param winner winner's name.
     */
    void isWinner(String winner);

    /**
     * Shows an alert when an opponent disconnected.
     */
    void opponentDisconnected();

    /**
     * Shows an alert when the player is disconnected from the server.
     */
    void disconnectedFromServer();

}



