package Messages;

/**
 * WorkerPositionResponse implements the message used to answer to
 * the worker position request by sending the X and Y coordinate on the board.
 */
public class WorkerPositionResponse extends Message{
    private static final long serialVersionUID = 100012L;
    private int coordinateX;
    private int coordinateY;

    /**
     * Builder of a worker position response message.
     * @param CoordinateX coordinate X indicated by the player
     * @param CoordinateY coordinate Y indicated by the player
     */
    public WorkerPositionResponse(int CoordinateX,int CoordinateY){
        messageID = WORKER_POSITION_RESPONSE;
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
