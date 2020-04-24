package Messages;

import Model.God;

import java.util.ArrayList;

/**
 * GodsListResponse send to the server the list
 * of gods the challenger has chosen to play with.
 */
public class GodsListResponse extends Message {
    private static final long serialVersionUID = 100008L;
    private static final int messageID = 203;
    private ArrayList<God> gods;

    public GodsListResponse(ArrayList<God> selectedGods){
        gods = selectedGods;
    }
    public int getMessageID(){
        return messageID;
    }
    public ArrayList<God> getGods(){
        return gods;
    }
}
