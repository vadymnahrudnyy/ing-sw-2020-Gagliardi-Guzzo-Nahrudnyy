package Server;

import Messages.*;
import Model.*;
import com.sun.security.sasl.gsskerb.JdkSASL;

import java.io.IOException;
import java.util.ArrayList;

/**
 * GameController class implement the Logic level of the game.
 * @author Vadym Nahrudnyy
 * @version 1.0
 */
public class GameController implements Runnable {
    private final ArrayList<God> godList;
    private final ArrayList<Power> powerList;
    private final VirtualView[] virtualViewsList;

    private final Game currentGame;

    private Message receivedMessage;
    private boolean receivedValidMessage;
    private boolean MoveAllowed,BuildAllowed;
    private static final int RESPONSE_MESSAGE_WAIT_TIMEOUT = 2000;
    private Worker movedWorker;

    public GameController(ArrayList<VirtualView> virtualViews, int numPlayers) {
        godList = Server.getGodsList();
        powerList = Server.getPowerList();
        virtualViewsList = (VirtualView[]) virtualViews.toArray();
        Player[] playersArray = new Player[numPlayers];
        for (int i=0;i<numPlayers;++i) {playersArray[i] = new Player(virtualViewsList[i].getUsername(),i,null,null);}
        currentGame = new Game(numPlayers,playersArray[0],playersArray);
    }

