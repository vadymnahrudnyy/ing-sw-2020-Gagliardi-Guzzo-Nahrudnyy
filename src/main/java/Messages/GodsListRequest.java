package Messages;

import Model.God;

import java.util.ArrayList;

/**
 * GodsList implements the message sending to the challenger the
 * list of gods and asking him to choose which of them will be used in the game.
 */
public class GodsListRequest {
    private static final long serialVersionUID = 100007L;
    private static final int messageID = 103;
    private final ArrayList<God> deck;

    public GodsListRequest(ArrayList<God> gods){
        deck = gods;
    }
    public int getMessageID(){
        return messageID;
    }
    public ArrayList<God> getDeck(){
        return deck;
    }
}
