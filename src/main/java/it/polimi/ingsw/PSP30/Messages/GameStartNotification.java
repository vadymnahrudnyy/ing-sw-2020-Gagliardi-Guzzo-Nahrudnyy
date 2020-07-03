package it.polimi.ingsw.PSP30.Messages;

import it.polimi.ingsw.PSP30.Model.Game;

/**
 * Implements the start game notification.
 */
public class GameStartNotification extends Message {
    private static final long serialVersionUID = 100006L;
    private final Game game;

    public GameStartNotification(Game currentGame){
        messageID = GAME_START_NOTIFICATION;
        game = currentGame;
    }

    public Game getGame(){return game;}
}
