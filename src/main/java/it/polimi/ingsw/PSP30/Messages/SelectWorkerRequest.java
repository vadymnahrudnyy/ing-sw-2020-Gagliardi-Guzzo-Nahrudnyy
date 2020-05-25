package it.polimi.ingsw.PSP30.Messages;

/**
 * SelectWorkerRequest implement the message asking the player
 * the worker he wants to move during the Move phase of the turn.
 */
public class SelectWorkerRequest extends Message {
    private static final long serialVersionUID = 100013L;

    public SelectWorkerRequest(){
        messageID = SELECT_WORKER_REQUEST;
    }
}
