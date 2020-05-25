package it.polimi.ingsw.PSP30.Model;

import java.io.Serializable;

/**
 * @author Vadym Nahrudnyy
 * @version 1.0
 */

public class IslandBoard implements Serializable {
    private static final long serialVersionUID = 50002L;
    public static final int TABLE_DIMENSION = 5; //Indicates the dimension of the game board, assumed to be a square. Count starting from 0
    private int numberCompleteTowers;
    private Space[][] matrix;

    /**
     * This method builds the game board. The game board is represented as a matrix N*N where N is the dimension of the board.
     * It doesn't need any parameter during the setting of the board.
     */
    public IslandBoard() {
        this.numberCompleteTowers = 0;
        this.matrix = new Space[TABLE_DIMENSION][TABLE_DIMENSION];
        int coordinateX,coordinateY;
        for (coordinateX = 0;coordinateX < TABLE_DIMENSION;++coordinateX){
            for (coordinateY = 0; coordinateY < TABLE_DIMENSION; ++coordinateY){
                matrix [coordinateX][coordinateY] = new Space(coordinateX+1,coordinateY+1);
            }
        }
    }

    public void setMatrix(Space[][] newmatrix) {
        this.matrix = newmatrix;
    }

    public Space[][] getMatrix() {
        return matrix;
    }

    public int getNumberCompleteTowers() {
        return numberCompleteTowers;
    }

    /**
     * Method getSpace is used to get a single Space from the board.
     * @param coordinateX indicates the X coordinate of the wanted space.
     * @param coordinateY indicates the Y coordinate of the wanted space.
     * @return the desired Space.
     */
    public Space getSpace(int coordinateX, int coordinateY) {
        return matrix[coordinateX-1][coordinateY-1];
    }

    public void setNumberCompleteTowers(int numberCompleteTowers) {
        this.numberCompleteTowers = numberCompleteTowers;
    }

    /**
     * The method increments the number of completed towers when a complete tower is built.
     */
    public void incrementNumberCompleteTowers() {
        int CompleteTowers = getNumberCompleteTowers();
        setNumberCompleteTowers(++CompleteTowers);
    }

}
