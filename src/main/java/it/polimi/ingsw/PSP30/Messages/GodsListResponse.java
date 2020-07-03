package it.polimi.ingsw.PSP30.Messages;

import java.util.ArrayList;

/**
 * Sends to the server the list of gods the challenger has chosen to play with.
 */
public class GodsListResponse extends Message {
    private static final long serialVersionUID = 100008L;
    private ArrayList<String> gods;

    public GodsListResponse(ArrayList<String> selectedGods){
        messageID = GODS_LIST_RESPONSE;
        gods = selectedGods;
    }
    public ArrayList<String> getGods(){
        return gods;
    }
}
