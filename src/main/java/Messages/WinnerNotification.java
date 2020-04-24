package Messages;

/**
 * WinnerNotification implements the message used to announce the winner
 */
public class WinnerNotification {
    private static final long serialVersionUID = 100018L;
    private static final int messageID = 304;
    private final String winnerUsername;

    /**
     * Builder of winner notification
     * @param winner username of the winner.
     */
    public WinnerNotification(String winner){
        winnerUsername = winner;
    }

    public static int getMessageID() {
        return messageID;
    }
}
