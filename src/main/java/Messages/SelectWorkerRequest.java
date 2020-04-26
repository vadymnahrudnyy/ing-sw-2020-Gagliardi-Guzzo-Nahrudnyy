package Messages;

/**
 * SelectWorkerRequest implement the message asking the player
 * the worker he wants to move.
 */
public class SelectWorkerRequest extends Message {
    private static final long serialVersionUID = 100013L;

    public SelectWorkerRequest(){
        messageID = SELECT_WORKER_REQUEST;
    }
}
