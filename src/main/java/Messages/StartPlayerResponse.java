package Messages;

/**
 * StartPlayerResponse implements the message sending to the
 * server the first player of the game.
 */
public class StartPlayerResponse {
    private static final long serialVersionUID = 100010L;
    private static final int messageID = 204;
    private final String startPlayerUsername;

    public StartPlayerResponse(String startPlayer){
        startPlayerUsername = startPlayer;
    }
    public int getMessageID() {
        return messageID;
    }
}
