package Model;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class PlayerTest {

    int[] power;
    God testGod;
    Player testPlayer;
    Worker[] testWorkers;
    Space firstCurrentPosition;
    Space secondCurrentPosition;

    @Before
    public void PlayerTestSetUp(){
        power= new int[1];
        power[0]=8;
        testGod = new God("Minotauro", 1, 3, "il mostro dalla testa di toro", power);
        firstCurrentPosition = new Space(4,3,4);
        secondCurrentPosition = new Space(2,3,4);
        testWorkers=new Worker[2];
        testWorkers[0] = new Worker(30, "Tom", 'm', firstCurrentPosition);
        testWorkers[1] = new Worker(50, "Tom", 'm', secondCurrentPosition);

    }

    @Test
    public void getUsername() {
        testPlayer= new Player("Tom", 30, testWorkers, testGod );
        assertEquals("Tom",testPlayer.getUsername());
    }

    @Test
    public void getUserID() {
        testPlayer= new Player("Tom", 30, testWorkers, testGod );
        assertEquals(30,testPlayer.getUserID());
    }

    @Test
    public void getWorkers() {
        testPlayer= new Player("Tom", 30, testWorkers, testGod );
        assertArrayEquals(testWorkers,testPlayer.getWorkers());
    }

    @Test
    public void getWorker() {
        testPlayer= new Player("Tom", 30, testWorkers, testGod );
        assertEquals(testWorkers[0],testPlayer.getWorker(0));

    }

    @Test
    public void setWorkers() {
        testPlayer= new Player("Tom", 30, null, testGod );
        testPlayer.setWorkers(testWorkers);
        assertArrayEquals(testWorkers,testPlayer.getWorkers());
    }

    @Test
    public void isHasMovedWorker() {
        testPlayer= new Player("Tom", 30, testWorkers, testGod );
        testPlayer.setHasMovedWorker(true);
        assertTrue(testPlayer.isHasMovedWorker());
    }

    @Test
    public void setHasMovedWorker() {
        testPlayer= new Player("Tom", 30, testWorkers, testGod );
        testPlayer.setHasMovedWorker(true);
        assertTrue(testPlayer.isHasMovedWorker());
    }

    @Test
    public void isHasBuilt() {
        testPlayer= new Player("Tom", 30, testWorkers, testGod );
        testPlayer.setHasBuilt(true);
        assertTrue(testPlayer.isHasBuilt());
    }

    @Test
    public void setHasBuilt() {
        testPlayer= new Player("Tom", 30, testWorkers, testGod );
        testPlayer.setHasBuilt(true);
        assertTrue(testPlayer.isHasBuilt());
    }

    @Test
    public void getGod() {
        testPlayer= new Player("Tom", 30, testWorkers, testGod );
        assertEquals(testGod, testPlayer.getGod());
    }

    @Test
    public void setGod() {
        testPlayer= new Player("Tom", 30, testWorkers, null );
        testPlayer.setGod(testGod);
        assertEquals(testGod,testPlayer.getGod());


    }
}