package it.polimi.ingsw.PSP30.Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import it.polimi.ingsw.PSP30.Messages.Message;
import it.polimi.ingsw.PSP30.Utils.QueueOfEvents;
import it.polimi.ingsw.PSP30.Messages.PingMessage;

/**
 * This class behave like the server for client
 * @version 1.0
 */
public class NetworkHandler implements Runnable {
    private Socket socket;
    private ObjectInputStream input;
    private static ObjectOutputStream output;
    private boolean isConnected=false;
    private final String serverAddress;
    private final int serverPort;
    protected static final QueueOfEvents incomingMessages= new QueueOfEvents();
    private boolean isPing=false;


    public NetworkHandler(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    public void connect() throws Exception {
        socket=new Socket(getServerAddress(),getServerPort());
        input=new ObjectInputStream(socket.getInputStream());
        output=new ObjectOutputStream(socket.getOutputStream());
        setConnected(true);
    }

    public static void sendMessage(Message message) {
        synchronized (output){
            try {
                output.flush();
                output.reset();
                output.writeObject(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public QueueOfEvents receive(Message receivedMessage) throws IOException {
        isPing(receivedMessage);
        if(!isPing) incomingMessages.enqueueEvent(receivedMessage);
        return incomingMessages;
    }

    public void close() throws Exception {
        socket.close();
        setConnected(false);
    }

    public void run() {
        try {
            connect();
        } catch (Exception e) {
            setConnected(false);
            Client.addressError();
        }
        while (isConnected){
            try {
                Message message= (Message) input.readObject();
                if(message!=null){
                    receive(message);
                }
            }
            catch (IOException e) {
                disconnect();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Setter of parameter connected.
     * @return true if the client is connected to the server or false if it is not connected.
     */
    public boolean isConnected() {
        return isConnected;
    }

    /**
     * Getter of parameter connected.
     * @param connected is the boolean that indicates if the client is connected to the server or not.
     */
    public void setConnected(boolean connected) {
        Client.setDisconnected(!connected);
        isConnected = connected;
    }
    /**
     *This class disconnect the client.
     */
    public void disconnect(){
        try {
            close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Getter of the server address.
     * @return the address of the server.
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
    public void setPing(boolean ping) {
        isPing = ping;
    }

    /**
     * Method that manages the ping message
     * @param message value of the message
     * @return true if the message was a ping
     * @throws IOException if there are connection problems.
     */
    public boolean isPing(Message message) throws IOException {
        if (message.getMessageID()==Message.PING_MESSAGE){
            setPing(true);
            sendMessage(new PingMessage());
        }
        return true;
    }
}