package Messages;

public class LobbyStatusNotification extends Message{
    private static final long serialVersionUID = 100005L;
    private static final int messageID = 301;
    private final int selectedLobby;
    private final int SlotsOccupied;

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
