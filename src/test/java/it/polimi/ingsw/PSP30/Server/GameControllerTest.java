package it.polimi.ingsw.PSP30.Server;

import it.polimi.ingsw.PSP30.Exception.Server.PlayerDisconnectedException;
import it.polimi.ingsw.PSP30.Messages.*;
import it.polimi.ingsw.PSP30.Model.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class GameControllerTest {
    Lobby testLobby;
    ServerSocket testSocket;
    GameController testGame;
    Thread testClientThread1, testClientThread2, testClientThread3;
    VirtualView testView1, testView2, testView3;
    ArrayList<VirtualView> testViewsList;
    LobbyTest.testClient testClient1, testClient2, testClient3;

    @Before
    public void setUp() {
        try {
            testSocket = new ServerSocket(LobbyTest.TEST_PORT);
        } catch (IOException e) {
            System.out.println("SocketCreationError");
        }
        testLobby = new Lobby();
        testClient1 = new LobbyTest.testClient();
        testClient2 = new LobbyTest.testClient();
        testClient3 = new LobbyTest.testClient();
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
        } catch (IOException e) {
            System.out.println("Caught: IOException");
        }
        testViewsList = new ArrayList<>();
        testViewsList.add(testView1);
        testViewsList.add(testView2);
        testViewsList.add(testView3);

        testGame = new GameController(testViewsList, 3);
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
        assertFalse(testGame.verifyValidPosition(testMoves,testMessage.getCoordinateX(),testMessage.getCoordinateY()));
        testMoves[1][1] = testMoves[2][3]= true;
        assertFalse(testGame.verifyValidPosition(testMoves,testMessage.getCoordinateX(),testMessage.getCoordinateY()));
        testMoves[1][2] = true;
        assertTrue(testGame.verifyValidPosition(testMoves,testMessage.getCoordinateX(),testMessage.getCoordinateY()));
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

    @Test
    public void countPossibleMoves() {
        boolean[][] testMoves = testGame.initializeMatrix(false);
        int possibleMoves = 0;
        assertEquals(possibleMoves,testGame.countPossibleMoves(testMoves));
        for (int i = 0; i < IslandBoard.TABLE_DIMENSION; i++)
            for (int j = 0; j < IslandBoard.TABLE_DIMENSION; j++){
                testMoves[i][j] = true;
                possibleMoves++;
                assertEquals(possibleMoves,testGame.countPossibleMoves(testMoves));
            }
    }

    @Test
    public void getOnlyMoveIndexes() {
        boolean[][] testMoves = testGame.initializeMatrix(false);
        assertNull(testGame.getOnlyMoveIndexes(testMoves));
        for (int i = 0; i < IslandBoard.TABLE_DIMENSION; i++)
            for (int j = 0; j < IslandBoard.TABLE_DIMENSION; j++){
                testMoves[i][j] = true;
                int[] onlyMove_test = testGame.getOnlyMoveIndexes(testMoves);
                assertEquals(i,onlyMove_test[0]);
                assertEquals(j,onlyMove_test[1]);
                testMoves[i][j] = false;
            }
    }

    @Test
    public void moveVictoryConditionSatisfied() {

    }

    @Test
    public void checkWorkerMovedUp() {
    }

    @Test
    public void makeMove() {
    }

    @Test
    public void isVictoryMove() {
    }

    @Test
    public void currentPlayerHasPower() {
    }

    @Test
    public void checkPossibleBuilds() {
    }

    @Test
    public void initializeMatrix() {

    }

    @Test
    public void testVerifyValidPosition() {
    }

    @Test
    public void testWorkerCanMakeMove() {
    }

    @Test
    public void removePlayerFromGame() {
    }

    @Test
    public void removePlayersWorkers() {
    }

    @Test
    public void possibleArtemisSecondMoveDestinations() {
        boolean[][] testArtemisSecondMoveDestinations = testGame.initializeMatrix(false);
        testGame.getCurrentGame().setCurrentPlayer(new Player("test",1,null,null));
        testGame.getCurrentBoard().getSpace(1,1).setWorkerInPlace(new Worker("test",'f',null,1));
        //worker current position X:1 Y:1 Previous position X:1 Y:2
        testArtemisSecondMoveDestinations[1][0] = true;
        testArtemisSecondMoveDestinations[1][1] = true;
        Space previous = testGame.getCurrentBoard().getSpace(1,2), current = testGame.getCurrentBoard().getSpace(1,1);
        assertTrue(Arrays.deepEquals(testArtemisSecondMoveDestinations, testGame.possibleArtemisSecondMoveDestinations(previous,current)));
        testArtemisSecondMoveDestinations[1][0] = false;
        testGame.getCurrentBoard().getSpace(2,1).setHeight(2);
        assertTrue(Arrays.deepEquals(testArtemisSecondMoveDestinations, testGame.possibleArtemisSecondMoveDestinations(previous,current)));
        testGame.getCurrentBoard().getSpace(2,2).setHasDome(true);
        testArtemisSecondMoveDestinations[1][1] = false;
        assertTrue(Arrays.deepEquals(testArtemisSecondMoveDestinations, testGame.possibleArtemisSecondMoveDestinations(previous,current)));
    }

    @Test
    public void checkHestiaAllowedBuilds() {
    }

    @Test
    public void getCurrentBoard() {
    }

    @Test
    public void setDisconnectionDetected() {
    }

    @Test
    public void useApolloPower() {
    }

    @Test
    public void useMinotaurPower() {
    }

    @Test
    public void handleDemeterPower() {
    }

    @Test
    public void handleHephaestusPower() {
    }

    @Test
    public void isPanVictoryMove() {
    }

    @Test
    public void handlePrometheusPower() {
    }

    @Test
    public void handleAresPower() {
    }

    @Test
    public void handleCronusPower() {
    }

    @Test
    public void victoryDeniedByHeraPower() {

    }

    @Test
    public void handleHestiaPower() {
    }

    @Test
    public void checkPossibleRemovals() {
        IslandBoard testBoard = testGame.getCurrentBoard();
        boolean[][] testRemovals = testGame.initializeMatrix(false);
        testBoard.getSpace(1,1).setHeight(3);
        testRemovals[0][0] = true;
        testBoard.getSpace(1,2).setHeight(2);
        testRemovals[0][1] = true;
        testBoard.getSpace(2,3).setHasDome(true);
        assertTrue(Arrays.deepEquals(testRemovals, testGame.checkPossibleRemovals(2, 2)));
        testBoard.getSpace(2,1).setHeight(1);
        testRemovals[1][0] = true;
        assertTrue(Arrays.deepEquals(testRemovals, testGame.checkPossibleRemovals(2, 2)));
    }
    @Test
    public void checkPossibleRemovals_secondTest() {
        IslandBoard testBoard = testGame.getCurrentBoard();
        boolean[][] testRemovals = testGame.initializeMatrix(false);
        testBoard.getSpace(2,1).setHasDome(true);
        testBoard.getSpace(2,2).setHasDome(true);
        testBoard.getSpace(1,2).setHasDome(true);
        assertTrue(Arrays.deepEquals(testRemovals, testGame.checkPossibleRemovals(1, 1)));
        testRemovals = testGame.initializeMatrix(false);
        testBoard.getSpace(5,4).setHasDome(true);
        testBoard.getSpace(4,5).setHasDome(true);
        testBoard.getSpace(4,4).setHeight(2);
        testRemovals[3][3] = true;
        assertTrue(Arrays.deepEquals(testRemovals, testGame.checkPossibleRemovals(5, 5)));
    }

    @Test
    public void checkPossiblePreBuild() {
        God Prometheus = null;
        for (God god:Server.getGodsList()) if (god.getName().equals("Prometheus")) Prometheus = god;
        Worker[] testWorkers = new Worker[2];
        IslandBoard testBoard = testGame.getCurrentBoard();
        testWorkers[0] = new Worker("test",'f',testBoard.getSpace(1,1),1);
        testWorkers[1] = new Worker("test",'f',testBoard.getSpace(5,5),1);
        Player testCurrentPlayer = new Player("test",1,testWorkers,Prometheus);
        testGame.getCurrentGame().setCurrentPlayer(testGame.currentPlayer = testCurrentPlayer);
        //Case both workers can move and build
        testBoard.getSpace(2,1).setHeight(2);
        testBoard.getSpace(2,2).setHasDome(true);
        testBoard.getSpace(1,2).setHeight(1);
        assertTrue(testGame.checkPossiblePreBuild());
        //only one worker can build and move
        testBoard.getSpace(4,4).setHeight(3);
        testBoard.getSpace(4,5).setHasDome(true);
        testBoard.getSpace(5,4).setHasDome(true);
        assertTrue(testGame.checkPossiblePreBuild());
        //none of workers can move
        testBoard.getSpace(2,1).setHasDome(true);
        testBoard.getSpace(1,2).setHasDome(true);
        assertFalse(testGame.checkPossiblePreBuild());
    }
}