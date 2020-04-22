package Messages;

import Model.God;

import java.util.ArrayList;

/**
 * This class implements the message sending the list of gods to the first player and requesting the gods to play with.
 */
public class GodsListRequest {
    private static final long serialVersionUID = 100007L;
    private static final int messageID = 103;
    private final boolean challenger;
    private final ArrayList<God> deck;

    public GodsListRequest(ArrayList<God> gods, boolean first){
        deck = gods;
        challenger = first;
    }
    public int getMessageID(){
        return messageID;
    }
    public ArrayList<God> getDeck(){
        return deck;
    }
}
