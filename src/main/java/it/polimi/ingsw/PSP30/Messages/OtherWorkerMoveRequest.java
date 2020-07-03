package it.polimi.ingsw.PSP30.Messages;

/**
 * Implements the request of moving the other worker.
 */
public class OtherWorkerMoveRequest extends Message {
    private static final long SerialVersionUID = 100030L;
    private final boolean[][] allowedMoves;

    public OtherWorkerMoveRequest(boolean[][]otherWorkerMoves){
        messageID = OTHER_WORKER_MOVE_REQUEST;
        allowedMoves = otherWorkerMoves;
    }

    public boolean[][] getAllowedMoves(){return allowedMoves;}
}
