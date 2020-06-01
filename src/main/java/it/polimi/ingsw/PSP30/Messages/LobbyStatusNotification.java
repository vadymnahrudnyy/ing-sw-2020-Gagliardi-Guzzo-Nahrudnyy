package it.polimi.ingsw.PSP30.Messages;

import java.util.ArrayList;

/**
 * LobbyStatusNotification sends to the player waiting for a new
 * game the actual status of the lobby.
 */
public class LobbyStatusNotification extends Message{
    private static final long serialVersionUID = 100005L;
    private final int selectedLobby;
    private final int SlotsOccupied;
    private final ArrayList<String> playersInLobby;
    /**
     * Builder of a new LobbyStatus message
     * @param Lobby indicates in which lobby the player is
     * @param SlotOccupation indicates the actual slots occupied in the selected lobby
     */
    public LobbyStatusNotification(int Lobby,int SlotOccupation, ArrayList<String> players){
        playersInLobby=players;
        messageID = LOBBY_STATUS_NOTIFICATION;
        selectedLobby = Lobby;
        SlotsOccupied = SlotOccupation;
    }
    public int getSelectedLobby(){
        return selectedLobby;
    }
    public int getSlotsOccupied(){
        return SlotsOccupied;
    }
    public ArrayList<String> getPlayersInLobby(){return playersInLobby;}
}
