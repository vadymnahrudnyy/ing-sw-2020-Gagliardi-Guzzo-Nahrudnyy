package Server;

import Messages.Message;
import Messages.SelectWorkerRequest;
import Messages.UsernameRequest;

import java.io.*;
import java.net.*;
import java.util.ArrayList;


/**
 * This class behave like the View for Controller and Model
 * @version 1.1
 */

public class VirtualView implements Runnable {


    private Socket client;
    private String username;
    private boolean isConnected;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Lobby lobby;
    private int numPlayers;

    @Override
    public void run() {
        try {

            SelectWorkerRequest numPlayersChoose = new SelectWorkerRequest(); //richiedo numeroGiocatori
            //ricevo numeroGiocatori

            do{
            UsernameRequest user = new UsernameRequest(); ////richiedo username
            //ricevo username
            if(checkUsername(username)) {
                insertInLobby (username, numPlayers);
            }
            else {
               //MESSAGGIO ERRORE "Username already taken. Try again."
            }
            }while (checkUsername(username));

            while (isConnected) receiveMessage(); //continuo a ricevere dal client

        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method insert the new client connected in to the lobby categorized by the number of players it wants to play with.
     * @param username is reffered to the new client connected.
     * @param numPlayers is the number of player the client wants in its game.
     */
    private void insertInLobby(String username, int numPlayers) {
        lobby.addPlayerToServer(username);
        if(numPlayers==2) lobby.addPlayerToTwoPlayersLobby(username);
        else if (numPlayers==3) lobby.addPlayerToThreePlayersLobby(username);
        lobby.addVirtualView(this);
        lobby.checkReady(numPlayers);
    }


    /**
     * This is the constructor of VirtualView class.
     *
     * @param client specifies the client connected to the server.
     */
    public VirtualView(Socket client) throws IOException {
        {
            this.client = client;
            this.output = new ObjectOutputStream(client.getOutputStream());
            this.input = new ObjectInputStream(client.getInputStream());

            //  this.guestString = "Guest" + (new Random()).nextInt(9999);
        }
        this.isConnected = true;

    }


    /**
     * This methods receives messages from client.
     *
     * @throws IOException            if there are exceptions that cannot be handled.
     * @throws ClassNotFoundException if the message does not belong to one of the expected types.
     */
    private void receiveMessage() throws IOException, ClassNotFoundException {
        Message message = (Message) input.readObject();
        if (message instanceof Message) {
            //((Message) message).accept(new MessageParser(this));
            return;
        }
        //((Message) message).accept(new MessageParser(this));
    }


    /**
     * This method allows the server to send a message.
     *
     * @param message indicates the output message from server.
     */
    public void sendMessage(Message message) throws IOException {
        output.writeObject(message);
    }

    /**
     * This method gets the username choose by the client.
     *
     * @return the username string.
     */
    public String getUsername() { return username; }

    /**
     * This method sets the username choose by the client
     *
     * @param username indicates the username string
     */
    public void setUsername(String username) { this.username = username; }

    /**
     * Getter for variable isConnected
     *
     * @return true if the connection is still open, false if the connection is closed
     */
    public boolean ping() { return isConnected; }

    /**
     * This method close the connection if one of the players disconnect
     */
    public synchronized void closeConnection() {
        try {
            //togli player
            client.close();
        } catch (IOException e) {
        }
        isConnected = false;
    }

    /**
     * Getter that return the Id of the connection for the specific client
     *
     * @return return the ID of the connection for the specific client
     */
    public SocketAddress getConnectionID() { return this.client.getRemoteSocketAddress(); }


    /**
     * This method checks that there is no other client that have the same username choose by the new client connected
     * @param username is referred to the new client
     * @return true if there is no other username like this, false if there is already a client connected that had choose that username
     */
    public boolean checkUsername(String username) {
        ArrayList<String> playerConnected = lobby.getServerPlayers();
        for (String player : playerConnected) {
            if (player.equals(username)) return false;
        }
        return true;
    }
}