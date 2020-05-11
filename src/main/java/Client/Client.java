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
    private static QueueOfEvents incomingMessages= new QueueOfEvents();
    private static NetworkHandler networkHandler;
    private static final int SOCKET_PORT = 50000;
    private static Scanner input;
    private static int numPlayers;
    private static CLI cli=new CLI();
    private static int x,y;
    private static boolean[][] allowedPosition, allowedMoves, allowedBuild, allowedToRemove;
    private static int i,j;
    private static String username;

    /**
     *This method creates a new network handler and creates a new thread
     */
    public static void startConnection(String serverAddress){
        networkHandler=new NetworkHandler(serverAddress, SOCKET_PORT);
        Thread network= new Thread(networkHandler);
        network.start();
    }


    /**
     *This is the main of the class: it starts the connection and manages all the messages received from the network handler
     */
    public static void main(String[] args) throws Exception {
        cli.gameInfo();
        cli.chooseServerAddress();
        input=new Scanner(System.in);
        serverAddress= input.nextLine();
        startConnection(serverAddress);


        boolean disconnected=false;
        while (!disconnected){
            Thread.sleep(50);
            Message receivedMessage;
            if((receivedMessage = NetworkHandler.incomingMessages.dequeueEvent())!=null){
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
                    case Message.INVALID_GOD_ERROR:
                        godError();
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
                        break;
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
     */
    public static void askUsername()  {
        cli.chooseUsername();
        input=new Scanner(System.in);
        username= input.nextLine();
        networkHandler.sendMessage(new UsernameResponse(username));
    }


    /**
     * This method manages not valid username error
     */
    private static void usernameError() {
        System.out.println("Username already chosen by another player!");
        askUsername();
    }


    /**
     * This method manages the number of player request and sends to the network handler the player's response
     */
    public static void askNumPlayers() {
        cli.chooseNumPlayers();
        input = new Scanner(System.in);
        numPlayers = input.nextInt();
        while(numPlayers != 2 && numPlayers != 3) {
            System.out.println("Invalid number of players selected. Please try again.");
            input = new Scanner(System.in);
            numPlayers = input.nextInt();
        }
        networkHandler.sendMessage(new NumPlayersResponse(numPlayers));
    }


    /**
     * This method manages the lobby status notification and shows number and status of the current lobby,
     * in order to inform if it isn't full yet
     */
    private static void lobbyStatusNotification(Message message) {
        cli.printLobbyStatus(((LobbyStatusNotification)message).getSelectedLobby(),((LobbyStatusNotification)message).getSlotsOccupied());
    }


    /**
     * This method manages the start notification and warns all the players in the lobby the game can start
     */
    private static void startNotification() {
        cli.startNotification();
    }


    /**
     * This method manages the request of choosing a list of gods message only for the first client connected
     * and send to the network handler the player's response
     * @param message GodListRequest
     * @throws IOException
     */
    public static void chooseGodsList(Message message) throws IOException {
        cli.showGodList(((GodsListRequest)message).getDeck(),((GodsListRequest)message).getNumPlayers());
        ArrayList<String> chosenGods = new ArrayList<>();
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
    public static void selectFirstPlayer(Message message) throws IOException {
        cli.printAllPlayers(((StartPlayerRequest)message).getPlayers(),username);
        input=new Scanner(System.in);
        String firstPlayer=input.nextLine();
        networkHandler.sendMessage(new StartPlayerResponse(firstPlayer));
    }


    /**
     * This method manages the request of choosing a list of gods message and send to the network handler the player's response
     * @throws IOException
     */
    public static void chooseGod(Message message) throws IOException {
        cli.chooseGod(((ChoseGodRequest) message).getGods(), ((ChoseGodRequest) message).getUnavailableGods());
        input = new Scanner(System.in);
        String chosen = input.nextLine();
        networkHandler.sendMessage(new ChoseGodResponse(chosen));
    }


    /**
     * This method shows the remaining god to the last player (whom has chosen the GodList of the current game)
     */
    public static void remainingGod(Message message){
        cli.printLastGod(((LastGodNotification) message).getGodsList(), ((LastGodNotification) message).getLastGod());
    }


    /**
     * This method manages not valid god error
     */
    public static void godError(){
        System.out.println("Attention! You did not write the name of the god correctly (the first letter must be capitalized).");
    }

    /**
     * This method shows where the player can put the selected worker and then he inserts the chosen coordinate
     * @throws IOException
     */
    public static void placeWorker(Message message) throws IOException {

        System.out.println("Choose the position of the worker " + ((WorkerPositionRequest) message).getCurrentWorker());
        allowedPosition=((WorkerPositionRequest) message).getAllowedPositions();
        input = new Scanner(System.in);
        System.out.println("Insert coordinate X: ");
        x = input.nextInt();
        System.out.println("Insert coordinate Y: ");
        y = input.nextInt();
        while (((x < 0||y < 0)||(x > IslandBoard.TABLE_DIMENSION || y > IslandBoard.TABLE_DIMENSION))||(!allowedPosition[x-1][y-1])){
            System.out.println("Position is not valid.");
            input = new Scanner(System.in);
            System.out.println("Insert coordinate X: ");
            x = input.nextInt();
            System.out.println("Insert coordinate Y: ");
            y = input.nextInt();
            }

        networkHandler.sendMessage(new WorkerPositionResponse(x, y));

    }


    /**
     * This method allows the player to choose what worker will be moved
     * @throws IOException
     */
    public static void selectWorker() throws IOException {
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
    private static void otherWorkerMove(Message message) throws IOException {

        cli.otherWorker();

        input=new Scanner(System.in);
        int x=input.nextInt();
        int y=input.nextInt();
        allowedMoves=((OtherWorkerMoveRequest) message).getAllowedMoves();

        while(!allowedMoves[x - 1][y - 1]) {
            System.out.println("Space already occupied! Choose other coordinates!");
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
    private static void usePower() throws IOException {
        networkHandler.sendMessage(new UsePowerResponse(cli.askPowerUsage()));

    }



    /**
     * This method gives to the player the chance to change the worker selected (after he has seen the possible moves)
     *  and allows the player to choose where he wants to move his worker
     * @param message MoveRequest
     * @throws IOException
     */
    public static void chooseMove(Message message) throws IOException {
        cli.printPossibleAction(((MoveRequest) message).getAllowedMoves());


        if(((MoveRequest)message).chaChangeWorker()&&cli.confirmChoice()) {
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

        while((x<0||y<0)||(x>IslandBoard.TABLE_DIMENSION||y>IslandBoard.TABLE_DIMENSION)||(!allowedMoves[x - 1][y - 1])) {
            System.out.println("Space already occupied! Choose other coordinates!");
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
    public static void chooseConstruction(Message message) throws IOException {
        cli.printPossibleAction(((BuildRequest) message).getAllowedMoves());
        cli.buildTower();

        input = new Scanner(System.in);
        int x = input.nextInt();
        int y = input.nextInt();

        allowedBuild = ((BuildRequest) message).getAllowedMoves();

        while(!allowedBuild[x - 1][y - 1]) {
            System.out.println("Space already occupied! Choose other coordinates!");
            input = new Scanner(System.in);
            x = input.nextInt();
            y = input.nextInt();
        }
        networkHandler.sendMessage(new BuildResponse(x, y));
    }


    /**
     * This method allows the player to choose what block remove
     * @param message BlockRemovalRequest
     */
    private static void removeBlock(Message message) {
        cli.chooseRemoval();
        input = new Scanner(System.in);
        x = input.nextInt();
        y = input.nextInt();
        allowedToRemove=((BlockRemovalRequest) message).getAllowedMoves();
        while(!allowedToRemove[x - 1][y - 1]) {
            System.out.println("Removal not allowed! Choose other coordinates.");
            input = new Scanner(System.in);
            x = input.nextInt();
            y = input.nextInt();
        }
        networkHandler.sendMessage(new BlockRemovalResponse(x,y));
    }


    /**
     * This method warns the chosen move is not valid
     */
    private static void invalidMove() {
        cli.invalidMove();
    }


    /**
     * This method warns there aren't more possible moves for the player
     */
    private static void noMovesAllowed() {
        cli.noPossibleMoves();
    }


    /**
     * This method shows the current (new) status of the Game
     * @param message GameStatusNotification
     */
    private static void statusNotification(Message message) {
        cli.printCurrentStatus(((GameStatusNotification) message).getUpdatedGame());
        cli.printCurrentBoard(((GameStatusNotification) message).getUpdatedGame());
    }


    /**
     * This method announces the name of the winner
     * @param message WinnerNotification
     */
    private static void winnerNotification(Message message) {
        cli.isWinner(((WinnerNotification) message).getWinnerUsername());

    }

}
