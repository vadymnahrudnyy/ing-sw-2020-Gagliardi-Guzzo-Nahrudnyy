package Server;

import java.net.*;

import java.util.ArrayList;

import Model.*;

/**
 * This class creates and manages the server
 * @version 1.1
 */

public class Server {

    private static final ArrayList<God> godsList = new ArrayList<God>();
    private static final ArrayList<Power> powerList = new ArrayList<Power>();
    private static final int MAX_PLAYERS = 100;
    private static final int SOCKET_PORT = 50000;
    private static final Lobby serverLobby = new Lobby ();
    private static ServerSocket server;
    private ArrayList<VirtualView> clients = new ArrayList<VirtualView>();


    public void main () {
        try {
            server = new ServerSocket(SOCKET_PORT);
            ConnectionAcceptance connectionAcceptor = new ConnectionAcceptance(this);
            Thread ConnectionsAcceptThread = new Thread(connectionAcceptor);
            ConnectionsAcceptThread.start();
        }
        catch(Exception e){
            System.out.println("Error: The server could not be initialized");
        }
    }



    public void startGame (){

    }

    public Lobby getServerLobby(){return serverLobby;}

    public ServerSocket getServerSocket(){return server;}


}

