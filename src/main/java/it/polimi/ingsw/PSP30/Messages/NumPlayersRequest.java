package it.polimi.ingsw.PSP30.Messages;

/**
 * Asks the player the type of the game he wants to play (two or three players).
 */
public class NumPlayersRequest extends Message{
    private static final long serialVersionUID = 100003L;

    public NumPlayersRequest(){
        messageID = NUM_PLAYERS_REQUEST;
    }
}
