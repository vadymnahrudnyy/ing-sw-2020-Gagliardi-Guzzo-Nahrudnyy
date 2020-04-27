package Server;

import Messages.*;
import Utils.QueueOfEvents;


import java.io.*;
import java.net.*;


/**
 * This class behave like the View for Controller and Model
 * @version 1.3
 */

public class VirtualView implements Runnable {
    private Socket client;
    private int numPlayers;
    private String username;
    private Lobby serverLobby;
    private boolean isConnected;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private QueueOfEvents incomingMessages= new QueueOfEvents();
    //private GameController controller;


    @Override
    public void run() {
        try {
            Message message;
            sendMessage(new UsernameRequest());
            do{
                message = (Message) input.readObject();
            }while (message.getMessageID() != 201);
            username = ((UsernameResponse) message).getUsername();
            sendMessage(new NumPlayersRequest());
            do{
                message = (Message) input.readObject();
            }while (message.getMessageID() != 202);
            numPlayers = ((NumPlayersResponse)message).getNumPlayers();
            insertInLobby ();
            while (isConnected) receiveMessage();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * This method insert the new client connected in to the lobby categorized by the number of players it wants to play with.
     */
    private void insertInLobby() {
        if(numPlayers==2 && checkUsername(username)) serverLobby.addPlayerToTwoPlayersLobby(username,this);
        else if (numPlayers==3 && checkUsername(username) ) serverLobby.addPlayerToThreePlayersLobby(username,this);

        //Errore nel caso lo username già è presente
        serverLobby.checkReady();
    }


    /**
     * This is the constructor of VirtualView class.
     * @param client specifies the client connected to the server.
     */
    public VirtualView(Socket client,Lobby lobby) throws IOException {
        {
            this.client = client;
            this.output = new ObjectOutputStream(client.getOutputStream());
            this.input = new ObjectInputStream(client.getInputStream());
            serverLobby = lobby;

        }
        this.isConnected = true;

    }


    /**
     * This methods receives messages from client, adds them into the queue and notify the cotroller of the update.
     *
     * @throws IOException            if there are exceptions that cannot be handled.
     * @throws ClassNotFoundException if the message does not belong to one of the expected types.
     */
    private void receiveMessage() throws IOException, ClassNotFoundException {
        try {
            Message message= (Message) input.readObject();

            incomingMessages.enqueueEvent(message);
        }
        catch (ClassNotFoundException | IOException e) {

        e.printStackTrace();

        }
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
     * This method close the connection if one of the players disconnect
     */
    public synchronized void closeConnection() {
        try {
            //togli player
            client.close();
        } catch (IOException e) {
            System.out.println("Disconnection Failed");
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
        return ((serverLobby.getTwoPlayersLobby().contains(username))||(serverLobby.getThreePlayersLobby().contains(username)));
    }

    /**
     * Getter of the queue of the incoming messages.
     * @return the queue of the incoming messages.
     */
    public QueueOfEvents getIncomingMessages() {
        return incomingMessages;
    }

    public Message dequeueFirstMessage(){
        return getIncomingMessages().dequeueEvent();
    }
}