    @Override
    public void run() {
        setupPhase();
        RoundHandle();
    }
    private void turnStart(Player player, VirtualView client, IslandBoard gameBoard,Power actualGodPower){
        currentGame.setCurrentPhase(TurnPhase.START);
        MoveAllowed = BuildAllowed = true;
        player.getWorkers()[0].setMovedUp(false);
        player.getWorkers()[1].setMovedUp(false);
        if (actualGodPower.getPowerID() == Power.OPPONENTS_NOT_MOVE_UP_POWER) currentGame.setAthenaMovedUp(false);
        if (actualGodPower.getPowerID() == Power.NOT_MOVE_UP_DOUBLE_BUILD_POWER){
            int[] validMessages = {Message.USE_POWER_RESPONSE};
            try{
                client.sendMessage(new UsePowerRequest());
                waitValidMessage(client,validMessages);
                if(((UsePowerResponse)receivedMessage).wantUsePower()){
                    client.sendMessage(new SelectWorkerRequest());
                    waitValidMessage(client,new int[Message.SELECT_WORKER_RESPONSE]);
                    Worker selectedWorker = getWorkerByCoordinates(((SelectWorkerResponse)receivedMessage).getCoordinateX(),((SelectWorkerResponse)receivedMessage).getCoordinateY());
                    boolean[][] allowedBuild = checkPossibleBuilds(selectedWorker.getWorkerPosition().getCoordinateX(),selectedWorker.getWorkerPosition().getCoordinateY());
                    Space buildSpace;
                    client.sendMessage(new BuildRequest(allowedBuild));
                    waitValidMessage(client,new int[]{Message.BUILD_RESPONSE});
                    buildSpace = gameBoard.getSpace(((BuildResponse)receivedMessage).getBuildCoordinateX(),((BuildResponse)receivedMessage).getBuildCoordinateY());
                    buildSpace.incrementHeight();
                    if (buildSpace.getHeight()==4) {
                        buildSpace.setHasDome(true);
                        gameBoard.incrementNumberCompleteTowers();
                    }
                    boolean[][] allowedMoves = checkPossibleMoves(selectedWorker.getWorkerPosition().getCoordinateX(),selectedWorker.getWorkerPosition().getCoordinateY());
                    for (int X = 0; X < IslandBoard.TABLE_DIMENSION;++X){
                        for(int Y = 0; Y < IslandBoard.TABLE_DIMENSION; ++Y){
                            if (gameBoard.getSpace(X+1,Y+1).getHeight()>selectedWorker.getWorkerPosition().getHeight()) allowedMoves[X][Y] = false;}}
                    if (workerCanMakeMove(allowedMoves)){
                        client.sendMessage(new MoveRequest(allowedMoves,false));
                        waitValidMessage(client,new int[]{Message.MOVE_RESPONSE});
                        Space movedToSpace;
                        selectedWorker.changePosition(gameBoard.getSpace(((MoveResponse)receivedMessage).getDestCoordinateX(),((MoveResponse)receivedMessage).getDestCoordinateY()));
                        MoveAllowed = false;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            //tratto la prima parte di turno di Prometeo
        }
    }

    private void Move(Player player, VirtualView client,IslandBoard gameBoard,Power actualGodPower){
        int[] validMessages = {Message.SELECT_WORKER_RESPONSE,Message.MOVE_RESPONSE};
        boolean moveMade = false;
        boolean[][] allowedMoves = new boolean[][]{};
        int selectedWorkerX = 0, selectedWorkerY = 0;
        Worker selectedWorker = getWorkerByCoordinates(selectedWorkerX,selectedWorkerY);
        try { client.sendMessage(new SelectWorkerRequest());
        } catch (IOException e) {
            System.out.println("Failed to send Select Worker Request"); }
        do{
            waitValidMessage(client,validMessages);
            if (receivedMessage.getMessageID() == Message.SELECT_WORKER_RESPONSE){
                selectedWorkerX = ((SelectWorkerResponse)receivedMessage).getCoordinateX();
                selectedWorkerY = ((SelectWorkerResponse)receivedMessage).getCoordinateY();
                selectedWorker = getWorkerByCoordinates(selectedWorkerX,selectedWorkerY);
                allowedMoves = checkPossibleMoves(selectedWorkerX,selectedWorkerY);
                if (workerCanMakeMove(allowedMoves)){
                    try{
                        client.sendMessage(new MoveRequest(allowedMoves,true));
                    } catch (IOException e ){
                        System.out.println("Move Request failed to player " + client.getUsername()); }
                }
                else {
                    Worker otherWorker = getPlayersOtherWorker(player,selectedWorker);
                    allowedMoves = checkPossibleMoves(otherWorker.getWorkerPosition().getCoordinateX(),otherWorker.getWorkerPosition().getCoordinateY());
                    try{
                        if (workerCanMakeMove(allowedMoves)) client.sendMessage(new OtherWorkerMoveRequest(allowedMoves));
                        else {
                            client.sendMessage(new NoPossibleMoveError());
                            if (currentGame.getNumPlayers()==2){
                                try{
                                    currentGame.nextPlayer();
                                    String winner = currentGame.getCurrentPlayer().getUsername();
                                    getVirtualViewByUsername(winner).sendMessage(new WinnerNotification(winner));
                                } catch (IOException e){
                                    System.out.println("winner Notification failed");
                                }
                            } else {
                                BuildAllowed = false;
                                moveMade = true;
                                removePlayerFromGame(currentGame.getCurrentPlayer());
                            }
                        }
                    } catch (IOException e ){
                        System.out.println("Other worker move request to player " + client.getUsername() + " failed"); }
                }
            }
            else{
                int destX = ((MoveResponse) receivedMessage).getDestCoordinateX();
                int destY = ((MoveResponse) receivedMessage).getDestCoordinateY();
                if (allowedMoves[destX-1][destY-1]){
                    Space fromSpace = gameBoard.getSpace(selectedWorkerX,selectedWorkerY);
                    Worker tempWorker = gameBoard.getSpace(destX, destY).getWorkerInPlace();
                    Space destSpace = gameBoard.getSpace(destX,destY);
                    if(tempWorker == null) selectedWorker.changePosition(destSpace);
                    else{//GESTIONE POTERI APOLLO e MINOTAURO
                        if (actualGodPower.getPowerID() == Power.WORKER_POSITION_EXCHANGE_POWER){
                            destSpace.removeWorkerInPlace();
                            selectedWorker.changePosition(destSpace);
                            tempWorker.setWorkerPosition(fromSpace);
                        }
                        else{//At the moment only two god powers can be used here Apollo and Minotaur
                            Space pushIntoSpace = gameBoard.getSpace(destX+(destX-selectedWorkerX),destY+(destY-selectedWorkerY));
                            tempWorker.changePosition(pushIntoSpace);
                            selectedWorker.changePosition(destSpace);
                        }
                    }
                    moveMade=true;

                    if (destSpace.getHeight() > fromSpace.getHeight()) selectedWorker.setMovedUp(true);
                    if (((destSpace.getHeight() == 3 && selectedWorker.isMovedUp())||((actualGodPower.getPowerID() == Power.TWO_BLOCK_FALL_VICTORY_POWER)&&((fromSpace.getHeight()-destSpace.getHeight())>=2)))&&((!(opponentHasPower(Power.PERIMETER_VICTORY_DENY_POWER)) || !destSpace.isPerimeter()))) victory(client.getUsername());
                    //ARTEMIS POWER
                    if (actualGodPower.getPowerID() == Power.DOUBLE_MOVE_POWER){
                        allowedMoves = checkPossibleMoves(destX,destY);
                        allowedMoves[fromSpace.getCoordinateX()-1][fromSpace.getCoordinateY()-1] = false;
                        if (workerCanMakeMove(allowedMoves)){
                            try{
                                client.sendMessage(new UsePowerRequest());
                                waitValidMessage(client,new int[]{Message.USE_POWER_RESPONSE});
                                if(((UsePowerResponse)receivedMessage).wantUsePower()) {
                                    try {
                                        client.sendMessage(new MoveRequest(allowedMoves, false));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    do {
                                        moveMade = false;
                                        waitValidMessage(client, new int[]{Message.MOVE_RESPONSE});
                                        destX = ((MoveResponse) receivedMessage).getDestCoordinateX();
                                        destY = ((MoveResponse) receivedMessage).getDestCoordinateY();
                                        if (allowedMoves[destX - 1][destY - 1]) {
                                            selectedWorker.changePosition(gameBoard.getSpace(destX, destY));
                                            moveMade = true; }
                                        else {
                                            try{
                                                client.sendMessage(new InvalidMoveError());
                                                client.sendMessage(new MoveRequest(allowedMoves,false));
                                            }  catch (IOException e) {
                                                e.printStackTrace(); }
                                        }
                                    } while(!moveMade);
                                    movedWorker = selectedWorker;
                                }
                            } catch (IOException e) {
                                e.printStackTrace(); }
                        }
                    }
                } else{
                    try{
                        client.sendMessage(new InvalidMoveError());
                    } catch (IOException e){
                        System.out.println("Sending invalid move error failed to player " + client.getUsername());
                    }
                }
            }
        }while(!moveMade);
    }

    private boolean opponentHasPower(int powerID){
        Player[] gamePlayers = currentGame.getPlayers();
        for (Player current:gamePlayers)
            if (current.getGod().getSinglePower(0)==powerID && currentGame.getCurrentPlayer() != current) return true;
        return false;
    }

    private void victory(String winnerUsername){
        for (VirtualView player:virtualViewsList) {
            try{
                player.sendMessage(new WinnerNotification(winnerUsername));
                //disconnect
            } catch (IOException e) {
                e.printStackTrace(); }
        }

    }
    private void removePlayerFromGame(Player playerToRemove){}
    private void RoundHandle(){
        Player currentPlayer = currentGame.getCurrentPlayer();
        VirtualView currentClient = getVirtualViewByUsername(currentPlayer.getUsername());
        IslandBoard currentBoard = currentGame.getGameBoard();
        Power currentGodPower = getPowerByID(currentPlayer.getGod().getSinglePower(0));
        boolean PrometheusFlag = false;
        //SETUP PHASE
        turnStart(currentPlayer,currentClient,currentBoard,currentGodPower);
        //MOVE PHASE
        currentGame.nextTurnPhase();
        if (MoveAllowed){
            Move(currentPlayer,currentClient,currentBoard,currentGodPower);
        }
        //BUILD PHASE
        currentGame.setCurrentPhase(TurnPhase.BUILD);
        if (BuildAllowed) Build(currentPlayer,currentClient,currentBoard,currentGodPower,movedWorker);
        if ((opponentHasPower(Power.FIVE_TOWER_VICTORY_POWER)||currentGodPower.getPowerID()==Power.FIVE_TOWER_VICTORY_POWER)&&(currentBoard.getNumberCompleteTowers()>=5)){
            for (Player player:currentGame.getPlayers())
                if (player.getGod().getSinglePower(0)==Power.FIVE_TOWER_VICTORY_POWER) victory(player.getUsername());
        }
        //End of turn
        currentGame.nextTurnPhase();
        if (currentGodPower.getPowerID() == Power.BLOCK_REMOVE_POWER){
            try{
                Worker otherWorker = getPlayersOtherWorker(currentPlayer,movedWorker);
                boolean [][] allowedRemovals = checkPossibleBuilds(otherWorker.getWorkerPosition().getCoordinateX(),otherWorker.getWorkerPosition().getCoordinateY());
                if (workerCanMakeMove(allowedRemovals)) {
                    currentClient.sendMessage(new UsePowerRequest());
                    waitValidMessage(currentClient,new int[]{Message.USE_POWER_RESPONSE});
                    if (((UsePowerResponse)receivedMessage).wantUsePower()){
                        currentClient.sendMessage(new BlockRemovalRequest(allowedRemovals));
                        waitValidMessage(currentClient,new int[]{Message.BLOCK_REMOVAL_RESPONSE});
                        currentBoard.getSpace(((BlockRemovalResponse) receivedMessage).getRemoveCoordinateX(),((BlockRemovalResponse) receivedMessage).getRemoveCoordinateY()).decrementHeight();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void Build(Player player,VirtualView client,IslandBoard gameBoard,Power actualPower,Worker selectedWorker){
        Space workerPosition = selectedWorker.getWorkerPosition();
        boolean[][] allowedBuild = checkPossibleBuilds(workerPosition.getCoordinateX(),workerPosition.getCoordinateY());
        try{
            client.sendMessage(new BuildRequest(allowedBuild));
        } catch (IOException e) {
            e.printStackTrace(); }
        int[] validMessages = {Message.BUILD_RESPONSE};
        waitValidMessage(client,validMessages);
        receivedMessage = (BuildResponse) receivedMessage;
        Space toBuildSpace = gameBoard.getSpace(((BuildResponse) receivedMessage).getBuildCoordinateX(),((BuildResponse) receivedMessage).getBuildCoordinateY());
        if (actualPower.getPowerID() == Power.BUILD_DOME_EVERYWHERE_POWER&&toBuildSpace.getHeight()!=3){
            try{
                client.sendMessage(new UsePowerRequest());
            } catch (IOException e) {
                e.printStackTrace(); }
            validMessages = new int[]{Message.USE_POWER_RESPONSE};
            waitValidMessage(client,validMessages);
            if(((UsePowerResponse) receivedMessage).wantUsePower()){
                toBuildSpace.setHasDome(true);
                toBuildSpace.incrementHeight();
            }
        }
        else {
            toBuildSpace.incrementHeight();
            if (toBuildSpace.getHeight() == 4) {
                toBuildSpace.setHasDome(true);
                gameBoard.incrementNumberCompleteTowers();
            }
        }
        if (actualPower.getTurnPhase()==TurnPhase.BUILD && actualPower.getActive()&&actualPower.getPowerID()!=Power.BUILD_DOME_EVERYWHERE_POWER){
            switch (actualPower.getPowerID()){
                case Power.DIFFERENT_SPACE_DOUBLE_BUILD_POWER:
                    allowedBuild = checkPossibleBuilds(workerPosition.getCoordinateX(),workerPosition.getCoordinateY());
                    allowedBuild[toBuildSpace.getCoordinateX()-1][toBuildSpace.getCoordinateY()-1] = false;
                    if (workerCanMakeMove(allowedBuild)){
                        try{
                            client.sendMessage(new UsePowerRequest());
                            waitValidMessage(client,new int[]{Message.USE_POWER_RESPONSE});
                            if (((UsePowerResponse)receivedMessage).wantUsePower()){
                                client.sendMessage(new BuildRequest(allowedBuild));
                                waitValidMessage(client,new int[]{Message.BUILD_RESPONSE});
                                toBuildSpace = gameBoard.getSpace(((BuildResponse) receivedMessage).getBuildCoordinateX(),((BuildResponse) receivedMessage).getBuildCoordinateY());
                                toBuildSpace.incrementHeight();
                                if(toBuildSpace.getHeight()==4) {
                                    toBuildSpace.setHasDome(true);
                                    gameBoard.incrementNumberCompleteTowers();
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace(); }
                    }
                    break;
                case Power.SAME_SPACE_DOUBLE_BUILD_POWER:
                    if(toBuildSpace.getHeight()<3){
                        try {
                            client.sendMessage(new UsePowerRequest());
                            waitValidMessage(client,new int[]{Message.USE_POWER_RESPONSE});
                            if(((UsePowerResponse) receivedMessage).wantUsePower()){
                                toBuildSpace.incrementHeight();
                            }
                        } catch (IOException e) {
                            e.printStackTrace(); }
                    }
                    break;
                case Power.NON_PERIMETER_DOUBLE_BUILD:
                    allowedBuild = checkPossibleBuilds(workerPosition.getCoordinateX(),workerPosition.getCoordinateY());
                    int tempX = 0;
                    int tempY;
                    for (tempY = 0; tempY < IslandBoard.TABLE_DIMENSION; ++tempY) allowedBuild[tempX][tempY] = false;
                    tempX = IslandBoard.TABLE_DIMENSION - 1;
                    for (tempY = 0; tempY < IslandBoard.TABLE_DIMENSION; ++tempY) allowedBuild[tempX][tempY] = false;
                    tempY = 0;
                    for (tempX = 0; tempX < IslandBoard.TABLE_DIMENSION; ++tempX) allowedBuild[tempX][tempY] = false;
                    tempY = IslandBoard.TABLE_DIMENSION - 1;
                    for (tempX = 0; tempX < IslandBoard.TABLE_DIMENSION; ++tempX) allowedBuild[tempX][tempY] = false;
                    if (workerCanMakeMove(allowedBuild)){
                        try {
                            client.sendMessage(new UsePowerRequest());
                            waitValidMessage(client,new int[]{Message.USE_POWER_RESPONSE});
                            if(((UsePowerResponse)receivedMessage).wantUsePower()){
                                client.sendMessage(new BuildRequest(allowedBuild));
                                waitValidMessage(client,new int[]{Message.BUILD_RESPONSE});
                                toBuildSpace = gameBoard.getSpace(((BuildResponse) receivedMessage).getBuildCoordinateX(),((BuildResponse) receivedMessage).getBuildCoordinateY());
                                toBuildSpace.incrementHeight();
                                if(toBuildSpace.getHeight()==4) {
                                    toBuildSpace.setHasDome(true);
                                    gameBoard.incrementNumberCompleteTowers();
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
    }


    private void setupPhase() {
        sendStartGameMessage();
        chooseGods();
        chooseStartPlayer();
        chooseWorkerPositions();
        startGame();
    }


    private void createWorker(int numWorker,Player owner,int coordinateX, int coordinateY){
        Worker[] workers = owner.getWorkers();
        if(workers == null) workers = new Worker[Game.WORKERS_PER_PLAYER];
        char gender = 'm';
        if (numWorker == 1) gender = 'f';
        Space workerPosition = currentGame.getGameBoard().getSpace(coordinateX,coordinateY);
        workers[numWorker-1] = new Worker(numWorker,owner.getUsername(),gender,workerPosition);
    }

    private boolean verifyValidPosition(boolean[][] allowedPositions,WorkerPositionResponse response){
        int coordinateX = response.getCoordinateX();
        int coordinateY = response.getCoordinateY();
        return allowedPositions[coordinateX - 1][coordinateY - 1];
    }



    /**
     * Method waitValidMessage
     * @param senderVirtualView virtual view of the client from who the controller needs a message
     * @param messageIDs list of accepted messages.
     */
    public void waitValidMessage(VirtualView senderVirtualView,int[] messageIDs) {
        receivedValidMessage = false;
        do {
            try{
                wait(RESPONSE_MESSAGE_WAIT_TIMEOUT);
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted");
            }
            receivedMessage = senderVirtualView.dequeueFirstMessage();
            while (receivedMessage != null) receivedValidMessage = checkMessageValidity(receivedMessage,messageIDs);
        } while (!receivedValidMessage);
    }
    /**
     * Method getGodByName is used to get a god by using the name
     * @param name Name of the God.
     * @return object of class God taken from the list of gods of the game.
     */
    private God getGodByName(String name){
        //magari provare ad aggiornare facendo in modo che usi una lista data
        for (God god:godList) {
            if (god.getName().equals(name)) return god;
        }
        return null;
    }
    /**
     * getVirtualViewByUsername method is used to get the network interface of one player using his username;
     * @param username username of the player.
     * @return the corresponding network interface
     */
    private VirtualView getVirtualViewByUsername(String username){
        for (VirtualView networkInterface:virtualViewsList) {
            if(networkInterface.getUsername().equals(username)) return networkInterface;
        }
        return null;
    }
    /**
     * checkMessageValidity is a private method used to control if the received message is among
     * the messages can be used by the game controller at the moment in order to continue the game.
     * @param messageToCheck is the received message.
     * @param allowedMessages the list of allowed messages
     * @return a boolean value. True if the message is allowed, false otherwise.
     */
    private boolean checkMessageValidity(Message messageToCheck,int[] allowedMessages){
        int receivedMessageID = messageToCheck.getMessageID();
        for (int allowedMessage : allowedMessages) {
            if (receivedMessageID == allowedMessage) return true;
        }
        return false;
    }
    /**
     * getWorkerByCoordinate is a private method used to get the worker in the selected Space
     * @param coordinateX is the X coordinate of the selected Space.
     * @param coordinateY is the Y coordinate of the selected Space.
     * @return the worker in desired place. The value is null in case no worker have been found in the space.
     */
    private Worker getWorkerByCoordinates(int coordinateX,int coordinateY){
        return currentGame.getGameBoard().getSpace(coordinateX,coordinateY).getWorkerInPlace();
    }

    private boolean[][] initializeMatrix(boolean value){
        boolean[][] allowedPositions = new boolean[IslandBoard.TABLE_DIMENSION][IslandBoard.TABLE_DIMENSION];
        for (int coordinateX = 0; coordinateX < IslandBoard.TABLE_DIMENSION; ++coordinateX)
            for (int coordinateY = 0; coordinateY < IslandBoard.TABLE_DIMENSION; ++coordinateY)
                allowedPositions[coordinateX][coordinateY] = value;
        return allowedPositions;
    }

    /**
     * Checks in the matrix indicating allowed moves if an action can be performed by the worker
     * @param allowedMoves is previously created matrix indicating where the worker can move or build;
     * @return a boolean value. True when an action can be performed, false otherwise.
     */
    private boolean workerCanMakeMove (boolean[][] allowedMoves){
        for (int X = 0; X < IslandBoard.TABLE_DIMENSION; ++X)
            for(int Y = 0; Y < IslandBoard.TABLE_DIMENSION; ++Y)
                if (allowedMoves[X][Y]) return true;
        return false;
    }

    /**
     * Check all the possible moves the worker can make.
     * @param workerCoordinateX coordinate X of the selected worker.
     * @param workerCoordinateY coordinate Y of the selected worker.
     * @return a boolean matrix with "true" value in the position where the worker can move.
     */
    private boolean[][] checkPossibleMoves(int workerCoordinateX,int workerCoordinateY){
        IslandBoard currentBoard = currentGame.getGameBoard();
        God currentGod = currentGame.getCurrentPlayer().getGod();
        Space checkedSpace;
        Worker workerInCheckedSpace;
        int currentHeight = currentBoard.getSpace(workerCoordinateX,workerCoordinateY).getHeight();
        boolean [][] allowedMoves = initializeMatrix(false);

        for (int X = workerCoordinateX-2; X < workerCoordinateX+1; ++X){
            for (int Y = workerCoordinateY-2; Y < workerCoordinateY +1; ++Y){
                if (X >= 0 || Y >= 0) {
                    checkedSpace = currentBoard.getSpace(X + 1, Y + 1);
                    if ((currentHeight >= checkedSpace.getHeight() - 1)&&!checkedSpace.getHasDome()) {//CuPoeEEeEEeEeEeEeEeEEE!!!
                        if (((workerInCheckedSpace = checkedSpace.getWorkerInPlace()) != null)&&(!checkedSpace.getHasDome())) {
                            if (!workerInCheckedSpace.getOwner().equals(currentGame.getCurrentPlayer().getUsername())) {
                                switch (currentGod.getSinglePower(0)) {
                                    case Power.WORKER_POSITION_EXCHANGE_POWER:
                                        allowedMoves[X][Y] = true;
                                        break;
                                    case Power.PUSH_WORKER_POWER:
                                        int tempX, tempY;
                                        tempX = X + (X - (workerCoordinateX - 1));
                                        tempY = Y + (Y - (workerCoordinateY - 1));
                                        if (tempX >= 0 && tempY >= 0) {
                                            Space tempSpace = currentBoard.getSpace(tempX, tempY);
                                            if ((tempSpace.getWorkerInPlace() != null) || (!tempSpace.getHasDome()))
                                                allowedMoves[X][Y] = true;
                                        }
                                        break;
                                }
                            }
                        } else{
                            if(!checkedSpace.getHasDome()) allowedMoves[X][Y] = true;
                        }
                    }//line below is ATHENA POWER
                    if (allowedMoves[X][Y]&&currentGame.hasAthenaMovedUpDuringLastRound()&&(checkedSpace.getHeight() > currentHeight)) allowedMoves[X][Y] = false;
                }
            }
        }
        return allowedMoves;
    }


    //SETUP PHASE
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
     * Method used to choose the gods before start playing.It asks the first player the list of gods to use, then the others chose their god card
     * and the first player automatically receives the remained god card.
     */
    public void chooseGods() {
        Player firstPlayer = currentGame.getCurrentPlayer();
        VirtualView firstVirtualView = virtualViewsList[0];
        try{
            firstVirtualView.sendMessage(new GodsListRequest(godList,currentGame.getNumPlayers()));
        } catch (IOException e){
            System.out.println("God list request failed to player " + firstVirtualView.getUsername());
        }
        int[] validMessages = new int[1];
        validMessages[0] = Message.GODS_LIST_RESPONSE;
        waitValidMessage(firstVirtualView,validMessages);
        ArrayList<String> gameGodsNames = ((GodsListResponse) receivedMessage).getGods();
        ArrayList<God> gameGods = new ArrayList<>();
        for (String godName:gameGodsNames) gameGods.add(getGodByName(godName));
        ArrayList<God> chosenGods = new ArrayList<>();
        for(int Index = 1; Index< currentGame.getNumPlayers();++Index){
            try{
                virtualViewsList[Index].sendMessage(new ChoseGodRequest(gameGods, chosenGods));
            } catch (IOException e) {
                System.out.println("God Request failed");
            }
            waitValidMessage(virtualViewsList[Index],validMessages);
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
        for(int index = 0; index < chosenGods.size();++index)
            if (!chosenGods.contains(gameGods.get(index))) return gameGods.get(index);
        return null;
    }
    /**
     * Method used to ask the Challenger the username of the start player.
     */
    private void chooseStartPlayer(){
        try{
            virtualViewsList[0].sendMessage(new StartPlayerRequest(currentGame.getPlayers()));
        } catch (IOException e){
            System.out.println("Start player request failed");
        }
        int[] validMessage = {Message.START_PLAYER_REQUEST};
        waitValidMessage(virtualViewsList[0],validMessage);
        currentGame.setStarterPlayer(((StartPlayerResponse) receivedMessage).getStartPlayerUsername());
    }
    /**
     * Used for asking the players the starter position of their workers.
     */
    private void chooseWorkerPositions(){
        currentGame.setCurrentPlayer(currentGame.getStarterPlayer());
        int tableDimension = currentGame.getGameBoard().getTableDimension();
        boolean [][] allowedPositions = initializeMatrix(true);
        int[] validMessages = new int[1];
        validMessages[0] = Message.WORKER_POSITION_RESPONSE;
        boolean validPosition;
        do {
            VirtualView currentClient = getVirtualViewByUsername(currentGame.getCurrentPlayer().getUsername());
            do{
                try{
                    currentClient.sendMessage(new WorkerPositionRequest(1,allowedPositions));
                }catch (IOException e){
                    System.out.println("Worker 1 position request failed to player " + currentClient.getUsername());
                }
                waitValidMessage(currentClient,validMessages);
                validPosition = verifyValidPosition(allowedPositions,(WorkerPositionResponse)receivedMessage);
            }while(!validPosition);
            createWorker(1,currentGame.getCurrentPlayer(),((WorkerPositionResponse) receivedMessage).getCoordinateX(),((WorkerPositionResponse) receivedMessage).getCoordinateY());
            do{
                try{
                    currentClient.sendMessage(new WorkerPositionRequest(2,allowedPositions));
                }catch (IOException e){
                    System.out.println("Worker 2 position request failed to player " + currentClient.getUsername());
                }
                waitValidMessage(currentClient,validMessages);
                validPosition = verifyValidPosition(allowedPositions,(WorkerPositionResponse)receivedMessage);
            }while(!validPosition);
            createWorker(2,currentGame.getCurrentPlayer(),((WorkerPositionResponse) receivedMessage).getCoordinateX(),((WorkerPositionResponse) receivedMessage).getCoordinateY());
            currentGame.nextPlayer();
        }while(currentGame.getCurrentPlayer() != currentGame.getStarterPlayer());

    }
    /**
     * Starts the game after that worker positions have been chosen.
     */
    private void startGame(){
        currentGame.setCurrentRound(1);
    }

    //ROUND HANDLER
    //MOVE
    /**
     * used to get the other worker of the player
     * @param player in the player considered
     * @param oneWorker is the worker we are considering
     * @return the other worker of the player
     */
    private Worker getPlayersOtherWorker(Player player,Worker oneWorker){
        if (player.getWorkers()[0] == oneWorker ) return player.getWorkers()[1];
        else return player.getWorkers()[0];
    }


    //BUILD
    private boolean[][] checkPossibleBuilds(int workerCoordinateX,int workerCoordinateY){
        boolean[][] allowedBuilds = initializeMatrix(false);
        for (int X = workerCoordinateX - 2; X < workerCoordinateX + 1;++ X){
            for (int Y = workerCoordinateY - 2; Y < workerCoordinateY + 1;++Y){
                Space currentSpace;
                if ((X >= 0 && Y >= 0)&&((!(currentSpace = currentGame.getGameBoard().getSpace(X + 1, Y + 2)).getHasDome()&&currentSpace.getWorkerInPlace()==null))) allowedBuilds[X][Y] = true;
            }
        }
        if (currentGame.getCurrentPlayer().getGod().getSinglePower(0)== Power.WORKER_POSITION_BUILD && currentGame.getGameBoard().getSpace(workerCoordinateX,workerCoordinateY).getHeight() < 3) allowedBuilds[workerCoordinateX-1][workerCoordinateY-1] = true;
        return allowedBuilds;
    }


    /**
     * is used to get the Power object using the ID of the power
     * @param ID is the ID of the power.
     * @return the desired power.
     */
    private Power getPowerByID(int ID){
        for (Power power:powerList) {
            if (power.getPowerID() == ID) return power;
        }
        return null;
    }
    private God getPlayersGod(String playersUsername){
        return((currentGame.getPlayerByUsername(playersUsername)).getGod());
    }
}