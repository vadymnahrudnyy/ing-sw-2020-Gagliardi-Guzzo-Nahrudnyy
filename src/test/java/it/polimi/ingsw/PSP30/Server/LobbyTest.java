package it.polimi.ingsw.PSP30.Server;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;

import java.net.Socket;
import java.io.IOException;
import java.net.ServerSocket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import static org.junit.Assert.*;


public class LobbyTest {
    Lobby testLobby;
    VirtualView testView1, testView2,testView3;
    Thread testThread1, testThread2, testThread3;
    Thread testClientThread1,testClientThread2,testClientThread3;
    ServerSocket testSocket;
    private static int TEST_NUM_PLAYERS,testNum;
    private static final int TEST_PORT = 50000,EMPTY_LOBBY = 0, ONE_PLAYER_IN_LOBBY = 1, TWO_PLAYERS_IN_LOBBY = 2;

    @Before
    public void setup()  {
        try {
            testSocket = new ServerSocket(TEST_PORT);
        } catch (IOException e) {
            System.out.println("SocketCreationError");
        }
        testClient testClient1 = new testClient();
        testClient testClient2 = new testClient();
        testClient testClient3 = new testClient();
        testClientThread1 = new Thread(testClient1);
        testClientThread2 = new Thread(testClient2);
        testClientThread3 = new Thread(testClient3);
        testClientThread1.start();
        testClientThread2.start();
        testClientThread3.start();
        testLobby = new Lobby();
        try {
            testView1 = new VirtualView(testSocket.accept(), testLobby);
            testView2 = new VirtualView(testSocket.accept(), testLobby);
            testView3 = new VirtualView(testSocket.accept(), testLobby);
        }catch (IOException e){
            System.out.println("Caught: IOException");
        }
        testThread1 = new Thread(testView1);
        testThread2 = new Thread(testView2);
        testThread3 = new Thread(testView3);
        testView1.setUsername("test1");
        testView2.setUsername("test2");
        testView3.setUsername("test3");
        testThread1.start();
        testThread2.start();
        testThread3.start();
    }
    @After
    public void clean(){
        testLobby.removePlayerFromLobby(testView1,testView1.getUsername(),testThread1);
        testLobby.removePlayerFromLobby(testView2,testView2.getUsername(),testThread2);
        testLobby.removePlayerFromLobby(testView3,testView3.getUsername(),testThread3);
        try {
            testSocket.close();
        } catch (IOException e) {
            System.out.println("Failed to close the socket");
        }
    }

    @Test
    public void addPlayerToLobby() {
        TEST_NUM_PLAYERS= 2;
        testView1.setNumPlayers(TEST_NUM_PLAYERS);
        testView2.setNumPlayers(TEST_NUM_PLAYERS);
        testNum = testLobby.getTwoPlayersLobbySlotsOccupied();
        assertEquals(EMPTY_LOBBY,testNum);
        testLobby.addPlayerToLobby(testView1.getNumPlayers(),testView1,testView1.getUsername(),testThread1);
        testNum = testLobby.getTwoPlayersLobbySlotsOccupied();
        assertEquals(ONE_PLAYER_IN_LOBBY,testNum);
        testLobby.addPlayerToLobby(testView2.getNumPlayers(),testView2,testView2.getUsername(),testThread2);
        testNum = testLobby.getTwoPlayersLobbySlotsOccupied();
        assertEquals(EMPTY_LOBBY,testNum);

        //Two players collision test
        testLobby.addPlayerToLobby(testView2.getNumPlayers(),testView2,testView2.getUsername(),testThread2);
        testNum = testLobby.getTwoPlayersLobbySlotsOccupied();
        assertEquals(ONE_PLAYER_IN_LOBBY,testNum);
        testLobby.addPlayerToLobby(testView2.getNumPlayers(),testView2,testView2.getUsername(),testThread2);
        testNum = testLobby.getTwoPlayersLobbySlotsOccupied();
        assertEquals(ONE_PLAYER_IN_LOBBY,testNum);


        //Three players lobby test
        TEST_NUM_PLAYERS = 3;
        testView1.setNumPlayers(TEST_NUM_PLAYERS);
        testView2.setNumPlayers(TEST_NUM_PLAYERS);
        testView3.setNumPlayers(TEST_NUM_PLAYERS);
        testNum = testLobby.getThreePlayersLobbySlotsOccupied();
        assertEquals(EMPTY_LOBBY,testNum);
        testLobby.addPlayerToLobby(testView1.getNumPlayers(),testView1,testView1.getUsername(),testThread1);
        testNum = testLobby.getThreePlayersLobbySlotsOccupied();
        assertEquals(ONE_PLAYER_IN_LOBBY,testNum);
        testLobby.addPlayerToLobby(testView2.getNumPlayers(),testView2,testView2.getUsername(),testThread2);
        testNum = testLobby.getThreePlayersLobbySlotsOccupied();
        assertEquals(TWO_PLAYERS_IN_LOBBY,testNum);
        testLobby.addPlayerToLobby(testView3.getNumPlayers(),testView3,testView3.getUsername(),testThread3);
        testNum = testLobby.getThreePlayersLobbySlotsOccupied();
        assertEquals(EMPTY_LOBBY,testNum);

        //Three players collision test
        testLobby.addPlayerToLobby(testView3.getNumPlayers(),testView3,testView3.getUsername(),testThread3);
        testNum = testLobby.getThreePlayersLobbySlotsOccupied();
        assertEquals(ONE_PLAYER_IN_LOBBY,testNum);
        testLobby.addPlayerToLobby(testView3.getNumPlayers(),testView3,testView3.getUsername(),testThread3);
        testNum = testLobby.getThreePlayersLobbySlotsOccupied();
        assertEquals(ONE_PLAYER_IN_LOBBY,testNum);
    }

