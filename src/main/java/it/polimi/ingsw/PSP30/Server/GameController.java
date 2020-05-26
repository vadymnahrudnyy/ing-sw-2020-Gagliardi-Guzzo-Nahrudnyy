package it.polimi.ingsw.PSP30.Server;


import it.polimi.ingsw.PSP30.Messages.*;
import it.polimi.ingsw.PSP30.Model.*;

import java.util.ArrayList;

/**
 * GameController class implement the Logic level of the game.
 * @author Vadym Nahrudnyy
 * @version 1.0
 */
class GameController implements Runnable {

    private Worker moveWorker;
    private final Game currentGame;
    private Message receivedMessage;
    private boolean MoveAllowed,BuildAllowed;
    private final ArrayList<God> gameGodList;
    private final ArrayList<Power> powerList;
    private boolean running = true;
    private final VirtualView[] virtualViewsList;
    private static final int RESPONSE_MESSAGE_WAIT_TIMEOUT = 20000;
    private Player currentPlayer;
    private VirtualView currentClient;


    public GameController(ArrayList<VirtualView> virtualViews, int numPlayers) {

        powerList = Server.getPowerList();
        virtualViewsList = new VirtualView[numPlayers];
        Player[] playersArray = new Player[numPlayers];
        for(int i=0;i<numPlayers;++i) virtualViewsList[i] = virtualViews.get(i);
        for (int i=0;i<numPlayers;++i) {playersArray[i] = new Player(virtualViewsList[i].getUsername(),i+1,null,null);}
        currentGame = new Game(numPlayers,playersArray[0],playersArray);
        if(numPlayers == 3) {
            gameGodList = new ArrayList<>();
            for (God threePlayersGods:Server.getGodsList()) if (threePlayersGods.getPlayersAllowed() >= God.ONLY_THREE_PLAYERS_ALLOWED ) gameGodList.add(threePlayersGods);
        }
        else gameGodList = Server.getGodsList();
    }

    @Override
    public void run() {
        setupPhase();
        while(running){
            TurnHandle();
        }
    }

    private void setupPhase() {
        sendStartGameMessage();
        chooseGods();
        chooseStartPlayer();
        chooseWorkerPositions();
        startGame();
    }

    private void turnInitializer(){
        currentGame.setCurrentPhase(TurnPhase.START);
        MoveAllowed = BuildAllowed = true;
        currentPlayer = currentGame.getCurrentPlayer();
        currentClient = getVirtualViewByUsername(currentPlayer.getUsername());
        currentPlayer.getWorkers()[0].setMovedUp(false);
        currentPlayer.getWorkers()[1].setMovedUp(false);
        currentPlayer.getWorkers()[0].setWasMoved(false);
        currentPlayer.getWorkers()[1].setWasMoved(false);
        if (currentPlayer.getGod().getSinglePower(0) == Power.OPPONENTS_NOT_MOVE_UP_POWER) currentGame.setAthenaMovedUp(false);
    }

