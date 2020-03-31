package Model;

/**
 * @author Vadym Nahrudnyy
 * @version 1.0
 */

public class IslandBoard {
    final int TableDimension = 4; //Indicates the dimension of the game board, assumed to be a square. Count starting from 0
    private int numberCompleteTowers;
    private Space[][] matrix;

    /**
     * This method builds the game board. The game board is represented as a matrix N*N where N is the dimension of the board.
     */
    public IslandBoard() {
        this.numberCompleteTowers = 0;
        this.matrix = new Space[TableDimension+1][TableDimension +1];
        int coordinateX,coordinateY;
        for (coordinateX = 0;coordinateX<=TableDimension;++coordinateX){
            for (coordinateY = 0; coordinateY <= TableDimension; ++coordinateY){
                matrix [coordinateX][coordinateY] = new Space(coordinateX,coordinateY,TableDimension);
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

    public Space getSpace(int coordinateX, int coordinateY) {
        return matrix[coordinateX][coordinateY];
    }

    public void setNumberCompleteTowers(int numberCompleteTowers) {
        this.numberCompleteTowers = numberCompleteTowers;
    }

    public void incrementNumberCompleteTowers() {
        int CompleteTowers = getNumberCompleteTowers();
        setNumberCompleteTowers(++CompleteTowers);
    }
    public void decrementNumberCompleteTowers(){
        int CompleteTowers = getNumberCompleteTowers();
        setNumberCompleteTowers(--CompleteTowers);
    }
}
