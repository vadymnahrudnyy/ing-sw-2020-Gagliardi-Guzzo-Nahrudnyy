package it.polimi.ingsw.PSP30.Messages;

import it.polimi.ingsw.PSP30.Model.God;

import java.util.ArrayList;

/**
 * Implements the message which notifies the last player that has remained only one god and it will be his card.
 */
public class LastGodNotification extends Message {
    private static final long serialVersionUID = 100021L;
    private final ArrayList<God> gods;
    private final God lastGod;

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