    private void turnStart(VirtualView client, IslandBoard gameBoard){
        turnInitializer();

        if (currentPlayer.getGod().getSinglePower(0) == Power.NOT_MOVE_UP_DOUBLE_BUILD_POWER){
                currentClient.sendMessage(new UsePowerRequest());
                notifyGameStatusToAll();
                waitValidMessage(currentClient,new int[]{Message.USE_POWER_RESPONSE});
                if(((UsePowerResponse)receivedMessage).wantUsePower()){
                    client.sendMessage(new SelectWorkerRequest());
                    waitValidMessage(client,new int[]{Message.SELECT_WORKER_RESPONSE});
                    moveWorker = getWorkerByCoordinates(((SelectWorkerResponse)receivedMessage).getCoordinateX(),((SelectWorkerResponse)receivedMessage).getCoordinateY());
                    boolean[][] allowedBuild = checkPossibleBuilds(moveWorker.getWorkerPosition().getCoordinateX(),moveWorker.getWorkerPosition().getCoordinateY());
                    client.sendMessage(new BuildRequest(allowedBuild));
                    waitValidMessage(client,new int[]{Message.BUILD_RESPONSE});
                    Space buildSpace = gameBoard.getSpace(((BuildResponse)receivedMessage).getBuildCoordinateX(),((BuildResponse)receivedMessage).getBuildCoordinateY());
                    blockAddInSpace(buildSpace);
                    notifyGameStatusToAll();
                    boolean[][] allowedMoves = checkPossibleMoves(moveWorker.getWorkerPosition().getCoordinateX(),moveWorker.getWorkerPosition().getCoordinateY());
                    for (int X = 0; X < IslandBoard.TABLE_DIMENSION;++X){
                        for(int Y = 0; Y < IslandBoard.TABLE_DIMENSION; ++Y){
                            if (gameBoard.getSpace(X+1,Y+1).getHeight()>moveWorker.getWorkerPosition().getHeight()) allowedMoves[X][Y] = false;}}
                    if (workerCanMakeMove(allowedMoves)){
                        client.sendMessage(new MoveRequest(allowedMoves,false));
                        waitValidMessage(client,new int[]{Message.MOVE_RESPONSE});
                        moveWorker.changePosition(gameBoard.getSpace(((MoveResponse)receivedMessage).getDestCoordinateX(),((MoveResponse)receivedMessage).getDestCoordinateY()));
                        MoveAllowed = false;
                        notifyGameStatusToAll();
                    }
                }
        }
    }



    private void Move(){
        boolean moveMade = false;
        boolean[][] allowedMoves = new boolean[][]{};
        IslandBoard gameBoard = currentGame.getGameBoard();
        Power actualGodPower = getPowerByID(currentPlayer.getGod().getSinglePower(0));
        Space fromSpace = null, destSpace;
        notifyGameStatusToAll();
        currentClient.sendMessage(new SelectWorkerRequest());
        do{
            waitValidMessage(currentClient,new int[]{Message.SELECT_WORKER_RESPONSE,Message.MOVE_RESPONSE});
            if ((receivedMessage.getMessageID() == Message.SELECT_WORKER_RESPONSE)&&((moveWorker = getSelectedWorker((SelectWorkerResponse) receivedMessage))!=null)){
                fromSpace = moveWorker.getWorkerPosition();
                allowedMoves = checkPossibleMoves(fromSpace.getCoordinateX(),fromSpace.getCoordinateY());
                if (workerCanMakeMove(allowedMoves))currentClient.sendMessage(new MoveRequest(allowedMoves,true));
                else {
                    moveWorker = getPlayersOtherWorker(currentPlayer,moveWorker);
                    fromSpace = moveWorker.getWorkerPosition();
                    if (workerCanMakeMove(allowedMoves = checkPossibleMoves(fromSpace.getCoordinateX(),fromSpace.getCoordinateY()))) currentClient.sendMessage(new OtherWorkerMoveRequest(allowedMoves));
                    else {
                        currentClient.sendMessage(new NoPossibleMoveError());
                        removePlayerFromGame(currentPlayer);
                        BuildAllowed = false;
                        break;
                    }
                }
            }
            if(receivedMessage.getMessageID() == Message.MOVE_RESPONSE){
                int destX = ((MoveResponse)receivedMessage).getDestCoordinateX(), destY = ((MoveResponse)receivedMessage).getDestCoordinateY();
                if (allowedMoves[destX-1][destY-1]) {
                    destSpace = currentGame.getGameBoard().getSpace(destX,destY);
                    Worker tempWorker = destSpace.getWorkerInPlace();
                    if (tempWorker == null) moveWorker.changePosition(destSpace);
                    else{
                        assert actualGodPower != null;
                        if (actualGodPower.getPowerID() == Power.WORKER_POSITION_EXCHANGE_POWER){
                            destSpace.removeWorkerInPlace();
                            moveWorker.changePosition(destSpace);
                            tempWorker.setWorkerPosition(fromSpace);
                            fromSpace.setWorkerInPlace(tempWorker);
                        }
                        else{
                            Space pushIntoSpace = gameBoard.getSpace(destX+(destX-moveWorker.getWorkerPosition().getCoordinateX()),destY+(destY-moveWorker.getWorkerPosition().getCoordinateY()));
                            tempWorker.changePosition(pushIntoSpace);
                            moveWorker.changePosition(destSpace);
                        }
                    }
                    moveMade = true;
                    moveWorker.setWasMoved(true);
                    notifyGameStatusToAll();
                    if(destSpace.getHeight()>fromSpace.getHeight()){
                        moveWorker.setMovedUp(true);
                        if (currentPlayer.getGod().getSinglePower(0)==Power.OPPONENTS_NOT_MOVE_UP_POWER) currentGame.setAthenaMovedUp(true);
                    }
                    if((((destSpace.getHeight()==Space.DOME_LEVEL-1)&&(moveWorker.isMovedUp()))||((currentPlayer.getGod().getSinglePower(0)==Power.TWO_BLOCK_FALL_VICTORY_POWER)&&(fromSpace.getHeight()-destSpace.getHeight() >= 2)))&&((!opponentHasPower(Power.PERIMETER_VICTORY_DENY_POWER))||(!destSpace.isPerimeter()))) victory(currentPlayer.getUsername());

                    if (actualGodPower.getPowerID() == Power.DOUBLE_MOVE_POWER) ArtemisPower(fromSpace);
                }
                else currentClient.sendMessage(new InvalidMoveError());
            }
        }while(!moveMade);
    }


