package Model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class DeckTest {


    @Test
    public void getCardList() {
        Power[] power= new Power[1];
        power[0] = new Power(true, true, false, TurnPhase.MOVE);
        God godid = new God(8, "Minotauro", 1, 3, "il mostro dalla testa di toro", power);
        ArrayList<God> cardlist = new ArrayList<>(Arrays.asList(godid));
        Deck deck = new Deck(cardlist);
        assertEquals(cardlist,deck.getCardList());
    }

    @Test
    public void setCardList() {
        Power[] power= new Power[1];
        power[0] = new Power(true, true, false, TurnPhase.MOVE);
        God godid = new God(8, "Minotauro", 1, 3, "il mostro dalla testa di toro", power);
        ArrayList<God> cardlist = new ArrayList<>(Arrays.asList(godid));
        Deck deck = new Deck(null);
        deck.setCardList(cardlist);
        assertEquals(cardlist,deck.getCardList());
    }
}