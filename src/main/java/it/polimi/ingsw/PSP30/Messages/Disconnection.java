package it.polimi.ingsw.PSP30.Messages;

public class Disconnection extends Message {
    private static final long serialVersionUID = 100023L;

    public Disconnection(){
        messageID = DISCONNECTION_MESSAGE;
    }
}
