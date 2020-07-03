package it.polimi.ingsw.PSP30.Model;



import it.polimi.ingsw.PSP30.Exception.Space.*;

import java.io.Serializable;

/**
 * This class manages the spaces which form the IslandBoard.
 * @author Vadym Nahrudnyy
 * @version 1.0
 */

public class Space implements Serializable {
    private static final long serialVersionUID = 50003L;
    private int height;
    private boolean hasDome;
    private final boolean isPerimeter;
    private final int coordinateX;
    private final int coordinateY;

    private Worker workerInPlace;

    public static final int DOME_LEVEL = 4;

    /**
     * Builds an instance of Space class.
     * @param coordinateX indicates the column of the table where the new Space will be located
     * @param coordinateY indicates the line of the table where the new Space will be located
     */
    public Space(int coordinateX, int coordinateY) {
        this.height = 0;
        this.hasDome = false;
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
        this.workerInPlace = null; //Workers will be placed by players during the Setup.
        this.isPerimeter = (coordinateX == 1)||(coordinateY == 1)||(coordinateX == IslandBoard.TABLE_DIMENSION)||(coordinateY == IslandBoard.TABLE_DIMENSION);
    }

    /**
     * Setter of the parameter height.
     * @param height number of buildings (on a space)
     */
    public void setHeight(int height) {
        int maxHeight = 4;
        if ((height >= 0)&&(height <= maxHeight)) {
            this.height = height;
        }
        else throw new InvalidHeightException();
    }

    /**
     * Getter of the parameter height.
     * @return the value of height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Setter of the parameter hasDome.
     * @param hasDome true if there is a dome, otherwise false
     */
    public void setHasDome(boolean hasDome) {
        this.hasDome = hasDome;
    }

    /**
     * Getter of the parameter coordinateX.
     * @return the value of coordinateX
     */
    public int getCoordinateX() {
        return coordinateX;
    }

    /**
     * Getter of the parameter coordinateY.
     * @return the value of coordinateY
     */
    public int getCoordinateY() {
        return coordinateY;
    }

    /**
     * Getter of the parameter hasDome.
     * @return boolean value of hasDome
     */
    public boolean getHasDome() {
        return hasDome;
    }

    /**
     * Adds the dome in the considered space.
     */
    public void buildDome() {
        boolean Dome = getHasDome();
        if (!Dome) {
            setHasDome(true);
            incrementHeight();
        }
        else throw new DomeAlreadyBuiltException();
    }

    /**
     * Removes the dome from the considered space.
     */
    public void removeDome() {
        boolean Dome = getHasDome();
        if (Dome) {
            setHasDome(false);
            decrementHeight();
        }
        else throw new DomeAlreadyMissingException();
    }

    /**
     * Setter of the parameter consideredWorker.
     * @param consideredWorker current Worker
     */
    public void setWorkerInPlace(Worker consideredWorker) {
        this.workerInPlace = consideredWorker;
    }

    /**
     * Removes the worker in the considered space.
     */
    public void removeWorkerInPlace(){
        this.setWorkerInPlace(null);
    }

    /**
     * Getter of the parameter workerInPlace.
     * @return the value of workerInPlace
     */
    public Worker getWorkerInPlace() {
        return workerInPlace;
    }

    /**
     * Increments the current value of height.
     */
    public void incrementHeight() {
        int height = getHeight();
        if (height < 4)setHeight(++height);
        else throw new TowerCompleteException();
    }

    /**
     * Decrements the current value of height.
     */
    public void decrementHeight() {
        int height = getHeight();
        if (height == 0) throw new MissingTowerException();
        else setHeight(height-1);
    }

    /**
     * Getter of the parameter isPerimeter.
     * @return boolean value of isPerimeter
     */
    public boolean isPerimeter() {
        return isPerimeter;
    }
}






