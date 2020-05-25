package Model;

import it.polimi.ingsw.PSP30.Model.IslandBoard;
import it.polimi.ingsw.PSP30.Model.Space;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class IslandBoardTest {
    int testedNumTowers;
    IslandBoard testBoard;
    IslandBoard newTestBoard;

    @Before public void IslandBoardTestSetup(){
        testedNumTowers = 4;
        testBoard = new IslandBoard();
        newTestBoard = new IslandBoard();
    }

    @Test
    public void setMatrix() {
        testBoard.setMatrix(newTestBoard.getMatrix());
        assertArrayEquals(newTestBoard.getMatrix(),testBoard.getMatrix());
    }

    @Test
    public void getMatrix() {
        /*
        Create 2 Island boards, sets the matrix of the first equals to the second
        and then checks that method getMatrix return the correct value
         */
        testBoard.setMatrix(newTestBoard.getMatrix());
        assertArrayEquals(newTestBoard.getMatrix(),testBoard.getMatrix());
    }

    @Test
    public void getNumberCompleteTowers() {
        testBoard.setNumberCompleteTowers(testedNumTowers);
        assertEquals(testedNumTowers,testBoard.getNumberCompleteTowers());
    }

    @Test
    public void getSpace() {
        int consideredX = 3,consideredY=3;
        Space testedSpace = newTestBoard.getSpace(consideredX,consideredY);
        testBoard.setMatrix(newTestBoard.getMatrix());
        assertEquals(testedSpace,testBoard.getSpace(consideredX,consideredY));
    }

    @Test
    public void setNumberCompleteTowers() {
        testBoard.setNumberCompleteTowers(testedNumTowers);
        assertEquals(testedNumTowers,testBoard.getNumberCompleteTowers());
    }

    @Test
    public void incrementNumberCompleteTowers() {
        testBoard.setNumberCompleteTowers(testedNumTowers);
        testBoard.incrementNumberCompleteTowers();
        assertEquals(testedNumTowers+1,testBoard.getNumberCompleteTowers());
    }
}
