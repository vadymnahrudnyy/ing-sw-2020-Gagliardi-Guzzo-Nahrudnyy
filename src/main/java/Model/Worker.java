package Model;

import Exception.Worker.InvalidWorkerPositionException;

import java.io.Serializable;

/**
 * @author Alessia Gagliardi
 * @version 1.0
 */


public class Worker implements Serializable {
    /**
     * This class manages the workers of a player on the board
     */

    private final int workerID;
    private final String owner;
    private boolean wasMoved;
    private boolean movedUp;
    private final char gender;
    private Space workerPosition;

    /**
     * @param workerID states the identification number of the considered worker where the worker should be moved to.
     * @param owner tells who is the player that own that worker.
     * @param gender indicates the gender of the worker ('m'=male and 'f'= female)
     * @param workerPosition tells the position of the worket on the board in that moment.
     */


    public Worker(int workerID, String owner, char gender, Space workerPosition) {
        this.workerID = workerID;
        this.owner = owner;
        this.wasMoved = false;
        this.movedUp = false;
        this.gender = gender;
        this.workerPosition = workerPosition;
    }


    /**
     * This method allows the worker to change position.
     * @param newPosition tells where the worker should be moved to.
     */

    public void changePosition(Space newPosition) {

        Space currentPosition = getWorkerPosition();
        currentPosition.removeWorkerInPlace();
        setWorkerPosition(newPosition);
        newPosition.setWorkerInPlace(this);
    }


    public int getWorkerID() {
        return workerID;
    }

    public String getOwner() {
        return owner;
    }

    public boolean isWasMoved() {
        return wasMoved;
    }

    public void setWasMoved(boolean wasMoved) {
        this.wasMoved = wasMoved;
    }

    public boolean isMovedUp() {
        return movedUp;
    }

    public void setMovedUp(boolean movedUp) {
        this.movedUp = movedUp;
    }

    public char getGender() {
        return gender;
    }

    public Space getWorkerPosition() {
        return workerPosition;
    }

    public void setWorkerPosition (Space workerPosition) throws InvalidWorkerPositionException {
        if(workerPosition.getHasDome() || workerPosition.getWorkerInPlace()!=null) throw new InvalidWorkerPositionException();
        else this.workerPosition = workerPosition;
    }



}
