package Server;

import java.util.ArrayList;
/**
 * Class Lobby implements a simple lobby for the server.
 *
 * @author Vadym Nahrudnyy
 * @version 1.0
 */

public class Lobby {
    final int maxServerPlayers;
    private final ArrayList<String> serverPlayers;
    private final ArrayList<String> twoPlayersLobby;
    private final ArrayList<String> threePlayersLobby;

    /**
     * Constructor of the server lobby.
     * @param maxPlayers indicates the maximum number of players can join the server simultaneously
     */
    public Lobby(int maxPlayers) {
        maxServerPlayers = maxPlayers;
        serverPlayers = new ArrayList<String>();
        twoPlayersLobby = new ArrayList<String>();
        threePlayersLobby = new ArrayList<String>();
    }
    /**
     * Method getMaxServerPlayers
     * @return the maximum number of players can be simultaneously connected to the server.
     */
    public int getMaxServerPlayers(){
        return maxServerPlayers;
    }
    /**
     * Method getServerPlayers
     * @return the list of players currently connected to the server
     */
    public ArrayList<String> getServerPlayers() {
        return serverPlayers;
    }
    /**
     * Method getNumberPlayersToServer
     * @return the number of player connected to the server.
     */
    public int getNumberPlayersConnectedToServer (){
        return serverPlayers.size();
    }
    /**
     * Method addPlayerToServer adds a new player to the list of connected players.
     * @param playerToAddUsername Username of the new player.
     */
    public void addPlayerToServer(String playerToAddUsername){
        serverPlayers.add(playerToAddUsername);
    }
    /**
     * Method removePlayerFromServer removes a player from the list of connected players.
     * @param playerToRemoveUsername Username of the player to remove;
     */
    public void removePlayerFromServer(String playerToRemoveUsername) {
        serverPlayers.remove(playerToRemoveUsername);
    }

    /**
     * Method getTwoPlayerLobby
     * @return the list of players waiting for a two players game.
     */
    public ArrayList<String> getTwoPlayersLobby(){
        return twoPlayersLobby;
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
     */
    public void addPlayerToTwoPlayersLobby(String playerToAddUsername){
        twoPlayersLobby.add(playerToAddUsername);
    }
    /**
     * Method removePlayerFromTwoPlayersLobby removes a player from the list of players waiting for a two players game.
     * @param playerToRemoveUsername Username of the player to remove.
     */
    public void removePlayerFromTwoPlayersLobby(String playerToRemoveUsername){
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
     * Method getThreePlayersLobbySlotsOccupied
     * @return the number of players waiting for a three players game.
     */
    public int getThreePlayersLobbySlotsOccupied() {
        return threePlayersLobby.size();
    }
    /**
     * Method addPlayerToThreePlayersLobby adds to the corresponding lobby a new player
     * @param playerToAddUsername Username of the player to add.
     */
    public void addPlayerToThreePlayersLobby(String playerToAddUsername){
        threePlayersLobby.add(playerToAddUsername);
    }
    /**
     * Method removePlayerFromTwoPlayersLobby removes a player from the list of players waiting for a three players game.
     * @param playerToRemoveUsername Username of the player to remove.
     */
    public void removePlayerFromThreePlayersLobby(String playerToRemoveUsername) {
        threePlayersLobby.remove(playerToRemoveUsername);
    }

    /**
     * Method resetTwoPlayersLobby removes the players from the two players lobby when the corresponding game starts.
     */
    public void resetTwoPlayersLobby (){
        twoPlayersLobby.clear();
    }
    /**
     * Method resetTwoPlayersLobby removes the players from the three players lobby when the corresponding game starts.
     */
    public void resetThreePlayersLobby() {
        threePlayersLobby.clear();
    }
}