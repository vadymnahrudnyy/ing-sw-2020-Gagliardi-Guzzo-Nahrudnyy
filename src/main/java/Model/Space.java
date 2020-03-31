package Model;


import Exceptions.Space.*;

/**
 * @author Vadym Nahrudnyy
 * @version 1.0
 */

public class Space {
    private int height;
    private boolean hasDome;
    private final boolean isPerimetral;
    private final int coordinateX;
    private final int coordinateY;

    private Worker workerInPlace;

    /**
     * @param coordinateX indicates the column of the table where the new Space will be located
     * @param coordinateY indicates the line of the table where the new Space will be located
     * @param TableDimension indicates the dimension (counting from 0) of the Game Table, supposed to be a square
     */
    public Space(int coordinateX, int coordinateY,int TableDimension) {
        this.height = 0;
        this.hasDome = false;
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
        this.workerInPlace = null; //Workers will be placed by players during the Setup.
        if ((coordinateX == 0)||(coordinateY == 0)||(coordinateX == TableDimension)||(coordinateY == TableDimension)){
            this.isPerimetral = true;
        }
        else {
            this.isPerimetral = false;
        }
    }

    public void setHeight(int height) throws InvalidHeightException {
        int maxHeight = 4;
        if ((height > 0)&&(height <= maxHeight)) {
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

    public void buildDome() throws DomeAlreadyBuiltException{
        boolean Dome = getHasDome();
        if (!Dome) {
            setHasDome(true);
            incrementHeight();
        }
        else throw new DomeAlreadyBuiltException();
    }

    public void removeDome() throws DomeAlreadyMissingException{
        boolean Dome = getHasDome();
        if (Dome) {
            setHasDome(false);
            decrementHeight();
        }
        else throw new DomeAlreadyMissingException();
    }

    public void setWorkerInPlace(Worker workerInPlace) {
        this.workerInPlace = workerInPlace;
    }
    public void removeWorkerInPlace(){
        this.workerInPlace = null;
    }

    public Worker getWorkerInPlace() {
        return workerInPlace;
    }

    public void incrementHeight() throws TowerCompleteException {
        int height = getHeight();
        if (height < 4)setHeight(++height);
        else throw new TowerCompleteException();
    }
    public void decrementHeight() throws MissingTowerException {
        int height = getHeight();
        if (height == 0) throw new MissingTowerException();
        else setHeight(--height);
    }






}






