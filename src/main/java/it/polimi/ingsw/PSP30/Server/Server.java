package it.polimi.ingsw.PSP30.Server;


import it.polimi.ingsw.PSP30.Model.God;
import it.polimi.ingsw.PSP30.Model.Power;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

/**
 * Implements the main class of the Server.
 * Creates list of gods and powers by reading configuration files, creates the Socket and finally accepts the connections from players.
 * @version 2.0
 */
public class Server {
    private static ServerSocket server;
    private static boolean running = false;
    private static final int SOCKET_PORT = 50000;
    public static final int CONNECTION_TIMEOUT = 3000;

    private static Thread lobbyStatusNotifierThread;

    private static final Lobby serverLobby = new Lobby ();

    private static final ArrayList<God> godsList = new GodParser().readGods();
    private static final ArrayList<Power> powerList = new PowerParser().readPowers();


    public static void main(String[] args) {
        System.out.println(Thread.currentThread() + " Server Started");
        serverSocketCreation();
        lobbyStatusThreadCreation();
        while (running) acceptPlayersConnections();
    }


    /**
     * Method used to get the list of Gods by other components of the server.
     * @return Array List containing the gods usable in game.
     */
    protected static ArrayList<God> getGodsList(){
        return godsList;
    }
    /**
     * Method used to get the list of Power by other components of the server.
     * @return Array List containing the powers of usable gods in game.
     */
    protected static ArrayList<Power> getPowerList() {
        return powerList;
    }
    /**
     * Method use to get the Thread notifying all the players in lobby about current lobby's status.
     * @return Lobby status notifier Thread.
     */
    protected static Thread getLobbyStatusNotifierThread(){
        return lobbyStatusNotifierThread;
    }

    /**
     * Method creating the server socket.
     * If socket is successfully created, running is set to "true", in case of error, running remains "false".
     */
    protected static void serverSocketCreation(){
        try{
            server = new ServerSocket(SOCKET_PORT);
            running = true;
            System.out.println(Thread.currentThread() + " Server Socket created. Port: " + SOCKET_PORT);
        } catch (IOException e) {
        System.out.println("Server initialization failed: Shutdown");
        }
    }

    /**
     * Method creating the Thread used to notify the status of the lobby.
     */
    protected static void lobbyStatusThreadCreation(){
        lobbyStatusNotifierThread = new Thread(new Lobby.LobbyStatusNotifier());
        lobbyStatusNotifierThread.start();
    }

    /**
     * Method accepting the connection from a client and then creates a virtual view for the client.
     */
    protected static void acceptPlayersConnections(){
        try{
            Socket newClient = server.accept();
            System.out.println(Thread.currentThread() + " New connection accepted from IP: " + newClient.getInetAddress());
            VirtualView newVirtualView = new VirtualView(newClient, serverLobby);
            Thread newVirtualViewThread = new Thread(newVirtualView);
            newVirtualViewThread.start();
            System.out.println(Thread.currentThread() + " Virtual View for user " + newClient.getInetAddress() + " created");
        }catch (IOException e){
            System.out.println(Thread.currentThread() + " Connection to the client failed");
        }
    }

}

