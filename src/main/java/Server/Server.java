package Server;

import java.net.*;
import java.io.*;
import java.util.ArrayList;

import Messages.NumPlayersRequest;
import Messages.NumPlayersResponse;
import Messages.UsernameRequest;
import Messages.UsernameResponse;
import Model.*;
import jdk.internal.org.objectweb.asm.commons.InstructionAdapter;

/**
 * This class creates and manages the server
 * @version 1.1
 */

public class Server {

    private static final ArrayList<God> godsList = new ArrayList<God>();
    private static final ArrayList<Power> powerList = new ArrayList<Power>();
    private static final int MAX_PLAYERS = 100;
    private static final int SOCKET_PORT = 50000;
    private static final Lobby serverLobby = new Lobby (MAX_PLAYERS);
    private static ServerSocket server;
    private ArrayList<VirtualView> clients = new ArrayList<VirtualView>();


    public static void main () {
        try {
            server = new ServerSocket(SOCKET_PORT);
            while (true) {
                Socket client = server.accept();
                VirtualView newVirtualView = new VirtualView(client);
                Thread newVirtualViewThread = new Thread(newVirtualView);
            }
        }
        catch(Exception e){

            System.out.println("Error: The server could not be initialized");

        }
    }

    public void startGame (){

    }
}

