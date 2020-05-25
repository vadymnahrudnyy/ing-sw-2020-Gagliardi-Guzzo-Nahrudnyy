package it.polimi.ingsw.PSP30.Messages;

/**
 * UsernameResponse is used for sending
 * to the server the player's username
 */
public class UsernameResponse extends Message {
    private static final long serialVersionUID = 100002L;
    private final String playerUsername;

    public UsernameResponse (String username){
        messageID = USERNAME_RESPONSE;
        playerUsername = username;
    }
    public String getUsername (){
        return playerUsername;
    }
}
