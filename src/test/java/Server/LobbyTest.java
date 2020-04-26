package Server;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.Assert.*;

public class LobbyTest {
    Lobby testLobby;
    String username1, username2, username3;
    VirtualView virtualView1, virtualView2, virtualView3;

    @Before
    public void Setup () {
        testLobby = new Lobby();
        username1 = "test1";
        username2 = "test2";
        username3 = "test3";
        try{
            virtualView1 = new VirtualView(new Socket(),testLobby);
            virtualView2 = new VirtualView(new Socket(),testLobby);
            virtualView3 = new VirtualView(new Socket(),testLobby);

        } catch (IOException e){
            System.out.println("Lobby class test Setup Failed");
        }
    }

    @Test
    public void getTwoPlayersLobby() {
        assertEquals(testLobby.getTwoPlayersLobby(),testLobby.getTwoPlayersLobby());
    }

    @Test
    public void getTwoPlayersLobbyVirtualViews() {
        assertEquals(testLobby.getTwoPlayersLobbyVirtualViews(),testLobby.getTwoPlayersLobbyVirtualViews());
    }

    @Test
    public void getTwoPlayersLobbySlotsOccupied() {
        assertEquals(0,testLobby.getTwoPlayersLobbySlotsOccupied());
        testLobby.addPlayerToTwoPlayersLobby(username1,virtualView1);
        assertEquals(1,testLobby.getTwoPlayersLobbySlotsOccupied());
        testLobby.addPlayerToTwoPlayersLobby(username2,virtualView2);
        assertEquals(2,testLobby.getTwoPlayersLobbySlotsOccupied());
    }

    @Test
    public void addPlayerToTwoPlayersLobby() {
        assertEquals(0,testLobby.getTwoPlayersLobbySlotsOccupied());
        testLobby.addPlayerToTwoPlayersLobby(username1,virtualView1);
        assertEquals(1,testLobby.getTwoPlayersLobbySlotsOccupied());
        assertTrue(testLobby.getTwoPlayersLobby().contains(username1));
        assertTrue(testLobby.getTwoPlayersLobbyVirtualViews().contains(virtualView1));
        testLobby.addPlayerToTwoPlayersLobby(username2,virtualView2);
        assertEquals(2,testLobby.getTwoPlayersLobbySlotsOccupied());
        assertTrue(testLobby.getTwoPlayersLobby().contains(username2));
        assertTrue(testLobby.getTwoPlayersLobbyVirtualViews().contains(virtualView2));
    }

    @Test
    public void removePlayerFromTwoPlayersLobby() {
        testLobby.addPlayerToTwoPlayersLobby(username1,virtualView1);
        testLobby.addPlayerToTwoPlayersLobby(username2,virtualView2);
        assertEquals(2,testLobby.getTwoPlayersLobbySlotsOccupied());
        testLobby.removePlayerFromTwoPlayersLobby(username2,virtualView2);
        assertEquals(1,testLobby.getTwoPlayersLobbySlotsOccupied());
        //assertFalse(testLobby.getTwoPlayersLobbyVirtualViews().contains(virtualView2));
        assertFalse(testLobby.getTwoPlayersLobby().contains(username2));
        testLobby.removePlayerFromTwoPlayersLobby(username1,virtualView1);
        assertEquals(0,testLobby.getTwoPlayersLobbySlotsOccupied());
        assertFalse(testLobby.getTwoPlayersLobby().contains(username1));
        //assertFalse(testLobby.getTwoPlayersLobbyVirtualViews().contains(virtualView1));
    }

    @Test
    public void getThreePlayersLobby() {
        assertEquals(testLobby.getThreePlayersLobby(),testLobby.getThreePlayersLobby());
    }
    @Test
    public void getThreePlayersLobbyVirtualViews() {
        assertEquals(testLobby.getThreePlayersLobbyVirtualViews(),testLobby.getThreePlayersLobbyVirtualViews());
    }
    @Test
    public void getThreePlayersLobbySlotsOccupied() {
        assertEquals(0,testLobby.getThreePlayersLobbySlotsOccupied());

    }

    @Test
    public void addPlayerToThreePlayersLobby() {
        assertEquals(0,testLobby.getThreePlayersLobbySlotsOccupied());
        testLobby.addPlayerToThreePlayersLobby(username1,virtualView1);
        assertEquals(1,testLobby.getThreePlayersLobbySlotsOccupied());
        testLobby.addPlayerToThreePlayersLobby(username2,virtualView2);
        assertEquals(2,testLobby.getThreePlayersLobbySlotsOccupied());
        testLobby.addPlayerToThreePlayersLobby(username3,virtualView3);
        assertEquals(3,testLobby.getThreePlayersLobbySlotsOccupied());
    }

