package Messages;

/**
 * WorkerPositionResponse implements the message used to answer to
 * the worker position request by sending the X and Y coordinate on the board.
 */
public class WorkerPositionResponse extends Message{
    private static final long serialVersionUID = 100012L;
    private static final int messageID = 205;
    private int coordinateX;
    private int coordinateY;

    /**
     * Builder of a worker position response message.
     * @param CoordinateX coordinate X indicated by the player
     * @param CoordinateY coordinate Y indicated by the player
     */
    public WorkerPositionResponse(int CoordinateX,int CoordinateY){
        coordinateX = CoordinateX;
        coordinateY = CoordinateY;
    }
    public int getMessageID() {
        return messageID;
    }
    public int getCoordinateX(){
        return coordinateX;
    }

    public int getCoordinateY() {
        return coordinateY;
    }
}
