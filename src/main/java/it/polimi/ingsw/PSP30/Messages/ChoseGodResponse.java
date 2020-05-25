package it.polimi.ingsw.PSP30.Messages;

/**
 * Class ChoseGodResponse implements the answer to ChoseGodRequest message.
 */
public class ChoseGodResponse extends Message {
    private static final long serialVersionUID = 10020L;
    private final String god;

    /**
     * Constructor of ChoseGodResponse
     * @param chosenGod God card chosen by the client.
     */
    public ChoseGodResponse(String chosenGod){
        messageID = CHOSE_GOD_RESPONSE;
        god = chosenGod;
    }
    public String getChosenGod(){
        return god;
    }
}
