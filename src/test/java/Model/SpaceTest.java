package Model;

import Exceptions.Space.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;



public class SpaceTest {

    @Test
    public void setHeightTest1() {
        Space testedSpace = new Space(4,3,4);
        testedSpace.setHeight(4);
        assertEquals(4,testedSpace.getHeight());
    }
    @Test (expected = Exceptions.Space.InvalidHeightException.class)
    public void setHeightTest2() /*throws Space.InvalidHeightException*/ {
        Space testedSpace = new Space(4,3,4);
        testedSpace.setHeight(5);
        assertEquals(5,testedSpace.getHeight());
    }

    @Test
    public void getHeight() {
        Space testedSpace = new Space(4,3,4);
        testedSpace.setHeight(4);
        assertEquals(4,testedSpace.getHeight());

    }

    @Test
    public void setHasDome() {
        Space testedSpace = new Space(4,3,4);
        testedSpace.setHasDome(true);
        assertTrue(testedSpace.getHasDome());
    }

    @Test
    public void getCoordinateX() {
        Space testedSpace = new Space(4,3,4);
        assertEquals(4,testedSpace.getCoordinateX());
    }

    @Test
    public void getCoordinateY() {
        Space testedSpace = new Space(4,3,4);
        assertEquals(3,testedSpace.getCoordinateY());
    }

    @Test
    public void getHasDome() {
        Space testedSpace = new Space(4,3,4);
        testedSpace.setHasDome(true);
        assertTrue(testedSpace.getHasDome());

    }

    @Test
    public void buildDome() {
        Space testedSpace = new Space(4,3,4);
        //testedSpace.setHasDome(false);//to be sure it has not got a dome
        testedSpace.buildDome();
        assertTrue(testedSpace.getHasDome());
    }

    @Test
    public void removeDome() {
        Space testedSpace = new Space(4,3,4);
        testedSpace.buildDome();
        testedSpace.removeDome();
        assertFalse(testedSpace.getHasDome());
    }

    @Test
    public void setWorkerInPlace() {
        Space testedSpace = new Space(4,3,4);
        Worker testWorker = new Worker(12,"TestOwner", 'f',testedSpace);
        testedSpace.setWorkerInPlace(testWorker);
        assertEquals(testWorker,testedSpace.getWorkerInPlace());
    }

    @Test
    public void removeWorkerInPlace() {
        Space testedSpace = new Space(4,3,4);
        Worker testWorker = new Worker(12,"TestOwner", 'f',testedSpace);
        testedSpace.setWorkerInPlace(testWorker);
        testedSpace.removeWorkerInPlace();
        assertEquals(null,testedSpace.getWorkerInPlace());
    }

    @Test
    public void getWorkerInPlace() {
        Space testedSpace = new Space(4,3,4);
        Worker testWorker = new Worker(12,"TestOwner", 'f',testedSpace);
        testedSpace.setWorkerInPlace(testWorker);
        assertEquals(testWorker,testedSpace.getWorkerInPlace());
    }

    @Test
    public void incrementHeight() {
        Space testedSpace = new Space(4,3,4);
        testedSpace.setHeight(3);
        testedSpace.incrementHeight();
        assertEquals(4,testedSpace.getHeight());
    }

    @Test
    public void decrementHeight() {
        Space testedSpace = new Space(4,3,4);
        testedSpace.setHeight(3);
        testedSpace.decrementHeight();
        assertEquals(2,testedSpace.getHeight());
    }
}