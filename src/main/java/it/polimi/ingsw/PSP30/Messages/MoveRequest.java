package it.polimi.ingsw.PSP30.Messages;

/**
 * Implements the message sending to player the possible moves of the selectedWorker
 * and asks the coordinates of the destination space.
 */
public class MoveRequest extends Message {
    private static final long serialVersionUID = 100015L;
    private final boolean[][] allowedMoves;
    private final boolean canChangeWorker;

    public MoveRequest(boolean[][] movesMatrix,boolean workerChangeAllowed){
        messageID = MOVE_REQUEST;
        allowedMoves = movesMatrix;
        canChangeWorker = workerChangeAllowed;
    }

    public boolean[][] getAllowedMoves() {
        return allowedMoves;
    }

    public boolean getChangeWorker(){return canChangeWorker;}
}
