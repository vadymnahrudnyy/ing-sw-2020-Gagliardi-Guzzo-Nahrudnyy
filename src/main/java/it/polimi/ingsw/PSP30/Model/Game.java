package it.polimi.ingsw.PSP30.Model;

import java.io.Serializable;

/**
 * Game class is the main class of the Model. It represents the state of the game.
 * @author Vadym Nahrudnyy
 * @version 1.2
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
     * Builds an instance of class Game.
     * @param numPlayers indicating the number of users playing the game
     * @param currentPlayer is used to indicate the Player who's making the move. At the beginning is the first Player joining the game ("The Challenger")
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

    /**
     * Setter of the parameter username.
     * @param username name chosen by the player
     */
    public void setStarterPlayer(String username){
        starterPlayer = getPlayerByUsername(username);
    }

    /**
     * Getter of the parameter startPlayer.
     * @return the string value of startedPlayer
     */
    public Player getStarterPlayer(){
        return starterPlayer;
    }

    /**
     * Getter of the parameter numPlayers.
     * @return the value of numPlayers
     */
    public int getNumPlayers() {
        return numPlayers;
    }

    /**
     * Getter of the parameter currentRound.
     * @return the value of currentRound
     */
    public int getCurrentRound() {
        return currentRound;
    }

    /**
     * Setter of the parameter currentRound.
     * @param currentRound number of the round
     */
    public void setCurrentRound(int currentRound) {
        this.currentRound = currentRound;
    }

    /**
     * Getter of the parameter currentPhase.
     * @return the value of currentPhase
     */
    public TurnPhase getCurrentPhase() {
        return currentPhase;
    }

    /**
     * Setter of the parameter currentPhase.
     * @param currentPhase type of phase of the game
     */
    public void setCurrentPhase(TurnPhase currentPhase) {
        this.currentPhase = currentPhase;
    }

    /**
     * Method nextTurnPhase set the parameter TurnPhase with the next value, in case the turn is finished (current phase is END)
     * the currentPhase is set to START.
     * @since version 1.1
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

    /**
     * Getter of the parameter currentPlayer.
     * @return the value of currentPlayer
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Setter of the parameter currentPlayer.
     * @param currentPlayer name of the current player
     */
    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    /**
     * Sets the next player in the list as current Player.
     * In case the player who made the turn is the last of the list, then the first player is set as current.
     * @since version 1.2
     */
    public void nextPlayer(){
        Player currentPlayer = getCurrentPlayer();
        int index = 0;
        while (index < players.length &&players[index]!=currentPlayer){++index;} //don't control index to be < num_players in that case a non valid player is playing
        if (index == players.length - 1) setCurrentPlayer(players[0]);
        else setCurrentPlayer(players[index+1]);
    }

    /**
     * Getter of the parameter towerWasCompleted.
     * @return boolean value of towerWasCompleted
     */
    public boolean getTowerWasCompleted() {
        return towerWasCompleted;
    }

    /**
     * Setter of the parameter towerWasCompleted.
     * @param towerWasCompleted true if the tower was completed, otherwise false
     */
    public void setTowerWasCompleted(boolean towerWasCompleted) {
        this.towerWasCompleted = towerWasCompleted;
    }

    /**
     * Setter of the parameter players.
     * @param players array of Player[] of the game
     */
    public void setPlayers(Player [] players) {
        this.players = players;
    }

    /**
     * Getter of the parameter players.
     * @return players
     */
    public Player[] getPlayers() {
        return players;
    }

    /**
     * Getter of the parameter gameBoard.
     * @return status of the gameBoard
     */
    public IslandBoard getGameBoard() {
        return gameBoard;
    }

    /**
     * Setter of the parameter gameBoard.
     * @param gameBoard status of the IslandBoard
     */
    public void setGameBoard(IslandBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    /**
     * Given a username returns the object players[] associated.
     * @param username name of the player
     * @return Player[] associated to the username, otherwise null
     */
    public Player getPlayerByUsername(String username){
        for(int index = 0; index<numPlayers;++index){
            if (players[index].getUsername().equals(username)) return players[index];
        }
        return null;
    }

    /**
     * Getter of the parameter AthenaMovedUp.
     * @return boolean value of AthenaMovedUp
     */
    public boolean hasAthenaMovedUpDuringLastRound() {
        return AthenaMovedUp;
    }

    /**
     * Setter of the parameter AthenaMovedUp.
     * @param movedUp true if Athena has moved up, otherwise false
     */
    public void setAthenaMovedUp(boolean movedUp){
        AthenaMovedUp = movedUp;
    }
}
