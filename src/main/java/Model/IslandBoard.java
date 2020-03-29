package Model;

/**
 * @author Vadym Nahrudnyy
 * @version 1.0
 */

public class IslandBoard {
    final int TableDimension = 5;
    private int numberCompleteTowers;
    private Space[][] matrix;

    public void setMatrix(Space[][] matrix) {
        this.matrix = new Space[TableDimension][TableDimension];
    }

    public Space[][] getMatrix() {
        return matrix;
    }

    public int getNumberCompleteTowers() {
        return numberCompleteTowers;
    }

    public Space getSpace(int coordinateX, int coordinateY) {
        return matrix[coordinateX][coordinateY];
    }

    public void setNumberCompleteTowers(int numberCompleteTowers) {
        this.numberCompleteTowers = numberCompleteTowers;
    }
}
