package Model;


/**
 * @author Alessia Guzzo
 */

public class Power {
    /**
     * This class describes the powers used in the game which are associated with Gods
     */

    private Boolean isActive;
    private Boolean isPlayerTurn;
    private Boolean isOpponent;
    private TurnPhase turnPhase;

    /**
     * @param isActive states if the power have to be activated by the player, so he decides to use it
     * @param isPlayerTurn if '1' is the player's turn
     * @param isOpponent flag that states if the power can be activated during other's players turn
     * @param turnPhase is an enum that indicates in what phase of the game the power can be used
     */
    public Power(Boolean isActive, Boolean isPlayerTurn, Boolean isOpponent, TurnPhase turnPhase) {

        this.isActive = isActive;
        this.isPlayerTurn = isPlayerTurn;
        this.isOpponent = isOpponent;
        this.turnPhase = turnPhase;
    }


    public Boolean getActive() {
        return isActive;
    }


    public Boolean getPlayerTurn() {
        return isPlayerTurn;
    }


    public Boolean getOpponent() {
        return isOpponent;
    }


    public TurnPhase getTurnPhase() {
        return turnPhase;
    }

}
