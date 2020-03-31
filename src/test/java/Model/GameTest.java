package Model;

import org.junit.Test;

import static org.junit.Assert.*;

public class GameTest {

    @Test
    public void getNum_players() {
        int testNum_players = 2;
        Player testPlayer = new Player("test",3,null,null);
        Player testPlayer2 = new Player("test2",4,null,null);
        Player[] players = new Player[testNum_players];
        players[0] = testPlayer;
        players[1] = testPlayer2;
        Game testedGame = new Game(testNum_players,testPlayer,players);
        assertEquals(testNum_players,testedGame.getNum_players());
    }

    @Test
    public void getCurrentRound() {
        int testNum_players = 2;
        int testRound = 6;
        Player testPlayer = new Player("test",3,null,null);
        Player testPlayer2 = new Player("test2",4,null,null);
        Player[] players = new Player[testNum_players];
        players[0] = testPlayer;
        players[1] = testPlayer2;
        Game testedGame = new Game(testNum_players,testPlayer,players);
        testedGame.setCurrentRound(testRound);
        assertEquals(testRound,testedGame.getCurrentRound());

    }

    @Test
    public void setCurrentRound() {
        int testNum_players = 2;
        int testRound = 6;
        Player testPlayer = new Player("test",3,null,null);
        Player testPlayer2 = new Player("test2",4,null,null);
        Player[] players = new Player[testNum_players];
        players[0] = testPlayer;
        players[1] = testPlayer2;
        Game testedGame = new Game(testNum_players,testPlayer,players);
        testedGame.setCurrentRound(testRound);
        assertEquals(testRound,testedGame.getCurrentRound());
    }

    @Test
    public void getCurrentPhase() {
        int testNum_players = 2;
        Player testPlayer = new Player("test",3,null,null);
        Player testPlayer2 = new Player("test2",4,null,null);
        Player[] players = new Player[testNum_players];
        players[0] = testPlayer;
        players[1] = testPlayer2;
        Game testedGame = new Game(testNum_players,testPlayer,players);
        TurnPhase testPhase = TurnPhase.ANYTIME;
        testedGame.setCurrentPhase(testPhase);
        assertEquals(testPhase,testedGame.getCurrentPhase());
    }

    @Test
    public void setCurrentPhase() {
        int testNum_players = 2;
        Player testPlayer = new Player("test",3,null,null);
        Player testPlayer2 = new Player("test2",4,null,null);
        Player[] players = new Player[testNum_players];
        players[0] = testPlayer;
        players[1] = testPlayer2;
        Game testedGame = new Game(testNum_players,testPlayer,players);
        TurnPhase testPhase = TurnPhase.ANYTIME;
        testedGame.setCurrentPhase(testPhase);
        assertEquals(testPhase,testedGame.getCurrentPhase());
    }

    @Test
    public void getCurrentPlayer() {
        int testNum_players = 2;
        Player testPlayer = new Player("test",3,null,null);
        Player testPlayer2 = new Player("test2",4,null,null);
        Player[] players = new Player[testNum_players];
        players[0] = testPlayer;
        players[1] = testPlayer2;
        Game testedGame = new Game(testNum_players,testPlayer,players);
        testedGame.setCurrentPlayer(testPlayer);
        assertEquals(testPlayer,testedGame.getCurrentPlayer());
    }

    @Test
    public void setCurrentPlayer() {
        int testNum_players = 2;
        Player testPlayer = new Player("test",3,null,null);
        Player testPlayer2 = new Player("test2",4,null,null);
        Player[] players = new Player[testNum_players];
        players[0] = testPlayer;
        players[1] = testPlayer2;
        Game testedGame = new Game(testNum_players,testPlayer,players);
        testedGame.setCurrentPlayer(testPlayer);
        assertEquals(testPlayer,testedGame.getCurrentPlayer());
    }

    @Test
    public void getTowerWasCompleted() {
        int testNum_players = 2;
        Player testPlayer = new Player("test",3,null,null);
        Player testPlayer2 = new Player("test2",4,null,null);
        Player[] players = new Player[testNum_players];
        players[0] = testPlayer;
        players[1] = testPlayer2;
        Game testedGame = new Game(testNum_players,testPlayer,players);
        testedGame.setCurrentPlayer(testPlayer);
        testedGame.setTowerWasCompleted(true);
        assertTrue(testedGame.getTowerWasCompleted());
    }

    @Test
    public void setTowerWasCompleted() {
        int testNum_players = 2;
        Player testPlayer = new Player("test",3,null,null);
        Player testPlayer2 = new Player("test2",4,null,null);
        Player[] players = new Player[testNum_players];
        players[0] = testPlayer;
        players[1] = testPlayer2;
        Game testedGame = new Game(testNum_players,testPlayer,players);
        testedGame.setCurrentPlayer(testPlayer);
        testedGame.setTowerWasCompleted(true);
        assertTrue(testedGame.getTowerWasCompleted());
    }

    @Test
    public void setPlayers() {
        int testNum_players = 2;
        Player testPlayer = new Player("test",3,null,null);
        Player testPlayer2 = new Player("test2",4,null,null);
        Player[] players = new Player[testNum_players];
        players[0] = testPlayer;
        players[1] = testPlayer2;
        Game testedGame = new Game(testNum_players,testPlayer,players);
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
        int testNum_players = 2;
        Player testPlayer = new Player("test",3,null,null);
        Player testPlayer2 = new Player("test2",4,null,null);
        Player[] players = new Player[testNum_players];
        players[0] = testPlayer;
        players[1] = testPlayer2;
        Game testedGame = new Game(testNum_players,testPlayer,players);
        IslandBoard testBoard = new IslandBoard();
        testedGame.setGameBoard(testBoard);
        assertEquals(testBoard,testedGame.getGameBoard());
    }

    @Test
    public void setGameBoard() {
        int testNum_players = 2;
        Player testPlayer = new Player("test",3,null,null);
        Player testPlayer2 = new Player("test2",4,null,null);
        Player[] players = new Player[testNum_players];
        players[0] = testPlayer;
        players[1] = testPlayer2;
        Game testedGame = new Game(testNum_players,testPlayer,players);
        IslandBoard testBoard = new IslandBoard();
        testedGame.setGameBoard(testBoard);
        assertEquals(testBoard,testedGame.getGameBoard());
    }
}