    @Test
    public void removePlayerFromThreePlayersLobby() {
        assertEquals(0,testLobby.getThreePlayersLobbySlotsOccupied());
        testLobby.addPlayerToThreePlayersLobby(username1,virtualView1);
        testLobby.addPlayerToThreePlayersLobby(username2,virtualView2);
        testLobby.addPlayerToThreePlayersLobby(username3,virtualView3);
        assertEquals(3,testLobby.getThreePlayersLobbySlotsOccupied());
        testLobby.removePlayerFromThreePlayersLobby(username1,virtualView1);
        assertEquals(2,testLobby.getThreePlayersLobbySlotsOccupied());
        testLobby.removePlayerFromThreePlayersLobby(username2,virtualView2);
        assertEquals(1,testLobby.getThreePlayersLobbySlotsOccupied());
        testLobby.removePlayerFromThreePlayersLobby(username3,virtualView3);
        assertEquals(0,testLobby.getThreePlayersLobbySlotsOccupied());

    }

    @Test
    public void resetTwoPlayersLobby() {
        assertEquals(0,testLobby.getTwoPlayersLobbySlotsOccupied());
        testLobby.addPlayerToTwoPlayersLobby(username1,virtualView1);
        testLobby.addPlayerToTwoPlayersLobby(username2,virtualView2);
        testLobby.setTwoPlayersLobbyReady(true);
        testLobby.resetTwoPlayersLobby();
        assertEquals(0,testLobby.getTwoPlayersLobbySlotsOccupied());
        assertFalse(testLobby.getThreePlayersLobbyReady());
    }
    @Test
    public void resetThreePlayersLobby() {
        assertEquals(0,testLobby.getThreePlayersLobbySlotsOccupied());
        testLobby.addPlayerToThreePlayersLobby(username1,virtualView1);
        testLobby.addPlayerToThreePlayersLobby(username2,virtualView2);
        testLobby.addPlayerToThreePlayersLobby(username3,virtualView3);
        testLobby.setThreePlayersLobbyReady(true);
        assertEquals(3,testLobby.getThreePlayersLobbySlotsOccupied());
        assertTrue(testLobby.getThreePlayersLobbyReady());
        testLobby.resetThreePlayersLobby();
        assertEquals(0,testLobby.getThreePlayersLobbySlotsOccupied());
        assertFalse(testLobby.getThreePlayersLobbyReady());
    }
    @Test
    public void getThreePlayersLobbyReady(){
        assertEquals(testLobby.getThreePlayersLobbyReady(),testLobby.getThreePlayersLobbyReady());
    }
    @Test
    public void getTwoPlayersLobbyReady(){
        assertEquals(testLobby.getTwoPlayersLobbyReady(),testLobby.getTwoPlayersLobbyReady());
    }

    @Test
    public void setTwoPlayersLobbyReady() {
        testLobby.setTwoPlayersLobbyReady(false);
        assertFalse(testLobby.getTwoPlayersLobbyReady());
        testLobby.setTwoPlayersLobbyReady(true);
        assertTrue(testLobby.getTwoPlayersLobbyReady());
    }

    @Test
    public void setThreePlayersLobbyReady() {
        testLobby.setThreePlayersLobbyReady(false);
        assertFalse(testLobby.getThreePlayersLobbyReady());
        testLobby.setThreePlayersLobbyReady(true);
        assertTrue(testLobby.getThreePlayersLobbyReady());
    }

    @Test
    public void checkReady() {
        testLobby.checkReady();
        assertFalse(testLobby.getTwoPlayersLobbyReady());
        assertFalse(testLobby.getThreePlayersLobbyReady());
        testLobby.addPlayerToTwoPlayersLobby(username1,virtualView1);
        testLobby.addPlayerToTwoPlayersLobby(username2,virtualView2);
        testLobby.checkReady();
        assertTrue(testLobby.getTwoPlayersLobbyReady());
        assertFalse(testLobby.getThreePlayersLobbyReady());
    }

    @Test
    /*two players lobby not ready,three players lobby ready*/
    public void checkReadyTest2(){
        testLobby.checkReady();
        assertFalse(testLobby.getTwoPlayersLobbyReady());
        assertFalse(testLobby.getThreePlayersLobbyReady());
        testLobby.addPlayerToThreePlayersLobby(username1,virtualView1);
        testLobby.addPlayerToThreePlayersLobby(username2,virtualView2);
        testLobby.addPlayerToThreePlayersLobby(username3,virtualView3);
        assertEquals(3,testLobby.getThreePlayersLobbySlotsOccupied());
        testLobby.checkReady();
        assertFalse(testLobby.getTwoPlayersLobbyReady());
        assertTrue(testLobby.getThreePlayersLobbyReady());
    }

    @Test
    public void checkReadyTest3(){
        testLobby.checkReady();
        assertFalse(testLobby.getTwoPlayersLobbyReady());
        assertFalse(testLobby.getThreePlayersLobbyReady());
        testLobby.addPlayerToTwoPlayersLobby(username1,virtualView1);
        testLobby.addPlayerToTwoPlayersLobby(username2,virtualView2);
        testLobby.addPlayerToThreePlayersLobby(username1,virtualView1);
        testLobby.addPlayerToThreePlayersLobby(username2,virtualView2);
        testLobby.addPlayerToThreePlayersLobby(username3,virtualView3);
        assertEquals(2,testLobby.getTwoPlayersLobbySlotsOccupied());
        assertEquals(3,testLobby.getThreePlayersLobbySlotsOccupied());
        testLobby.checkReady();
        assertTrue(testLobby.getThreePlayersLobbyReady());
        assertTrue(testLobby.getTwoPlayersLobbyReady());
    }
}