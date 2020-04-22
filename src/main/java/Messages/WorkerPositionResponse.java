package Messages;

public class WorkerPositionResponse extends Message{
    private static final long serialVersionUID = 100012L;
    private static final int messageID = 205;
    private int coordinateX;
    private int coordinateY;

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
