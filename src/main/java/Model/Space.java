package Model;

/**
 * @author Vadym Nahrudnyy
 * @version 1.0
 */

public class Space {
    private int height;
    private final int coordinateX;
    private final int coordinateY;
    private boolean hasDome;
    private Worker workerInPlace;

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
    }

    public void setHeight(int height) {
        this.height = height;
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

    public void setWorkerInPlace(Worker workerInPlace) {
        this.workerInPlace = workerInPlace;
    }

    public Worker getWorkerInPlace() {
        return workerInPlace;
    }
}

