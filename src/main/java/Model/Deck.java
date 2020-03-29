package Model;
import java.util.ArrayList;

/**
 * @author Alessia Guzzo
 */

public class Deck {
    /**
     * This class uses java.util.ArrayList for implementing the ArrayList which
     * includes all the types of GodCard that can be used during the game session
     */
    private ArrayList<God> cardList;


    public Deck(ArrayList<God> cardList) {
        this.cardList = cardList;
    }


    public ArrayList<God> getCardList() {
        return cardList;
    }


    public void setCardList(ArrayList<God> cardList) {
        this.cardList = cardList;
    }
}

