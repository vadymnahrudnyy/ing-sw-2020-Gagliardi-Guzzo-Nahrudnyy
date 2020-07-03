package it.polimi.ingsw.PSP30.Messages;

/**
 * Implements the message which asks the player if he wants to use the power of his card.
 */
public class UsePowerRequest extends Message {
    private static final long serialVersionUID = 100027L;

    public UsePowerRequest(){
        messageID = USE_POWER_REQUEST;
    }
}
