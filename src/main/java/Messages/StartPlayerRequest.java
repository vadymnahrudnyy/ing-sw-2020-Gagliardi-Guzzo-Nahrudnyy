package Messages;

import Model.Player;

/**
 * StartPlayerRequest implements the message sending to
 * the challenger the list of player and asking who
 * will be the first player to make the move.
 */
public class StartPlayerRequest extends Message {
    private static final long serialVersionUID = 100009L;
    private final Player[] players;

    public StartPlayerRequest(Player[] playersList){
        messageID = START_PLAYER_REQUEST;
        players = playersList;
    }
    public Player[] getPlayers() {
        return players;
    }
}