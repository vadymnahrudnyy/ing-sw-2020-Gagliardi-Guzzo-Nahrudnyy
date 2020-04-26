package Messages;

import java.io.Serializable;

/**
 * Class implementing the start game notification.
 */
public class GameStartNotification extends Message {
    private static final long serialVersionUID = 100006L;

    public GameStartNotification(){
        messageID = GAME_START_NOTIFICATION;
    }
}
