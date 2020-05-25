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
    private static final int SOCKET_PORT = 50000;
    private static final Lobby serverLobby = new Lobby ();
    private static final ArrayList<God> godsList = GodParser.readGods();
    private static final ArrayList<Power> powerList = PowerParser.readPowers();


    public static void main(String[] args) {
        System.out.println("Server Started");
        try {
            server = new ServerSocket(SOCKET_PORT);
            System.out.println("Server Socket created. Port used: "+ SOCKET_PORT);
        } catch (IOException e) {
            System.out.println("Server initialization failed: Server will shutdown");
        }
            //noinspection InfiniteLoopStatement
        while (true) {
            try{
                Socket newClient = server.accept();
                System.out.println("new connection accepted from IP: " + newClient.getInetAddress());
                VirtualView newVirtualView = new VirtualView(newClient, serverLobby);
                Thread newVirtualViewThread = new Thread(newVirtualView);
                newVirtualViewThread.start();
                System.out.println("Virtual View for user "+newClient.getInetAddress()+" created");
            }catch (IOException e){
                System.out.println("Connection to the client failed");
            }
        }
    }

    /**
     * Method used to get the list of Gods by other components of the server.
     * @return Array List containing the gods usable in game.
     */
    public static ArrayList<God> getGodsList(){
        return godsList;
    }

    /**
     * Method used to get the list of Power by other components of the server.
     * @return Array List containing the powers of usable gods in game.
     */
    public static ArrayList<Power> getPowerList() {
        return powerList;
    }
}

