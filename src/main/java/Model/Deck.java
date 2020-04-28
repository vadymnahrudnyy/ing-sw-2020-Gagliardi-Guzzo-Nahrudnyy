package Model;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Alessia Guzzo
 */

public class Deck implements Serializable {
    private static final long serialVersionUID = 50004L;
    /**
     * Deck class uses java.util.ArrayList for implementing the ArrayList which
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

