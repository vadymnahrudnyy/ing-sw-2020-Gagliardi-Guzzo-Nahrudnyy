package it.polimi.ingsw.PSP30.Messages;

/**
 * Implements the message which alerts the player the username he has chosen isn't valid.
 */
public class UsernameTakenError extends Message{
    private static final long serialVersionUID = 100026L;

    public UsernameTakenError(){
        messageID = USERNAME_TAKEN_ERROR;
    }
}
