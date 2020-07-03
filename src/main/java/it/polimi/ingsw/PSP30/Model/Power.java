package it.polimi.ingsw.PSP30.Model;

/**
 * Power class describes the usage of the specific power.
 * @author Alessia Guzzo
 */

public class Power {

    private final int powerID;
    private final Boolean isActive;
    private final Boolean usableOnPlayerTurn;
    private final Boolean validOnOpponentTurn;
    private final TurnPhase turnPhase;

    public static final int WORKER_POSITION_EXCHANGE_POWER = 1;
    public static final int DOUBLE_MOVE_POWER = 2;
    public static final int OPPONENTS_NOT_MOVE_UP_POWER = 3;
    public static final int BUILD_DOME_EVERYWHERE_POWER = 4;
    public static final int DIFFERENT_SPACE_DOUBLE_BUILD_POWER = 5;
    public static final int SAME_SPACE_DOUBLE_BUILD_POWER = 6;
    public static final int PUSH_WORKER_POWER = 8;
    public static final int TWO_BLOCK_FALL_VICTORY_POWER = 9;
    public static final int NOT_MOVE_UP_DOUBLE_BUILD_POWER = 10;//Prometheus Power
    public static final int BLOCK_REMOVE_POWER = 12;
    public static final int FIVE_TOWER_VICTORY_POWER = 16;
    public static final int PERIMETER_VICTORY_DENY_POWER = 20;
    public static final int NON_PERIMETER_DOUBLE_BUILD = 21;
    public static final int WORKER_POSITION_BUILD = 30;


    /**
     * Constructor of Power class.
     * @param powerID ID of the power
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

    /**
     * Getter of the parameter powerID.
     * @return the value of powerID
     */
    public int getPowerID() {
        return powerID;
    }

    /**
     * Getter of the parameter isActive.
     * @return boolean value of isActive
     */
    public Boolean getActive() {
        return isActive;
    }

    /**
     * Getter of the parameter usableOnPlayerTurn.
     * @return boolean value of usableOnPlayerTurn
     */
    public Boolean getUsableOnPlayerTurn() { return usableOnPlayerTurn; }

    /**
     * Getter of the parameter validOnOpponentTurn.
     * @return boolean value of validOnOpponentTurn
     */
    public Boolean getValidOnOpponentTurn() { return validOnOpponentTurn; }

    /**
     * Getter of the parameter turnPhase.
     * @return the value of turnPhase
     */
    public TurnPhase getTurnPhase() {
        return turnPhase;
    }

}
