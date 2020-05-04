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
    private final Socket client;
    private int numPlayers;
    private String username;
    private Lobby serverLobby;
    private boolean isConnected;
    private boolean pingReceived;
    private final ObjectInputStream input;
    private final ObjectOutputStream output;
    private QueueOfEvents incomingMessages= new QueueOfEvents();
    //private GameController controller;


    @Override
    public void run() {
        try {
            System.out.println("User connected");
            Message message;
            sendMessage(new UsernameRequest());
            Ping connectionChecker = new Ping(this);
            Thread pingThread = new Thread(connectionChecker);
            pingThread.start();
//
            do{
                message = (Message) input.readObject();
            }while (message.getMessageID() != 201);
            username = ((UsernameResponse) message).getUsername();
            sendMessage(new NumPlayersRequest());
            do{
                message = (Message) input.readObject();
            }while (message.getMessageID() != 202);
            numPlayers = ((NumPlayersResponse)message).getNumPlayers();

//
            while (isConnected) receiveMessage();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This is the constructor of VirtualView class.
     * @param client specifies the client connected to the server.
     */
    public VirtualView(Socket client,Lobby lobby) throws IOException {
        System.out.println("creating virtual view");
        this.client = client;
        this.output = new ObjectOutputStream(client.getOutputStream());
        System.out.println("prova1");
        this.input = new ObjectInputStream(client.getInputStream());
        System.out.println("prova2");
        serverLobby = lobby;
        this.isConnected = true;
    }



    /**
     * This methods receives messages from client, adds them into the queue and notify the controller of the update.
     *
     * @throws IOException            if there are exceptions that cannot be handled.
     * @throws ClassNotFoundException if the message does not belong to one of the expected types.
     */
    private void receiveMessage() throws IOException, ClassNotFoundException {
        try {
            Message message= (Message) input.readObject();
            if (message.getMessageID() == Message.PING_MESSAGE) pingReceived = true;
            else incomingMessages.enqueueEvent(message);
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
    public synchronized void sendMessage(Message message) throws IOException {
        synchronized (output){
            output.writeObject(message);
        }
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
     * Getter of the queue of the incoming messages.
     * @return the queue of the incoming messages.
     */
    public QueueOfEvents getIncomingMessages() {
        return incomingMessages;
    }

    public Message dequeueFirstMessage(){
        return getIncomingMessages().dequeueEvent();
    }

    /**
     * Inner class Ping implements the separate thread for each virtual view. It sends a ping message to the client and waits at least 10 seconds.
     * After the timeout the thread checks if ping was received, if not it enqueues a disconnection message which will cause the end of the game.
     */
    private static class Ping implements Runnable {
        private final VirtualView client;
        private static final int CONNECTION_TIMEOUT = 10000;//10 seconds

        public Ping(VirtualView userVirtualView){
            client = userVirtualView;
        }
        @Override
        public void run() {
            do{
                try {
                    client.pingReceived = false;
                    client.sendMessage(new PingMessage());
                    wait(CONNECTION_TIMEOUT);
                    if (!client.pingReceived) client.incomingMessages.enqueueEvent(new Disconnection());
                } catch (IOException | InterruptedException e) {
                    client.incomingMessages.enqueueEvent(new Disconnection());
                    client.isConnected = false;
                }
            } while (client.pingReceived);

        }
    }
}