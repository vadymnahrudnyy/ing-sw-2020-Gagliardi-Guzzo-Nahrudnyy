package Messages;

import Model.God;

import java.util.ArrayList;

/**
 * GodsList implements the message sending to the challenger the
 * list of gods and asking him to choose which of them will be used in the game.
 */
public class GodsListRequest extends Message {
    private static final long serialVersionUID = 100007L;
    private final ArrayList<God> deck;

    public GodsListRequest(ArrayList<God> gods){
        messageID = GODS_LIST_REQUEST;
        deck = gods;
    }
    public ArrayList<God> getDeck(){
        return deck;
    }
}
