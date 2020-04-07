package Model;

import org.junit.Test;

import static org.junit.Assert.*;

public class PlayerTest {

    @Test
    public void getUsername() {
        Power[] power= new Power[1];
        power[0] = new Power(true, true, false, TurnPhase.MOVE);
        God testGod = new God(8, "Minotauro", 1, 3, "il mostro dalla testa di toro", power);
        Space currentPosition = new Space(4,3,4);
        Worker[] testworker=new Worker[1];
        testworker[0] = new Worker(30, "Tom", 'm', currentPosition);
        Player testPlayer= new Player("Tom", 30, testworker, testGod );
        assertEquals("Tom",testPlayer.getUsername());
    }

    @Test
    public void getUserID() {
        Power[] power= new Power[1];
        power[0] = new Power(true, true, false, TurnPhase.MOVE);
        God testGod = new God(8, "Minotauro", 1, 3, "il mostro dalla testa di toro", power);
        Space currentPosition = new Space(4,3,4);
        Worker[] testworker=new Worker[1];
        testworker[0] = new Worker(30, "Tom", 'm', currentPosition);
        Player testPlayer= new Player("Tom", 30, testworker, testGod );
        assertEquals(30,testPlayer.getUserID());
    }

    @Test
    public void getWorkers() {
        Space currentPosition1 = new Space(4,3,4);
        Space currentPosition2 = new Space(2,3,4);
        Worker[] testWorkers=new Worker[2];
        testWorkers[0] = new Worker(30, "Player1", 'm', currentPosition1);
        testWorkers[1] = new Worker(25, "Player1", 'm', currentPosition2);
        Player testPlayer= new Player("Player1", 30, testWorkers, null );
        assertArrayEquals(testWorkers,testPlayer.getWorkers());
    }

    @Test
    public void getWorker() {

        Space currentPosition1 = new Space(4,3,4);
        Space currentPosition2 = new Space(2,3,4);
        Worker[] testWorkers=new Worker[2];
        testWorkers[0] = new Worker(30, "Player1", 'm', currentPosition1);
        testWorkers[1] = new Worker(25, "Player1", 'm', currentPosition2);
        Player testPlayer= new Player("Player1", 30, testWorkers, null );
        assertEquals(testWorkers[0],testPlayer.getWorker(0));

    }

    @Test
    public void setWorkers() {
        Space currentPosition = new Space(4,3,4);
        Player testPlayer = new Player("test",3,null,null);
        Worker[] testworker=new Worker[1];
        testworker[0] = new Worker(30, "Tom", 'm', currentPosition);
        testPlayer.setWorkers(testworker);
        assertArrayEquals(testworker,testPlayer.getWorkers());
    }

    @Test
    public void isHasMovedWorker() {
        Power[] power = new Power[1];
        power[0] = new Power(true, true, false, TurnPhase.MOVE);
        God testGod = new God(8, "Minotauro", 1, 3, "il mostro dalla testa di toro", power);
        Space currentPosition = new Space(4, 3, 4);
        Worker[] testworker = new Worker[1];
        testworker[0] = new Worker(30, "Tom", 'm', currentPosition);
        Player testPlayer = new Player("Tom", 30, testworker, testGod);
        testPlayer.setHasMovedWorker(true);
        assertTrue(testPlayer.isHasMovedWorker());
    }

    @Test
    public void setHasMovedWorker() {
        Power[] power = new Power[1];
        power[0] = new Power(true, true, false, TurnPhase.MOVE);
        God testGod = new God(8, "Minotauro", 1, 3, "il mostro dalla testa di toro", power);
        Space currentPosition = new Space(4, 3, 4);
        Worker[] testworker = new Worker[1];
        testworker[0] = new Worker(30, "Tom", 'm', currentPosition);
        Player testPlayer = new Player("Tom", 30, testworker, testGod);
        testPlayer.setHasMovedWorker(true);
        assertTrue(testPlayer.isHasMovedWorker());
    }

    @Test
    public void isHasBuilt() {
        Power[] power = new Power[1];
        power[0] = new Power(true, true, false, TurnPhase.MOVE);
        God testGod = new God(8, "Minotauro", 1, 3, "il mostro dalla testa di toro", power);
        Space currentPosition = new Space(4, 3, 4);
        Worker[] testworker = new Worker[1];
        testworker[0] = new Worker(30, "Tom", 'm', currentPosition);
        Player testPlayer = new Player("Tom", 30, testworker, testGod);
        testPlayer.setHasBuilt(true);
        assertTrue(testPlayer.isHasBuilt());
    }

    @Test
    public void setHasBuilt() {
        Power[] power = new Power[1];
        power[0] = new Power(true, true, false, TurnPhase.MOVE);
        God testGod = new God(8, "Minotauro", 1, 3, "il mostro dalla testa di toro", power);
        Space currentPosition = new Space(4, 3, 4);
        Worker[] testworker = new Worker[1];
        testworker[0] = new Worker(30, "Tom", 'm', currentPosition);
        Player testPlayer = new Player("Tom", 30, testworker, testGod);
        testPlayer.setHasBuilt(true);
        assertTrue(testPlayer.isHasBuilt());
    }

    @Test
    public void getGod() {
        Power[] power= new Power[1];
        power[0] = new Power(true, true, false, TurnPhase.MOVE);
        God testGod = new God(8, null, 1, 3, "il mostro dalla testa di toro", power);
        Space currentPosition = new Space(4,3,4);
        Worker[] testworker=new Worker[1];
        testworker[0] = new Worker(30, "Tom", 'm', currentPosition);
        Player testPlayer= new Player("Tom", 30, testworker, testGod );
        assertEquals(testGod, testPlayer.getGod());
    }

    @Test
    public void setGod() {
        Power[] power= new Power[1];
        power[0] = new Power(true, true, false, TurnPhase.MOVE);
        God testGod = new God(8, null, 1, 3, "il mostro dalla testa di toro", power);
        Space currentPosition = new Space(4,3,4);
        Worker[] testworker=new Worker[1];
        testworker[0] = new Worker(30, "Tom", 'm', currentPosition);
        Player testPlayer= new Player("Tom", 30, testworker, null );
        testPlayer.setGod(testGod);
        assertEquals(testGod,testPlayer.getGod());


    }
}