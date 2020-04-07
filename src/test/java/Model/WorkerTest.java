package Model;

import Exception.Worker.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class WorkerTest {

    @Test
    public void changePosition() {
        Space currPosition = new Space(4,3,4);
        Worker testWorker= new Worker(23, "Tom", 'm', currPosition);
        Space newPosition = new Space(2,2,4);
        testWorker.changePosition(newPosition);
        assertEquals(newPosition, testWorker.getWorkerPosition());
    }

    @Test
    public void getWorkerID() {
        Space currentPosition = new Space(4,3,4);
        Worker testWorker = new Worker(30, "Tom", 'm', currentPosition);
        assertEquals(30,testWorker.getWorkerID());
    }

    @Test
    public void getOwner() {
        Space currentPosition = new Space(4,3,4);
        Worker testWorker = new Worker(30, "Tom", 'm', currentPosition);
        assertEquals("Tom",testWorker.getOwner());

    }

    @Test
    public void isWasMoved() {
        Space currentPosition = new Space(4,3,4);
        Worker testWorker = new Worker(30, "Tom", 'm', currentPosition);
        testWorker.setWasMoved(true);
        assertTrue(testWorker.isWasMoved());
    }


    @Test
    public void setWasMoved() {
        Space currentPosition = new Space(4,3,4);
        Worker testWorker = new Worker(30, "Tom", 'm', currentPosition);
        testWorker.setWasMoved(true);
        assertTrue(testWorker.isWasMoved());
    }

    @Test
    public void isMovedUp() {
        Space currentPosition = new Space(4,3,4);
        Worker testWorker = new Worker(30, "Tom", 'm', currentPosition);
        testWorker.setMovedUp(true);
        assertTrue(testWorker.isMovedUp());
    }

    @Test
    public void setMovedUp() {
        Space currentPosition = new Space(4,3,4);
        Worker testWorker = new Worker(30, "Tom", 'm', currentPosition);
        testWorker.setMovedUp(true);
        assertTrue(testWorker.isMovedUp());
    }

    @Test
    public void getGender() {
        Space currentPosition = new Space(4,3,4);
        Worker testWorker = new Worker(30, "Tom", 'm', currentPosition);
        assertEquals('m',testWorker.getGender());
    }

    @Test
    public void getWorkerPosition() {
        Space currentPosition = new Space(4,3,4);
        Worker testWorker = new Worker(30, "Tom", 'm', currentPosition);
        testWorker.setWorkerPosition(currentPosition);
        assertEquals(currentPosition,testWorker.getWorkerPosition());
    }

    @Test
    public void setWorkerPosition() {
        Space currentPosition = new Space(4,3,4);
        Worker testWorker = new Worker(30, "Tom", 'm', null);
        testWorker.setWorkerPosition(currentPosition);
        assertEquals(currentPosition,testWorker.getWorkerPosition());
    }

      /*  @Test (expected = InvalidWorkerPositionException.class)
        public void setWorkerPositionException()  {
        Space currentPosition = new Space(4,3,4);
        Worker placedWorker = new Worker(30, "Tom", 'm', null);
        placedWorker.setWorkerPosition(currentPosition);
        Worker testWorker = new Worker(30, "Tom", 'm', null);
        testWorker.setWorkerPosition(currentPosition);
        assertEquals(currentPosition,testWorker.getWorkerPosition());
    }*/
}
