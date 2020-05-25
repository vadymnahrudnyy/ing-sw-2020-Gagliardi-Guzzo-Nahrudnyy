package it.polimi.ingsw.PSP30.Messages;

/**
 * StartPlayerResponse implements the message sending to the
 * server the first player of the game.
 */
public class StartPlayerResponse extends Message{
    private static final long serialVersionUID = 100010L;
    private final String startPlayerUsername;

    public StartPlayerResponse(String startPlayer){
        messageID = START_PLAYER_RESPONSE;
        startPlayerUsername = startPlayer;
    }

    public String getStartPlayerUsername() {
        return startPlayerUsername;
    }
}
