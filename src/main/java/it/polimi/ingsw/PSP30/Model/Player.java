package it.polimi.ingsw.PSP30.Model;

import java.io.Serializable;

/**
 * @author Alessia Gagliardi
 * @version 1.0
 */


public class Player implements Serializable {
    /**
     * This class manages the player during the game.
     */
    private static final long serialVersionUID = 50006L;
    private final String username;
    private final int userID;
    private Worker [ ] workers;
    private boolean hasMovedWorker;
    private boolean hasBuilt;
    private God god;

    /**
     * @param username indicates the player's username.
     * @param userID associate the player with a unique identification number.
     * @param workers tells which are the worker associated with that player.
     * @param god tells which god card the player owm.
     */

    public Player(String username, int userID, Worker[] workers, God god) {
        this.username = username;
        this.userID = userID;
        this.workers = workers;
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

    public Worker getWorker(int i){
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
