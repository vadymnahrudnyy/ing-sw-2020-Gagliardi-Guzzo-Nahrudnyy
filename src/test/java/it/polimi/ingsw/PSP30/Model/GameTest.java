package it.polimi.ingsw.PSP30.Model;

import it.polimi.ingsw.PSP30.Model.Game;
import it.polimi.ingsw.PSP30.Model.IslandBoard;
import it.polimi.ingsw.PSP30.Model.Player;
import it.polimi.ingsw.PSP30.Model.TurnPhase;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameTest {
    int testRound;
    int testNum_players;
    Player testPlayer;
    Player testPlayer2;
    Player[] players;
    Game testedGame;

    @Before public void GameTestSetup(){
        testNum_players = 2;
        testPlayer = new Player("test",3,null,null);
        testPlayer2 = new Player("test2",4,null,null);
        players = new Player[testNum_players];
        players[0] = testPlayer;
        players[1] = testPlayer2;
        testRound = 6;
        testedGame = new Game(testNum_players,testPlayer,players);
    }

    @Test
    public void getNum_players() {
        assertEquals(testNum_players,testedGame.getNumPlayers());
    }

    @Test
    public void getCurrentRound() {
        testedGame.setCurrentRound(testRound);
        assertEquals(testRound,testedGame.getCurrentRound());
    }

    @Test
    public void setCurrentRound() {
        testedGame.setCurrentRound(testRound);
        assertEquals(testRound,testedGame.getCurrentRound());
    }

    @Test
    public void getCurrentPhase() {
        TurnPhase testPhase = TurnPhase.ANYTIME;
        testedGame.setCurrentPhase(testPhase);
        assertEquals(testPhase,testedGame.getCurrentPhase());
    }

    @Test
    public void setCurrentPhase() {
        TurnPhase testPhase = TurnPhase.ANYTIME;
        testedGame.setCurrentPhase(testPhase);
        assertEquals(testPhase,testedGame.getCurrentPhase());
    }

    @Test
    public void getCurrentPlayer() {
        testedGame.setCurrentPlayer(testPlayer);
        assertEquals(testPlayer,testedGame.getCurrentPlayer());
    }

    @Test
    public void setCurrentPlayer() {
        testedGame.setCurrentPlayer(testPlayer);
        assertEquals(testPlayer,testedGame.getCurrentPlayer());
    }

    @Test
    public void getTowerWasCompleted() {
        testedGame.setCurrentPlayer(testPlayer);
        testedGame.setTowerWasCompleted(true);
        assertTrue(testedGame.getTowerWasCompleted());
    }

    @Test
    public void setTowerWasCompleted() {
        testedGame.setCurrentPlayer(testPlayer);
        testedGame.setTowerWasCompleted(true);
        assertTrue(testedGame.getTowerWasCompleted());
    }

    @Test
    public void setPlayers() {
        Player testPlayer3 = new Player("test3",5,null,null);
        Player testPlayer4 = new Player("test4",6,null,null);
        Player [] newplayers = new Player[testNum_players];
        newplayers[0] = testPlayer3;
        newplayers[1] = testPlayer4;
        testedGame.setPlayers(newplayers);
        assertArrayEquals(newplayers,testedGame.getPlayers());
    }

    @Test
    public void getGameBoard() {
        IslandBoard testBoard = new IslandBoard();
        testedGame.setGameBoard(testBoard);
        assertEquals(testBoard,testedGame.getGameBoard());
    }

    @Test
    public void setGameBoard() {
        IslandBoard testBoard = new IslandBoard();
        testedGame.setGameBoard(testBoard);
        assertEquals(testBoard,testedGame.getGameBoard());
    }

    @Test
    public void nextTurnPhase() {
        testedGame.setCurrentPhase(TurnPhase.START);
        testedGame.nextTurnPhase();
        assertEquals(TurnPhase.MOVE,testedGame.getCurrentPhase());
        testedGame.nextTurnPhase();
        assertEquals(TurnPhase.BUILD,testedGame.getCurrentPhase());
        testedGame.nextTurnPhase();
        assertEquals(TurnPhase.END,testedGame.getCurrentPhase());
        testedGame.nextTurnPhase();
        assertEquals(TurnPhase.START,testedGame.getCurrentPhase());
    }

    @Test
    public void nextPlayer(){
        testedGame.setCurrentPlayer(testPlayer);
        testedGame.nextPlayer();
        assertEquals(testPlayer2,testedGame.getCurrentPlayer());
        testedGame.nextPlayer();
        assertEquals(testPlayer,testedGame.getCurrentPlayer());
    }

    @Test
    public void nextPlayer_threePlayersCase(){
        Player testPlayer3 = new Player("test3",3,null,null);
        Player[] threePlayers = new Player[3];
        threePlayers[0] = players[0];
        threePlayers[1] = players[1];
        threePlayers[2] = testPlayer3;
        Game newTestedGame = new Game(3,players[0],threePlayers);
        newTestedGame.nextPlayer();
        assertEquals(testPlayer2,newTestedGame.getCurrentPlayer());
        newTestedGame.nextPlayer();
        assertEquals(testPlayer3,newTestedGame.getCurrentPlayer());
        newTestedGame.nextPlayer();
        assertEquals(testPlayer,newTestedGame.getCurrentPlayer());
    }

}