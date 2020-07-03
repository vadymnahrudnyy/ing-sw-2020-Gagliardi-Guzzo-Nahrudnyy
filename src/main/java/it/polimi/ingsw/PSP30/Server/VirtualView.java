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
    private Thread pingThread;
    private Thread virtualViewThread;
    private Thread associatedGameThread;
    private GameController associatedGame;
    private final ObjectInputStream input;
    private final ObjectOutputStream output;
    private final QueueOfEvents incomingMessages;
    private  Message message;

    /**
     * Constructor of VirtualView class instance.
     * @param clientSocket The socket of the user connected to the server.
     * @param lobby Server Lobby
     * @throws IOException When an error occurs in creating the socket or input-output streams.
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
        System.out.println(Thread.currentThread() + " User " + client.getInetAddress() + " connected");
        try {
            Ping connectionChecker = new Ping(this);
            pingThread = new Thread(connectionChecker);
            pingThread.start();
            System.out.println(Thread.currentThread() + " Ping Thread for user "+client.getInetAddress()+" created");

            askNumPlayers();
            askUsername();

            while (connected) receiveMessage();
        } catch (SocketException e) {
            System.out.println(Thread.currentThread() + " User " + client.getInetAddress() + " disconnected");
            if (isInLobby) {
                serverLobby.removePlayerFromLobby(this,username);
                closeConnection();
            }
            else if (isInGame) {
                associatedGame.setDisconnectionDetected();
                associatedGameThread.interrupt();
                try {
                    Thread.sleep(100000);
                } catch (InterruptedException ignored) { }
            }
            else closeConnection();
        }

        System.out.println(Thread.currentThread() + " Closed -- " + "Virtual view of player: " + username);
    }

    protected void setNumPlayers(int value){
        numPlayers = value;
    }

    protected void setUsername(String name){
        username = name;
    }

    protected void setAssociatedGame(GameController game){
        associatedGame = game;
    }

    /**
     * Method used to ask the player the number of players the client wants to play with.
     * @throws SocketException in case there is an error with the socket.
     */
    private void askNumPlayers () throws SocketException {
        int selectedNumPlayer;
        do{
            sendMessage(new NumPlayersRequest());
            do receiveMessage();
            while(message.getMessageID() != Message.NUM_PLAYERS_RESPONSE);
            selectedNumPlayer = ((NumPlayersResponse)message).getNumPlayers();
        } while (selectedNumPlayer != 2 && selectedNumPlayer != 3);
        setNumPlayers(((NumPlayersResponse) message).getNumPlayers());
    }

    /**
     * Method used to ask the client his username.
     * @throws SocketException in case there is an error with the socket.
     */
    private void askUsername() throws SocketException {
        do {
            sendMessage(new UsernameRequest());
            do {
                receiveMessage();
            } while (message.getMessageID() != Message.USERNAME_RESPONSE);
            setUsername(((UsernameResponse) message).getUsername());

            serverLobby.addPlayerToLobby(numPlayers, this, username, virtualViewThread);
        } while (!isInLobby && !isInGame);
    }

    /**
     * Method used to read a message(if there is one) from the input stream  and interrupts the controller thread.
     * In case a socket error occurs, usually meaning the player disconnected, enqueues in the message queue, the disconnection message
     * which will be handled by the controller during the first request of interaction from the player.
     * @throws SocketException When Socket error occurs.
     */
    private void receiveMessage() throws SocketException {
        try{
                message = (Message) input.readObject();
                if (message.getMessageID() == Message.PING_MESSAGE) {
                    pingReceived = true;
                    pingThread.interrupt();
                    return;
                }
                if (message.getMessageID() == Message.DISCONNECTION_MESSAGE) {
                    if (isInLobby) {
                        serverLobby.removePlayerFromLobby(this, username);
                        closeConnection();
                        return;
                    } else if (isInGame) {
                        associatedGame.setDisconnectionDetected();
                        associatedGameThread.interrupt();
                        return;
                    }
                }
                incomingMessages.enqueueEvent(message);
                if (isInGame) associatedGameThread.interrupt();
        }
        catch (ClassNotFoundException | IOException e) {throw new SocketException();}
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
                if(connected) System.out.println(Thread.currentThread() + " Error in sending message to " + username);
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
            sendMessage(new Disconnection());
            input.close();
            output.close();
            client.close();

        } catch (IOException ignored) {}
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
    protected boolean getInGame(){
        return isInGame;
    }
    protected void setInLobby(boolean value){
        isInLobby = value;
    }
    protected void setConnected(boolean value){
        connected = value;
    }



    /**
     * Inner class Ping implements the separate thread for each virtual view. It sends a ping message to the client and waits at least 10 seconds.
     * After the timeout the thread checks if ping was received, if not it enqueues a disconnection message which will cause the end of the game.
     */
    private static class Ping implements Runnable {
        private final VirtualView client;
        private int missedPing = 0;

        public Ping(VirtualView userVirtualView){
            client = userVirtualView;
        }
        @Override
        public void run() {
            do{
                try {
                    client.sendMessage(new PingMessage());
                    //noinspection BusyWait
                    Thread.sleep(Server.CONNECTION_TIMEOUT);
                    missedPing++;
                    if (missedPing > 3){
                        if(client.isInGame){
                            client.connected = false;
                            client.associatedGame.setDisconnectionDetected();
                            client.associatedGameThread.interrupt();
                        }
                        else if (client.isInLobby){
                            client.serverLobby.removePlayerFromLobby(client,client.getUsername());
                        }
                        else {
                            client.closeConnection();
                        }
                    }
                } catch (InterruptedException e) {
                    if (client.pingReceived) {
                        client.pingReceived = false;
                        missedPing = 0;
                    }
                }
            } while (this.client.connected);
        }
    }
}