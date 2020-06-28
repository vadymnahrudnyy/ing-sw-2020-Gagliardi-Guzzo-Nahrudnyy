package it.polimi.ingsw.PSP30.Messages;

/**
 * UsePowerResponse implements the message sent with the choice of the player to use the power or not.
 */
public class UsePowerResponse extends Message{
    private static final long serialVersionUID = 100028L;
    private final boolean usePower;

    public UsePowerResponse(boolean userAnswer){
        messageID = USE_POWER_RESPONSE;
        usePower = userAnswer;
    }

    public boolean wantUsePower(){return usePower;}
}