    /**
     * Method used for handling a player's turn and eventually Ares power.
     */
    private void TurnHandle(){
        Player currentPlayer = currentGame.getCurrentPlayer();
        VirtualView currentClient = getVirtualViewByUsername(currentPlayer.getUsername());
        IslandBoard currentBoard = currentGame.getGameBoard();
        Power currentGodPower = getPowerByID(currentPlayer.getGod().getSinglePower(0));

        turnStart(currentClient,currentBoard);

        currentGame.nextTurnPhase();
        if (MoveAllowed){
            Move();
        }
        //BUILD PHASE
        currentGame.setCurrentPhase(TurnPhase.BUILD);
        if (BuildAllowed) Build(currentClient,currentGodPower,moveWorker);
        if ((opponentHasPower(Power.FIVE_TOWER_VICTORY_POWER)||currentGodPower.getPowerID()==Power.FIVE_TOWER_VICTORY_POWER)&&(currentBoard.getNumberCompleteTowers()>=5)){
            for (Player player:currentGame.getPlayers())
                if (player.getGod().getSinglePower(0)==Power.FIVE_TOWER_VICTORY_POWER) victory(player.getUsername());
        }
        //End of turn
        currentGame.nextTurnPhase();
        if (currentGodPower.getPowerID() == Power.BLOCK_REMOVE_POWER){
                Worker otherWorker = getPlayersOtherWorker(currentPlayer,moveWorker);
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
        currentGame.nextPlayer();
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
     * Method used to handle the double build powers. Receives a matrix with allowed moves
     * and asks the player if he want to use the power. If affirmative answer has been receives, the normal build process is made.
     * @param allowedBuild matrix with allowed build positions.
     * @param client virtual view of the player
     */
    private void secondBuildMake(boolean[][] allowedBuild,VirtualView client){
        if (workerCanMakeMove(allowedBuild)){
            client.sendMessage(new UsePowerRequest());
            waitValidMessage(client,new int[]{Message.USE_POWER_RESPONSE});
            if (((UsePowerResponse)receivedMessage).wantUsePower()){
                client.sendMessage(new BuildRequest(allowedBuild));
                waitValidMessage(client,new int[]{Message.BUILD_RESPONSE});
                Space toBuildSpace = currentGame.getGameBoard().getSpace(((BuildResponse) receivedMessage).getBuildCoordinateX(),((BuildResponse) receivedMessage).getBuildCoordinateY());
                blockAddInSpace(toBuildSpace);
            }
        }
    }

    private void Build(VirtualView client,Power actualPower,Worker selectedWorker){
        Space workerPosition = selectedWorker.getWorkerPosition();
        boolean[][] allowedBuild = checkPossibleBuilds(workerPosition.getCoordinateX(),workerPosition.getCoordinateY());
        client.sendMessage(new BuildRequest(allowedBuild));
        waitValidMessage(client,new int[]{Message.BUILD_RESPONSE});
        Space toBuildSpace = currentGame.getGameBoard().getSpace(((BuildResponse) receivedMessage).getBuildCoordinateX(),((BuildResponse) receivedMessage).getBuildCoordinateY());
        buildInSpace(client,toBuildSpace,actualPower);
        notifyGameStatusToAll();

        if (actualPower.getTurnPhase()==TurnPhase.BUILD && actualPower.getActive()&&actualPower.getPowerID()!=Power.BUILD_DOME_EVERYWHERE_POWER){
            switch (actualPower.getPowerID()){
                case Power.DIFFERENT_SPACE_DOUBLE_BUILD_POWER:
                    allowedBuild = checkPossibleBuilds(workerPosition.getCoordinateX(),workerPosition.getCoordinateY());
                    allowedBuild[toBuildSpace.getCoordinateX()-1][toBuildSpace.getCoordinateY()-1] = false;
                    if(workerCanMakeMove(allowedBuild))secondBuildMake(allowedBuild,client);
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
                    for (tempX = 0, tempY = IslandBoard.TABLE_DIMENSION - 1; tempX < IslandBoard.TABLE_DIMENSION; ++tempX) allowedBuild[tempX][tempY] = false;
                    for (tempX = tempY = IslandBoard.TABLE_DIMENSION - 1; tempY > 0; --tempY) allowedBuild[tempX][tempY] = false;
                    for (tempX = IslandBoard.TABLE_DIMENSION - 1,tempY = 0; tempX > 0; --tempX) allowedBuild[tempX][tempY] = false;
                    if(workerCanMakeMove(allowedBuild))secondBuildMake(allowedBuild,client);
                    break;
            }
        }
    }


    //DOUBLE CHECKED
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

    private void chooseGods() {
        System.out.println("Choosing gods");
        Player firstPlayer = currentGame.getCurrentPlayer();
        VirtualView firstVirtualView = virtualViewsList[0];
        System.out.println("Sending Gods List to first player");
        boolean validGodsReceived;
        ArrayList<God> gameGods=new ArrayList<>(),chosenGods = new ArrayList<>();
        do {
            if(!(validGodsReceived = correctGodChose(firstVirtualView,gameGods))) firstVirtualView.sendMessage(new InvalidGodError());
        }while(!validGodsReceived);
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
     * Method asking the first player the list of gods and checks the validity of the answer.
     * @param firstVirtualView virtual view of the first player.
     * @param gameGods empty List where the gods will be inserted.
     * @return a boolean value. True if the player has chosen the gods correctly.
     */
    private boolean correctGodChose(VirtualView firstVirtualView,ArrayList<God> gameGods){
        gameGods.clear();
        firstVirtualView.sendMessage(new GodsListRequest(gameGodList,currentGame.getNumPlayers()));
        waitValidMessage(firstVirtualView,new int[]{Message.GODS_LIST_RESPONSE});
        System.out.println("Gods List received from first player");
        ArrayList<String> gameGodsNames = ((GodsListResponse) receivedMessage).getGods();
        for (String receivedGodName:gameGodsNames){
            God currentGod = getGodByName(receivedGodName);
            if (currentGod == null) return false;
            else gameGods.add(currentGod);
        }
        return true;
    }
    /**
     * Private method of the controller used to identify the god of the first player.
     * @param gameGods gods chosen to be used in game.
     * @param chosenGods gods chosen by other players.
     * @return the god which will be used by the first player.
     */
    private God getRemainedGod(ArrayList<God> gameGods,ArrayList<God> chosenGods){
        int index=0;
        for (; index<gameGods.size()-1;++index) if (!chosenGods.contains(gameGods.get(index))) return gameGods.get(index);
        return gameGods.get(index);
    }
    /**
     * Method used to ask the Challenger the username of the start player.
     * If unvalid input is detected it sends an error to the client.
     */
    private void chooseStartPlayer(){
        boolean validUsernameReceived = false;
        Player starterPlayer;
        do {
            virtualViewsList[0].sendMessage(new StartPlayerRequest(currentGame.getPlayers()));
            waitValidMessage(virtualViewsList[0], new int[]{Message.START_PLAYER_RESPONSE});
            starterPlayer = currentGame.getPlayerByUsername(((StartPlayerResponse)receivedMessage).getStartPlayerUsername());
            if(starterPlayer!=null) {
                currentGame.setStarterPlayer(((StartPlayerResponse) receivedMessage).getStartPlayerUsername());
                validUsernameReceived=true;
            }
            else virtualViewsList[0].sendMessage(new InvalidStarterPlayerError());
        }while(!validUsernameReceived);
    }
    /**
     * Used for asking the players the starter position of their workers.
     */
    private void chooseWorkerPositions(){
        currentGame.setCurrentPlayer(currentGame.getStarterPlayer());
        boolean [][] allowedPositions = initializeMatrix(true);
        boolean validPosition;
        notifyGameStatusToAll();
        do {
            VirtualView currentClient = getVirtualViewByUsername(currentGame.getCurrentPlayer().getUsername());
            assert currentClient != null;
            do{
                currentClient.sendMessage(new WorkerPositionRequest(1,allowedPositions));
                waitValidMessage(currentClient,new int[]{Message.WORKER_POSITION_RESPONSE});
                validPosition = verifyValidPosition(allowedPositions,(WorkerPositionResponse)receivedMessage);
            }while(!validPosition);
            allowedPositions[((WorkerPositionResponse) receivedMessage).getCoordinateX()-1][((WorkerPositionResponse) receivedMessage).getCoordinateY()-1] = false;
            createWorker(1,currentGame.getCurrentPlayer(),((WorkerPositionResponse) receivedMessage).getCoordinateX(),((WorkerPositionResponse) receivedMessage).getCoordinateY());
            notifyGameStatusToAll();
            do{
                currentClient.sendMessage(new WorkerPositionRequest(2,allowedPositions));
                waitValidMessage(currentClient,new int[]{Message.WORKER_POSITION_RESPONSE});
                validPosition = verifyValidPosition(allowedPositions,(WorkerPositionResponse)receivedMessage);
            }while(!validPosition);
            allowedPositions[((WorkerPositionResponse) receivedMessage).getCoordinateX()-1][((WorkerPositionResponse) receivedMessage).getCoordinateY()-1] = false;
            createWorker(2,currentGame.getCurrentPlayer(),((WorkerPositionResponse) receivedMessage).getCoordinateX(),((WorkerPositionResponse) receivedMessage).getCoordinateY());
            notifyGameStatusToAll();
            currentGame.nextPlayer();
        }while(currentGame.getCurrentPlayer() != currentGame.getStarterPlayer());
    }
    //MOVE
    /**
     * Check all the possible moves the worker can make.
     * @param workerCoordinateX coordinate X of the selected worker.
     * @param workerCoordinateY coordinate Y of the selected worker.
     * @return a boolean matrix with "true" value in the position where the worker can move.
     */
    private boolean[][] checkPossibleMoves(int workerCoordinateX,int workerCoordinateY){
        IslandBoard currentBoard = getCurrentBoard();
        God currentGod = currentGame.getCurrentPlayer().getGod();
        Space checkedSpace;
        Worker workerInCheckedSpace;
        int currentHeight = currentBoard.getSpace(workerCoordinateX,workerCoordinateY).getHeight();
        boolean [][] allowedMoves = initializeMatrix(false);

        for (int X = workerCoordinateX-2; X < workerCoordinateX+1; ++X){
            for (int Y = workerCoordinateY-2; Y < workerCoordinateY +1; ++Y){
                if ((X >= 0 && X < IslandBoard.TABLE_DIMENSION) && (Y >= 0 && Y < IslandBoard.TABLE_DIMENSION)) {
                    checkedSpace = currentBoard.getSpace(X + 1, Y + 1);
                    if ((currentHeight >= checkedSpace.getHeight() - 1)&&!checkedSpace.getHasDome()) {
                        if (((workerInCheckedSpace = checkedSpace.getWorkerInPlace()) != null)) {
                            if (!workerInCheckedSpace.getOwner().equals(currentGame.getCurrentPlayer().getUsername())) {
                                switch (currentGod.getSinglePower(0)) {
                                    case Power.WORKER_POSITION_EXCHANGE_POWER:
                                        allowedMoves[X][Y] = true;
                                        break;
                                    case Power.PUSH_WORKER_POWER:
                                        int tempX, tempY;
                                        tempX = X + (X - (workerCoordinateX - 1));
                                        tempY = Y + (Y - (workerCoordinateY - 1));
                                        if (tempX >= 0 && tempY >= 0 && tempX < IslandBoard.TABLE_DIMENSION && tempY < IslandBoard.TABLE_DIMENSION) {
                                            Space tempSpace = currentBoard.getSpace(tempX, tempY);
                                            if ((tempSpace.getWorkerInPlace() == null) && (!tempSpace.getHasDome())) allowedMoves[X][Y] = true;
                                        }
                                        break;
                                }
                            }
                        } else allowedMoves[X][Y] = true;
                    }//line below is ATHENA POWER
                    if (allowedMoves[X][Y]&&currentGame.hasAthenaMovedUpDuringLastRound()&&(checkedSpace.getHeight() > currentHeight)) allowedMoves[X][Y] = false;
                }
            }
        }
        return allowedMoves;
    }
    //BUILD
    /**
     * Method used to identify the spaces a given worker can build
     * @param workerCoordinateX coordinate x of the moved worker.
     * @param workerCoordinateY coordinate y of the moved worker.
     * @return Boolean matrix with true value in the cells the worker can build.
     */
    private boolean[][] checkPossibleBuilds(int workerCoordinateX,int workerCoordinateY){
        boolean[][] allowedBuilds = initializeMatrix(false);
        for (int X = workerCoordinateX - 2; X < workerCoordinateX + 1;++ X){
            for (int Y = workerCoordinateY - 2; Y < workerCoordinateY + 1;++Y){
                Space currentSpace;
                if(((X >= 0 && X < IslandBoard.TABLE_DIMENSION) && (Y >= 0 && Y < IslandBoard.TABLE_DIMENSION))&&((!(currentSpace = currentGame.getGameBoard().getSpace(X + 1, Y + 1)).getHasDome()&&currentSpace.getWorkerInPlace()==null))) allowedBuilds[X][Y] = true;
            }
        }
        if (currentGame.getCurrentPlayer().getGod().getSinglePower(0)== Power.WORKER_POSITION_BUILD && currentGame.getGameBoard().getSpace(workerCoordinateX,workerCoordinateY).getHeight() < (Space.DOME_LEVEL-1)) allowedBuilds[workerCoordinateX-1][workerCoordinateY-1] = true;
        return allowedBuilds;
    }
    /**
     * Method used to add a block in a given space checking also if a dome must be built.
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

    //AUXILIARY METHODS
    /**
     * Starts the game after that worker positions have been chosen.
     */
    private void startGame(){
        currentGame.setCurrentRound(1);
    }
    /**
     * Method used to send a Game Status Notification to all players of the game.
     */
    private void notifyGameStatusToAll(){
        for (VirtualView virtualView : virtualViewsList) {
            virtualView.sendMessage(new GameStatusNotification(currentGame));
        }
    }
    /**
     * Creates a new matrix of TABLE_DIMENSION x TABLE_DIMENSION dimension.
     * @param value initial value of each item in the matrix.
     * @return the created matrix.
     */
    protected boolean[][] initializeMatrix(boolean value){
        boolean[][] allowedPositions = new boolean[IslandBoard.TABLE_DIMENSION][IslandBoard.TABLE_DIMENSION];
        for (int coordinateX = 0; coordinateX < IslandBoard.TABLE_DIMENSION; ++coordinateX)
            for (int coordinateY = 0; coordinateY < IslandBoard.TABLE_DIMENSION; ++coordinateY)
                allowedPositions[coordinateX][coordinateY] = value;
        return allowedPositions;
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
     * Checks if the the client has chosen a valid position. This is first checked in the client in order to ensure
     * better performance and reduce socket load and double checked on the server for ensuring security.
     * @param allowedPositions matrix of allowed positions.
     * @param response Message received from the client
     * @return boolean value indicating with "true" that a valid move has been chosen by the client. false otherwise.
     */
    protected boolean verifyValidPosition(boolean[][] allowedPositions,WorkerPositionResponse response){
        int coordinateX = response.getCoordinateX();
        int coordinateY = response.getCoordinateY();
        return allowedPositions[coordinateX - 1][coordinateY - 1];
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
        if(workers == null) owner.setWorkers(workers = new Worker[Game.WORKERS_PER_PLAYER]);
        char gender = 'm';
        if (numWorker == 1) gender = 'f';
        Space workerPosition = currentGame.getGameBoard().getSpace(coordinateX,coordinateY);
        workerPosition.setWorkerInPlace(workers[numWorker-1] = new Worker(owner.getUsername(),gender,workerPosition,owner.getUserID()));
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
     * Method getGodByName is used to get a god by using the name
     * @param name Name of the God.
     * @return object of class God taken from the list of gods of the game.
     */
    private God getGodByName(String name){
        for (God god:gameGodList) if (god.getName().equals(name)) return god;
        return null;
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
     * used to get the other worker of the player
     * @param player in the player considered
     * @param oneWorker is the worker we are considering
     * @return the other worker of the player
     */
    private Worker getPlayersOtherWorker(Player player,Worker oneWorker){
        if (player.getWorkers()[0] == oneWorker ) return player.getWorkers()[1];
        else return player.getWorkers()[0];
    }

    /**
     * Method used to notify to all players the winner of the game and close connections
     * @param winnerUsername Username of the winner.
     */
    private void victory(String winnerUsername){
        for (VirtualView player:virtualViewsList) {
            player.sendMessage(new WinnerNotification(winnerUsername));
            //player.disconnect();
        }
    }
    /**
     * Checks in the matrix indicating allowed moves if an action can be performed by the worker
     * @param allowedMoves is previously created matrix indicating where the worker can move or build;
     * @return a boolean value. True when an action can be performed, false otherwise.
     */
    protected boolean workerCanMakeMove (boolean[][] allowedMoves){
        for (int X = 0; X < IslandBoard.TABLE_DIMENSION; ++X)
            for(int Y = 0; Y < IslandBoard.TABLE_DIMENSION; ++Y)
                if (allowedMoves[X][Y]) return true;
        return false;
    }

    /**
     * Method used to delete a player from the game. In case of two players game, the remained players wins the game,
     * so the victory method is called in order to notify the winner.
     * @param playerToRemove player to remove from the game
     */
    private void removePlayerFromGame(Player playerToRemove) {
        Player[] currentPlayers = currentGame.getPlayers();
        if (currentGame.getNumPlayers() == 2) {
            if ((currentPlayers = currentGame.getPlayers())[0]==playerToRemove) victory(currentPlayers[1].getUsername());
            else victory(currentPlayers[0].getUsername());
        }
        else {
            Player[] newPlayersArray = new Player[currentGame.getNumPlayers() - 1];
            Player tempPlayer;
            int newPlayersArrayIndex = 0;
            for (int i = 0; i < currentGame.getNumPlayers(); ++i) {
                if ((tempPlayer = currentPlayers[i]) != playerToRemove) {
                    newPlayersArray[newPlayersArrayIndex] = tempPlayer;
                    ++newPlayersArrayIndex;
                    if (currentGame.getStarterPlayer() == currentPlayers[i]) {
                        if (i == 2) currentGame.setStarterPlayer(newPlayersArray[0].getUsername());
                        else currentGame.setStarterPlayer(currentPlayers[i + 1].getUsername());
                    }
                } else {
                    if (i > 0) currentGame.setCurrentPlayer(currentPlayers[i - 1]);
                    else currentGame.setCurrentPlayer(currentPlayers[1]);
                }
            }
            currentGame.setPlayers(newPlayersArray);
        }
    }
    /**
     * Method waitValidMessage
     * @param senderVirtualView virtual view of the client from who the controller needs a message
     * @param messageIDs list of accepted messages.
     */
    private void waitValidMessage(VirtualView senderVirtualView,int[] messageIDs) {
        boolean receivedValidMessage = false;
        System.out.println("Waiting for a valid message");
        do {
            try{
                while((receivedMessage = senderVirtualView.dequeueFirstMessage())==null) Thread.sleep(RESPONSE_MESSAGE_WAIT_TIMEOUT);
                System.out.println("New message received, checking validity");
                receivedValidMessage = checkMessageValidity(receivedMessage,messageIDs);
            } catch (InterruptedException e) {
                System.out.println("");
            }
        } while (!receivedValidMessage);
    }
    /**
     * Method used to get the worker selected by the player. If the selected worker is invalid it send an error message to the client.
     * @param message received select worker response message.
     * @return The selected worker, null if it's invalid
     */
    private Worker getSelectedWorker(SelectWorkerResponse message){
        int X = message.getCoordinateX(),Y=message.getCoordinateY();
        Worker selectedWorker;
        if ((X<1||X>IslandBoard.TABLE_DIMENSION)||(Y<1||Y>IslandBoard.TABLE_DIMENSION)||(((selectedWorker = getWorkerByCoordinates(X,Y))==null))||!selectedWorker.getOwner().equals(currentPlayer.getUsername())){
            currentClient.sendMessage(new InvalidWorkerError());
            currentClient.sendMessage(new SelectWorkerRequest());
        }
        else return selectedWorker;
        return null;
    }

    /**
     * Method used to implement Artemis power.
     * @param previousPosition the start position during first move of the worker selected by the player.
     */
    private void ArtemisPower(Space previousPosition){
        boolean moveMade = false;
        Space fromSpace = moveWorker.getWorkerPosition();
        boolean[][] allowedMoves = possibleArtemisSecondMoveDestinations(previousPosition,fromSpace);
        if (workerCanMakeMove(allowedMoves)){
            currentClient.sendMessage(new UsePowerRequest());
            waitValidMessage(currentClient,new int[]{Message.USE_POWER_RESPONSE});
            if (((UsePowerResponse)receivedMessage).wantUsePower()) do{
                currentClient.sendMessage(new MoveRequest(allowedMoves,false));
                waitValidMessage(currentClient,new int[]{Message.MOVE_RESPONSE});
                int destX = ((MoveResponse) receivedMessage).getDestCoordinateX(), destY = ((MoveResponse) receivedMessage).getDestCoordinateY();
                if (allowedMoves[destX-1][destY-1]){
                    moveWorker.changePosition(currentGame.getGameBoard().getSpace(destX,destY));
                    moveMade = true;
                }
                else {
                    currentClient.sendMessage(new InvalidMoveError());
                    currentClient.sendMessage(new MoveRequest(allowedMoves,false));
                }
            }while(!moveMade);
        }
    }

    /**
     * Method used to create the boolean matrix of possible moves for the second move of the player having Artemis god card.
     * @param previousSpace the position of the worker before the first move
     * @param currentSpace the current position of the worker
     * @return boolean matrix indicating with true the position where the selected worker can move, false otherwise.
     */
    protected boolean[][] possibleArtemisSecondMoveDestinations(Space previousSpace,Space currentSpace){
        boolean[][] allowedMoves = checkPossibleMoves(currentSpace.getCoordinateX(),currentSpace.getCoordinateY());
        allowedMoves[previousSpace.getCoordinateX()-1][previousSpace.getCoordinateY()-1] = false;
        return allowedMoves;
    }

    /**
     * Method used to get the current game Board
     * @return the current game board.
     */
    protected IslandBoard getCurrentBoard(){
        return currentGame.getGameBoard();
    }
}
