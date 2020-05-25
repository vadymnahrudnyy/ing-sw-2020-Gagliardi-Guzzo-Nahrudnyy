package it.polimi.ingsw.PSP30.Model;



import it.polimi.ingsw.PSP30.Exception.Space.*;

import java.io.Serializable;

/**
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

    public void setHeight(int height) {
        int maxHeight = 4;
        if ((height >= 0)&&(height <= maxHeight)) {
            this.height = height;
        }
        else throw new InvalidHeightException();
    }

    public int getHeight() {
        return height;
    }

    public void setHasDome(boolean hasDome) {
        this.hasDome = hasDome;
    }

    public int getCoordinateX() {
        return coordinateX;
    }

    public int getCoordinateY() {
        return coordinateY;
    }

    public boolean getHasDome() {
        return hasDome;
    }

    public void buildDome() {
        boolean Dome = getHasDome();
        if (!Dome) {
            setHasDome(true);
            incrementHeight();
        }
        else throw new DomeAlreadyBuiltException();
    }

    public void removeDome() {
        boolean Dome = getHasDome();
        if (Dome) {
            setHasDome(false);
            decrementHeight();
        }
        else throw new DomeAlreadyMissingException();
    }

    public void setWorkerInPlace(Worker consideredWorker) {
        this.workerInPlace = consideredWorker;
    }

    public void removeWorkerInPlace(){
        this.setWorkerInPlace(null);
    }

    public Worker getWorkerInPlace() {
        return workerInPlace;
    }

    public void incrementHeight() {
        int height = getHeight();
        if (height < 4)setHeight(++height);
        else throw new TowerCompleteException();
    }

    public void decrementHeight() {
        int height = getHeight();
        if (height == 0) throw new MissingTowerException();
        else setHeight(height-1);
    }

    public boolean isPerimeter() {
        return isPerimeter;
    }
}






