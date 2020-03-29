package it.polimi.ingsw.PSP30;

/**
 * @author Vadym Nahrudnyy
 */

public class Game {
    private int num_players;
    private int currentRound;
    private TurnPhase currentPhase;
    private Player currentPlayer;
    private boolean towerWasCompleted; //if "true", a tower was completed during this turn.
    private Player players [];
    private IslandBoard gameBoard;

    public int getNum_players() {
        return num_players;
    }

    public void setNum_players(int num_players) {
        this.num_players = num_players;
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

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public boolean isTowerWasCompleted() {
        return towerWasCompleted;
    }

    public void setTowerWasCompleted(boolean towerWasCompleted) {
        this.towerWasCompleted = towerWasCompleted;
    }

    public void setPlayers() {
        this.players = new Player[num_players];
    }

    public IslandBoard getGameBoard() {
        return gameBoard;
    }

    public void setGameBoard(IslandBoard gameBoard) {
        this.gameBoard = gameBoard;
    }
}
