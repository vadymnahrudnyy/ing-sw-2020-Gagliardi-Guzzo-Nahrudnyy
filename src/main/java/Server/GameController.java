package Server;

import Messages.*;
import Model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class GameController implements Runnable {
    private final ArrayList<God> godList;
    private final ArrayList<Power> powerList;
    private final VirtualView[] virtualViewsList;
    private Game currentGame;
    private Message receivedMessage;
    private boolean receivedValidMessage;
    private static final int RESPONSE_MESSAGE_WAIT_TIMEOUT = 2000;

    public GameController(ArrayList<God> gods, ArrayList<Power> powers, ArrayList<Player> players, ArrayList<VirtualView> virtualViews, int numPlayers) {
        godList = gods;
        powerList = powers;
        virtualViewsList = (VirtualView[]) virtualViews.toArray();
        Player[] playersArray = new Player[numPlayers];
        for (int i=0;i<numPlayers;++i) {playersArray[i] = new Player(virtualViewsList[i].getUsername(),i,null,null);}
        currentGame = new Game(numPlayers,playersArray[0],playersArray);
    }

    @Override
    public void run() {
        setupPhase();

    }

    private void setupPhase() {
        sendStartGameMessage();
        chooseGods();
        chooseStartPlayer();
        chooseWorkerPositions();
        startGame();
    }
    private void chooseWorkerPositions(){
        currentGame.setCurrentPlayer(currentGame.getStarterPlayer());
        int tableDimension = currentGame.getGameBoard().getTableDimension();
        boolean [][] allowedPositions = initializeAllowedPositions(tableDimension);
        boolean validPosition;
        do {
            VirtualView currentClient = getVirtualViewByUsername(currentGame.getCurrentPlayer().getUsername());
            do{
                try{
                    currentClient.sendMessage(new WorkerPositionRequest(1,allowedPositions));
                }catch (IOException e){
                    System.out.println("Worker 1 position request failed to player " + currentClient.getUsername());
                }
                waitValidMessage(currentClient,Message.WORKER_POSITION_RESPONSE);
                validPosition = verifyValidPosition(allowedPositions,(WorkerPositionResponse)receivedMessage);
            }while(!validPosition);
            do{
                try{
                    currentClient.sendMessage(new WorkerPositionRequest(2,allowedPositions));
                }catch (IOException e){
                    System.out.println("Worker 2 position request failed to player " + currentClient.getUsername());
                }
                waitValidMessage(currentClient,Message.WORKER_POSITION_RESPONSE);
                validPosition = verifyValidPosition(allowedPositions,(WorkerPositionResponse)receivedMessage);
            }while(!validPosition);
            currentGame.nextPlayer();
        }while(currentGame.getCurrentPlayer() != currentGame.getStarterPlayer());
    }

    private boolean verifyValidPosition(boolean[][] allowedPositions,WorkerPositionResponse response){
        int coordinateX = response.getCoordinateX();
        int coordinateY = response.getCoordinateY();
        if (allowedPositions[coordinateX - 1][coordinateY - 1]){
            allowedPositions[coordinateX-1][coordinateY-1] = false;
            return true;
        }
        else return false;
    }
    private boolean[][] initializeAllowedPositions(int tableDimension){
        boolean[][] allowedPositions = new boolean[tableDimension][tableDimension];
        for (int coordinateX = 0; coordinateX < tableDimension;++coordinateX){
            for (int coordinateY = 0; coordinateY < tableDimension;++coordinateY){
                allowedPositions[coordinateX][coordinateY] = true;
            }
        }
        return allowedPositions;
    }
    private void chooseStartPlayer(){
        try{
            virtualViewsList[0].sendMessage(new StartPlayerRequest(currentGame.getPlayers()));
        } catch (IOException e){
            System.out.println("Start player request failed");
        }
        waitValidMessage(virtualViewsList[0],Message.START_PLAYER_RESPONSE);
        currentGame.setStarterPlayer(((StartPlayerResponse) receivedMessage).getStartPlayerUsername());

    }

    private void startGame(){
        currentGame.setCurrentPhase(TurnPhase.START);
        currentGame.setCurrentRound(1);
    }

    /**
     * Method used to notify all player about the game being start
     */
    private void sendStartGameMessage() {
        for (VirtualView view : virtualViewsList) {
            try {
                view.sendMessage(new GameStartNotification());
            } catch (IOException e) {
                System.out.println("Game start notification error to client " + view.getUsername());
            }
        }
    }

    /**
     * Method used to choose the gods before start playing.
     */
    public void chooseGods() {
        Player firstPlayer = currentGame.getPlayers()[0];
        VirtualView firstVirtualView = virtualViewsList[0];
        try{
            firstVirtualView.sendMessage(new GodsListRequest(godList,currentGame.getNumPlayers()));
        } catch (IOException e){
            System.out.println("God list request failed to player " + firstVirtualView.getUsername());
        }
        waitValidMessage(firstVirtualView,Message.GODS_LIST_RESPONSE);
        ArrayList<God> gameGods = ((GodsListResponse) receivedMessage).getGods();
        int Index = 1;
        ArrayList<God> chosenGods = new ArrayList<>();
        while (Index < currentGame.getNumPlayers()){
            try{
                virtualViewsList[Index].sendMessage(new ChoseGodRequest(gameGods, chosenGods));
            } catch (IOException e) {
                System.out.println("God Request failed");
            }
            waitValidMessage(virtualViewsList[Index],Message.CHOSE_GOD_RESPONSE);
            God receivedGod = getGodByName(((ChoseGodResponse)receivedMessage).getChosenGod());
            Player actualPlayer = currentGame.getPlayerByUsername(virtualViewsList[Index].getUsername());
            if (actualPlayer == null) System.out.println("Wanted player doesn't exist");
            else {
                actualPlayer.setGod(receivedGod);
                chosenGods.add(receivedGod);
            }
        }
        God remainedGod = getRemainedGod(gameGods,chosenGods);
        try {
            virtualViewsList[0].sendMessage(new LastGodNotification(gameGods,remainedGod));
        } catch(IOException e){
            System.out.println("Last god notification failed");
        }
    }

    /**
     * Private method of the controller used to identify the god of the first player.
     * @param gameGods gods chosen to be used in game.
     * @param chosenGods gods chosen by other players.
     * @return the god which will be used by the first player.
     */
    private God getRemainedGod(ArrayList<God> gameGods,ArrayList<God> chosenGods){
        for(int index = 0; index < chosenGods.size();++index){
            if (!chosenGods.contains(gameGods.get(index))) return gameGods.get(index);
        }
        return null;
    }

    /**
     * Method waitValidMessage
     * @param senderVirtualView virtual view of the client from who the controller needs a message
     * @param messageID message type required.
     */
    public void waitValidMessage(VirtualView senderVirtualView,int messageID) {
        do {
            receivedValidMessage = false;
            try{
                wait(RESPONSE_MESSAGE_WAIT_TIMEOUT);
            } catch (InterruptedException e) {
                System.out.println("Interrupted thread");
            }
            while (((receivedMessage = senderVirtualView.dequeueFirstMessage()) != null) && (receivedMessage.getMessageID() != messageID)) {
                receivedValidMessage = false;
            }
            receivedValidMessage = receivedMessage != null;
        }while (!receivedValidMessage);
    }

    private God getGodByName(String name){
        for (God god:godList) {
            if (god.getName().equals(name)) return god;
        }
        return null;
    }

    private VirtualView getVirtualViewByUsername(String username){
        for (int index = 0; index < currentGame.getNumPlayers();++index){
            if (virtualViewsList[index].equals(username)) return virtualViewsList[index];
        }
        return null;
    }


}