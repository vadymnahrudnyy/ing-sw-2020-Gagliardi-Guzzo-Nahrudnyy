package Messages;

/**
 * WorkerPositionRequest implements the message to ask a player
 * the start position of specified worker among the available ones.
 */
public class WorkerPositionRequest extends Message{
    private static final long serialVersionUID = 100011L;
    private final int currentWorker;
    private final boolean[][] allowedPositions;

    /**
     * Builder of a worker position request message
     * @param worker indicates the current worker.
     * @param positions indicates the available positions.
     */
    public WorkerPositionRequest(int worker,boolean[][] positions){
        messageID = WORKER_POSITION_REQUEST;
        allowedPositions = positions;
        currentWorker = worker;
    }
    public boolean[][] getAllowedPositions(){
        return allowedPositions;
    }

    public int getCurrentWorker() { return currentWorker; }
}
