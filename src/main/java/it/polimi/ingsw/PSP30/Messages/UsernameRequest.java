package it.polimi.ingsw.PSP30.Messages;

/**
 * UsernameRequest is used by server to ask player's username after accepting the connection.
 */
public class UsernameRequest extends Message{
    private static final long serialVersionUID = 100001L;

    public UsernameRequest(){
        messageID = USERNAME_REQUEST;
    }
}
