package it.polimi.ingsw.PSP30.Messages;

/**
 * Implements the message which warns the player the worker he has chosen isn't valid.
 */
public class InvalidWorkerError extends Message {
    private static final long serialVersionUID = 100035L;

    public InvalidWorkerError(){
        messageID = INVALID_WORKER_ERROR;
    }
}
