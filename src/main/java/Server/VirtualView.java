package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


/**
 * This class behave like the View for Controller and Model
 * @author Alessia Gagliardi
 * @version 1.0
 */

public class VirtualView implements Runnable{
    public Server server;
    private Socket client;

    private ObjectOutputStream output;
    private ObjectInputStream input;

    private String username;
    private boolean isConnected;

    /**
     * This is the constructor of VirtualView class
     * @param client specifies the client connected to the server
     */
    public VirtualView(Socket client, Server server) {
        this.server=server;
        this.isConnected=true;
        this.client = client;
    }


    /**
     * This method allows the server to manage the connection between a client and the server
     * @throws IOException if the stream from the client is not valid
     */

    private void handleClientConnection() throws IOException
    {
        try {
            while (true) {
                output = new ObjectOutputStream(client.getOutputStream());
                input = new ObjectInputStream(client.getInputStream());
            }

            /*Object message;
            do {
                message = input.readObject();
                if (message != null) receiveMessage(message);
            } while (message != null);*/
        }
        catch (/*ClassNotFoundException | ClassCastException e*/) {
            System.out.println("invalid stream from client");
        }

        client.close();
    }


    /**
     * This method checks the connection
     */
    @Override
    public void run()
    {
        try {
            handleClientConnection();
        } catch (IOException e) {
            System.out.println("client " + client.getInetAddress() + " connection dropped");
        }
    }


    /**
     * This class handles messages from client to server
     * @param message indicates the input from client
     */
    private void receiveMessage (Object message){

    }

    /**
     * This method allows the server to send a message
     * @param message indicates the output message from server
     */
    public void sendMessage (Object message){

    }


    /**
     * This method gets the username choose by the client
     * @return the username string
     */
    public String getUsername() {
        return username;
    }

    /**
     * This method sets the username choose by the client
     * @param username indicates the username string
     */
    public void setUsername(String username) {
        this.username = username;
    }


   //LE SEGUENTI DUE CLASSI SONO NECESSARIE????


    /**
     * Getter for variable isConnected
     * @return true if the connection is still open, false if the connection is closed
     */
    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    
    public void closeConnection () throws IOException{
        if(input!=null) input.close();
        if(output!=null) output.close();
        client.close();
    }


}
