package Messages;

import Model.God;

import java.util.ArrayList;

public class LastGodNotification extends Message {
    private static final long serialVersionUID = 100021L;
    private ArrayList<God> gods;
    private God lastGod;

    public LastGodNotification (ArrayList<God> godsList,God remainedGod){
        messageID = LAST_GOD_NOTIFICATION;
        gods = godsList;
        lastGod = remainedGod;
    }

    public God getLastGod() {
        return lastGod;
    }

    public ArrayList<God> getGodsList(){
        return gods;
    }
}
