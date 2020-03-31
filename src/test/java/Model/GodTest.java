package Model;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class GodTest {


    @Test
    public void getGodID() {
        Power[] power= new Power[1];
        power[0] = new Power(true, true, false, TurnPhase.MOVE);
        God godid = new God(8, "Minotauro", 1, 3, "il mostro dalla testa di toro", power);
        assertEquals(8,godid.getGodID());
    }



    @Test
    public void getName() {
        Power[] power= new Power[1];
        power[0] = new Power(true, true, false, TurnPhase.MOVE);
        God godid = new God(8, "Minotauro", 1, 3, "il mostro dalla testa di toro", power);
        assertEquals("Minotauro",godid.getName());
    }

    @Test
    public void getPlayersAllowed() {
        Power[] power= new Power[1];
        power[0] = new Power(true, true, false, TurnPhase.MOVE);
        God godid = new God(8, "Minotauro", 1, 3, "il mostro dalla testa di toro", power);
        assertEquals(3,godid.getPlayersAllowed());
    }

    @Test
    public void getDescription() {
        Power[] power= new Power[1];
        power[0] = new Power(true, true, false, TurnPhase.MOVE);
        God godid = new God(8, "Minotauro", 1, 3, "il mostro dalla testa di toro", power);
        assertEquals("il mostro dalla testa di toro",godid.getDescription());
    }

    @Test
    public void getPowers() {
        Power[] power= new Power[1];
        power[0] = new Power(true, true, false, TurnPhase.MOVE);
        God godid = new God(8, "Minotauro", 1, 3, "il mostro dalla testa di toro", power);
        assertArrayEquals(power,godid.getPowers());
    }

    @Test
    public void getSinglePower() {
        Power[] power= new Power[1];
        power[0] = new Power(true, true, false, TurnPhase.MOVE);
        God godid = new God(8, "Minotauro", 1, 3, "il mostro dalla testa di toro", power);
        assertEquals(power[0],godid.getSinglePower(0));
    }
}