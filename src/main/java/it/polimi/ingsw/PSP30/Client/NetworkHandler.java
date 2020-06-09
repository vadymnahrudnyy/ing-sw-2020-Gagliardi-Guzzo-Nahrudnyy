package it.polimi.ingsw.PSP30.Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import it.polimi.ingsw.PSP30.Messages.Disconnection;
import it.polimi.ingsw.PSP30.Messages.Message;
import it.polimi.ingsw.PSP30.Utils.QueueOfEvents;
import it.polimi.ingsw.PSP30.Messages.PingMessage;

/**
 * This class behave like the server for client
 * @version 1.0
 */
public class NetworkHandler implements Runnable {
    private static Socket socket;
    private static ObjectInputStream input;
    private static ObjectOutputStream output;
    private static boolean isConnected=false;
    private final String serverAddress;
    private final int serverPort;
    protected static final QueueOfEvents incomingMessages= new QueueOfEvents();
    private boolean pingReceived = false;
    private Thread pingThread;
    private static Thread clientThread;


    public NetworkHandler(String serverAddress, int serverPort, Thread thread) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        clientThread=thread;
    }

    public void connect() throws Exception {
        socket=new Socket(getServerAddress(),getServerPort());
        input=new ObjectInputStream(socket.getInputStream());
        output=new ObjectOutputStream(socket.getOutputStream());
        setConnected(true);
        clientThread.interrupt();
    }

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

    public void receive(Message receivedMessage) throws IOException {
        if(!isPing(receivedMessage)) incomingMessages.enqueueEvent(receivedMessage);
    }

    public static void close() throws Exception {
        socket.close();
        setConnected(false);
    }

    public void run() {
        try {
            connect();
        } catch (Exception e) {
            setConnected(false);
            Client.addressError();
            clientThread.interrupt();
            try{
                Thread.sleep(10000);
            }catch (InterruptedException interruptedException){
                interruptedException.printStackTrace();
            }
        }
        if(isConnected){
            Runnable Ping = new Runnable() {
                @SuppressWarnings("BusyWait")
                @Override
                public void run() {
                    int missedPings = 0;
                    do{
                        try {
                            sendMessage(new PingMessage());
                            //10 Second
                            int PING_TIMEOUT = 9000;//10 Seconds
                            Thread.sleep(PING_TIMEOUT);
                            if (!pingReceived) missedPings++;
                            if(missedPings > 3){
                                incomingMessages.enqueueEvent(new Disconnection());
                                isConnected = false;
                            }
                        } catch (InterruptedException e) {
                            if (pingReceived){
                                setPingReceived(false);
                                missedPings = 0;
                            }
                        }
                    }while(isConnected);
                }
            };
            pingThread = new Thread(Ping);
            pingThread.start();

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
     * Getter of parameter connected.
     * @return true if the client is connected to the server or false if it is not connected.
     */
    public static boolean isConnected() {
        return isConnected;
    }

    /**
     * Getter of parameter connected.
     * @param connected is the boolean that indicates if the client is connected to the server or not.
     */
    public static void setConnected(boolean connected) {
        Client.setDisconnected(!connected);
        isConnected = connected;
    }

    /**
     *This class disconnect the client.
     */
    public static void disconnect(){
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
    public void setPingReceived(boolean ping) {
        pingReceived = ping;
    }

    /**
     * Method that manages the ping message
     * @param message value of the message
     * @return true if the message was a ping
     * @throws IOException if there are connection problems.
     */
    public boolean isPing(Message message) throws IOException {
        if (message.getMessageID()==Message.PING_MESSAGE){
            setPingReceived(true);
            pingThread.interrupt();
            return true;
        }
        return false;
    }
}