package it.polimi.ingsw.PSP30.Messages;

import it.polimi.ingsw.PSP30.Model.God;

import java.util.ArrayList;

/**
 * Implements the message asking the player the god he will use during the game.
 * It sends to client 2 lists, one with all gods will be used in the game and the other with already chosen gods.
 */
public class ChoseGodRequest extends Message {
    private static final long serialVersionUID = 100019L;
    private final ArrayList<God> gods;
    private final ArrayList<God> unavailableGods;

    /**
     * Constructor of ChoseGodRequest
     * @param gameGods all the gods used in the game.
     * @param alreadyChosenGods list of gods unavailable to the player.
     */
    public ChoseGodRequest(ArrayList<God> gameGods, ArrayList<God> alreadyChosenGods) {
        messageID = CHOSE_GOD_REQUEST;
        gods = gameGods;
        unavailableGods = alreadyChosenGods;
    }

    public ArrayList<God> getGods() {
        return gods;
    }

    public ArrayList<God> getUnavailableGods() {
        return unavailableGods;
    }
}
