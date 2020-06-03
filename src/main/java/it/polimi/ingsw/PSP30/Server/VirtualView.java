package it.polimi.ingsw.PSP30.Server;


import it.polimi.ingsw.PSP30.Messages.*;
import it.polimi.ingsw.PSP30.Utils.QueueOfEvents;

import java.io.*;
import java.net.*;


/**
 * This class behave like the View for Controller and Model
 * @version 1.5
 */

public class VirtualView implements Runnable {
    private int numPlayers;
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
            System.out.println("User " + client.getInetAddress() + " connected");
            Message message;
            try {
                do {
                    sendMessage(new NumPlayersRequest());
                    message = (Message) input.readObject();
                } while (message.getMessageID() != Message.NUM_PLAYERS_RESPONSE && (((NumPlayersResponse) message).getNumPlayers() != 2 && ((NumPlayersResponse) message).getNumPlayers() != 3));
                setNumPlayers(((NumPlayersResponse) message).getNumPlayers());
                do {
                    do {
                        sendMessage(new UsernameRequest());
                        message = (Message) input.readObject();
                    } while (message.getMessageID() != Message.USERNAME_RESPONSE);
                    setUsername(((UsernameResponse) message).getUsername());
                    serverLobby.addPlayerToLobby(numPlayers, this, username, virtualViewThread);
                    Thread.sleep(100);
                } while (!isInLobby);
            } catch (SocketException e) {
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
    protected void setNumPlayers(int value){
        numPlayers = value;
    }

    protected void setUsername(String name){
        username = name;
    }

    /**
     * Method used to read a message(if there is one) from the input stream  and interrupts the controller thread.
     * In case a socket error occurs, usually meaning the player disconnected, enqueues in the message queue, the disconnection message
     * which will be handled by the controller during the first request of interaction from the player.
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
     * Method used to send a message to the client. Before each operation it
     * flushes the stream in order to avoid usually serialized objects to be cached.
     * @param message the message to send.
     */
    protected void sendMessage(Message message) {
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
    protected String getUsername() { return username; }

    /**
     * Method used to obtain the desired number of player for the game of the associated client.
     * @return desired number of players.
     */
    protected int getNumPlayers(){
        return numPlayers;
    }

    /**
     * Method used to set the associatedGameThread for an instance of class VirtualView.
     * @param gameThread the Game the player is associated to.
     */
    protected void setAssociatedGameThread(Thread gameThread){
        associatedGameThread = gameThread;
    }

    /**
     * Method used to obtain the thread the given virtual view is running on.
     * @return Thread object.
     */
    protected Thread getVirtualViewThread(){
        return virtualViewThread;
    }

    /**
     * This method close the connection if one of the players disconnect
     */
    protected synchronized void closeConnection() {
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
    protected QueueOfEvents getIncomingMessages() {
        return incomingMessages;
    }

    /**
     * Method used to read the first message in the queue.
     * @return the first message of the queue.
     */
    public  Message dequeueFirstMessage(){
        return getIncomingMessages().dequeueEvent();
    }

    protected void setInGame(boolean value){
        isInGame = value;
    }
    protected void setInLobby(boolean value){
        isInLobby = value;
    }
    protected boolean isInGame(){
        return isInGame;
    }
    protected boolean isInLobby(){
        return isInLobby;
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