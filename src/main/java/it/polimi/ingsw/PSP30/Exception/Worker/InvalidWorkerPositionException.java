package it.polimi.ingsw.PSP30.Exception.Worker;

/**
 * @author Alessia Gagliardi
 * @version 1.0
 */

public class InvalidWorkerPositionException extends RuntimeException{
    /**
     * This class is used in the case that a player wants to move his worker into an already occupied position.
     */
    public InvalidWorkerPositionException() {
        super ("An error occurred, the position has been already occupied");
    }
}
