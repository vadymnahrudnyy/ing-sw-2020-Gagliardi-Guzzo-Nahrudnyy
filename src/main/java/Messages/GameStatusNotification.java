package Messages;

import Model.Game;

/**
 * Class used to notify the users about the current state of the game.
 */
public class GameStatusNotification {
    private static final long serialVersionUID = 100017L;
    private static final int messageID = 303;
    private final Game updatedGame;

    /**
     * Builder of a GameStatusNotification instance
     * @param actualGame is the Game class object representing the game status.
     */
    public GameStatusNotification(Game actualGame){
        updatedGame = actualGame;
    }

    public Game getUpdatedGame() {
        return updatedGame;
    }

    public static int getMessageID() {
        return messageID;
    }
}
