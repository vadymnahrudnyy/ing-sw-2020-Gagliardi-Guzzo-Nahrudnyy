package it.polimi.ingsw.PSP30.Messages;

/**
 * Implements the message asking the player to build.
 */
public class BuildRequest extends Message {
    private static final long serialVersionUID = 100024L;

    private final boolean[][] allowedBuilds;

    public BuildRequest(boolean[][] movesMatrix){
        messageID = BUILD_REQUEST;
        allowedBuilds = movesMatrix;
    }

    public boolean[][] getAllowedMoves() {
        return allowedBuilds;
    }
}
