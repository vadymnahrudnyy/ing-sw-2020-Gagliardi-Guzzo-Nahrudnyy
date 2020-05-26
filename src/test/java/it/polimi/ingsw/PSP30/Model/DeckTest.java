package it.polimi.ingsw.PSP30.Model;

import it.polimi.ingsw.PSP30.Model.Deck;
import it.polimi.ingsw.PSP30.Model.God;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class DeckTest {

    ArrayList<God> cardlist;
    int[] power;
    God god;

    @Before public void DeckTestSetUp() {

        power= new int[1];
        power[0] = 6;
        god = new God( "Minotauro", 1, 3, "il mostro dalla testa di toro", power);
        ArrayList<God> cardlist = new ArrayList<>(Arrays.asList(god));

    }

    @Test
    public void getCardList() {
        Deck deck = new Deck(cardlist);
        assertEquals(cardlist,deck.getCardList());
    }

    @Test
    public void setCardList() {
        Deck deck = new Deck(null);
        deck.setCardList(cardlist);
        assertEquals(cardlist,deck.getCardList());
    }
}