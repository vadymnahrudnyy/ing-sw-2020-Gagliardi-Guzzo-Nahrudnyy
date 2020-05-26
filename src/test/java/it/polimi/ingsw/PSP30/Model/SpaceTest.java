package it.polimi.ingsw.PSP30.Model;


import it.polimi.ingsw.PSP30.Exception.Space.*;
import it.polimi.ingsw.PSP30.Model.Space;
import it.polimi.ingsw.PSP30.Model.Worker;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SpaceTest {
    Space testedSpace;
    Space perimetralSpace;
    int testCoordinateX;
    int testCoordinateY;
    int testTableDimension;
    Worker testWorker;

    @Before public void SpaceTestSetup(){
        testCoordinateX = 5;
        testCoordinateY = 3;
        testTableDimension = 4;
        testedSpace = new Space(testCoordinateX,testCoordinateY);
        Worker testWorker = new Worker("TestOwner",'f',testedSpace ,1);
    }

    @Test
    public void setHeightTest1() {
        testedSpace.setHeight(4);
        assertEquals(4,testedSpace.getHeight());
    }
    @Test (expected = InvalidHeightException.class)
    public void setHeightTest2(){
        testedSpace.setHeight(5);
        assertEquals(5,testedSpace.getHeight());
    }

    @Test
    public void getHeight() {
        testedSpace.setHeight(4);
        assertEquals(4,testedSpace.getHeight());
    }

    @Test
    public void setHasDome() {
        testedSpace.setHasDome(true);
        assertTrue(testedSpace.getHasDome());
    }

    @Test
    public void getCoordinateX() {
        assertEquals(testCoordinateX,testedSpace.getCoordinateX());
    }

    @Test
    public void getCoordinateY() {
        assertEquals(testCoordinateY,testedSpace.getCoordinateY());
    }

    @Test
    public void getHasDome() {
        testedSpace.setHasDome(true);
        assertTrue(testedSpace.getHasDome());
    }

    @Test
    public void buildDome() {
        testedSpace.setHasDome(false);
        testedSpace.setHeight(0);
        testedSpace.buildDome();
        assertTrue(testedSpace.getHasDome());
    }

    @Test (expected = DomeAlreadyBuiltException.class)
    public void buildDomeException() {
        testedSpace.setHasDome(true);
        testedSpace.setHeight(3);
        testedSpace.buildDome();
        assertTrue(testedSpace.getHasDome());
    }

    @Test
    public void removeDome() {
        testedSpace.setHeight(0);
        testedSpace.buildDome();
        testedSpace.removeDome();
        assertFalse(testedSpace.getHasDome());
    }

    @Test (expected = DomeAlreadyMissingException.class)
    public void removeDomeException() {
        testedSpace.setHeight(1);
        testedSpace.setHasDome(false);
        testedSpace.removeDome();
        assertFalse(testedSpace.getHasDome());
    }

    @Test
    public void setWorkerInPlace() {
        testedSpace.setWorkerInPlace(testWorker);
        assertEquals(testWorker,testedSpace.getWorkerInPlace());
    }

    @Test
    public void removeWorkerInPlace() {
        testedSpace.setWorkerInPlace(testWorker);
        testedSpace.removeWorkerInPlace();
        assertNull(null,testedSpace.getWorkerInPlace());
    }

    @Test
    public void getWorkerInPlace() {
        testedSpace.setWorkerInPlace(testWorker);
        assertEquals(testWorker,testedSpace.getWorkerInPlace());
    }

    @Test
    public void incrementHeight() {
        testedSpace.setHeight(3);
        testedSpace.incrementHeight();
        assertEquals(4,testedSpace.getHeight());
    }
    @Test (expected = TowerCompleteException.class)
    public void incrementHeightException() {
        testedSpace.setHeight(4);
        testedSpace.incrementHeight();
        assertEquals(5,testedSpace.getHeight());
    }

    @Test
    public void decrementHeight() {
        testedSpace.setHeight(3);
        testedSpace.decrementHeight();
        assertEquals(2,testedSpace.getHeight());
    }

    @Test (expected = MissingTowerException.class)
    public void decrementHeightException() {
        testedSpace.setHeight(0);
        testedSpace.decrementHeight();
        assertEquals(-1,testedSpace.getHeight());
    }
    @Test
    public void buildPerimeterSpace(){
        perimetralSpace = new Space(1,2);
        assertTrue(perimetralSpace.isPerimeter());
        System.out.println(1);
        perimetralSpace = new Space(3,1);
        assertTrue(perimetralSpace.isPerimeter());
        perimetralSpace = new Space(5,2);
        assertTrue(perimetralSpace.isPerimeter());
        perimetralSpace = new Space(3,5);
        assertTrue(perimetralSpace.isPerimeter());
        perimetralSpace = new Space(3,3);
        assertFalse(perimetralSpace.isPerimeter());
    }
}