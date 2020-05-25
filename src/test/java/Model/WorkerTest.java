package Model;


import it.polimi.ingsw.PSP30.Model.Space;
import it.polimi.ingsw.PSP30.Model.Worker;
import it.polimi.ingsw.PSP30.Exception.Worker.InvalidWorkerPositionException;
import org.junit.Test;

import static org.junit.Assert.*;

public class WorkerTest {

    @Test
    public void changePosition() {
        Space currPosition = new Space(4,3);
        Worker testWorker= new Worker("Owner", 'm', currPosition, 1);
        Space newPosition = new Space(2,2);
        testWorker.changePosition(newPosition);
        assertEquals(newPosition, testWorker.getWorkerPosition());
    }


    @Test
    public void isWasMoved() {
        Space currentPosition = new Space(4,3);
        Worker testWorker = new Worker("Owner", 'm', currentPosition, 1);
        testWorker.setWasMoved(true);
        assertTrue(testWorker.isWasMoved());
    }


    @Test
    public void setWasMoved() {
        Space currentPosition = new Space(4,3);
        Worker testWorker = new Worker("Owner", 'm', currentPosition, 1);
        testWorker.setWasMoved(true);
        assertTrue(testWorker.isWasMoved());
    }

    @Test
    public void isMovedUp() {
        Space currentPosition = new Space(4,3);
        Worker testWorker = new Worker("Owner", 'm', currentPosition, 1);
        testWorker.setMovedUp(true);
        assertTrue(testWorker.isMovedUp());
    }

    @Test
    public void setMovedUp() {
        Space currentPosition = new Space(4,3);
        Worker testWorker = new Worker("Owner", 'm', currentPosition, 1);
        testWorker.setMovedUp(true);
        assertTrue(testWorker.isMovedUp());
    }

    @Test
    public void getGender() {
        Space currentPosition = new Space(4,3);
        Worker testWorker = new Worker("Owner", 'm',currentPosition, 1);
        assertEquals('m',testWorker.getGender());
    }

    @Test
    public void getWorkerPosition() {
        Space currentPosition = new Space(4,3);
        Worker testWorker = new Worker("Owner", 'm', currentPosition, 1);
        testWorker.setWorkerPosition(currentPosition);
        assertEquals(currentPosition,testWorker.getWorkerPosition());
    }

    @Test
    public void setWorkerPosition() {
        Space currentPosition = new Space(4,3);
        Worker testWorker = new Worker("Owner", 'm', currentPosition, 1);
        testWorker.setWorkerPosition(currentPosition);
        currentPosition.setWorkerInPlace(testWorker);
        assertEquals(currentPosition,testWorker.getWorkerPosition());
    }

    @Test
    public void getColor(){
        int color=1;
        Space currentPosition = new Space(4,3);
        Worker testWorker = new Worker("Owner", 'm', currentPosition, color);
        assertEquals(color, testWorker.getColor());
    }


    @Test (expected = InvalidWorkerPositionException.class)
    public void setPositionTest2(){
        Space currentPosition = new Space(4,3);
        Worker placedWorker = new Worker("Tom", 'm', currentPosition, 1);
        placedWorker.setWorkerPosition(currentPosition);
        currentPosition.setWorkerInPlace(placedWorker);
        Worker testWorker = new Worker("Tom",'m' ,currentPosition , 1);
        testWorker.setWorkerPosition(currentPosition);
        assertEquals(currentPosition,testWorker.getWorkerPosition());
    }
}
