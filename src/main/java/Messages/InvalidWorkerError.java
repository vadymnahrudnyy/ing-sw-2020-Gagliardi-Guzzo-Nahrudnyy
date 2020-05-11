package Messages;

public class InvalidWorkerError extends Message {
    private static final long serialVersionUID = 100035L;

    public InvalidWorkerError(){
        messageID = INVALID_WORKER_ERROR;
    }
}
