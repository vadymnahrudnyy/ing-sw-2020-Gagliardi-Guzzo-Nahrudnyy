package Messages;

/**
 * NumPlayersResponse is used to send to the server
 * the type of game the player wants to play.
 */
public class NumPlayersResponse extends Message{
    private static final long serialVersionUID = 100004L;
    private final int numPlayers;

    public NumPlayersResponse (int numberPlayers){
        messageID = NUM_PLAYERS_RESPONSE;
        numPlayers = numberPlayers;
    }
    public int getNumPlayers (){
        return numPlayers;
    }
}