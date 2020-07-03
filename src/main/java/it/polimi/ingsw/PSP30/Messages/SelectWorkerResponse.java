package it.polimi.ingsw.PSP30.Messages;

/**
 * Implements the message sending to server the worker the player wants to move.
 */
public class SelectWorkerResponse extends Message {
    private static final long serialVersionUID = 100014L;
    private final int coordinateX;
    private final int coordinateY;

    /**
     * Builder of a SelectWorkerResponse message
     * @param CoordinateX indicates the X coordinate of the selected worker.
     * @param CoordinateY indicates the Y coordinate of the selected worker.
     */
    public SelectWorkerResponse(int CoordinateX,int CoordinateY){
        messageID = SELECT_WORKER_RESPONSE;
        coordinateX = CoordinateX;
        coordinateY = CoordinateY;
    }
    public int getCoordinateX(){
        return coordinateX;
    }

    public int getCoordinateY() {
        return coordinateY;
    }
}
