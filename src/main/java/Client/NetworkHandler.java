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
    private boolean isConnected;
    private QueueOfEvents incomingMessages= new QueueOfEvents();
    //private GameController controller;

    public NetworkHandler (Socket socket) throws IOException {
        this.socket=socket;
        this.input= new ObjectInputStream(socket.getInputStream());
        this.output=new ObjectOutputStream(socket.getOutputStream());
    }
    @Override
    public void run() {
        try {
            Message message;
            while(isConnected) {
                receiveMessage();
            }

        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    private void receiveMessage() throws IOException, ClassNotFoundException {
            try {
                Message message= (Message) input.readObject();
                incomingMessages.enqueueEvent(message);

                }

            catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();

            }
    }


    private void sendMessage(Message message) throws IOException {
        output.writeObject(message);
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }
}
