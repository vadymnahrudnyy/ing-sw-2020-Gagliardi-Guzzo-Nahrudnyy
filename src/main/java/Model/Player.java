package Model;

/**
 * @author Alessia Gagliardi
 * @version 1.0
 */


public class Player{
    /**
     * This class creates an object Player.
     */

    private final String username;
    private final int userID;
    private Worker [ ] workers;
    private boolean hasMovedWorker;
    private boolean hasBuilt;
    private God god;

    public Player(String username, int userID, Worker[] workers, God god) {
        this.username = username;
        this.userID = userID;
        this.workers = new Worker[2];
        this.hasMovedWorker = false;
        this.hasBuilt = false;
        this.god = god;
    }

    public String getUsername() {
        return username;
    }

    public int getUserID() {
        return userID;
    }

    public Worker[] getWorkers() {
        return workers;
    }

    public Worker getworker(int i){
        return workers[i];
    }

    public void setWorkers(Worker[] workers) {
        this.workers = workers;
    }

    public boolean isHasMovedWorker() {
        return hasMovedWorker;
    }

    public void setHasMovedWorker(boolean hasMovedWorker) {
        this.hasMovedWorker = hasMovedWorker;
    }

    public boolean isHasBuilt() {
        return hasBuilt;
    }

    public void setHasBuilt(boolean hasBuilt) {
        this.hasBuilt = hasBuilt;
    }

    public God getGod() {
        return god;
    }

    public void setGod(God god) {
        this.god = god;
    }
}
