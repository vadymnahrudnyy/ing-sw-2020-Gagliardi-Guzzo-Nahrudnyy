package Model;

import org.junit.Test;

import static org.junit.Assert.*;

public class IslandBoardTest {

    @Test
    public void setMatrix() {
        IslandBoard testBoard = new IslandBoard();
        IslandBoard newTestBoard = new IslandBoard();
        testBoard.setMatrix(newTestBoard.getMatrix());
        assertArrayEquals(newTestBoard.getMatrix(),testBoard.getMatrix());
    }

    @Test
    public void getMatrix() {
        /*
        Create 2 Island boards, sets the matrix of the first equals to the second
        and then checks that method getMatrix return the correct value
         */
        IslandBoard testBoard = new IslandBoard();
        IslandBoard newTestBoard = new IslandBoard();
        testBoard.setMatrix(newTestBoard.getMatrix());
        assertArrayEquals(newTestBoard.getMatrix(),testBoard.getMatrix());
    }

    @Test
    public void getNumberCompleteTowers() {
        IslandBoard testBoard = new IslandBoard();
        testBoard.setNumberCompleteTowers(5);
        assertEquals(5,testBoard.getNumberCompleteTowers());
    }

    @Test
    public void getSpace() {
        int consideredX = 3,consideredY=3;
        IslandBoard testBoard = new IslandBoard();
        IslandBoard newTestBoard = new IslandBoard();
        Space testedSpace = newTestBoard.getSpace(consideredX,consideredY);
        testBoard.setMatrix(newTestBoard.getMatrix());
        assertEquals(testedSpace,testBoard.getSpace(consideredX,consideredY));
    }

    @Test
    public void setNumberCompleteTowers() {
        IslandBoard testBoard = new IslandBoard();
        testBoard.setNumberCompleteTowers(6);
        assertEquals(6,testBoard.getNumberCompleteTowers());
    }

    @Test
    public void incrementNumberCompleteTowers() {
        IslandBoard testBoard = new IslandBoard();
        int testedNumTowers = 5;
        testBoard.setNumberCompleteTowers(testedNumTowers);
        testBoard.incrementNumberCompleteTowers();
        assertEquals(testedNumTowers+1,testBoard.getNumberCompleteTowers());
    }

    @Test
    public void decrementNumberCompleteTowers() {
        IslandBoard testBoard = new IslandBoard();
        int testedNumTowers = 5;
        testBoard.setNumberCompleteTowers(testedNumTowers);
        testBoard.decrementNumberCompleteTowers();
        assertEquals(testedNumTowers-1,testBoard.getNumberCompleteTowers());
    }
}