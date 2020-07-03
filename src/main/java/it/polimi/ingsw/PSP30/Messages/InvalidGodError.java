package it.polimi.ingsw.PSP30.Messages;

/**
 * Implements the message which warns the player the god he has chosen isn't valid.
 */
public class InvalidGodError extends Message {
    private static final long serialVersionUID = 100034L;

    public InvalidGodError(){
        messageID = INVALID_GOD_ERROR;
    }
}
