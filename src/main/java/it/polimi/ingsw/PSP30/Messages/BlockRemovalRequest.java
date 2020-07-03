package it.polimi.ingsw.PSP30.Messages;

/**
 * Implements the request of removing a block.(Ares power)
 */
public class BlockRemovalRequest extends Message {
    private static final long serialVersionUID = 100032L;

    private final boolean[][] allowedToRemove;

    public BlockRemovalRequest(boolean[][] removesMatrix){
        messageID = BLOCK_REMOVAL_REQUEST;
        allowedToRemove = removesMatrix;
    }

    public boolean[][] getAllowedMoves() {
        return allowedToRemove;
    }
}