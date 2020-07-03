package it.polimi.ingsw.PSP30.Messages;

/**
 * Implements the message which warns the player the move he has chosen isn't possible.
 */
public class NoPossibleMoveError extends Message{
    private static final long serialVersionUID = 100029L;

    public NoPossibleMoveError(){
        messageID = NO_POSSIBLE_MOVE_ERROR;
    }
}
