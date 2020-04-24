package Messages;

/**
 * MoveRequest implements the message sending
 * to player the possible moves of the selectedWorker
 * and asking the coordinates of the destination space.
 */
public class MoveRequest extends Message {
    private static final long serialVersionUID = 100015L;
    private static final int messageID = 107;
    private final boolean[][] allowedMoves;

    public MoveRequest(boolean[][] movesMatrix){
        allowedMoves = movesMatrix;
    }

    public boolean[][] getAllowedMoves() {
        return allowedMoves;
    }

    public int getMessageID() {
        return messageID;
    }

}
