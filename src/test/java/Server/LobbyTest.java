package Server;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LobbyTest {
    int MaxServerPlayers;
    Lobby testLobby;
    String username1;
    String username2;
    String username3;

    @Before
    public void Setup () {
        MaxServerPlayers = 200;
        testLobby = new Lobby(MaxServerPlayers);
        username1 = "test1";
        username2 = "test2";
        username3 = "test3";
    }

    @Test
    public void getMaxServerPlayers(){
        assertEquals(MaxServerPlayers,testLobby.getMaxServerPlayers());
    }
    @Test
    public void getServerPlayers() {
        assertEquals(testLobby.getServerPlayers(),testLobby.getServerPlayers());
    }
    @Test
    public void getNumberPlayersConnectedToServer() {
        for (int i = 1; i <= testLobby.getMaxServerPlayers(); i++){
            testLobby.addPlayerToServer("test");
            assertEquals(i,testLobby.getNumberPlayersConnectedToServer());
        }
    }
    @Test
    public void addPlayerToServer() {
        testLobby.addPlayerToServer(username1);
        assertEquals(1,testLobby.getServerPlayers().size());
        assertTrue(testLobby.getServerPlayers().contains(username1));
        testLobby.addPlayerToServer(username2);
        assertEquals(2,testLobby.getServerPlayers().size());
        assertTrue(testLobby.getServerPlayers().contains(username2));
        testLobby.addPlayerToServer(username3);
        assertEquals(3,testLobby.getServerPlayers().size());
        assertTrue(testLobby.getServerPlayers().contains(username3));
    }
    @Test
    public void removePlayerFromServer() {
        testLobby.addPlayerToServer(username1);
        testLobby.addPlayerToServer(username2);
        testLobby.addPlayerToServer(username3);
        testLobby.removePlayerFromServer(username3);
        assertFalse(testLobby.getServerPlayers().contains(username3));
        testLobby.removePlayerFromServer(username2);
        assertFalse(testLobby.getServerPlayers().contains(username2));
        testLobby.removePlayerFromServer(username1);
        assertFalse(testLobby.getServerPlayers().contains(username1));
    }
    @Test
    public void getTwoPlayersLobby() {
        assertEquals(testLobby.getTwoPlayersLobby(),testLobby.getTwoPlayersLobby());
    }
    @Test
    public void getTwoPlayersLobbySlotsOccupied() {
        testLobby.addPlayerToTwoPlayersLobby(username1);
        assertEquals(1,testLobby.getTwoPlayersLobbySlotsOccupied());
        testLobby.addPlayerToTwoPlayersLobby(username2);
        assertEquals(2,testLobby.getTwoPlayersLobbySlotsOccupied());
    }

    @Test
    public void addPlayerToTwoPlayersLobby() {
        testLobby.addPlayerToTwoPlayersLobby(username1);
        assertEquals(1,testLobby.getTwoPlayersLobbySlotsOccupied());
        testLobby.addPlayerToTwoPlayersLobby(username2);
        assertEquals(2,testLobby.getTwoPlayersLobbySlotsOccupied());
    }
    @Test
    public void removePlayerFromTwoPlayersLobby() {
        testLobby.addPlayerToTwoPlayersLobby(username1);
        testLobby.addPlayerToTwoPlayersLobby(username2);
        assertTrue(testLobby.getTwoPlayersLobby().contains(username1));
        testLobby.removePlayerFromTwoPlayersLobby(username1);
        assertFalse(testLobby.getTwoPlayersLobby().contains(username1));
        assertTrue(testLobby.getTwoPlayersLobby().contains(username2));
        testLobby.removePlayerFromTwoPlayersLobby(username2);
        assertFalse(testLobby.getTwoPlayersLobby().contains(username2));
    }
    @Test
    public void getThreePlayersLobby() {
        assertEquals(testLobby.getThreePlayersLobby(),testLobby.getThreePlayersLobby());
    }

    @Test
    public void getThreePlayersLobbySlotsOccupied() {
        testLobby.addPlayerToThreePlayersLobby(username1);
        assertEquals(1,testLobby.getThreePlayersLobbySlotsOccupied());
        testLobby.addPlayerToThreePlayersLobby(username2);
        assertEquals(2,testLobby.getThreePlayersLobbySlotsOccupied());
        testLobby.addPlayerToThreePlayersLobby(username3);
        assertEquals(3,testLobby.getThreePlayersLobbySlotsOccupied());
    }

    @Test
    public void addPlayerToThreePlayersLobby() {
        testLobby.addPlayerToThreePlayersLobby(username1);
        assertEquals(1,testLobby.getThreePlayersLobbySlotsOccupied());
        testLobby.addPlayerToThreePlayersLobby(username2);
        assertEquals(2,testLobby.getThreePlayersLobbySlotsOccupied());
        testLobby.addPlayerToThreePlayersLobby(username3);
        assertEquals(3,testLobby.getThreePlayersLobbySlotsOccupied());
    }

    @Test
    public void removePlayerFromThreePlayersLobby() {
        testLobby.addPlayerToThreePlayersLobby(username1);
        testLobby.addPlayerToThreePlayersLobby(username2);
        testLobby.addPlayerToThreePlayersLobby(username3);
        assertEquals(3,testLobby.getThreePlayersLobbySlotsOccupied());
        testLobby.removePlayerFromThreePlayersLobby(username1);
        assertEquals(2,testLobby.getThreePlayersLobbySlotsOccupied());
        testLobby.removePlayerFromThreePlayersLobby(username2);
        assertEquals(1,testLobby.getThreePlayersLobbySlotsOccupied());
        testLobby.removePlayerFromThreePlayersLobby(username3);
        assertEquals(0,testLobby.getThreePlayersLobbySlotsOccupied());
    }

    @Test
    public void resetTwoPlayersLobby() {
        testLobby.addPlayerToTwoPlayersLobby(username1);
        testLobby.addPlayerToTwoPlayersLobby(username2);
        assertEquals(2,testLobby.getTwoPlayersLobbySlotsOccupied());
        testLobby.resetTwoPlayersLobby();
        assertEquals(0,testLobby.getTwoPlayersLobbySlotsOccupied());
    }

    @Test
    public void resetThreePlayersLobby() {
        testLobby.addPlayerToThreePlayersLobby(username1);
        testLobby.addPlayerToThreePlayersLobby(username2);
        testLobby.addPlayerToThreePlayersLobby(username3);
        assertEquals(3,testLobby.getThreePlayersLobbySlotsOccupied());
        testLobby.resetThreePlayersLobby();
        assertEquals(0,testLobby.getThreePlayersLobbySlotsOccupied());
    }
}