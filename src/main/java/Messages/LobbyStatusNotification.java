package Messages;

/**
 * LobbyStatusNotification sends to the player waiting for a new
 * game the actual status of the lobby.
 */
public class LobbyStatusNotification extends Message{
    private static final long serialVersionUID = 100005L;
    private static final int messageID = 301;
    private final int selectedLobby;
    private final int SlotsOccupied;

    /**
     * Builder of a new LobbyStatus message
     * @param Lobby indicates in which lobby the player is
     * @param SlotOccupation indicates the actual slots occupied in the selected lobby
     */
    public LobbyStatusNotification(int Lobby,int SlotOccupation){
        selectedLobby = Lobby;
        SlotsOccupied = SlotOccupation;
    }
    public int getSelectedLobby(){
        return selectedLobby;
    }
    public int getSlotsOccupied(){
        return SlotsOccupied;
    }
}
