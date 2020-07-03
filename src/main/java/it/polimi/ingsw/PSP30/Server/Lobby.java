package it.polimi.ingsw.PSP30.Server;

import it.polimi.ingsw.PSP30.Messages.LobbyStatusNotification;
import it.polimi.ingsw.PSP30.Messages.UsernameTakenError;

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
    private static final ArrayList<VirtualView> twoPlayersLobbyVirtualViews = new ArrayList<>();
    private static final ArrayList<VirtualView> threePlayersLobbyVirtualViews = new ArrayList<>();

    /**
     * Method getTwoPlayersLobby
     * @return the list of players waiting for a two players game.
     */
    protected static synchronized ArrayList<String> getTwoPlayersLobby(){
        return twoPlayersLobby;
    }
    /**
     * Method getTwoPlayersLobbyVirtualViews
     * @since version 2.0
     * @return the list of virtual views of the players in the two players lobby.
     */
    protected static synchronized ArrayList<VirtualView> getTwoPlayersLobbyVirtualViews(){
        return twoPlayersLobbyVirtualViews;
    }
    /**
     * Method getTwoPlayersLobbySlotsOccupied
     * @return the number of players waiting for a two players game.
     */
    protected synchronized int getTwoPlayersLobbySlotsOccupied(){
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
        System.out.println(Thread.currentThread() + " Player: " + playerToRemoveUsername +" removed from two players lobby");
    }
    /**
     * Method getThreePlayersLobby
     * @return the list of players waiting for a three players game.
     */
    protected static synchronized ArrayList<String> getThreePlayersLobby() {
        return threePlayersLobby;
    }
    /**
     * Method getThreePlayersLobbyVirtualViews
     * @since version 2.0
     * @return the list of virtual views of the players in the three players lobby.
     */
    protected static synchronized ArrayList<VirtualView> getThreePlayersLobbyVirtualViews(){
        return threePlayersLobbyVirtualViews;
    }
    /**
     * Method getThreePlayersLobbySlotsOccupied
     * @return the number of players waiting for a three players game.
     */
    protected synchronized int getThreePlayersLobbySlotsOccupied() {
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
     * Method removePlayerFromThreePlayersLobby removes a player from the list of players waiting for a three players game.
     * @param playerToRemoveUsername Username of the player to remove.
     * @param playerToRemoveVirtualView Virtual view of the player to remove.
     */
    private static synchronized void removePlayerFromThreePlayersLobby(String playerToRemoveUsername,VirtualView playerToRemoveVirtualView) {
        threePlayersLobby.remove(playerToRemoveUsername);
        threePlayersLobbyVirtualViews.remove(playerToRemoveVirtualView);
        playerToRemoveVirtualView.setInLobby(false);
        System.out.println(Thread.currentThread() + " Player: " + playerToRemoveUsername +" removed from three players lobby");
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
    private synchronized void checkReady(){
        if(getTwoPlayersLobbySlotsOccupied()==2) setTwoPlayersLobbyReady(true);
        if (getThreePlayersLobbySlotsOccupied()==3) setThreePlayersLobbyReady(true);
    }

    /**
     * Check the username of the player and insert it in the lobby.
     * In case the username is already present, the insertion is aborted and an error message is sent to the player.
     * Once a insertion is made successfully, if a game is ready to start, the method starts it in a new thread.
     * @since version 2.1
     * @param desiredNumPlayers Number of players in the game wanted by the player.
     * @param client VirtualView of the player.
     * @param username username of the player.
     */
     protected synchronized void addPlayerToLobby(int desiredNumPlayers,VirtualView client,String username,Thread viewThread){
         if (desiredNumPlayers == 2){
             if (!(getTwoPlayersLobby().contains(username))) {
                 addPlayerToTwoPlayersLobby(username,client);
                 viewThread.interrupt();
                 System.out.println(Thread.currentThread() + " " + username + " added to lobby");
             }
             else {
                 client.sendMessage(new UsernameTakenError());
                 System.out.println(Thread.currentThread() + " A player with username: " + username + " is already waiting in two players lobby");
             }
         }
         else {
             if (!(getThreePlayersLobby().contains(username))) {
                 addPlayerToThreePlayersLobby(username,client);
                 viewThread.interrupt();
                 System.out.println(Thread.currentThread() + " " + username + " added to lobby");
             }
             else {
                 client.sendMessage(new UsernameTakenError());
                 System.out.println(Thread.currentThread() + " A player with username: " + username + " is already waiting in three players lobby");
             }
         }
         startGame();
    }

    /**
     * Method used to start a game when a lobby is full.
     * @since version 2.2
     */
    private synchronized void startGame(){
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
     * Method used to delete a player from a lobby. It checks the lobby the player is in and
     * then removes it from the lobby using the corresponding method.
     * @since version 2.1
     */
    protected synchronized void removePlayerFromLobby(VirtualView client, String username){
        if (getTwoPlayersLobby().contains(username))removePlayerFromTwoPlayersLobby(username,client);
        else removePlayerFromThreePlayersLobby(username,client);

    }


    protected static class LobbyStatusNotifier implements Runnable{
        private static final int LOBBY_NOTIFY_TIMEOUT  = 2000;
        @Override
        public void run() {
            //noinspection InfiniteLoopStatement
            while(true){
                try {
                    //noinspection BusyWait
                    Thread.sleep(LOBBY_NOTIFY_TIMEOUT);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (VirtualView view:twoPlayersLobbyVirtualViews) view.sendMessage(new LobbyStatusNotification(2,twoPlayersLobby.size(),twoPlayersLobby));
                for (VirtualView view: threePlayersLobbyVirtualViews) view.sendMessage(new LobbyStatusNotification(3,threePlayersLobby.size(),threePlayersLobby));
            }
        }
    }
}
