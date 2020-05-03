package Client;
import Messages.*;
import Model.*;
import Utils.QueueOfEvents;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;
import View.CLI;



/**
 * This class represent the Client.
 * @version 1.0
 */
public class Client {
    private static String serverAddress;
    private QueueOfEvents incomingMessages= new QueueOfEvents();
    private NetworkHandler networkHandler;
    private static final int SOCKET_PORT = 50000;
    private Scanner input;
    private int numPlayers;
    private CLI cli;
    private int x,y;
    private ArrayList<String> chosenGods;
    private String chosen;
    private boolean[][] allowedPosition, allowedMoves, allowedBuild;
    private int numWorker;
    private int i,j;

    /**
     *This method creates a new network handler and creates a new thread.
     * @throws Exception
     */
    public void startConnection() throws Exception {
        networkHandler=new NetworkHandler(serverAddress, SOCKET_PORT);
        Thread network= new Thread(networkHandler);
        network.start();
    }


    /**
     *This is the main of the class: it starts the connection and manages all the messages received from the network handler.
     * @throws Exception
     */
    public void main() throws Exception {
        //richiede server
        cli.gameInfo();
        startConnection();
        boolean disconnected=false;
        while (!disconnected){
            wait(50);
            if(incomingMessages.dequeueEvent()!=null){
                Message receivedMessage = incomingMessages.dequeueEvent();
                switch (receivedMessage.getMessageID()){
                    case Message.START_PLAYER_REQUEST:
                        selectFirstPlayer(receivedMessage);
                        break;
                    case Message.USERNAME_REQUEST:
                        askUsername(receivedMessage);
                        break;
                    case Message.NUM_PLAYERS_REQUEST:
                        askNumPlayers(receivedMessage);
                        break;
                    case Message.GODS_LIST_REQUEST:
                        chooseGodsList(receivedMessage);
                        break;
                    case Message.CHOSE_GOD_REQUEST:
                        chooseGod(receivedMessage);
                        break;
                    case Message.LAST_GOD_NOTIFICATION:
                        remainingGod(receivedMessage);
                        break;
                    case Message.MOVE_REQUEST:
                        chooseMove(receivedMessage);
                        break;
                    case Message.WORKER_POSITION_REQUEST:
                        selectWorker(receivedMessage);
                        break;
                    /*case Message.errore_scelta_move:
                        errorChosenMove(receivedMessage);
                        break;

                     */
                    case Message.GAME_START_NOTIFICATION:
                        startNotification(receivedMessage);
                        break;
                    case Message.GAME_STATUS_NOTIFICATION:
                        statusNotification(receivedMessage);
                        break;
                    case Message.WINNER_NOTIFICATION:
                        winnerNotification(receivedMessage);
                        break;
                    case Message.LOBBY_STATUS_NOTIFICATION:
                        lobbyStatusNotification(receivedMessage);
                        break;
                    case Message.BUILD_REQUEST:
                        chooseConstruction(receivedMessage);
                        break;
                    case Message.DISCONNECTION_MESSAGE:
                        disconnected=true;
                        networkHandler.disconnect();
                        break;
                }
            }
        }
    }


    /**
     *This method manages the username request message and send to the network handler the player's response.
     * @throws IOException
     */
    public void askUsername(Message message) throws IOException {
        cli.chooseUsername();
        input=new Scanner(System.in);
        String username= input.nextLine();
        networkHandler.sendMessage(new UsernameResponse(username));
    }


    /**
     *This method manages the number of player request message and send to the network handler the player's response.
     * @throws IOException
     */
    public void askNumPlayers(Message message) throws IOException {
        cli.chooseNumPlayers();
        input = new Scanner(System.in);
        numPlayers = input.nextInt();
        while(numPlayers!=2 || numPlayers!=3) {
            System.out.println("Numero giocatori scelto non valido. Riprova.");
            input = new Scanner(System.in);
            numPlayers = input.nextInt();
        }
        networkHandler.sendMessage(new NumPlayersResponse(numPlayers));
    }


    /**
     * This method show number and status of the current lobby, in order to inform if the current lobby isn't full yet
     */
    private void lobbyStatusNotification(Message message) {
        cli.printLobbyStatus(((LobbyStatusNotification)message).getSelectedLobby(),((LobbyStatusNotification)message).getSlotsOccupied());
    }


    /**
     * This method warn all the players in the lobby that the game can start     * @param message
     */
    private void startNotification(Message message) {
        cli.startNotification();
    }


