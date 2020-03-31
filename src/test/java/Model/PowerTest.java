package Model;

import org.junit.Test;

import static org.junit.Assert.*;

public class PowerTest {

    @Test
    public void getActive() {
        Power power= new Power(false,true,false,TurnPhase.WINCONDITION);
        assertEquals(false,power.getActive());
    }

    @Test
    public void getPlayerTurn() {
        Power power= new Power(false,true,false,TurnPhase.WINCONDITION);
        assertEquals(true,power.getPlayerTurn());
    }

    @Test
    public void getOpponent() {
        Power power= new Power(false,true,false,TurnPhase.WINCONDITION);
        assertEquals(false,power.getOpponent());
    }

    @Test
    public void getTurnPhase() {
        Power power= new Power(false,true,false,TurnPhase.WINCONDITION);
        assertEquals(TurnPhase.WINCONDITION,power.getTurnPhase());
    }
}