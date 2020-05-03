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
    private boolean[][] allowedPosition, allowedMoves, allowedBuild, allowedToRemove;
    private int numWorker=0;
    private int i,j;

    /**
     *This method creates a new network handler and creates a new thread
     * @throws Exception
     */
    public void startConnection() throws Exception {
        networkHandler=new NetworkHandler(serverAddress, SOCKET_PORT);
        Thread network= new Thread(networkHandler);
        network.start();
    }


    /**
     *This is the main of the class: it starts the connection and manages all the messages received from the network handler
     * @throws Exception
     */
    public void main() throws Exception {
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
                        askUsername();
                        break;
                    case Message.NUM_PLAYERS_REQUEST:
                        askNumPlayers();
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
                    case Message.LOBBY_STATUS_NOTIFICATION:
                        lobbyStatusNotification(receivedMessage);
                        break;
                    case Message.GAME_START_NOTIFICATION:
                        startNotification();
                        break;
                    case Message.WORKER_POSITION_REQUEST:
                        placeWorker(receivedMessage);
                        break;
                    case Message.SELECT_WORKER_REQUEST:
                        selectWorker();
                        break;
                    case Message.USE_POWER_REQUEST:
                        usePower();
                        break;
                    case Message.MOVE_REQUEST:
                        chooseMove(receivedMessage);
                        break;
                    case Message.BLOCK_REMOVAL_REQUEST:
                        removeBlock(receivedMessage);
                        break;
                    case Message.GAME_STATUS_NOTIFICATION:
                        statusNotification(receivedMessage);
                        break;
                    case Message.BUILD_REQUEST:
                        chooseConstruction(receivedMessage);
                        break;
                    case Message.INVALID_MOVE_ERROR:
                        invalidMove();
                        break;
                    case Message.NO_POSSIBLE_MOVE_ERROR:
                        noMovesAllowed();
                        break;
                    case Message.OTHER_WORKER_MOVE_REQUEST:
                        otherWorkerMove(receivedMessage);
                        break;
                    case Message.USERNAME_TAKEN_ERROR:
                        usernameError();
                    case Message.WINNER_NOTIFICATION:
                        winnerNotification(receivedMessage);
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
     *This method manages the username request and send to the network handler the player's response
     * @throws IOException
     */
    public void askUsername() throws IOException {
        cli.chooseUsername();
        input=new Scanner(System.in);
        String username= input.nextLine();
        networkHandler.sendMessage(new UsernameResponse(username));
    }


    /**
     * This method manages not valid username error
     * @throws IOException
     */
    private void usernameError() throws IOException {
        System.out.println("Username già scelto da un altro giocatore!");
        askUsername();
    }


    /**
     * This method manages the number of player request and sends to the network handler the player's response
     * @throws IOException
     */
    public void askNumPlayers() throws IOException {
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
     * This method manages the lobby status notification and shows number and status of the current lobby,
     * in order to inform if it isn't full yet
     */
    private void lobbyStatusNotification(Message message) {
        cli.printLobbyStatus(((LobbyStatusNotification)message).getSelectedLobby(),((LobbyStatusNotification)message).getSlotsOccupied());
    }


    /**
     * This method manages the start notification and warns all the players in the lobby the game can start
     */
    private void startNotification() {
        cli.startNotification();
    }


    /**
     * This method manages the request of choosing a list of gods message only for the first client connected
     * and send to the network handler the player's response
     * @param message GodListRequest
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
     * This method manages the selection of the first player of the game, he is chosen by the one who have chosen the God's list,
     * and sends to the network handler the player's response
     * @throws IOException
     */
    public void selectFirstPlayer(Message message) throws IOException {
        cli.printAllPlayers(((StartPlayerRequest)message).getPlayers());
        input=new Scanner(System.in);
        String firstPlayer=input.nextLine();
        networkHandler.sendMessage(new StartPlayerResponse(firstPlayer));
    }


    /**
     * This method manages the request of choosing a list of gods message and send to the network handler the player's response
     * @throws IOException
     */
    public void chooseGod(Message message) throws IOException {
        cli.chooseGod(((ChoseGodRequest) message).getGods(), ((ChoseGodRequest) message).getUnavailableGods());
        input = new Scanner(System.in);
        String chosen = input.nextLine();
        networkHandler.sendMessage(new ChoseGodResponse(chosen));
    }


    /**
     * This method shows the remaining god to the last player (whom has chosen the GodList of the current game)
     */
    public void remainingGod(Message message){
        cli.printLastGod(((LastGodNotification) message).getGodsList(), ((LastGodNotification) message).getLastGod());
    }


    /**
     * This method shows where the player can put the selected worker and then he inserts the chosen coordinate
     * @throws IOException
     */
    public void placeWorker(Message message) throws IOException {
        cli.askWorkerPosition();

        input = new Scanner(System.in);
        x = input.nextInt();
        y = input.nextInt();

        System.out.println("Scegli dove posizionare il worker " + ((WorkerPositionRequest) message).getCurrentWorker());
        allowedPosition=((WorkerPositionRequest) message).getAllowedPositions();
        while(!allowedPosition[x - 1][y - 1]) {
            System.out.println("Spazio già occupato! Scegli altre coordinate!");
            input = new Scanner(System.in);
            x = input.nextInt();
            y = input.nextInt();
        }

        networkHandler.sendMessage(new WorkerPositionResponse(x, y));

    }


    /**
     * This method allows the player to choose what worker will be moved
     * @throws IOException
     */
    public void selectWorker() throws IOException {
        cli.chooseWorker();
        input=new Scanner(System.in);
        int x=input.nextInt();
        int y=input.nextInt();

        networkHandler.sendMessage(new SelectWorkerResponse(x, y));
    }


    /**
     * This method is used when the worker selected can't be moved, so he is advised to use the other worker and then he chooses the move
     * @param message OtherWorkerMoveRequest
     * @throws IOException
     */
    private void otherWorkerMove(Message message) throws IOException {

        cli.otherWorker();

        input=new Scanner(System.in);
        int x=input.nextInt();
        int y=input.nextInt();
        allowedMoves=((OtherWorkerMoveRequest) message).getAllowedMoves();

        while(!allowedMoves[x - 1][y - 1]) {
            System.out.println("Spazio già occupato! Scegli altre coordinate.");
            input = new Scanner(System.in);
            x = input.nextInt();
            y = input.nextInt();
        }
        networkHandler.sendMessage(new MoveResponse(x, y));
    }


    /**
     * This method asks the player if he wants to use the God's power
     * @throws IOException
     */
    private void usePower() throws IOException {
        cli.askPowerUsage();
        networkHandler.sendMessage(new UsePowerResponse(cli.askPowerUsage()));

    }



    /**
     * This method gives to the player the chance to change the worker selected (after he has seen the possible moves)
     *  and allows the player to choose where he wants to move his worker
     * @param message MoveRequest
     * @throws IOException
     */
    public void chooseMove(Message message) throws IOException {
        cli.printPossibleAction(((MoveRequest) message).getAllowedMoves());
        cli.confirmChoice();

        if(cli.confirmChoice()) {
            input = new Scanner(System.in);
            int x = input.nextInt();
            int y = input.nextInt();
            networkHandler.sendMessage(new SelectWorkerResponse(x, y));
        }

        cli.moveWorker();

        input=new Scanner(System.in);
        int x=input.nextInt();
        int y=input.nextInt();

        allowedMoves=((MoveRequest) message).getAllowedMoves();

        while(!allowedMoves[x - 1][y - 1]) {
            System.out.println("Spazio già occupato! Scegli altre coordinate!");
            input = new Scanner(System.in);
            x = input.nextInt();
            y = input.nextInt();
        }
        networkHandler.sendMessage(new MoveResponse(x, y));
    }


    /**
     * This method allows the player to choose where he wants to build a tower
     * @param message BuildRequest
     * @throws IOException
     */
    public void chooseConstruction(Message message) throws IOException {
        cli.printPossibleAction(((BuildRequest) message).getAllowedMoves());
        cli.buildTower();

        input = new Scanner(System.in);
        int x = input.nextInt();
        int y = input.nextInt();

        allowedBuild = ((BuildRequest) message).getAllowedMoves();

        while(!allowedBuild[x - 1][y - 1]) {
            System.out.println("Spazio già occupato! Scegli altre coordinate!");
            input = new Scanner(System.in);
            x = input.nextInt();
            y = input.nextInt();
        }
        networkHandler.sendMessage(new BuildResponse(x, y));
    }


    /**
     * This method allows the player to choose what block remove
     * @param message BlockRemovalRequest
     * @throws IOException
     */
    private void removeBlock(Message message) throws IOException {
        cli.chooseRemoval();
        input = new Scanner(System.in);
        x = input.nextInt();
        y = input.nextInt();
        allowedToRemove=((BlockRemovalRequest) message).getAllowedMoves();
        while(!allowedToRemove[x - 1][y - 1]) {
            System.out.println("Rimozione non consentita! Scegli altre coordinate.");
            input = new Scanner(System.in);
            x = input.nextInt();
            y = input.nextInt();
        }
        networkHandler.sendMessage(new BlockRemovalResponse(x,y));
    }


    /**
     * This method warns the chosen move is not valid
     */
    private void invalidMove() {
        cli.invalidMove();
    }


    /**
     * This method warns there aren't more possible moves for the player
     */
    private void noMovesAllowed() {
        cli.noPossibleMoves();
    }


    /**
     * This method shows the current (new) status of the Game
     * @param message GameStatusNotification
     */
    private void statusNotification(Message message) {
        cli.printCurrentBoard(((GameStatusNotification) message).getUpdatedGame());
    }


    /**
     * This method announces the name of the winner
     * @param message WinnerNotification
     */
    private void winnerNotification(Message message) {
        cli.isWinner(((WinnerNotification) message).getWinnerUsername());

    }

}
