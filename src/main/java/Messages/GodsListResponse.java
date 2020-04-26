package Messages;

import Model.God;

import java.util.ArrayList;

/**
 * GodsListResponse send to the server the list
 * of gods the challenger has chosen to play with.
 */
public class GodsListResponse extends Message {
    private static final long serialVersionUID = 100008L;
    private ArrayList<God> gods;

    public GodsListResponse(ArrayList<God> selectedGods){
        messageID = GODS_LIST_RESPONSE;
        gods = selectedGods;
    }
    public ArrayList<God> getGods(){
        return gods;
    }
}
