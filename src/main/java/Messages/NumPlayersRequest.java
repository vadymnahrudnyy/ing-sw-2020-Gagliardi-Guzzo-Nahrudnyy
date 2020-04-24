package Messages;

/**
 * NumPlayersRequest is used to ask the player
 * the type of the game he wants to play(two or three players).
 */
public class NumPlayersRequest extends Message{
    private static final long serialVersionUID = 100003L;
    private static final int messageID = 102;

    public int getID (){
        return messageID;
    }
}
