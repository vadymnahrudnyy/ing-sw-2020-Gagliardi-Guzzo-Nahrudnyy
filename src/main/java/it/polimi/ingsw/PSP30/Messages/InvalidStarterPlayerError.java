package it.polimi.ingsw.PSP30.Messages;

/**
 * Implements the message which warns the player whom has chosen the starter opponent, that the selected opponent isn't valid.
 */

public class InvalidStarterPlayerError extends Message {
    private static final long serialVersionUID = 100036L;

    public InvalidStarterPlayerError(){
        messageID = INVALID_STARTER_PLAYER_ERROR;
    }
}
