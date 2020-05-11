package Server;

import Messages.UsernameTakenError;

import java.util.ArrayList;
/**
 * Class Lobby implements a simple lobby for the server.
 *
 * @author Vadym Nahrudnyy
 * @version 2.2
 */

public class Lobby {
    private static boolean twoPlayersLobbyReady = false;
    private static boolean threePlayersLobbyReady = false;
    private static final ArrayList<String> twoPlayersLobby = new ArrayList<>();
    private static final ArrayList<String> threePlayersLobby = new ArrayList<>();
    private static final ArrayList<VirtualView> twoPlayersLobbyVirtualViews = new ArrayList<VirtualView>();
    private static final ArrayList<VirtualView> threePlayersLobbyVirtualViews = new ArrayList<VirtualView>();

    /**
     * Method getTwoPlayersLobby
     * @return the list of players waiting for a two players game.
     */
    private synchronized ArrayList<String> getTwoPlayersLobby(){
        return twoPlayersLobby;
    }
    /**
     * Method getTwoPlayersLobbyVirtualViews
     * @since version 2.0
     * @return the list of virtual views of the players in the two players lobby.
     */
    private synchronized ArrayList<VirtualView> getTwoPlayersLobbyVirtualViews(){
        return twoPlayersLobbyVirtualViews;
    }
    /**
     * Method getTwoPlayersLobbySlotsOccupied
     * @return the number of players waiting for a two players game.
     */
    private synchronized int getTwoPlayersLobbySlotsOccupied(){
        return twoPlayersLobby.size();
    }
    /**
     * Method addPlayerToTwoPlayersLobby adds to the lobby a new player.
     * @param playerToAddUsername Username of the new player.
     * @param playerToAddVirtualView Virtual view of the new player;
     */
    private  synchronized void addPlayerToTwoPlayersLobby(String playerToAddUsername,VirtualView playerToAddVirtualView){
        twoPlayersLobby.add(playerToAddUsername);
        twoPlayersLobbyVirtualViews.add(playerToAddVirtualView);
        playerToAddVirtualView.setInLobby(true);
    }
    /**
     * Method removePlayerFromTwoPlayersLobby removes a player from the list of players waiting for a two players game.
     * @param playerToRemoveUsername Username of the player to remove.
     * @param playerToRemoveVirtualView Virtual view of the player to remove.
     */
    private synchronized void removePlayerFromTwoPlayersLobby(String playerToRemoveUsername,VirtualView playerToRemoveVirtualView){
        twoPlayersLobbyVirtualViews.remove(playerToRemoveVirtualView);
        twoPlayersLobby.remove(playerToRemoveUsername);
        playerToRemoveVirtualView.setInLobby(false);
    }
    /**
     * Method getThreePlayersLobby
     * @return the list of players waiting for a three players game.
     */
    private synchronized ArrayList<String> getThreePlayersLobby() {
        return threePlayersLobby;
    }
    /**
     * Method getThreePlayersLobbyVirtualViews
     * @since version 2.0
     * @return the list of virtual views of the players in the three players lobby.
     */
    private synchronized ArrayList<VirtualView> getThreePlayersLobbyVirtualViews(){
        return threePlayersLobbyVirtualViews;
    }
    /**
     * Method getThreePlayersLobbySlotsOccupied
     * @return the number of players waiting for a three players game.
     */
    private synchronized int getThreePlayersLobbySlotsOccupied() {
        return threePlayersLobby.size();
    }
    /**
     * Method addPlayerToThreePlayersLobby adds to the corresponding lobby a new player
     * @param playerToAddUsername Username of the player to add.
     * @param playerToAddVirtualView Virtual view of the new player.
     */
    private synchronized void addPlayerToThreePlayersLobby(String playerToAddUsername,VirtualView playerToAddVirtualView){
        threePlayersLobby.add(playerToAddUsername);
        threePlayersLobbyVirtualViews.add(playerToAddVirtualView);
        playerToAddVirtualView.setInLobby(true);
    }
    /**
     * Method removePlayerFromTwoPlayersLobby removes a player from the list of players waiting for a three players game.
     * @param playerToRemoveUsername Username of the player to remove.
     * @param playerToRemoveVirtualView Virtual view of the player to remove.
     */
    private static synchronized void removePlayerFromThreePlayersLobby(String playerToRemoveUsername,VirtualView playerToRemoveVirtualView) {
        threePlayersLobby.remove(playerToRemoveUsername);
        threePlayersLobbyVirtualViews.remove(playerToRemoveVirtualView);
        playerToRemoveVirtualView.setInLobby(false);
    }
    private synchronized boolean getTwoPlayersLobbyReady(){return twoPlayersLobbyReady;}
    private synchronized boolean getThreePlayersLobbyReady(){return threePlayersLobbyReady;}
    /**
     * Method resetTwoPlayersLobby removes the players from the two players lobby when the corresponding game starts.
     */

