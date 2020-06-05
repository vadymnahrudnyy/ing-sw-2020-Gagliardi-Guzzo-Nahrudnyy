package it.polimi.ingsw.PSP30.Messages;

import it.polimi.ingsw.PSP30.Exception.Server.PlayerDisconnectedException;

public class PlayerDisconnectedError extends Message{
    private static final long serialVersionUID = 100037L ;

    public PlayerDisconnectedError(){
        messageID = PLAYER_DISCONNECTED_ERROR;
    }
}
