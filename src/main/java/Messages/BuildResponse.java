
package Messages;

/**
 * BuildResponse implements the message sending to server the
 * coordinates where to move the selectedWorker
 */
public class BuildResponse extends Message {
    private static final long serialVersionUID = 100025L;
    private final int buildCoordinateX;
    private final int buildCoordinateY;

    /**
     * BuildResponse message builder
     * @param coordinateX coordinate X where to build.
     * @param coordinateY coordinate Y where to build.
     */
    public BuildResponse(int coordinateX, int coordinateY){
        messageID = BUILD_RESPONSE;
        buildCoordinateX = coordinateX;
        buildCoordinateY = coordinateY;
    }
    public int getBuildCoordinateX() {
        return buildCoordinateX;
    }
    public int getBuildCoordinateY() {
        return buildCoordinateY;
    }
}