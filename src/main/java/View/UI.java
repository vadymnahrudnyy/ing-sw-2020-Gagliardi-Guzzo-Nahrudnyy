package View;

import Messages.Message;
import Model.Game;
import Model.God;
import Model.Player;

import java.io.IOException;
import java.util.ArrayList;

//o interface

/**
 * Interface for standardize the behaviors of the various UI (CLI and GUI)
 */

public interface UI {

    public void gameInfo();

    public void chooseUsername();

    public void chooseNumPlayers();

    public void printLobbyStatus(int selectedLobby, int slotsOccupied);

    public void startNotification();

    public void showGodList(ArrayList<God> gods, int numPlayers);

    public void printAllPlayers(Player[] players);

    public void chooseGod(ArrayList<God> godList, ArrayList<God> unavailableList);

    public void printLastGod(ArrayList<God> godList, God lastGod);

    public void askWorkerPosition();

    public void chooseWorker();

    public boolean confirmChoice();

    public void moveWorker();

    public void printPossibleAction(boolean[][] allowed);

    public void buildTower();

    public void askPowerUsage();

    public void printCurrentBoard(Game updatedGame);

    public void isWinner(String winner);

}



