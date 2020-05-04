package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionAcceptance implements Runnable {
    private final Lobby serverLobby;
    private final ServerSocket serverSocket;
    private Socket newClient;
    private VirtualView newVirtualView;

    public ConnectionAcceptance(){
        serverLobby = Server.getServerLobby();
        serverSocket = Server.getServerSocket();
        newClient = null;
        newVirtualView = null;


    }
    @Override
    public void run() {
        try {
            while (Server.isRunning()) {
                newClient = serverSocket.accept();
                System.out.println("BANANA accepted");
                newVirtualView = new VirtualView(newClient, serverLobby);
                Thread newVirtualViewThread = new Thread(newVirtualView);
                newVirtualViewThread.start();
                System.out.println("Virtual View created");
            }
        }catch (IOException e){
            System.out.println("Connection to the client failed");
        }
    }
}
