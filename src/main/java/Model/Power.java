package Model;

/**
 * @author Alessia Guzzo
 */

public class Power {
    /**
     * Power class describes the usage of the specific power
     */

    private final int powerID;
    private Boolean isActive;
    private Boolean usableOnPlayerTurn;
    private Boolean validOnOpponentTurn;
    private TurnPhase turnPhase;


    /**
     * @param isActive states if the power have to be activated by the player
     * @param usableOnPlayerTurn indicates if the power can be used during the player's turn
     * @param validOnOpponentTurn flag that states if the power can be activated during the opponent's turn
     * @param turnPhase is an enum that indicates in what phase of the game the power can be used
     */
    public Power(int powerID, Boolean isActive, Boolean usableOnPlayerTurn, Boolean validOnOpponentTurn, TurnPhase turnPhase) {

        this.powerID= powerID;
        this.isActive = isActive;
        this.usableOnPlayerTurn = usableOnPlayerTurn;
        this.validOnOpponentTurn = validOnOpponentTurn;
        this.turnPhase = turnPhase;
    }

    public int getPowerID() {
        return powerID;
    }

    public Boolean getActive() {
        return isActive;
    }

    public Boolean getUsableOnPlayerTurn() { return usableOnPlayerTurn; }

    public Boolean getValidOnOpponentTurn() { return validOnOpponentTurn; }

    public TurnPhase getTurnPhase() {
        return turnPhase;
    }

}
