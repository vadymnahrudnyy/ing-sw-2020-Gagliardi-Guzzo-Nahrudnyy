package Messages;

/**
 * NumPlayersResponse is used to send to the server
 * the type of game the player wants to play.
 */
public class NumPlayersResponse extends Message{
    private static final long serialVersionUID = 100004L;
    private static final int messageID = 202;
    private final int numPlayers;

    public NumPlayersResponse (int numberPlayers){
        numPlayers = numberPlayers;
    }
    public int getID (){
        return messageID;
    }
    public int getNumPlayers (){
        return numPlayers;
    }
}