    private synchronized void resetTwoPlayersLobby (){
        setTwoPlayersLobbyReady(false);
        twoPlayersLobby.clear();
        twoPlayersLobbyVirtualViews.clear();
    }
    /**
     * Method resetThreePlayersLobby removes the players from the three players lobby when the corresponding game starts.
     */

    private synchronized void resetThreePlayersLobby() {
        setThreePlayersLobbyReady(false);
        threePlayersLobby.clear();
        threePlayersLobbyVirtualViews.clear();
    }
    /**
     * Setter method of twoPlayersLobbyReady flag.
     * @since version 2.0
     * @param ready the new status value.
     */

    private synchronized void setTwoPlayersLobbyReady(boolean ready){
        twoPlayersLobbyReady = ready;
    }
    /**
     * Setter method of threePlayersLobbyReady flag.
     * @since version 2.0
     * @param ready the new status value.
     */
    private synchronized void setThreePlayersLobbyReady(boolean ready){
        threePlayersLobbyReady = ready;
    }
    /**
     * Method checkReady verifies if the lobbies are ready to start the game.
     */
    private  synchronized void checkReady(){
        if(getTwoPlayersLobbySlotsOccupied()==2) setTwoPlayersLobbyReady(true);
        if (getThreePlayersLobbySlotsOccupied()==3) setThreePlayersLobbyReady(true);
    }

    /**
     * Check the username of the player and insert it in the lobby.In case the username is already present it sends to the player an error.
     * @since version 2.1
     * @param desiredNumPlayers Number of players in the game wanted by the player.
     * @param client VirtualView of the player.
     * @param username username of the player.
     */
     synchronized void addPlayerToLobby(int desiredNumPlayers,VirtualView client,String username,Thread viewThread){
        System.out.println("Adding user"+ username +" to lobby");
            if (desiredNumPlayers == 2){
                if (!(getTwoPlayersLobby().contains(username))) {
                    addPlayerToTwoPlayersLobby(username,client);
                    viewThread.interrupt();
                }
                else client.sendMessage(new UsernameTakenError());
            }
            else {
                if (!(getThreePlayersLobby().contains(username))) {
                    addPlayerToThreePlayersLobby(username,client);
                    viewThread.interrupt();
                }
                else client.sendMessage(new UsernameTakenError());
            }
        System.out.println("Added to lobby");
        checkReady();
        GameController newGame;
        Thread newGameThread;
        if (getTwoPlayersLobbyReady()) {
            newGame = new GameController(getTwoPlayersLobbyVirtualViews(), 2);
            newGameThread = new Thread(newGame);
            for (VirtualView view:getTwoPlayersLobbyVirtualViews()) {
                view.setAssociatedGameThread(newGameThread);
                view.setInLobby(false);
                view.setInGame(true);
            }
            newGameThread.start();
            resetTwoPlayersLobby();
        }
        if (getThreePlayersLobbyReady()) {
            newGame = new GameController(getThreePlayersLobbyVirtualViews(), 3);
            newGameThread = new Thread(newGame);
            for (VirtualView view:getThreePlayersLobbyVirtualViews()) {
                view.setAssociatedGameThread(newGameThread);
                view.setInLobby(false);
                view.setInGame(true);
            }
            newGameThread.start();
            resetThreePlayersLobby();
        }
    }


    /**
     * Method used to delete a player from a lobby.
     * @since version 2.1
     */
    public synchronized void removePlayerFromLobby(VirtualView client, String username,Thread viewThread){
        if (getTwoPlayersLobby().contains(username))removePlayerFromTwoPlayersLobby(username,client);
        else removePlayerFromThreePlayersLobby(username,client);
    }
}
