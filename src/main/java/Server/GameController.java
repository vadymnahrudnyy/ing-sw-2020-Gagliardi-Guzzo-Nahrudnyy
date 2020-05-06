package Server;

import Messages.*;
import Model.*;

import java.util.ArrayList;

/**
 * GameController class implement the Logic level of the game.
 * @author Vadym Nahrudnyy
 * @version 1.0
 */
public class GameController implements Runnable {

    private Worker movedWorker;
    private final Game currentGame;
    private Message receivedMessage;
    private boolean MoveAllowed,BuildAllowed;
    private final ArrayList<God> godList;
    private final ArrayList<Power> powerList;
    private final VirtualView[] virtualViewsList;
    private static final int RESPONSE_MESSAGE_WAIT_TIMEOUT = 2000;


    public GameController(ArrayList<VirtualView> virtualViews, int numPlayers) {
        godList = Server.getGodsList();
        powerList = Server.getPowerList();
        virtualViewsList = new VirtualView[numPlayers];
        Player[] playersArray = new Player[numPlayers];
        for(int i=0;i<numPlayers;++i) virtualViewsList[i] = virtualViews.get(i);
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
                client.sendMessage(new UsePowerRequest());
                waitValidMessage(client,new int[]{Message.USE_POWER_RESPONSE});
                if(((UsePowerResponse)receivedMessage).wantUsePower()){
                    client.sendMessage(new SelectWorkerRequest());
                    waitValidMessage(client,new int[]{Message.SELECT_WORKER_RESPONSE});
                    Worker selectedWorker = getWorkerByCoordinates(((SelectWorkerResponse)receivedMessage).getCoordinateX(),((SelectWorkerResponse)receivedMessage).getCoordinateY());
                    boolean[][] allowedBuild = checkPossibleBuilds(selectedWorker.getWorkerPosition().getCoordinateX(),selectedWorker.getWorkerPosition().getCoordinateY());
                    client.sendMessage(new BuildRequest(allowedBuild));
                    waitValidMessage(client,new int[]{Message.BUILD_RESPONSE});
                    Space buildSpace = gameBoard.getSpace(((BuildResponse)receivedMessage).getBuildCoordinateX(),((BuildResponse)receivedMessage).getBuildCoordinateY());
                    blockAddInSpace(buildSpace);
                    boolean[][] allowedMoves = checkPossibleMoves(selectedWorker.getWorkerPosition().getCoordinateX(),selectedWorker.getWorkerPosition().getCoordinateY());
                    for (int X = 0; X < IslandBoard.TABLE_DIMENSION;++X){
                        for(int Y = 0; Y < IslandBoard.TABLE_DIMENSION; ++Y){
                            if (gameBoard.getSpace(X+1,Y+1).getHeight()>selectedWorker.getWorkerPosition().getHeight()) allowedMoves[X][Y] = false;}}
                    if (workerCanMakeMove(allowedMoves)){
                        client.sendMessage(new MoveRequest(allowedMoves,false));
                        waitValidMessage(client,new int[]{Message.MOVE_RESPONSE});
                        selectedWorker.changePosition(gameBoard.getSpace(((MoveResponse)receivedMessage).getDestCoordinateX(),((MoveResponse)receivedMessage).getDestCoordinateY()));
                        MoveAllowed = false;
                    }
                }
        }
    }

    private void Move(Player player, VirtualView client,IslandBoard gameBoard,Power actualGodPower){
        int[] validMessages = {Message.SELECT_WORKER_RESPONSE,Message.MOVE_RESPONSE};
        boolean moveMade = false;
        boolean[][] allowedMoves = new boolean[][]{};
        int selectedWorkerX = 0, selectedWorkerY = 0;
        Worker selectedWorker = getWorkerByCoordinates(selectedWorkerX,selectedWorkerY);
        client.sendMessage(new SelectWorkerRequest());
        do{
            waitValidMessage(client,new int[]{Message.SELECT_WORKER_RESPONSE,Message.MOVE_RESPONSE});
            if (receivedMessage.getMessageID() == Message.SELECT_WORKER_RESPONSE){
                selectedWorkerX = ((SelectWorkerResponse)receivedMessage).getCoordinateX();
                selectedWorkerY = ((SelectWorkerResponse)receivedMessage).getCoordinateY();
                selectedWorker = getWorkerByCoordinates(selectedWorkerX,selectedWorkerY);
                allowedMoves = checkPossibleMoves(selectedWorkerX,selectedWorkerY);
                if (workerCanMakeMove(allowedMoves)) client.sendMessage(new MoveRequest(allowedMoves,true));

                else {
                    Worker otherWorker = getPlayersOtherWorker(player,selectedWorker);
                    allowedMoves = checkPossibleMoves(otherWorker.getWorkerPosition().getCoordinateX(),otherWorker.getWorkerPosition().getCoordinateY());
                        if (workerCanMakeMove(allowedMoves)) client.sendMessage(new OtherWorkerMoveRequest(allowedMoves));
                        else {
                            client.sendMessage(new NoPossibleMoveError());
                            if (currentGame.getNumPlayers()==2){
                                    currentGame.nextPlayer();
                                    String winner = currentGame.getCurrentPlayer().getUsername();
                                    getVirtualViewByUsername(winner).sendMessage(new WinnerNotification(winner));
                            } else {
                                BuildAllowed = false;
                                moveMade = true;
                                removePlayerFromGame(currentGame.getCurrentPlayer());
                            }
                        }
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
                                client.sendMessage(new UsePowerRequest());
                                waitValidMessage(client,new int[]{Message.USE_POWER_RESPONSE});
                                if(((UsePowerResponse)receivedMessage).wantUsePower()) {
                                    client.sendMessage(new MoveRequest(allowedMoves, false));
                                    do {
                                        moveMade = false;
                                        waitValidMessage(client, new int[]{Message.MOVE_RESPONSE});
                                        destX = ((MoveResponse) receivedMessage).getDestCoordinateX();
                                        destY = ((MoveResponse) receivedMessage).getDestCoordinateY();
                                        if (allowedMoves[destX - 1][destY - 1]) {
                                            selectedWorker.changePosition(gameBoard.getSpace(destX, destY));
                                            moveMade = true; }
                                        else {
                                                client.sendMessage(new InvalidMoveError());
                                                client.sendMessage(new MoveRequest(allowedMoves,false));
                                        }
                                    } while(!moveMade);
                                    movedWorker = selectedWorker;
                                }
                        }
                    }
                } else client.sendMessage(new InvalidMoveError());
            }
        }while(!moveMade);
    }



    private void victory(String winnerUsername){
        for (VirtualView player:virtualViewsList) {
                player.sendMessage(new WinnerNotification(winnerUsername));
                player.closeConnection();
        }
    }
    private void removePlayerFromGame(Player playerToRemove){}
    private void RoundHandle(){
        Player currentPlayer = currentGame.getCurrentPlayer();
        VirtualView currentClient = getVirtualViewByUsername(currentPlayer.getUsername());
        IslandBoard currentBoard = currentGame.getGameBoard();
        Power currentGodPower = getPowerByID(currentPlayer.getGod().getSinglePower(0));
        //SETUP PHASE
        turnStart(currentPlayer,currentClient,currentBoard,currentGodPower);
        //MOVE PHASE
        currentGame.nextTurnPhase();
        if (MoveAllowed){
            Move(currentPlayer,currentClient,currentBoard,currentGodPower);
        }
        //BUILD PHASE
        currentGame.setCurrentPhase(TurnPhase.BUILD);
        if (BuildAllowed) Build(currentClient,currentBoard,currentGodPower,movedWorker);
        if ((opponentHasPower(Power.FIVE_TOWER_VICTORY_POWER)||currentGodPower.getPowerID()==Power.FIVE_TOWER_VICTORY_POWER)&&(currentBoard.getNumberCompleteTowers()>=5)){
            for (Player player:currentGame.getPlayers())
                if (player.getGod().getSinglePower(0)==Power.FIVE_TOWER_VICTORY_POWER) victory(player.getUsername());
        }
        //End of turn
        currentGame.nextTurnPhase();
        if (currentGodPower.getPowerID() == Power.BLOCK_REMOVE_POWER){
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
        }
    }

    /**
     * Method used to build in the space indicated by the player.
     * @param client virtual view of the current player.
     * @param toBuild the space to build into.
     * @param godPower the power current player's god.
     */
    private void buildInSpace(VirtualView client,Space toBuild,Power godPower){
        if (toBuild.getHeight() < (Space.DOME_LEVEL - 1) && godPower.getPowerID() == Power.BUILD_DOME_EVERYWHERE_POWER){
            client.sendMessage(new UsePowerRequest());
            waitValidMessage(client,new int[]{Message.USE_POWER_RESPONSE});
            if (((UsePowerResponse)receivedMessage).wantUsePower()){
                toBuild.setHasDome(true);
                toBuild.incrementHeight();
                return;
            }
        }
        blockAddInSpace(toBuild);
    }

    /**
     * Method used to add a block in a given space
     * @param toBuild the space where to build a block.
     */
    private void blockAddInSpace(Space toBuild){
        toBuild.incrementHeight();
        if (toBuild.getHeight() == Space.DOME_LEVEL){
            toBuild.setHasDome(true);
            currentGame.setTowerWasCompleted(true);
            currentGame.getGameBoard().incrementNumberCompleteTowers();
        }
    }

    private void secondBuildMake(boolean[][] allowedBuild,VirtualView client,IslandBoard gameBoard){
        if (workerCanMakeMove(allowedBuild)){
            client.sendMessage(new UsePowerRequest());
            waitValidMessage(client,new int[]{Message.USE_POWER_RESPONSE});
            if (((UsePowerResponse)receivedMessage).wantUsePower()){
                client.sendMessage(new BuildRequest(allowedBuild));
                waitValidMessage(client,new int[]{Message.BUILD_RESPONSE});
                Space toBuildSpace = gameBoard.getSpace(((BuildResponse) receivedMessage).getBuildCoordinateX(),((BuildResponse) receivedMessage).getBuildCoordinateY());
                blockAddInSpace(toBuildSpace);
            }
        }
    }

    private void Build(VirtualView client,IslandBoard gameBoard,Power actualPower,Worker selectedWorker){
        Space workerPosition = selectedWorker.getWorkerPosition();
        boolean[][] allowedBuild = checkPossibleBuilds(workerPosition.getCoordinateX(),workerPosition.getCoordinateY());
        client.sendMessage(new BuildRequest(allowedBuild));
        waitValidMessage(client,new int[]{Message.BUILD_RESPONSE});
        Space toBuildSpace = currentGame.getGameBoard().getSpace(((BuildResponse) receivedMessage).getBuildCoordinateX(),((BuildResponse) receivedMessage).getBuildCoordinateY());
        buildInSpace(client,toBuildSpace,actualPower);

        if (actualPower.getTurnPhase()==TurnPhase.BUILD && actualPower.getActive()&&actualPower.getPowerID()!=Power.BUILD_DOME_EVERYWHERE_POWER){
            switch (actualPower.getPowerID()){
                case Power.DIFFERENT_SPACE_DOUBLE_BUILD_POWER:
                    allowedBuild = checkPossibleBuilds(workerPosition.getCoordinateX(),workerPosition.getCoordinateY());
                    allowedBuild[toBuildSpace.getCoordinateX()-1][toBuildSpace.getCoordinateY()-1] = false;
                    secondBuildMake(allowedBuild,client,gameBoard);
                    break;
                case Power.SAME_SPACE_DOUBLE_BUILD_POWER:
                    if(toBuildSpace.getHeight()<3){
                            client.sendMessage(new UsePowerRequest());
                            waitValidMessage(client,new int[]{Message.USE_POWER_RESPONSE});
                            if(((UsePowerResponse) receivedMessage).wantUsePower()){
                                toBuildSpace.incrementHeight();
                            }
                    }
                    break;
                case Power.NON_PERIMETER_DOUBLE_BUILD:
                    allowedBuild = checkPossibleBuilds(workerPosition.getCoordinateX(),workerPosition.getCoordinateY());
                    int tempX = 0, tempY;
                    for (tempY = 0; tempY < IslandBoard.TABLE_DIMENSION; ++tempY) allowedBuild[tempX][tempY] = false;
                    for (tempX = 0; tempX < IslandBoard.TABLE_DIMENSION; ++tempX) allowedBuild[tempX][tempY] = false;
                    for (tempY = IslandBoard.TABLE_DIMENSION - 1; tempY > 0; --tempY) allowedBuild[tempX][tempY] = false;
                    for (tempX = IslandBoard.TABLE_DIMENSION - 1; tempX > 0; --tempX) allowedBuild[tempX][tempY] = false;
                    secondBuildMake(allowedBuild,client,gameBoard);
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
        boolean receivedValidMessage = false;
        System.out.println("Waiting for a valid message");
        do {
            try{
                while((receivedMessage = senderVirtualView.dequeueFirstMessage())==null) Thread.sleep(RESPONSE_MESSAGE_WAIT_TIMEOUT);
                System.out.println("New message received, checking validity");
                receivedValidMessage = checkMessageValidity(receivedMessage,messageIDs);
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted");
            }
        } while (!receivedValidMessage);
    }
    /**
     * Method getGodByName is used to get a god by using the name
     * @param name Name of the God.
     * @return object of class God taken from the list of gods of the game.
     */
    private God getGodByName(String name){
        for (God god:godList) if (god.getName().equals(name)) return god;
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

    /**
     * Creates a new matrix of TABLE_DIMENSION x TABLE_DIMENSION dimension.
     * @param value initial value of each item in the matrix.
     * @return the created matrix.
     */
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
        for (VirtualView view : virtualViewsList) view.sendMessage(new GameStartNotification());
    }
    /**
     * Method used to choose the gods before start playing.It asks the first player the list of gods to use, then the others chose their god card
     * and the first player automatically receives the remained god card.
     */
    public void chooseGods() {
        System.out.println("Choosing gods");
        Player firstPlayer = currentGame.getCurrentPlayer();
        VirtualView firstVirtualView = virtualViewsList[0];
        System.out.println("Sending Gods List to first player");
        firstVirtualView.sendMessage(new GodsListRequest(godList,currentGame.getNumPlayers()));
        waitValidMessage(firstVirtualView,new int[]{Message.GODS_LIST_RESPONSE});
        System.out.println("Gods List received from first player");
        ArrayList<String> gameGodsNames = ((GodsListResponse) receivedMessage).getGods();
        ArrayList<God> gameGods = new ArrayList<>();
        for (String godName:gameGodsNames) gameGods.add(getGodByName(godName));
        ArrayList<God> chosenGods = new ArrayList<>();
        for(int Index = 1; Index< currentGame.getNumPlayers();++Index){
            virtualViewsList[Index].sendMessage(new ChoseGodRequest(gameGods, chosenGods));
            waitValidMessage(virtualViewsList[Index],new int[]{Message.CHOSE_GOD_RESPONSE});
            System.out.println("Chosen god: "+((ChoseGodResponse)receivedMessage).getChosenGod());
            God receivedGod = getGodByName(((ChoseGodResponse)receivedMessage).getChosenGod());
            Player actualPlayer = currentGame.getPlayerByUsername(virtualViewsList[Index].getUsername());
            if (actualPlayer == null) System.out.println("Wanted player doesn't exist");
            else {
                actualPlayer.setGod(receivedGod);
                chosenGods.add(receivedGod);
            }
        }
        God remainedGod = getRemainedGod(gameGods,chosenGods);
        firstPlayer.setGod(remainedGod);
        virtualViewsList[0].sendMessage(new LastGodNotification(gameGods,remainedGod));

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
        virtualViewsList[0].sendMessage(new StartPlayerRequest(currentGame.getPlayers()));
        waitValidMessage(virtualViewsList[0],new int[]{Message.START_PLAYER_RESPONSE});
        currentGame.setStarterPlayer(((StartPlayerResponse) receivedMessage).getStartPlayerUsername());
    }
    /**
     * Method used during the setup of the game. Create a worker instance in the position indicated by the player
     * @param numWorker used to distinguish between first and second player's worker.
     * @param owner indicates the player who owns the created worker.
     * @param coordinateX X coordinate of the worker Position.
     * @param coordinateY Y coordinate of the worker Position.
     */
    private void createWorker(int numWorker,Player owner,int coordinateX, int coordinateY){
        Worker[] workers = owner.getWorkers();
        if(workers == null) workers = new Worker[Game.WORKERS_PER_PLAYER];
        char gender = 'm';
        if (numWorker == 1) gender = 'f';
        Space workerPosition = currentGame.getGameBoard().getSpace(coordinateX,coordinateY);
        workers[numWorker-1] = new Worker(owner.getUsername(),gender,workerPosition, 1); //da sistemare
    }
    /**
     * Used for asking the players the starter position of their workers.
     */
    private void chooseWorkerPositions(){
        currentGame.setCurrentPlayer(currentGame.getStarterPlayer());
        boolean [][] allowedPositions = initializeMatrix(true);
        boolean validPosition;
        do {
            VirtualView currentClient = getVirtualViewByUsername(currentGame.getCurrentPlayer().getUsername());
            do{
                currentClient.sendMessage(new WorkerPositionRequest(1,allowedPositions));
                waitValidMessage(currentClient,new int[]{Message.WORKER_POSITION_RESPONSE});
                validPosition = verifyValidPosition(allowedPositions,(WorkerPositionResponse)receivedMessage);
            }while(!validPosition);
            createWorker(1,currentGame.getCurrentPlayer(),((WorkerPositionResponse) receivedMessage).getCoordinateX(),((WorkerPositionResponse) receivedMessage).getCoordinateY());
            currentClient.sendMessage(new GameStatusNotification(currentGame));
            do{
                currentClient.sendMessage(new WorkerPositionRequest(2,allowedPositions));
                waitValidMessage(currentClient,new int[]{Message.WORKER_POSITION_RESPONSE});
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
        if (currentGame.getCurrentPlayer().getGod().getSinglePower(0)== Power.WORKER_POSITION_BUILD && currentGame.getGameBoard().getSpace(workerCoordinateX,workerCoordinateY).getHeight() < (Space.DOME_LEVEL-1)) allowedBuilds[workerCoordinateX-1][workerCoordinateY-1] = true;
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

    /**
     * Method used to check if any of opponents of current Player has a given power
     * @param powerID The power to be checked.
     * @return true if an opponent has the indicated power, false otherwise.
     */
    private boolean opponentHasPower(int powerID){
        Player[] gamePlayers = currentGame.getPlayers();
        for (Player current:gamePlayers)
            if (current.getGod().getSinglePower(0)==powerID && currentGame.getCurrentPlayer() != current) return true;
        return false;
    }

    /**
     * Method used to send a Game Status Notification to all players of the game.
     */
    private void notifyGameStatusToAll(){
        for (VirtualView player:virtualViewsList) player.sendMessage(new GameStatusNotification(currentGame));
    }
}