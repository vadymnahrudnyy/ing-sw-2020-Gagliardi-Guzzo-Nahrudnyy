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
    private String username;
    private final Lobby serverLobby;
    private boolean connected = true;
    private boolean pingReceived;
    private final ObjectInputStream input;
    private final ObjectOutputStream output;
    private final QueueOfEvents incomingMessages= new QueueOfEvents();

    /**
     * Constructor of VirtualView class.
     * @param client specifies the client connected to the server.
     */
    public VirtualView(Socket client,Lobby lobby) throws IOException {
        this.client = client;
        this.output = new ObjectOutputStream(client.getOutputStream());
        this.input = new ObjectInputStream(client.getInputStream());
        serverLobby = lobby;
    }


    @Override
    public void run() {
        try {
            System.out.println("User "+client.getInetAddress()+" connected");
            Message message;
            System.out.println("Ping Thread Created");
            boolean insertedInLobby;
            try{
                do {
                sendMessage(new NumPlayersRequest());
                message = (Message) input.readObject();
                } while (message.getMessageID() != Message.NUM_PLAYERS_RESPONSE);
                int numPlayers = ((NumPlayersResponse) message).getNumPlayers();
                do {
                    do {
                        sendMessage(new UsernameRequest());
                        message = (Message) input.readObject();
                    } while (message.getMessageID() != Message.USERNAME_RESPONSE);
                    username = ((UsernameResponse) message).getUsername();
                    insertedInLobby = serverLobby.addPlayerToLobby(numPlayers,this,username);
                }while(!insertedInLobby);
            } catch (SocketException e){
                connected = false;
            }
            Ping connectionChecker = new Ping(this);
            Thread pingThread = new Thread(connectionChecker);
            pingThread.start();
            System.out.println("Ping Thread Created");

            while (connected) try{
                receiveMessage();
            }catch(SocketException e){
                connected = false;
            }
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This methods receives messages from client, adds them into the queue and notify the controller of the update.
     */
    private void receiveMessage() {
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
    public synchronized void sendMessage(Message message) {
        synchronized (output){
            try{
                output.writeObject(message);
        } catch (IOException e) {
                e.printStackTrace();
            }
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
            input.close();
            output.close();
            client.close();

        } catch (IOException e) {
            System.out.println("Disconnection Failed");
        }
        connected = false;
    }

    /**
     * Getter that return the Id of the connection for the specific client
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
                    Thread.sleep(CONNECTION_TIMEOUT);
                    if (!client.pingReceived) client.incomingMessages.enqueueEvent(new Disconnection());
                } catch (InterruptedException e) {
                    client.incomingMessages.enqueueEvent(new Disconnection());
                    client.connected = false;
                }
            } while (client.pingReceived);
        }
    }
}