package Messages;

public class WorkerPositionRequest extends Message{
    private static final long serialVersionUID = 100011L;
    private static final int messageID = 105;
    private final int currentWorker;
    private final boolean[][] allowedPositions;

    public WorkerPositionRequest(int worker,boolean[][] positions){
        allowedPositions = positions;
        currentWorker = worker;
    }

    public int getMessageID() {
        return messageID;
    }
    public boolean[][] getAllowedPositions(){
        return allowedPositions;
    }
}
