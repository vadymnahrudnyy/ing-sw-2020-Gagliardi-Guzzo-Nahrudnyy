package Server;

import java.net.*;
import java.io.*;
import java.util.ArrayList;

/**
 * This class creates and manages the server
 * @author Alessia Gagliardi
 * @version 1.0
 */

public class Server{

    private ServerSocket server;
    private ArrayList<VirtualView> clients = new ArrayList<VirtualView>();
    private Lobby lobby = new Lobby;
    private final int port;


    /**
     * This is the constructor of the class
     * @param port specifies which is the port through which the clients can communicate with the server
     */
    public Server(int port) {
        this.port=port;
    }


    /**
     * This method create the server
     */

    public void start() {
        try {
            server = new ServerSocket(port);
            while (true){
                Socket socket= server.accept();
                VirtualView client= new VirtualView();
                clients.add(client);
            }
        } catch (IOException e) {
            System.out.println("Error");
        }
    }


    /**
     * This class controls if the username chose by the new players is already used by another player and in that case the Server asks the player to choose another username
     * @param username tells which username the player chose
     * @return true if the username is not in use already or false if the username is already been taken
     */
    public boolean checkUsername (String username){
        try{
            for ( )
                if (/*controlla che nella lobby non ci sia giù user con stesso nome*/){
                }

        } catch (Exception e){
            System.out.println("Error");
        }
        return true;
    }



    public boolean checkNumPlayers(){
        try{
                if (lobbies.size() == maxNumGiocatori)
                    return true;;
            }
        } catch (Exception e){
            System.out.println("Error");
        }



    public synchronized void initializeGame (){

        }

    public synchronized void startGame (){
        if(checkNumPlayers()==true){

        }
    }

    /**
     * This is the main which creates the object Server and initialize the port
     */
        public static void main ()
        {
            Server server = new Server(6789);
        }


