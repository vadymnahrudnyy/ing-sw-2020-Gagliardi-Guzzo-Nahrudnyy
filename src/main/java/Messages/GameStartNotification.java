package Messages;

/**
 * Class implementing the start game notification.
 */
public class GameStartNotification extends Message{
    private static final long serialVersionUID = 100006L;
    private static final int messageID = 302;

    public int getMessageID(){
        return messageID;
    }
}
