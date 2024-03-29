package it.polimi.ingsw.PSP30.Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import it.polimi.ingsw.PSP30.Server.Server;
import it.polimi.ingsw.PSP30.Messages.Message;
import it.polimi.ingsw.PSP30.Utils.QueueOfEvents;
import it.polimi.ingsw.PSP30.Messages.PingMessage;
import it.polimi.ingsw.PSP30.Messages.Disconnection;

/**
 * This class is used for communication with the Server.
 * @version 1.0
 */
public class NetworkHandler implements Runnable {
    private static Socket socket;
    private static ObjectInputStream input;
    private static ObjectOutputStream output;

    private final int serverPort;
    private final String serverAddress;
    private boolean pingReceived = false;
    private static boolean isConnected=false;

    private Thread pingThread;
    private static Thread clientThread;

    protected static final QueueOfEvents incomingMessages= new QueueOfEvents();

    public NetworkHandler(String serverAddress, int serverPort, Thread thread) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        clientThread=thread;
    }

    /**
     * This method is executed on a new thread, it starts the connection with the server and when the connection is established it
     * distinguishes between ping or normal messages. If if a ping message is received, the received ping's flag is set to "true"
     * and then the ping thread is interrupted, otherwise the message is queued.
     */
    public void run() {
        try {
            connect();
        } catch (Exception e) {
            setConnected(false);
            Client.addressError();
            clientThread.interrupt();
            try{
                Thread.sleep(10000);
            }catch (InterruptedException ignored){}
        }

        if(isConnected){
            Runnable Ping = () -> {
                int missedPings = 0;
                do{
                    try {
                        sendMessage(new PingMessage());
                        Thread.sleep(Server.CONNECTION_TIMEOUT);
                        if (!pingReceived) missedPings++;
                        if(missedPings > 2) disconnect();
                    } catch (InterruptedException e) {
                        if (pingReceived){
                            setPingReceived(false);
                            missedPings = 0;
                        }
                    }
                }while(isConnected);
            };
            pingThread = new Thread(Ping);
            pingThread.start();

        }
        while (isConnected){
            try {
                Message message= (Message) input.readObject();
                if(message!=null) receive(message);
            }
            catch (IOException | ClassNotFoundException e) {//error in communication
                disconnect();
            }
        }
    }


    /**
     * Getter of parameter connected.
     * @return true if the client is connected to the server or false if it is not connected
     */
    public static boolean isConnected() {
        return isConnected;
    }

    /**
     * Getter of parameter connected.
     * @param connected is the boolean that indicates if the client is connected to the server or not
     */
    public static void setConnected(boolean connected) {
        Client.setDisconnected(!connected);
        isConnected = connected;
    }

    /**
     * This method establishes the connection and creates ObjectInput and ObjectOutput streams.
     * @throws Exception is thrown when the connection attempt has failed
     */
    public void connect() throws Exception {
        socket=new Socket(getServerAddress(),getServerPort());
        input=new ObjectInputStream(socket.getInputStream());
        output=new ObjectOutputStream(socket.getOutputStream());
        setConnected(true);
        clientThread.interrupt();
    }

    /**
     *This class disconnect the client.
     */
    public static void disconnect(){

        incomingMessages.enqueueEvent(new Disconnection());
        clientThread.interrupt();
        try {
            close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method manages the sending of a message.
     * @param message is the message which has to be sent
     */
    public static void sendMessage(Message message) {
        //noinspection SynchronizeOnNonFinalField
        synchronized (output){
            try {
                output.flush();
                output.reset();
                output.writeObject(message);
            } catch (IOException e) {
                isConnected = false;
                disconnect();
            }
        }
    }

    /**
     * This method manages the reception of a message and the consequent queued of it.
     * @param receivedMessage is the incoming message
     */
    public void receive(Message receivedMessage) {
        if (isPing(receivedMessage)) return;
        incomingMessages.enqueueEvent(receivedMessage);
        if (receivedMessage.getMessageID() == Message.DISCONNECTION_MESSAGE || receivedMessage.getMessageID() == Message.PLAYER_DISCONNECTED_ERROR) {
            //clientThread.interrupt();
            setConnected(false);
        }
    }

    /**
     * This method closes the socket.
     * @throws Exception when the socket failed to be closed
     */
    public static void close() throws Exception {
        socket.close();
        setConnected(false);
    }

    /**
     * Getter of the server address.
     * @return the address of the server
     */
    public String getServerAddress() {
        return serverAddress;
    }

    /**
     * Getter of the server port.
     * @return the port of the server
     */
    public int getServerPort() {
        return serverPort;
    }

    /**
     * Setter parameter ping
     * @param ping value of the parameter
     */
    public void setPingReceived(boolean ping) {
        pingReceived = ping;
    }

    /**
     * Method that manages the ping message
     * @param message value of the message
     * @return true if the message was a ping
     */
    public boolean isPing(Message message) {
        if (message.getMessageID()==Message.PING_MESSAGE){
            setPingReceived(true);
            pingThread.interrupt();
            return true;
        }
        return false;
    }
}