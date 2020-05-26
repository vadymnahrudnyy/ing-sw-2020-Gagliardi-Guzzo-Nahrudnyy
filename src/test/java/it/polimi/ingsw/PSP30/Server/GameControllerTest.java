package it.polimi.ingsw.PSP30.Server;

import it.polimi.ingsw.PSP30.Messages.WorkerPositionResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class GameControllerTest {
    Lobby testLobby;
    ServerSocket testSocket;
    GameController testGame;
    Thread testClientThread1, testClientThread2, testClientThread3;
    VirtualView testView1, testView2, testView3;
    ArrayList<VirtualView> testViewsList;

    @Before
    public void setUp(){
        try {
            testSocket = new ServerSocket(50000);
        } catch (IOException e) {
            System.out.println("SocketCreationError");
        }
        testLobby = new Lobby();
        LobbyTest.testClient testClient1 = new LobbyTest.testClient();
        LobbyTest.testClient testClient2 = new LobbyTest.testClient();
        LobbyTest.testClient testClient3 = new LobbyTest.testClient();
        testClientThread1 = new Thread(testClient1);
        testClientThread2 = new Thread(testClient2);
        testClientThread3 = new Thread(testClient3);
        testClientThread1.start();
        testClientThread2.start();
        testClientThread3.start();

        try {
            testView1 = new VirtualView(testSocket.accept(), testLobby);
            testView2 = new VirtualView(testSocket.accept(), testLobby);
            testView3 = new VirtualView(testSocket.accept(), testLobby);
        }catch (IOException e){
            System.out.println("Caught: IOException");
        }
        testViewsList = new ArrayList<>();
        testViewsList.add(testView1);
        testViewsList.add(testView2);
        testViewsList.add(testView3);
    }

    @After
    public void tearDown() throws Exception {
        testSocket.close();
    }

    @Test
    public void verifyValidPosition() {
        testGame = new GameController(testViewsList,2);
        WorkerPositionResponse testMessage = new WorkerPositionResponse(2,3);
        boolean[][]testMoves = testGame.initializeMatrix(false);
        assertFalse(testGame.verifyValidPosition(testMoves,testMessage));
        testMoves[1][1] = testMoves[2][3]= true;
        assertFalse(testGame.verifyValidPosition(testMoves,testMessage));
        testMoves[1][2] = true;
        assertTrue(testGame.verifyValidPosition(testMoves,testMessage));
    }

    @Test
    public void workerCanMakeMove() {
        //test case 1 - no possible moves
        testGame = new GameController(testViewsList,2);
        boolean[][] testMoves = testGame.initializeMatrix(false);
        assertFalse(testGame.workerCanMakeMove(testMoves));
        //test case 2 - only one possible move
        testMoves[2][3] = true;
        assertTrue(testGame.workerCanMakeMove(testMoves));
        //test case 3 - impossible but simulates the case when more than one move is possible
        testMoves = testGame.initializeMatrix(true);
        assertTrue(testGame.workerCanMakeMove(testMoves));
    }

}