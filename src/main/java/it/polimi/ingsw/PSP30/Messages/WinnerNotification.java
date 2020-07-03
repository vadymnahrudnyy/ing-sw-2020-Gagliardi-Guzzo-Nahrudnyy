package it.polimi.ingsw.PSP30.Messages;

/**
 * Implements the message used to announce the winner.
 */
public class WinnerNotification extends Message{
    private static final long serialVersionUID = 100018L;
    private final String winnerUsername;

    /**
     * Builder of winner notification
     * @param winner username of the winner.
     */
    public WinnerNotification(String winner){
        messageID = WINNER_NOTIFICATION;
        winnerUsername = winner;
    }
    public String getWinnerUsername(){
        return winnerUsername;
    }
}
