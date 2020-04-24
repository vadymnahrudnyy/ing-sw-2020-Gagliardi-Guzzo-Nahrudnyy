package Messages;

/**
 * WorkerPositionRequest implements the message to ask a player
 * the start position of specified worker among the available ones.
 */
public class WorkerPositionRequest extends Message{
    private static final long serialVersionUID = 100011L;
    private static final int messageID = 105;
    private final int currentWorker;
    private final boolean[][] allowedPositions;

    /**
     * Builder of a worker position request message
     * @param worker indicates the current worker.
     * @param positions indicates the available positions.
     */
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
