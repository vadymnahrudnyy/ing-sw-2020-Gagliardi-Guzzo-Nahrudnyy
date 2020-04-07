package Model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class PowerTest {

    Power power;

    @Before
    public void PowerTestSetUp() {
        power = new Power(5, true, false, true, TurnPhase.WINCONDITION);
    }

    @Test
    public void getPowerID() {
        assertEquals(5, power.getPowerID());
    }

    @Test
    public void getActive() {
        assertEquals(true, power.getActive());
    }

    @Test
    public void getUsableOnPlayerTurn() {
        assertEquals(false, power.getUsableOnPlayerTurn());
    }

    @Test
    public void getValidOnOpponentTurn() {
        assertEquals(true, power.getValidOnOpponentTurn());
    }

    @Test
    public void getTurnPhase() {
        assertEquals(TurnPhase.WINCONDITION, power.getTurnPhase());
    }

}