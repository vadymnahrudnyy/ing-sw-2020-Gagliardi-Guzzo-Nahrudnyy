package it.polimi.ingsw.PSP30.Messages;

/**
 * InvalidMoveError implements the message which warns the player the move he has selected isn't valid.
 */
public class InvalidMoveError extends Message {
    private static final long serialVersionUID = 100031L;

    public InvalidMoveError(){messageID = INVALID_MOVE_ERROR;}
}
