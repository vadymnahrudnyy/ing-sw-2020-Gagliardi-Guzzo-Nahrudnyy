
package it.polimi.ingsw.PSP30.Messages;

/**
 * BlockRemovalResponse implements the message sending to server
 * the coordinates of the space from which to remove a block.
 */
public class BlockRemovalResponse extends Message {
    private static final long serialVersionUID = 100033L;
    private final int removeCoordinateX;
    private final int removeCoordinateY;

    /**
     * BlockRemovalResponse message builder
     * @param coordinateX coordinate X where to remove the block.
     * @param coordinateY coordinate Y where to remove the block.
     */
    public BlockRemovalResponse(int coordinateX, int coordinateY){
        messageID = BLOCK_REMOVAL_RESPONSE;
        removeCoordinateX = coordinateX;
        removeCoordinateY = coordinateY;
    }
    public int getRemoveCoordinateX() {
        return removeCoordinateX;
    }
    public int getRemoveCoordinateY() {
        return removeCoordinateY;
    }
}