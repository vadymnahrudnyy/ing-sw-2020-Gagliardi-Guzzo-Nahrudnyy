package it.polimi.ingsw.PSP30.Model;

import java.io.Serializable;

/**
 * @author Vadym Nahrudnyy
 * @version 1.2
 * Game class is the main class of the Model. It represents the state of the game.
 */

public class Game implements Serializable {
    private static final long serialVersionUID = 50001L;
    private final int numPlayers;
    private int currentRound;
    private Player[] players;
    private Player currentPlayer;
    private Player starterPlayer;
    private IslandBoard gameBoard;
    private TurnPhase currentPhase;
    private boolean towerWasCompleted; //if "true", a tower was completed during this turn.
    private boolean AthenaMovedUp;

    public static final int WORKERS_PER_PLAYER = 2;

    /**
     * To build an instance of class Gaming are needed these parametres
     * @param numPlayers indicating the number of users playing the game.
     * @param currentPlayer is used to indicate the Player who's making the move. At the beginning is the first Player joining the game ("The Challenger").
     * @param playersList is the list of players who joined the game
     */
    public Game(int numPlayers, Player currentPlayer, Player[] playersList) {
        this.numPlayers = numPlayers;
        this.currentRound = 0;
        this.currentPhase = TurnPhase.SETUP;
        this.currentPlayer = currentPlayer;
        this.towerWasCompleted = false;
        this.gameBoard = new IslandBoard();
        this.players = new Player[numPlayers];
        int index;
        for (index = 0; index < numPlayers;++index){
            players[index] = playersList[index];
        }
    }

    public void setStarterPlayer(String username){
        starterPlayer = getPlayerByUsername(username);
    }
    public Player getStarterPlayer(){
        return starterPlayer;
    }
    public int getNumPlayers() {
        return numPlayers;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(int currentRound) {
        this.currentRound = currentRound;
    }

    public TurnPhase getCurrentPhase() {
        return currentPhase;
    }

    public void setCurrentPhase(TurnPhase currentPhase) {
        this.currentPhase = currentPhase;
    }

    /**
     * @since version 1.1
     * Method nextTurnPhase set the parameter TurnPhase with the next value, in case the turn is finished (current phase is END)
     * the currentPhase is set to START.
     */
    public void nextTurnPhase(){
        TurnPhase currentPhase = getCurrentPhase();
        switch (currentPhase){
            case START:
                setCurrentPhase(TurnPhase.MOVE);
                break;
            case MOVE:
                setCurrentPhase(TurnPhase.BUILD);
                break;
            case BUILD:
                setCurrentPhase(TurnPhase.END);
                break;
            case END:
                setCurrentPhase(TurnPhase.START);
                break;
        }
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    /**
     * @since version 1.2
     * Method nextPlayer sets the next player in the list as current Player.
     * In case the player who made the turn is the last of the list, then the first player is set as current.
     */
    public void nextPlayer(){
        Player currentPlayer = getCurrentPlayer();
        int index = 0;
        while (players[index]!=currentPlayer){++index;} //don't control index to be < num_players in that case a non valid player is playing
        if (index == numPlayers - 1) setCurrentPlayer(players[0]);
        else setCurrentPlayer(players[index+1]);
    }

    public boolean getTowerWasCompleted() {
        return towerWasCompleted;
    }

    public void setTowerWasCompleted(boolean towerWasCompleted) {
        this.towerWasCompleted = towerWasCompleted;
    }

    public void setPlayers(Player [] players) {
        this.players = players;
    }

    public Player[] getPlayers() {
        return players;
    }

    public IslandBoard getGameBoard() {
        return gameBoard;
    }

    public void setGameBoard(IslandBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    public Player getPlayerByUsername(String username){
        for(int index = 0; index<numPlayers;++index){
            if (players[index].getUsername().equals(username)) return players[index];
        }
        return null;
    }

    public boolean hasAthenaMovedUpDuringLastRound() {
        return AthenaMovedUp;
    }
    public void setAthenaMovedUp(boolean movedUp){
        AthenaMovedUp = movedUp;
    }
}
