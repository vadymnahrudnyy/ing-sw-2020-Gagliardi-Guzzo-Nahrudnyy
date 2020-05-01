package Client;
import Messages.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import Utils.QueueOfEvents;

/**
 * This class behave like the server for client
 * @version 1.0
 */
public class NetworkHandler implements Runnable {
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private boolean isConnected=false;
    private String username;
    private final String serverAddress;
    private final int serverPort;
    private QueueOfEvents incomingMessages= new QueueOfEvents();
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

    public void sendMessage(Message message) throws IOException {
        output.writeObject(message);
    }

    public QueueOfEvents receive() throws IOException {
        Message message=new Message();
        isPing(message);
        if(!isPing) incomingMessages.enqueueEvent(message);
        return incomingMessages;
    }

    public void close() throws Exception {
        socket.close();
        setConnected(false);
    }

    public void run() {
        try {
            Socket server=new Socket(serverAddress, serverPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (isConnected){
            try {
                Message message= (Message) input.readObject();
                if(message!=null){
                    receive();
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
        isConnected = connected;
    }
    /**
     *This class disconnect the client.
     */
    private void disconnect(){
        try {
            close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Getter for the parameter username.
     * @return the player's username
     */
    public String getUsername() {
        return username;
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
        if (message.getMessageID()==501){
            setPing(true);
            sendMessage(new PingMessage());
        }
        return true;
    }
}