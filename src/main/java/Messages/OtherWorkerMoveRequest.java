package Messages;

public class OtherWorkerMoveRequest extends Message {
    private static final long SerialVersionUID = 100030L;
    private boolean[][] allowedMoves;

    public OtherWorkerMoveRequest(boolean[][]otherWorkerMoves){
        messageID = OTHER_WORKER_MOVE_REQUEST;
        allowedMoves = otherWorkerMoves;
    }

    public boolean[][] getAllowedMoves(){return allowedMoves;}
}
