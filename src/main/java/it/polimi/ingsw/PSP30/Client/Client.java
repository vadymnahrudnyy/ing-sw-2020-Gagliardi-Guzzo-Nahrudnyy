package it.polimi.ingsw.PSP30.Client;
import it.polimi.ingsw.PSP30.Utils.QueueOfEvents;

import java.util.Scanner;

import it.polimi.ingsw.PSP30.View.CLI;
import it.polimi.ingsw.PSP30.View.GUI;
import it.polimi.ingsw.PSP30.View.UI;
import it.polimi.ingsw.PSP30.Messages.*;


/**
 * This class represent the Client.
 * @version 1.0
 */

public class Client {
    private static String serverAddress=null;
    public static NetworkHandler networkHandler;
    private static final int SOCKET_PORT = 50000;
    private static Scanner input;
    private static CLI cli=new CLI();
    private static int numPlayers;
    private static String username;
    private static UI ui;
    private static boolean disconnected = true;
    private static Thread clientThread;
    private static final long START_CONNECTION_TIMEOUT = 100000;
    public static boolean changedWorker=false;



    public static void setNumPlayer(int value){
        numPlayers=value;
    }

    public static int getNumPlayers(){
        return numPlayers;
    }

    public static void setUsername(String username) {
        Client.username = username;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUI(UI userInterface){ ui=userInterface; }

    public static void setDisconnected(boolean value){ disconnected = value; }

    public static void setServerAddress(String address){ serverAddress=address; }

    public static Thread getClientThread(){ return(clientThread); }

    public static void interruptClientThread(){ clientThread.interrupt(); }


    /**
     * This method creates a new network handler and a new thread.
     */
    public static void startConnection(String serverAddress){
        networkHandler=new NetworkHandler(serverAddress, SOCKET_PORT,Thread.currentThread());
        Thread network= new Thread(networkHandler);
        network.start();
        try{
            Thread.sleep(START_CONNECTION_TIMEOUT);
        }catch (InterruptedException ignored) {
        }
    }


    /**
     * This is the main method of the class: it starts the connection and manages all the messages received from the network handler.
     */
    public static void main(String[] args) {
        clientThread = Thread.currentThread();
        GUI gui = new GUI();
        Thread ThreadGUI = new Thread(gui);
        ThreadGUI.start();
        try{
            Thread.sleep(100000000);
        } catch (InterruptedException e){
            System.out.println("");
        }

        do {
            ui.chooseServerAddress();
            System.out.println("");
            startConnection(serverAddress);
        }while(disconnected);



        while (!disconnected){
            try {
                Thread.sleep(150);
                Message receivedMessage;
                if((receivedMessage = NetworkHandler.incomingMessages.dequeueEvent())!=null) {
                    switch (receivedMessage.getMessageID()) {
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
                            startNotification(receivedMessage);
                            break;
                        case Message.START_PLAYER_REQUEST:
                            selectFirstPlayer(receivedMessage);
                            break;
                        case Message.INVALID_STARTER_PLAYER_ERROR:
                            firstPlayerError();
                            break;
                        case Message.WORKER_POSITION_REQUEST:
                            placeWorker(receivedMessage);
                            break;
                        case Message.SELECT_WORKER_REQUEST:
                            selectWorker();
                            break;
                        case Message.INVALID_WORKER_ERROR:
                            workerError();
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
                            disconnected = true;
                            break;
                        case Message.DISCONNECTION_MESSAGE:
                            ui.disconnectedFromServer();
                            disconnected = true;
                            break;
                        case Message.PLAYER_DISCONNECTED_ERROR:
                           ui.opponentDisconnected();
                           break;

                        default:
                         disconnected = !NetworkHandler.isConnected();
                    }
                }
            }catch (InterruptedException ignored){}
        }
    }

    /**
     * This method manages not valid address error.
     */
    public static void addressError() {
        ui.errorServerAddress();
    }


    /**
     * This method manages the username request and send to the network handler the player's response.
     */
    public static void askUsername()  {
        ui.chooseUsername();
    }


    /**
     * This method manages not valid username error.
     */
    private static void usernameError() {
        ui.usernameError();
        askUsername();
    }


    /**
     * This method manages the number of player request and sends to the network handler the player's response.
     */
    public static void askNumPlayers() {
        ui.chooseNumPlayers();
    }


    /**
     * This method manages the lobby status notification and shows number and status of the current lobby,
     * in order to inform if it isn't full yet.
     */
    private static void lobbyStatusNotification(Message message) {
        ui.printLobbyStatus(((LobbyStatusNotification)message).getSelectedLobby(),((LobbyStatusNotification)message).getSlotsOccupied(),((LobbyStatusNotification)message).getPlayersInLobby());
    }


    /**
     * This method handles the start notification and warns all the players in the lobby the game can start.
     */
    private static void startNotification(Message message) {
        ui.startNotification((GameStartNotification) message);
    }


    /**
     * This method manages the request of choosing a list of gods message only for the first client connected
     * and send to the network handler the player's response.
     * @param message GodListRequest
     */
    public static void chooseGodsList(Message message) {
        ui.showGodList(((GodsListRequest)message).getDeck());
        ui.chooseGameGods();
    }


    /**
     * This method manages the selection of the first player of the game, he is chosen by the one who have chosen the God's list,
     * and sends to the network handler the player's response.
     */
    public static void selectFirstPlayer(Message message) {
        ui.printAllPlayers(((StartPlayerRequest)message).getPlayers());
        ui.chooseFirstPlayer();
    }


    /**
     * This method manages not valid first player's choice.
     */
    public static void firstPlayerError() {
        ui.playerError();
    }


    /**
     * This method handles the request of choosing a list of gods message and sends to the network handler the player's response.
     */
    public static void chooseGod(Message message) {
        ui.chooseGod(((ChoseGodRequest) message).getGods(), ((ChoseGodRequest) message).getUnavailableGods());
    }


    /**
     * This method shows the remaining God to the last player (whom has chosen the GodList of the current game).
     */
    public static void remainingGod(Message message){
        ui.showLastGod(((LastGodNotification) message).getGodsList(), ((LastGodNotification) message).getLastGod());
    }


    /**
     * This method manages not valid god error.
     */
    public static void godError(){
       ui.godChoiceError();
    }


    /**
     * This method manages the initial placement of the workers.
     */
    public static void placeWorker(Message message) {
        ui.placeWorkerInSpace(((WorkerPositionRequest) message).getCurrentWorker(),((WorkerPositionRequest) message).getAllowedPositions());
    }


    /**
     * This method allows the player to choose what worker will be moved.
     */
    public static void selectWorker() {
        ui.chooseWorker();
    }


    /**
     * This method handles not valid worker selection.
     */
    public static void workerError() {
        ui.workerChosenError();
    }


    /**
     * This method is used when the worker selected can't be moved, so he is advised to use the other worker and then he chooses the move.
     * @param message OtherWorkerMoveRequest
     */
    private static void otherWorkerMove(Message message) {
        ui.otherWorker();
        ui.printPossibleAction(((OtherWorkerMoveRequest) message).getAllowedMoves());
        ui.moveOtherWorker(((OtherWorkerMoveRequest) message).getAllowedMoves());
    }


    /**
     * This method asks the player if he wants to use the God's power.
     */
    private static void usePower() {
        ui.askPowerUsage();
    }


    /**
     * This method gives to the player the chance to change the worker selected (after he has seen the possible moves)
     *  and allows the player to choose where he wants to move his worker.
     * @param message MoveRequest
     */
    public static void chooseMove(Message message) {
        ui.printPossibleAction(((MoveRequest) message).getAllowedMoves());
        ui.changeWorker(((MoveRequest)message).getChangeWorker());
        ui.moveWorker(((MoveRequest) message).getAllowedMoves());
    }


    /**
     * This method allows the player to choose where he wants to build a tower.
     * @param message of BuildRequest type
     */
    public static void chooseConstruction(Message message) {
        ui.printPossibleAction(((BuildRequest) message).getAllowedMoves());
        ui.buildTower(((BuildRequest) message).getAllowedMoves());
    }


    /**
     * This method allows the player to choose what block remove.
     * @param message BlockRemovalRequest
     */
    private static void removeBlock(Message message) {
        ui.chooseRemoval(((BlockRemovalRequest)message).getAllowedMoves());
    }


    /**
     * This method warns the chosen move is not valid.
     */
    private static void invalidMove() {
        ui.invalidMove();
    }


    /**
     * This method warns if there aren't more possible moves for the player.
     */
    private static void noMovesAllowed() {
        ui.noPossibleMoves();
    }


    /**
     * This method shows the current (new) status of the Game.
     * @param message GameStatusNotification
     */
    private static void statusNotification(Message message) {
        ui.printCurrentStatus(((GameStatusNotification) message).getUpdatedGame());
        ui.printCurrentBoard(((GameStatusNotification) message).getUpdatedGame());
    }


    /**
     * This method announces the winner.
     * @param message WinnerNotification
     */
    private static void winnerNotification(Message message) {
        ui.isWinner(((WinnerNotification) message).getWinnerUsername());
    }

    /**
     * This method handles the transmission of a message from Client to Server.
     * @param message message which has to be sent
     */
     public static void sendMessageToServer(Message message){
        NetworkHandler.sendMessage(message);
    }
}