    @Test
    public void removePlayerFromLobby() {
        //Two Player lobby remove test
        TEST_NUM_PLAYERS = 2;
        testView1.setNumPlayers(TEST_NUM_PLAYERS);
        testNum = testLobby.getTwoPlayersLobbySlotsOccupied();
        assertEquals(EMPTY_LOBBY,testNum);
        testLobby.addPlayerToLobby(testView1.getNumPlayers(),testView1,testView1.getUsername(),testThread1);
        testNum = testLobby.getTwoPlayersLobbySlotsOccupied();
        assertEquals(ONE_PLAYER_IN_LOBBY,testNum);
        testLobby.removePlayerFromLobby(testView1,testView1.getUsername(),testThread1);
        testNum = testLobby.getTwoPlayersLobbySlotsOccupied();
        assertEquals(EMPTY_LOBBY,testNum);

        //Three players lobby remove test
        TEST_NUM_PLAYERS = 3;
        testView1.setNumPlayers(TEST_NUM_PLAYERS);
        testView2.setNumPlayers(TEST_NUM_PLAYERS);
        testNum = testLobby.getThreePlayersLobbySlotsOccupied();
        assertEquals(EMPTY_LOBBY,testNum);
        testLobby.addPlayerToLobby(testView1.getNumPlayers(),testView1,testView1.getUsername(),testThread1);
        testNum = testLobby.getThreePlayersLobbySlotsOccupied();
        assertEquals(ONE_PLAYER_IN_LOBBY,testNum);
        testLobby.addPlayerToLobby(testView2.getNumPlayers(),testView2,testView2.getUsername(),testThread2);
        testNum = testLobby.getThreePlayersLobbySlotsOccupied();
        assertEquals(TWO_PLAYERS_IN_LOBBY,testNum);
        testLobby.removePlayerFromLobby(testView2,testView2.getUsername(),testThread2);
        testNum = testLobby.getThreePlayersLobbySlotsOccupied();
        assertEquals(ONE_PLAYER_IN_LOBBY,testNum);
        testLobby.removePlayerFromLobby(testView1,testView1.getUsername(),testThread1);
        testNum = testLobby.getThreePlayersLobbySlotsOccupied();
        assertEquals(EMPTY_LOBBY,testNum);
    }

    private static class testClient implements Runnable{
        Socket socket;
        ObjectInputStream input;
        ObjectOutputStream output;

        @Override
        public void run() {
            try {
                socket = new Socket("127.0.0.1",TEST_PORT);
                input = new ObjectInputStream(socket.getInputStream());
                output = new ObjectOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}