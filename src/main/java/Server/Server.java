package Server;

import java.net.*;

import java.util.ArrayList;

import Model.*;

/**
 * This class creates and manages the server
 * @version 1.3
 */

public class Server {

    private static final ArrayList<God> godsList = new ArrayList<>();
    private static final ArrayList<Power> powerList = new ArrayList<>();
    private static final int MAX_PLAYERS = 100;
    private static final int SOCKET_PORT = 50000;
    private static final Lobby serverLobby = new Lobby ();
    private static ServerSocket server;
    private static boolean Running = true;


    public static void main(String[] args) {
        try {
            server = new ServerSocket(SOCKET_PORT);
            ConnectionAcceptance connectionAcceptor = new ConnectionAcceptance();
            Thread ConnectionsAcceptThread = new Thread(connectionAcceptor);
            ConnectionsAcceptThread.start();
            System.out.println("Connection acceptance created");
        }
        catch(Exception e){
            System.out.println("Error: The server could not be initialized");
        }
    }

    public static Lobby getServerLobby(){return serverLobby;}

    public static ServerSocket getServerSocket(){return server;}


    public static ArrayList<God> getGodsList() {
        return godsList;
    }

    public static ArrayList<Power> getPowerList() {
        return powerList;
    }
    public static boolean isRunning() {
        return Running;
    }


}

