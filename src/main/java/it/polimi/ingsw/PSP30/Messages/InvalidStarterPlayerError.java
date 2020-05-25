package it.polimi.ingsw.PSP30.Messages;

public class InvalidStarterPlayerError extends Message {
    private static final long serialVersionUID = 100036L;

    public InvalidStarterPlayerError(){
        messageID = INVALID_STARTER_PLAYER_ERROR;
    }
}
