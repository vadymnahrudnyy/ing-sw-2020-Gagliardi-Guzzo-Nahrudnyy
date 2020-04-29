package Messages;

import java.io.Serializable;

/**
 * Abstract class implementing the messages that will be sent between Server and Client.
 */
public class Message implements Serializable {
    protected int messageID;

    public static final int USERNAME_REQUEST = 101;
    public static final int NUM_PLAYERS_REQUEST = 102;
    public static final int GODS_LIST_REQUEST = 103;
    public static final int START_PLAYER_REQUEST = 104;
    public static final int WORKER_POSITION_REQUEST = 105;
    public static final int SELECT_WORKER_REQUEST = 106;
    public static final int MOVE_REQUEST = 107;
    public static final int CHOSE_GOD_REQUEST = 108;
    public static final int BUILD_REQUEST = 109;
    public static final int USERNAME_RESPONSE = 201;
    public static final int NUM_PLAYERS_RESPONSE = 202;
    public static final int GODS_LIST_RESPONSE = 203;
    public static final int START_PLAYER_RESPONSE = 204;
    public static final int WORKER_POSITION_RESPONSE = 205;
    public static final int SELECT_WORKER_RESPONSE = 206;
    public static final int MOVE_RESPONSE = 207;
    public static final int BUILD_RESPONSE = 209;
    public static final int CHOSE_GOD_RESPONSE = 208;
    public static final int LOBBY_STATUS_NOTIFICATION = 301;
    public static final int GAME_START_NOTIFICATION = 302;
    public static final int GAME_STATUS_NOTIFICATION = 303;
    public static final int WINNER_NOTIFICATION = 304;
    public static final int LAST_GOD_NOTIFICATION = 305;
    public static final int PING_MESSAGE = 501;
    public static final int DISCONNECTION_MESSAGE = 502;

    public int getMessageID() {
        return messageID;
    }

}
