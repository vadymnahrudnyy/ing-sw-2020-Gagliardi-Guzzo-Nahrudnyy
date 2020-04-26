package Messages;

/**
 * UsernameRequest is used by server to ask
 * player's username after accepting the connection.
 */
public class UsernameRequest extends Message{
    private static final long serialVersionUID = 100001L;
    private static final int USERNAME_REQUEST = 101;

    public UsernameRequest(){
        messageID = USERNAME_REQUEST;
    }
}
