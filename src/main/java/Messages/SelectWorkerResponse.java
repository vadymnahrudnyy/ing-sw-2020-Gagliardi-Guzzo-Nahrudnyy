package Messages;

public class SelectWorkerResponse extends Message {
    private static final long serialVersionUID = 100014L;
    private static final int messageID = 206;
    private final int coordinateX;
    private final int coordinateY;

    public SelectWorkerResponse(int CoordinateX,int CoordinateY){
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
