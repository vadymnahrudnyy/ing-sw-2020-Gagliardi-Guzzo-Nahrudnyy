package it.polimi.ingsw.PSP30.Model;

import java.io.Serializable;

/**
 * This class manages the player during the game.
 * @author Alessia Gagliardi
 * @version 1.0
 */

public class Player implements Serializable {

    private static final long serialVersionUID = 50006L;
    private final String username;
    private final int userID;
    private Worker [ ] workers;
    private boolean hasMovedWorker;
    private boolean hasBuilt;
    private God god;

    /**
     * Builds an instance of Player class.
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

    /**
     * Getter of the parameter username.
     * @return the value of username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Getter of the parameter userID.
     * @return the value of userID
     */
    public int getUserID() {
        return userID;
    }

    /**
     * Getter of the parameter workers.
     * @return the value of workers, array of Worker
     */
    public Worker[] getWorkers() {
        return workers;
    }

    /**
     * Getter of the parameter workers.
     * @param i number of the worker
     * @return the value of workers[i]
     */
    public Worker getWorker(int i){
        return workers[i];
    }

    /**
     * Setter of the parameter workers.
     * @param workers array of Worker
     */
    public void setWorkers(Worker[] workers) {
        this.workers = workers;
    }

    /**
     * Getter of the parameter hasMovedWorker.
     * @return boolean value of hasMovedWorker
     */
    public boolean isHasMovedWorker() {
        return hasMovedWorker;
    }

    /**
     * Setter of the parameter hasMovedWorker.
     * @param hasMovedWorker true if the worker has moved, otherwise false
     */
    public void setHasMovedWorker(boolean hasMovedWorker) {
        this.hasMovedWorker = hasMovedWorker;
    }

    /**
     * Getter of the parameter hasBuilt.
     * @return boolean value of hasBuilt
     */
    public boolean isHasBuilt() {
        return hasBuilt;
    }

    /**
     * Setter of the parameter hasBuilt.
     * @param hasBuilt true if the god has built, otherwise false
     */
    public void setHasBuilt(boolean hasBuilt) {
        this.hasBuilt = hasBuilt;
    }

    /**
     * Getter of the parameter god.
     * @return the value of god
     */
    public God getGod() {
        return god;
    }

    /**
     * Setter of the parameter god.
     * @param god god considered
     */
    public void setGod(God god) {
        this.god = god;
    }
}
