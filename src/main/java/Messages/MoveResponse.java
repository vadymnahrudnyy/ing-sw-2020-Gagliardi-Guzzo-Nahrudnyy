package Messages;

/**
 * MoveResponse implements the message sending to server the
 * coordinates where to move the selectedWorker
 */
public class MoveResponse extends Message {
    private static final long serialVersionUID = 100016L;
    private static final int messageID = 207;
    private final int destCoordinateX;
    private final int destCoordinateY;

    /**
     * MoveResponse message builder
     * @param coordinateX destination coordinate X
     * @param coordinateY destionatio coordinate Y
     */
    public MoveResponse(int coordinateX, int coordinateY){
        destCoordinateX = coordinateX;
        destCoordinateY = coordinateY;
    }

    public int getDestCoordinateX() {
        return destCoordinateX;
    }

    public int getDestCoordinateY() {
        return destCoordinateY;
    }

    public int getMessageID() {
        return messageID;
    }

}