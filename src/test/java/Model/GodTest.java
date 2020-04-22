package Model;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class GodTest {

    int[] power;
    God god;

    @Before public void GodTestSetUp() {

        power= new int[1];
        power[0] = 6;
        god = new God("Minotauro", 1, 3, "il mostro dalla testa di toro", power);
    }

    @Test
    public void getName() {
        assertEquals("Minotauro",god.getName());
    }

    @Test
    public void getPlayersAllowed() {
        assertEquals(3,god.getPlayersAllowed());
    }

    @Test
    public void getDescription() {
        assertEquals("il mostro dalla testa di toro",god.getDescription());
    }


    @Test
    public void testGetPowers() { assertArrayEquals(power,god.getPowers()); }

    @Test
    public void testGetSinglePower() { assertEquals(power[0],god.getSinglePower(0));
    }
}