package Server;

import java.util.ArrayList;
/**
 * Class Lobby implements a simple lobby for the server.
 *
 * @author Vadym Nahrudnyy
 * @version 2.0
 */

public class Lobby {
    private boolean twoPlayersLobbyReady;
    private boolean threePlayersLobbyReady;
    private final ArrayList<String> twoPlayersLobby;
    private final ArrayList<String> threePlayersLobby;
    private  ArrayList<VirtualView> twoPlayersLobbyVirtualViews;
    private final ArrayList<VirtualView> threePlayersLobbyVirtualViews;

    /**
     * Constructor of the server lobby.
     */
    public Lobby() {
        twoPlayersLobbyReady = false;
        threePlayersLobbyReady = false;
        twoPlayersLobby = new ArrayList<String>();
        threePlayersLobby = new ArrayList<String>();
        twoPlayersLobbyVirtualViews = new ArrayList<VirtualView>();
        threePlayersLobbyVirtualViews = new ArrayList<VirtualView>();
    }

    /**
     * Method getTwoPlayersLobby
     * @return the list of players waiting for a two players game.
     */
    public ArrayList<String> getTwoPlayersLobby(){
        return twoPlayersLobby;
    }
    /**
     * Method getTwoPlayersLobbyVirtualViews
     * @since versione 2.0
     * @return the list of virtual views of the players in the two players lobby.
     */
    public ArrayList<VirtualView> getTwoPlayersLobbyVirtualViews(){
        return twoPlayersLobbyVirtualViews;
    }
    /**
     * Method getTwoPlayersLobbySlotsOccupied
     * @return the number of players waiting for a two players game.
     */
    public int getTwoPlayersLobbySlotsOccupied(){
        return twoPlayersLobby.size();
    }
    /**
     * Method addPlayerToTwoPlayersLobby adds to the lobby a new player.
     * @param playerToAddUsername Username of the new player.
     * @param playerToAddVirtualView Virtual view of the new player;
     */
    public void addPlayerToTwoPlayersLobby(String playerToAddUsername,VirtualView playerToAddVirtualView){
        twoPlayersLobby.add(playerToAddUsername);
        twoPlayersLobbyVirtualViews.add(playerToAddVirtualView);
    }
    /**
     * Method removePlayerFromTwoPlayersLobby removes a player from the list of players waiting for a two players game.
     * @param playerToRemoveUsername Username of the player to remove.
     * @param playerToRemoveVirtualView Virtual view of the player to remove.
     */
    public synchronized void removePlayerFromTwoPlayersLobby(String playerToRemoveUsername,VirtualView playerToRemoveVirtualView){
        twoPlayersLobbyVirtualViews.remove(playerToRemoveVirtualView);
        twoPlayersLobby.remove(playerToRemoveUsername);
    }
    /**
     * Method getThreePlayersLobby
     * @return the list of players waiting for a three players game.
     */
    public ArrayList<String> getThreePlayersLobby() {
        return threePlayersLobby;
    }
    /**
     * Method getThreePlayersLobbyVirtualViews
     * @since version 2.0
     * @return the list of virtual views of the players in the three players lobby.
     */
    public ArrayList<VirtualView> getThreePlayersLobbyVirtualViews(){
        return threePlayersLobbyVirtualViews;
    }
    /**
     * Method getThreePlayersLobbySlotsOccupied
     * @return the number of players waiting for a three players game.
     */
    public int getThreePlayersLobbySlotsOccupied() {
        return threePlayersLobby.size();
    }
    /**
     * Method addPlayerToThreePlayersLobby adds to the corresponding lobby a new player
     * @param playerToAddUsername Username of the player to add.
     * @param playerToAddVirtualView Virtual view of the new player.
     */
    public void addPlayerToThreePlayersLobby(String playerToAddUsername,VirtualView playerToAddVirtualView){
        threePlayersLobby.add(playerToAddUsername);
        threePlayersLobbyVirtualViews.add(playerToAddVirtualView);
    }
    /**
     * Method removePlayerFromTwoPlayersLobby removes a player from the list of players waiting for a three players game.
     * @param playerToRemoveUsername Username of the player to remove.
     * @param playerToRemoveVirtualView Virtual view of the player to remove.
     */
    public void removePlayerFromThreePlayersLobby(String playerToRemoveUsername,VirtualView playerToRemoveVirtualView) {
        threePlayersLobby.remove(playerToRemoveUsername);
        threePlayersLobbyVirtualViews.remove(playerToRemoveVirtualView);
    }
    public boolean getTwoPlayersLobbyReady(){return twoPlayersLobbyReady;}
    public boolean getThreePlayersLobbyReady(){return threePlayersLobbyReady;}
    /**
     * Method resetTwoPlayersLobby removes the players from the two players lobby when the corresponding game starts.
     */

    public void resetTwoPlayersLobby (){
        setTwoPlayersLobbyReady(false);
        twoPlayersLobby.clear();
        twoPlayersLobbyVirtualViews.clear();
    }
    /**
     * Method resetThreePlayersLobby removes the players from the three players lobby when the corresponding game starts.
     */

    public void resetThreePlayersLobby() {
        setThreePlayersLobbyReady(false);
        threePlayersLobby.clear();
        threePlayersLobbyVirtualViews.clear();
    }
    /**
     * Setter method of twoPlayersLobbyReady flag.
     * @since version 2.0
     * @param ready the new status value.
     */

    public void setTwoPlayersLobbyReady(boolean ready){
        twoPlayersLobbyReady = ready;
    }
    /**
     * Setter method of threePlayersLobbyReady flag.
     * @since version 2.0
     * @param ready the new status value.
     */
    public void setThreePlayersLobbyReady(boolean ready){
        threePlayersLobbyReady = ready;
    }
    /**
     * Method checkReady verifies if the lobbies are ready to start the game.
     */
    public void checkReady(){
        if(getTwoPlayersLobbySlotsOccupied()==2) setTwoPlayersLobbyReady(true);
        if (getThreePlayersLobbySlotsOccupied()==3) setThreePlayersLobbyReady(true);
    }

}
