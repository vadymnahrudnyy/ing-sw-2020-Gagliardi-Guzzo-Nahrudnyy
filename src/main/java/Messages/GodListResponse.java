package Messages;

import Model.God;

import java.util.ArrayList;

public class GodListResponse extends Message {
    private static final long serialVersionUID = 100008L;
    private static final int messageID = 203;
    private ArrayList<God> gods;

    public GodListResponse(ArrayList<God> selectedGods){
        gods = selectedGods;
    }
    public int getMessageID(){
        return messageID;
    }
    public ArrayList<God> getGods(){
        return gods;
    }
}
