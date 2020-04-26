package Messages;

import Model.Game;

import java.io.Serializable;

/**
 * Class used to notify the users about the current state of the game.
 */
public class GameStatusNotification extends Message {
    private static final long serialVersionUID = 100017L;
    private final Game updatedGame;

    /**
     * Builder of a GameStatusNotification instance
     * @param actualGame is the Game class object representing the game status.
     */
    public GameStatusNotification(Game actualGame){
        messageID = GAME_STATUS_NOTIFICATION;
        updatedGame = actualGame;
    }
    public Game getUpdatedGame() {
        return updatedGame;
    }
}
