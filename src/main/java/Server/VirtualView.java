package Server;

import Messages.*;
import Utils.QueueOfEvents;

import java.io.*;
import java.net.*;


/**
 * This class behave like the View for Controller and Model
 * @version 1.5
 */

public class VirtualView implements Runnable {
    private String username;
    private final Socket client;
    private final Lobby serverLobby;
    private boolean connected = true;
    private boolean pingReceived;
    private boolean isInGame = false;
    private boolean isInLobby = false;
    private Thread virtualViewThread;
    private Thread associatedGameThread;
    private final ObjectInputStream input;
    private final ObjectOutputStream output;
    private final QueueOfEvents incomingMessages;


    /**
     * Constructor of VirtualView class instance.
     * @param clientSocket the socket of the user connected to the server.
     */
    public VirtualView(Socket clientSocket,Lobby lobby) throws IOException {
        serverLobby = lobby;
        client = clientSocket;
        incomingMessages= new QueueOfEvents();
        output = new ObjectOutputStream(client.getOutputStream());
        input = new ObjectInputStream(client.getInputStream());
    }


    @Override
    public void run() {
        virtualViewThread = Thread.currentThread();
        try {
            System.out.println("User "+client.getInetAddress()+" connected");
            Message message;
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
                    serverLobby.addPlayerToLobby(numPlayers,this,username,virtualViewThread);
                    Thread.sleep(1000000);
                }while(!isInLobby);
            } catch (SocketException e){
                connected = false;
                closeConnection();
            } catch (InterruptedException e) {
                boolean reset = Thread.interrupted();
            }

            //Ping connectionChecker = new Ping(this);
            //Thread pingThread = new Thread(connectionChecker);
            //pingThread.start();
            //System.out.println("Ping Thread Created");
            //System.out.println("Ping Thread for user "+client.getInetAddress()+" created");


            while (connected) receiveMessage();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * This methods receives messages from client, adds them into the queue and notify the controller of the update.
     */
    private void receiveMessage(){
        try{
            try{
                Message message= (Message) input.readObject();
                if (message.getMessageID() == Message.PING_MESSAGE) pingReceived = true;
                else incomingMessages.enqueueEvent(message);
                associatedGameThread.interrupt();
            }catch (SocketException e){
                System.out.println("User "+client.getInetAddress()+" disconnected");
                if (isInLobby) {
                    serverLobby.removePlayerFromLobby(this,username,virtualViewThread);
                    closeConnection();
                }
                if (isInGame) {
                    incomingMessages.enqueueEvent(new Disconnection());
                    try {
                        Thread.sleep(1000000000);
                    } catch (InterruptedException interruption) {
                        closeConnection();
                    }
                }
            }
        }
        catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method used to send a message to the client
     * @param message the message to send.
     */
    public void sendMessage(Message message) {
        synchronized (output){
            try{
                output.flush();
                output.reset();
                output.writeObject(message);
        } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Method used to get the username chosen by the player associated to the virtual list
     * @return the username string.
     */
    public String getUsername() { return username; }

    /**
     * Method used to set the associatedGameThread for an instance of class VirtualView.
     * @param gameThread the Game the player is associated to.
     */
    public void setAssociatedGameThread(Thread gameThread){
        associatedGameThread = gameThread;
    }

    /**
     * This method close the connection if one of the players disconnect
     */
    public synchronized void closeConnection() {
        try {
            connected = false;
            input.close();
            output.close();
            client.close();

        } catch (IOException e) {
            System.out.println("Disconnection Failed");
        }
        connected = false;
    }

    /**
     * Getter of the queue of the incoming messages.
     * @return the queue of the incoming messages.
     */
    public QueueOfEvents getIncomingMessages() {
        return incomingMessages;
    }

    /**
     * Method used to read the first message in the queue.
     * @return the first message of the queue.
     */
    public  Message dequeueFirstMessage(){
        return getIncomingMessages().dequeueEvent();
    }

    public void setInGame(boolean value){
        isInGame = value;
    }
    public void setInLobby(boolean value){
        isInLobby = value;
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