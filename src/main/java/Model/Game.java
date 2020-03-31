package Model;

/**
 * @author Vadym Nahrudnyy
 * @version 1.0
 */

public class Game {
    private final int num_players;
    private int currentRound;
    private TurnPhase currentPhase;
    private Player currentPlayer;
    private boolean towerWasCompleted; //if "true", a tower was completed during this turn.
    private Player[] players;
    private IslandBoard gameBoard;

    public Game(int num_players, Player currentPlayer, Player[] playersList) {
        this.num_players = num_players;
        this.currentRound = 0;
        this.currentPhase = TurnPhase.SETUP;
        this.currentPlayer = currentPlayer;
        this.towerWasCompleted = false;
        this.gameBoard = new IslandBoard();
        this.players = new Player[num_players];
        int index;
        for (index = 0; index < num_players;++index){
            players[index] = playersList[index];
        }
    }

    public int getNum_players() {
        return num_players;
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
}
