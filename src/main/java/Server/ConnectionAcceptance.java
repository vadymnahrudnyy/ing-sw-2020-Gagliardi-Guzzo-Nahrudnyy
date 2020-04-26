package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionAcceptance implements Runnable {
    private final Lobby serverLobby;
    private final ServerSocket serverSocket;
    private Socket newClient;
    private VirtualView newVirtualView;

    public ConnectionAcceptance(Server server){
        serverLobby = server.getServerLobby();
        serverSocket = server.getServerSocket();
        newClient = null;
        newVirtualView = null;


    }
    @Override
    public void run() {
        try {
            while (true) {
                newClient = serverSocket.accept();
                newVirtualView = new VirtualView(newClient, serverLobby);
            }
        }catch (IOException e){
            System.out.println("Connection to the client failed");
        }
    }
}
