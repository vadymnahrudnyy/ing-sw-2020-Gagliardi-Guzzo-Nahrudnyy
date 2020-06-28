package it.polimi.ingsw.PSP30.Messages;

/**
 * Class Disconnection implements the notification message after the disconnection.
 */
public class Disconnection extends Message {
    private static final long serialVersionUID = 100023L;

    public Disconnection(){
        messageID = DISCONNECTION_MESSAGE;
    }
}