    /**
     * This method manages the request of choosing a list of gods message only for the first client connected
     * and send to the network handler the player's response.
     * @throws IOException
     */
    public  void chooseGodsList(Message message) throws IOException {
        cli.showGodList(((GodsListRequest)message).getDeck(),((GodsListRequest)message).getNumPlayers());
        for (int i=0; i<numPlayers; i++){
            input = new Scanner(System.in);
            String chosen = input.nextLine();
            chosenGods.add(chosen);
        }
        networkHandler.sendMessage(new GodsListResponse(chosenGods));
    }


    /**
     * This method manages the selection of the first player of the game, he is chosen by the one who have chosen the God's list
     * @throws IOException
     */
    public void selectFirstPlayer(Message message) throws IOException {
        cli.printAllPlayers(((StartPlayerRequest)message).getPlayers());
        input=new Scanner(System.in);
        String firstPlayer=input.nextLine();
        networkHandler.sendMessage(new StartPlayerResponse(firstPlayer));
    }


    /**
     * This method manages the request of choosing a list of gods message and send to the network handler the player's response.
     * @throws IOException
     */
    public void chooseGod(Message message) throws IOException {
        cli.chooseGod(((ChoseGodRequest) message).getGods(), ((ChoseGodRequest) message).getUnavailableGods());
        input = new Scanner(System.in);
        String chosen = input.nextLine();
        networkHandler.sendMessage(new ChoseGodResponse(chosen));
    }

    /**
     * This method show the remaining god to the last player (who has chosen the GodList of the current game)
     */
    public void remainingGod(Message message){
        cli.printLastGod(((LastGodNotification) message).getGodsList(), ((LastGodNotification) message).getLastGod());
    }

    /**
     * This method show where the player can put the selected worker and then he inserts the coordinates
     * @throws IOException
     */
    public void placeWorker(Message message) throws IOException {
        cli.askWorkerPosition();

        input = new Scanner(System.in);
        x = input.nextInt();
        y = input.nextInt();
        allowedPosition=((WorkerPositionRequest) message).getAllowedPositions();
        while(allowedPosition[x-1][y-1]==false) {
            System.out.println("Spazio già occupato! Scegli altre coordinate!");
            input = new Scanner(System.in);
            x = input.nextInt();
            y = input.nextInt();
        }

        networkHandler.sendMessage(new WorkerPositionResponse(x, y));

    }



    /**
     * This method allow the player to choose what worker will be moved
     * @throws IOException
     */
    public void selectWorker(Message message) throws IOException {
        cli.chooseWorker();
        input=new Scanner(System.in);
        int x=input.nextInt();
        int y=input.nextInt();

        networkHandler.sendMessage(new SelectWorkerResponse(x, y));
        // coloro worker selezionato
    }


    /**
     * This method give to the player the chance to change the worker selected after he has seen the possible moves,
     *  and allow the player to choose where he wants to move his worker
     * @throws IOException
     */
    public void chooseMove(Message message) throws IOException {
        //mostra board
        cli.printPossibleAction(((MoveRequest) message).getAllowedMoves());
        cli.confirmChoice();
        //va bene o cagata?
        if(cli.confirmChoice()==true) {
            input = new Scanner(System.in);
            int x = input.nextInt();
            int y = input.nextInt();
            networkHandler.sendMessage(new SelectWorkerResponse(x, y));
        }

        cli.moveWorker();

        input=new Scanner(System.in);
        int x=input.nextInt();
        int y=input.nextInt();
        //ok?
        allowedMoves=((MoveRequest) message).getAllowedMoves();

        while(allowedMoves[x-1][y-1]==false) {
            System.out.println("Spazio già occupato! Scegli altre coordinate!");
            input = new Scanner(System.in);
            x = input.nextInt();
            y = input.nextInt();
        }
        networkHandler.sendMessage(new MoveResponse(x, y));
        //chiama metodo per stampare board
    }


    public void chooseConstruction(Message message) throws IOException {
        cli.printPossibleAction(((BuildRequest) message).getAllowedMoves());
        cli.buildTower();

        input = new Scanner(System.in);
        int x = input.nextInt();
        int y = input.nextInt();

        allowedBuild = ((BuildRequest) message).getAllowedMoves();

        while(allowedBuild[x-1][y-1]==false) {
            System.out.println("Spazio già occupato! Scegli altre coordinate!");
            input = new Scanner(System.in);
            x = input.nextInt();
            y = input.nextInt();
        }
        networkHandler.sendMessage(new BuildResponse(x, y));
    }



    private void statusNotification(Message message) throws IOException  {
        cli.printCurrentBoard(((GameStatusNotification) message).getUpdatedGame());
    }


    //gestire
    public void errorChosenMove(Message message) throws IOException {
        //stampa errore
    }



    private void winnerNotification(Message message) {
        cli.isWinner(((WinnerNotification) message).getWinnerUsername());

    }

}

    //vuoi usare potere aggiuntivo