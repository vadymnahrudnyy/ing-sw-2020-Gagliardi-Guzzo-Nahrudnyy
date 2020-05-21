package Model;

import Exception.Worker.InvalidWorkerPositionException;

import java.io.Serializable;

/**
 * @author Alessia Gagliardi
 * @version 1.1
 */


public class Worker implements Serializable {
    /**
     * This class manages the workers of a player on the board
     */

    private final String owner;
    private Space workerPosition;
    private final int color;
    private final char gender;
    private boolean movedUp;
    private boolean wasMoved;
    public final static int COLOR_RED=1;
    public final static int COLOR_BLACK=2;
    public final static int COLOR_BLUE=3;



    /**
     * This is the constructor of the class Worker
     * @param owner tells who is the player that own that worker.
     * @param gender indicates the gender of the worker ('m'=male and 'f'= female)
     * @param workerPosition tells the position of the worker on the board in that moment.
     * @param color defines which color belongs to the worker
     */


    public Worker(String owner, char gender, Space workerPosition, int color) {
        this.owner = owner;
        this.wasMoved = false;
        this.movedUp = false;
        this.gender = gender;
        this.workerPosition = workerPosition;
        this.color=color;
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


    /**
     * Getter of the parameter color
     * @return the value set for color
     */
    public int getColor() {
        return color;
    }


    /**
     * Getter of the parameter owner
     * @return the value set for owner
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Getter of the parameter wasMoved
     * @return the value set for wasMoved
     */
    public boolean isWasMoved() {
        return wasMoved;
    }

    /**
     * Setter of the parameter wasMoved
     * @param wasMoved indicates the value of the parameter
     */
    public void setWasMoved(boolean wasMoved) {
        this.wasMoved = wasMoved;
    }

    /**
     * Getter of the parameter movedUp
     * @return the value set for movedUP
     */
    public boolean isMovedUp() {
        return movedUp;
    }

    /**
     * Setter of the parameter movedUp
     * @param movedUp indicates the value of parameter
     */
    public void setMovedUp(boolean movedUp) {
        this.movedUp = movedUp;
    }

    /**
     * Getter of the parameter getGender
     * @return the value set for getGender
     */
    public char getGender() {
        return gender;
    }

    /**
     * Getter of the parameter workerPosition
     * @return the value set for workerPosition
     */
    public Space getWorkerPosition() {
        return workerPosition;
    }


    /**
     * Setter of the parameter workerPosition
     * @param workerPosition indicates the value of parameter
     * @throws InvalidWorkerPositionException if the worker cannot move into that position
     */
    public void setWorkerPosition (Space workerPosition) throws InvalidWorkerPositionException {
        if(workerPosition.getHasDome() || workerPosition.getWorkerInPlace()!=null) throw new InvalidWorkerPositionException();
        else this.workerPosition = workerPosition;
    }



}
