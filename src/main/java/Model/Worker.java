package Model;

/**
 * @author Alessia Gagliardi
 * @version 1.0
 */


public class Worker{

    private final int workerID;
    private final String owner;
    private boolean wasMoved;
    private boolean movedUp;
    private final char gender;
    private Space workerPosition;


    public Worker(int workerID, String owner, char gender, Space workerPosition) {
        this.workerID = workerID;
        this.owner = owner;
        this.wasMoved = false ;
        this.movedUp = false;
        this.gender = gender;
        this.workerPosition = null;
    }

    /**
     * This method allows the worker to change position.
     * @param newPosition tells where the worker should be moved to.
     */

    public void changePosition(Space newPosition){
       
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

    public void setWorkerPosition(Space workerPosition) {
        this.workerPosition = workerPosition;
    }
}
