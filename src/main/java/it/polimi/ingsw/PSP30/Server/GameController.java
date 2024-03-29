package it.polimi.ingsw.PSP30.Server;


import it.polimi.ingsw.PSP30.Exception.Server.PlayerDisconnectedException;
import it.polimi.ingsw.PSP30.Messages.*;
import it.polimi.ingsw.PSP30.Model.*;

import java.util.ArrayList;

/**
 * GameController is a runnable class implementing the game itself. An instance of GameController is used for only one game.
 * Running in a separate thread it allows to handle multiple games without interference.
 * @author Vadym Nahrudnyy
 * @version 2.1
 */
class GameController implements Runnable {


    private final Game currentGame;
    private Message receivedMessage;
    private boolean moveAllowed,buildAllowed;
    private final ArrayList<God> gameGodList;
    private final ArrayList<Power> powerList;
    private boolean running = true;

    private boolean disconnectionDetected = false;
    private boolean moveMade = false, canMoveUp = true, canSelectWorker;
    private Space startSpace, destSpace;
    private boolean[][] allowedMoves, allowedBuilds;
    private Worker moveWorker;
    protected Player currentPlayer;
    protected VirtualView currentClient;
    private final IslandBoard gameBoard;
    private final VirtualView[] virtualViewsList;

    private static final int RESPONSE_MESSAGE_WAIT_TIMEOUT = 20000; //Maximum time the thread will wait for a new message.

    /**
     * Constructor of GameController.
     * @param virtualViews the list of virtual views of the players in the game.
     * @param numPlayers Number of player in the game.
     */
    public GameController(ArrayList<VirtualView> virtualViews, int numPlayers) {
        powerList = Server.getPowerList();
        virtualViewsList = new VirtualView[numPlayers];
        Player[] playersArray = new Player[numPlayers];
        for(int i=0;i<numPlayers;++i) {
            virtualViewsList[i] = virtualViews.get(i);
            virtualViews.get(i).setAssociatedGame(this);
        }
        for (int i=0;i<numPlayers;++i) {playersArray[i] = new Player(virtualViewsList[i].getUsername(),i+1,null,null);}
        currentGame = new Game(numPlayers,playersArray[0],playersArray);
        gameBoard = currentGame.getGameBoard();
        if (numPlayers == 2) gameGodList = Server.getGodsList();
        else gameGodList = threePlayerGodsListInitialization();
    }

    @Override
    public void run() {
        try{
            setupPhase();
        while(running){
            TurnHandle();
        }
        }catch (PlayerDisconnectedException e){
                allPlayersDisconnect();
        }
        System.out.println(Thread.currentThread()+" game finished! Thread terminated");
    }

    /**
     * Disconnects all the players of the game.
     */
    private void allPlayersDisconnect(){
        for (VirtualView client : virtualViewsList) {
            client.sendMessage(new PlayerDisconnectedError());
            try{
                Thread.sleep(100);
            }
            catch (InterruptedException ignored){}

            client.closeConnection();
            client.getVirtualViewThread().interrupt();
        }
    }

    /**
     * Setup of the game before starting it.
     * @throws PlayerDisconnectedException When a disconnection is detected.
     */
    private void setupPhase() throws PlayerDisconnectedException {
        sendStartGameMessage();
        chooseGods();
        chooseStartPlayer();
        chooseWorkerPositions();
        startGame();
    }

    /**
     * Handles player's turn using corresponding method for different phases.
     * @throws PlayerDisconnectedException When a disconnection is detected.
     */
    private void TurnHandle() throws PlayerDisconnectedException {
        currentPlayer = currentGame.getCurrentPlayer();

        //START PHASE
        turnInitializer();
        handlePrometheusPower();

        //MOVE PHASE
        currentGame.nextTurnPhase();
        if (moveAllowed){
            Move();
        }

        //BUILD PHASE
        currentGame.setCurrentPhase(TurnPhase.BUILD);
        if (buildAllowed) {
            Build();
            handleCronusPower();
        }

        //END PHASE
        currentGame.nextTurnPhase();
        handleAresPower();
        //Passing the turn
        currentGame.nextPlayer();
    }

    /**
     *  Initializes the flags used during the turn to their default values.
     */
    private void turnInitializer(){
        currentGame.setCurrentPhase(TurnPhase.START);
        moveAllowed = buildAllowed = canMoveUp = canSelectWorker = true;
        currentPlayer = currentGame.getCurrentPlayer();
        currentClient = getVirtualViewByUsername(currentPlayer.getUsername());
        currentPlayer.getWorkers()[0].setMovedUp(false);
        currentPlayer.getWorkers()[1].setMovedUp(false);
        currentPlayer.getWorkers()[0].setWasMoved(false);
        currentPlayer.getWorkers()[1].setWasMoved(false);
        if (currentPlayerHasPower(Power.OPPONENTS_NOT_MOVE_UP_POWER)) currentGame.setAthenaMovedUp(false);
    }

    /**
     * Handles the move phase of the player's turn.
     * Selects the worker to move and then makes the moves itself, in case using god powers.
     * @throws PlayerDisconnectedException When a disconnection is detected.
     */
    private void Move() throws PlayerDisconnectedException {
        moveMade = false;
        buildAllowed = true;
        notifyGameStatusToAll();
        if (canSelectWorker)currentClient.sendMessage(new SelectWorkerRequest());
        else if (currentPlayerHasPower(Power.NOT_MOVE_UP_DOUBLE_BUILD_POWER)){
            allowedMoves = checkPossibleMoves(startSpace.getCoordinateX(),startSpace.getCoordinateY());
            if (workerCanMakeMove(allowedMoves))currentClient.sendMessage(new MoveRequest(allowedMoves,false));
            else removePlayerFromGame(currentPlayer,currentClient);
        }
        do{
            waitValidMessage(currentClient,new int[]{Message.SELECT_WORKER_RESPONSE,Message.MOVE_RESPONSE});
            if ((receivedMessage.getMessageID() == Message.SELECT_WORKER_RESPONSE)&&((moveWorker = getSelectedWorker((SelectWorkerResponse) receivedMessage))!=null)) selectWorkerHandler();
            else if (receivedMessage.getMessageID() == Message.MOVE_RESPONSE){
                int destX = ((MoveResponse)receivedMessage).getDestCoordinateX(), destY = ((MoveResponse)receivedMessage).getDestCoordinateY();
                if (allowedMoves[destX-1][destY-1]) {
                    makeMove(destX,destY);
                    notifyGameStatusToAll();
                    checkWorkerMovedUp();
                    if (moveVictoryConditionSatisfied()) {
                        victory(currentPlayer.getUsername());
                        break;
                    }
                    if (currentPlayerHasPower(Power.DOUBLE_MOVE_POWER)) handleArtemisPower(startSpace);
                }
                else currentClient.sendMessage(new InvalidMoveError());
            }
        }while(!moveMade);
    }

    /**
     * Handles Build phase of the turn. Checks the possible builds and sends them to the player.
     * @throws PlayerDisconnectedException When a disconnection is detected.
     */
    private void Build() throws PlayerDisconnectedException {
        boolean validBuildReceived;
        Space workerPosition = moveWorker.getWorkerPosition();
        allowedBuilds = checkPossibleBuilds(workerPosition.getCoordinateX(),workerPosition.getCoordinateY());
        do{
            currentClient.sendMessage(new BuildRequest(allowedBuilds));
            waitValidMessage(currentClient,new int[]{Message.BUILD_RESPONSE});
            validBuildReceived = verifyValidPosition(allowedBuilds,((BuildResponse) receivedMessage).getBuildCoordinateX(),(((BuildResponse) receivedMessage).getBuildCoordinateY()));
        }while (!validBuildReceived);

        Space toBuildSpace = currentGame.getGameBoard().getSpace(((BuildResponse) receivedMessage).getBuildCoordinateX(),((BuildResponse) receivedMessage).getBuildCoordinateY());
        buildInSpace(currentClient,toBuildSpace);
        notifyGameStatusToAll();

        if(currentPlayerHasPower(Power.DIFFERENT_SPACE_DOUBLE_BUILD_POWER)) handleDemeterPower(toBuildSpace);
        else if(currentPlayerHasPower(Power.SAME_SPACE_DOUBLE_BUILD_POWER)) handleHephaestusPower(toBuildSpace);
        else if (currentPlayerHasPower(Power.NON_PERIMETER_DOUBLE_BUILD)) handleHestiaPower();
    }

    /**
     * Counts the number of moves (move or build) a worker can make given a boolean matrix with allowed moves.
     * @param possibleMoves Boolean matrix with allowed values.
     * @return Integer number indicating the number of possible moves.
     */
    protected int countPossibleMoves(boolean[][] possibleMoves){
        int moves = 0;
        for (int i=0;i<IslandBoard.TABLE_DIMENSION;i++)
            for (int j = 0; j< IslandBoard.TABLE_DIMENSION;j++)
                if(possibleMoves[i][j])
                    moves++;
        return moves;
    }

    /**
     * Looks for the matrix indexes of the only possible move on the board.
     * @param possibleMoves Boolean matrix containing only one "true" value.
     * @return Array of two integers standing for the indexes in the matrix where the move is possible.
     */
    protected int[] getOnlyMoveIndexes(boolean[][] possibleMoves){
        int[] indexes = new int[2];
        for (int i=0;i<IslandBoard.TABLE_DIMENSION;i++)
            for (int j = 0; j< IslandBoard.TABLE_DIMENSION;j++)
                if(possibleMoves[i][j]) {
                    indexes[0] = i;
                    indexes[1] = j;
                    return indexes;
                }
        return null;
    }

    /**
     * Selects the worker before making the move, if the worker can't be moved
     * the other worker of the player is considered. In case both of them can't move, the player is removed from the game.
     */
    private void selectWorkerHandler(){
        startSpace = moveWorker.getWorkerPosition();
        allowedMoves = checkPossibleMoves(startSpace.getCoordinateX(),startSpace.getCoordinateY());
        if (workerCanMakeMove(allowedMoves)) currentClient.sendMessage(new MoveRequest(allowedMoves,true));
        else{
            moveWorker = getPlayersOtherWorker(currentPlayer,moveWorker);
            startSpace = moveWorker.getWorkerPosition();
            allowedMoves = checkPossibleMoves(startSpace.getCoordinateX(), startSpace.getCoordinateY());
            if (workerCanMakeMove(allowedMoves)) currentClient.sendMessage(new OtherWorkerMoveRequest(allowedMoves));
            else {
                currentClient.sendMessage(new NoPossibleMoveError());
                removePlayerFromGame(currentPlayer,currentClient);
                moveMade = true;
                buildAllowed = moveAllowed = false;
            }
        }
    }

    /**
     * Checks if the current player has made a victory move.
     * @return true if the player won, false otherwise.
     */
    protected boolean moveVictoryConditionSatisfied(){
        return((isVictoryMove()||isPanVictoryMove())&&(!victoryDeniedByHeraPower()));
    }

    /**
     *  After a move, checks if the worker moved up and set the corresponding flag.
     */
    protected void checkWorkerMovedUp(){
        if (destSpace.getHeight() > startSpace.getHeight()) moveWorker.setMovedUp(true);
        if (moveWorker.isMovedUp() && currentPlayerHasPower(Power.OPPONENTS_NOT_MOVE_UP_POWER)) currentGame.setAthenaMovedUp(true);
    }

    /**
     * Makes the move after receiving the destination coordinates from player and checking the validity of the response.
     * @param destX Coordinate X of the destination space.
     * @param destY Coordinate Y of the destination space.
     */
    protected void makeMove(int destX, int destY){
        destSpace = currentGame.getGameBoard().getSpace(destX,destY);
        Worker tempWorker = destSpace.getWorkerInPlace();
        if (tempWorker == null) moveWorker.changePosition(destSpace);
        else {
            if (currentPlayerHasPower(Power.WORKER_POSITION_EXCHANGE_POWER)) useApolloPower(tempWorker);
            else useMinotaurPower(tempWorker,destX,destY);
        }
        moveMade = true;
        moveWorker.setWasMoved(true);
    }

    /**
     * Verifies if the move has been made is a standard win move.
     * @return "true" if a win move has been made, "false" otherwise.
     */
    protected boolean isVictoryMove(){
        return (destSpace.getHeight() == Space.DOME_LEVEL - 1) && (moveWorker.isMovedUp());
    }

    /**
     * Checks if the current player's god has a specified power.
     * @param powerID The ID of the power to check.
     * @return "true" if the god has the specified power, "false otherwise"
     */
    protected boolean currentPlayerHasPower(int powerID){
        return (currentPlayer.getGod().getSinglePower(0) == powerID);
    }

    /**
     * Builds in the space indicated by the player.
     * @param client virtual view of the current player.
     * @param toBuild the space to build into.
     * @throws PlayerDisconnectedException When a disconnection is detected.
     */
    private void buildInSpace(VirtualView client,Space toBuild) throws PlayerDisconnectedException {
        if (toBuild.getHeight() < (Space.DOME_LEVEL - 1) && currentPlayerHasPower(Power.BUILD_DOME_EVERYWHERE_POWER)){
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

    //SETUP PHASE
    /**
     * Send the notification of game start to all players.
     */
    private void sendStartGameMessage() {
        for (VirtualView view : virtualViewsList) view.sendMessage(new GameStartNotification(currentGame));
    }

    /**
     * Selects the gods before start playing.It asks the first player the list of gods to use, then
     * the others chose their god card and the first player automatically receives the remained god card.
     * @throws PlayerDisconnectedException When a disconnection is detected.
     */
    private void chooseGods() throws PlayerDisconnectedException {
        System.out.println(Thread.currentThread() + " Choosing gods");
        Player firstPlayer = currentGame.getCurrentPlayer();
        VirtualView firstVirtualView = virtualViewsList[0];
        System.out.println(Thread.currentThread() + " Sending Gods List to first player");
        boolean validGodsReceived;
        ArrayList<God> gameGods=new ArrayList<>(),chosenGods = new ArrayList<>();
        do {
            if(!(validGodsReceived = correctGodChose(firstVirtualView,gameGods))) firstVirtualView.sendMessage(new InvalidGodError());
        }while(!validGodsReceived);
        notifyGameStatusToAll();
        God receivedGod;
        for(int Index = 1; Index< currentGame.getNumPlayers();++Index){
            do{
                virtualViewsList[Index].sendMessage(new ChoseGodRequest(gameGods, chosenGods));
                waitValidMessage(virtualViewsList[Index],new int[]{Message.CHOSE_GOD_RESPONSE});
                System.out.println(Thread.currentThread() + " Chosen god: "+((ChoseGodResponse)receivedMessage).getChosenGod());
                receivedGod= getGodByName(((ChoseGodResponse)receivedMessage).getChosenGod());
            } while (receivedGod == null || chosenGods.contains(receivedGod));

            Player actualPlayer = currentGame.getPlayerByUsername(virtualViewsList[Index].getUsername());
            if (actualPlayer == null) System.out.println(Thread.currentThread() + " Wanted player doesn't exist");
            else {
                actualPlayer.setGod(receivedGod);
                chosenGods.add(receivedGod);
                notifyGameStatusToAll();
            }
        }
        God remainedGod = getRemainedGod(gameGods,chosenGods);
        firstPlayer.setGod(remainedGod);
        virtualViewsList[0].sendMessage(new LastGodNotification(gameGods,remainedGod));
        notifyGameStatusToAll();
    }

    /**
     * Asksthe first player the list of gods and checks the validity of the answer.
     * @param firstVirtualView virtual view of the first player.
     * @param gameGods empty List where the gods will be inserted.
     * @return a boolean value. True if the player has chosen the gods correctly.
     * @throws PlayerDisconnectedException When a disconnection is detected.
     */
    private boolean correctGodChose(VirtualView firstVirtualView,ArrayList<God> gameGods) throws PlayerDisconnectedException {
        gameGods.clear();
        firstVirtualView.sendMessage(new GodsListRequest(gameGodList,currentGame.getNumPlayers()));
        waitValidMessage(firstVirtualView,new int[]{Message.GODS_LIST_RESPONSE});
        System.out.println(Thread.currentThread() + " Gods List received from first player");
        ArrayList<String> gameGodsNames = ((GodsListResponse) receivedMessage).getGods();
        for (String receivedGodName:gameGodsNames){
            God currentGod = getGodByName(receivedGodName);
            if (currentGod == null) return false;
            else gameGods.add(currentGod);
        }
        return true;
    }
    /**
     * Gets the god will be assigned to the first player.
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
     * Getter of currentGame. Used for test purposes
     * @return the Game object used to store the game status.
     */
    protected Game getCurrentGame(){
        return currentGame;
    }
    /**
     * Asks the Challenger the username of the start player.
     * If invalid input is detected it sends an error to the client.
     * @throws PlayerDisconnectedException When a disconnection is detected.
     */
    private void chooseStartPlayer() throws PlayerDisconnectedException {
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
     * Asks every player players the starter position of their workers.
     * @throws PlayerDisconnectedException When a disconnection is detected.
     */
    private void chooseWorkerPositions() throws PlayerDisconnectedException {
        currentGame.setCurrentPlayer(currentGame.getStarterPlayer());
        boolean [][] allowedPositions = initializeMatrix(true);
        boolean validPosition;
        do {
            notifyGameStatusToAll();
            VirtualView currentClient = getVirtualViewByUsername(currentGame.getCurrentPlayer().getUsername());
            assert currentClient != null;
            do{
                currentClient.sendMessage(new WorkerPositionRequest(1,allowedPositions));
                waitValidMessage(currentClient,new int[]{Message.WORKER_POSITION_RESPONSE});
                validPosition = verifyValidPosition(allowedPositions,((WorkerPositionResponse) receivedMessage).getCoordinateX(),(((WorkerPositionResponse) receivedMessage).getCoordinateY()));
            }while(!validPosition);
            allowedPositions[((WorkerPositionResponse) receivedMessage).getCoordinateX()-1][((WorkerPositionResponse) receivedMessage).getCoordinateY()-1] = false;
            createWorker(1,currentGame.getCurrentPlayer(),((WorkerPositionResponse) receivedMessage).getCoordinateX(),((WorkerPositionResponse) receivedMessage).getCoordinateY());
            notifyGameStatusToAll();
            do{
                currentClient.sendMessage(new WorkerPositionRequest(2,allowedPositions));
                waitValidMessage(currentClient,new int[]{Message.WORKER_POSITION_RESPONSE});
                validPosition = verifyValidPosition(allowedPositions,((WorkerPositionResponse) receivedMessage).getCoordinateX(),(((WorkerPositionResponse) receivedMessage).getCoordinateY()));
            }while(!validPosition);
            allowedPositions[((WorkerPositionResponse) receivedMessage).getCoordinateX()-1][((WorkerPositionResponse) receivedMessage).getCoordinateY()-1] = false;
            createWorker(2,currentGame.getCurrentPlayer(),((WorkerPositionResponse) receivedMessage).getCoordinateX(),((WorkerPositionResponse) receivedMessage).getCoordinateY());
            notifyGameStatusToAll();
            currentGame.nextPlayer();
        }while(currentGame.getCurrentPlayer() != currentGame.getStarterPlayer());
    }
    //MOVE
    /**
     * Checks all the possible moves the worker can make.
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
                                            Space tempSpace = currentBoard.getSpace(tempX+1, tempY+1);
                                            if ((tempSpace.getWorkerInPlace() == null) && (!tempSpace.getHasDome())) allowedMoves[X][Y] = true;
                                        }
                                        break;
                                }
                            }
                        } else allowedMoves[X][Y] = true;
                    }//line below is ATHENA POWER or prometheus notMoveUpCondition
                    if (allowedMoves[X][Y]&&(currentGame.hasAthenaMovedUpDuringLastRound()||!canMoveUp)&&(checkedSpace.getHeight() > currentHeight)) allowedMoves[X][Y] = false;
                }
            }
        }
        return allowedMoves;
    }
    //BUILD
    /**
     * Identifies the spaces a given worker can build in.
     * @param workerCoordinateX coordinate x of the moved worker.
     * @param workerCoordinateY coordinate y of the moved worker.
     * @return Boolean matrix with true value in the cells the worker can build.
     */
    protected boolean[][] checkPossibleBuilds(int workerCoordinateX,int workerCoordinateY){
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
     * Adds a block in a given space checking also if a dome must be built.
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
     * Builds the god list in a three players game.
     * @return the list of gods can be used during the game.
     */
    private ArrayList<God> threePlayerGodsListInitialization(){
        ArrayList<God> threePlayersGodsList = new ArrayList<>();
        for (God threePlayersGod:Server.getGodsList()) if (threePlayersGod.getPlayersAllowed() >= God.ONLY_THREE_PLAYERS_ALLOWED) threePlayersGodsList.add(threePlayersGod);
        return threePlayersGodsList;
    }
    /**
     * Sends to every player the Game Status Notification.
     */
    private void notifyGameStatusToAll(){
        for (VirtualView virtualView : virtualViewsList) virtualView.sendMessage(new GameStatusNotification(currentGame));
    }
    /**
     * Creates a new matrix of TABLE_DIMENSION x TABLE_DIMENSION dimension.
     * @param value initial value of each item in the matrix.
     * @return the created matrix.
     */
    protected boolean[][] initializeMatrix(boolean value){
        boolean[][] matrix = new boolean[IslandBoard.TABLE_DIMENSION][IslandBoard.TABLE_DIMENSION];
        for (int coordinateX = 0; coordinateX < IslandBoard.TABLE_DIMENSION; ++coordinateX)
            for (int coordinateY = 0; coordinateY < IslandBoard.TABLE_DIMENSION; ++coordinateY)
                matrix[coordinateX][coordinateY] = value;
        return matrix;
    }
    /**
     * Checks if the received message is among the messages can be used by
     * the game controller at the moment in order to continue the game.
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
     * Checks if the the client has chosen a valid position. This is first checked on player's side in order to ensure
     * better performance and reduce socket load and then double checked on the server in order to ensure security.
     * @param allowedPositions matrix of allowed positions.
     * @param receivedX Coordinate X received from the client.
     * @param receivedY Coordinate Y received from the client.
     * @return boolean value indicating with "true" that a valid move has been chosen by the client. false otherwise.
     */
    protected boolean verifyValidPosition(boolean[][] allowedPositions,int receivedX,int receivedY){
        if (receivedX <= 0 || receivedX > IslandBoard.TABLE_DIMENSION || receivedY <= 0 || receivedY > IslandBoard.TABLE_DIMENSION) return false;
        else return allowedPositions[receivedX - 1][receivedY - 1];
    }
    /**
     * Creates a worker instance in the position indicated by the player.
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
     * Checks if any of opponents of current player has a given power.
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
     * Get the network interface of one player using his username;
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
     * Gets a god by using his name
     * @param name Name of the God.
     * @return object of class God taken from the list of gods of the game.
     */
    protected God getGodByName(String name){
        for (God god:gameGodList) if (god.getName().equals(name)) return god;
        return null;
    }

    /**
     * Gets the worker in a selected Space
     * @param coordinateX is the X coordinate of the selected Space.
     * @param coordinateY is the Y coordinate of the selected Space.
     * @return the worker in desired place. The value is null in case no worker have been found in the space.
     */
    private Worker getWorkerByCoordinates(int coordinateX,int coordinateY){
        return currentGame.getGameBoard().getSpace(coordinateX,coordinateY).getWorkerInPlace();
    }
    /**
     * Gets the other worker of the player
     * @param player in the player considered
     * @param oneWorker is the worker we are considering
     * @return the other worker of the player
     */
    private Worker getPlayersOtherWorker(Player player,Worker oneWorker){
        if (player.getWorkers()[0] == oneWorker ) return player.getWorkers()[1];
        else return player.getWorkers()[0];
    }

    /**
     * Notifies to all players the winner of the game and disconnect them.
     * @param winnerUsername Username of the winner.
     */
    private void victory(String winnerUsername){
        for (VirtualView player:virtualViewsList) player.sendMessage(new WinnerNotification(winnerUsername));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {}
        for (VirtualView player:virtualViewsList) {
            player.closeConnection();
            try {
                Thread.sleep(200);
            } catch (InterruptedException ignored) {}
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {}
        moveAllowed = buildAllowed = running = false;
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
     * Removes a player from the game.
     * In case of two players game, the remained players wins the game, so the victory method is called in order to notify the winner.
     * In three players game the player is removed from the list of players but still remains in virtual view list and received updates about the status.
     * @param playerToRemove Player to remove from the game.
     * @param clientToRemove Virtual view of the player to remove.
     */
    protected void removePlayerFromGame(Player playerToRemove,VirtualView clientToRemove) {
        if (playerToRemove.getGod().getSinglePower(0) == Power.OPPONENTS_NOT_MOVE_UP_POWER) currentGame.setAthenaMovedUp(false); //disable Athena Power
        clientToRemove.setInGame(false);
        Player[] currentPlayers = currentGame.getPlayers();
        if (currentPlayers.length == 2) {
            if ((currentPlayers = currentGame.getPlayers())[0]==playerToRemove) victory(currentPlayers[1].getUsername());
            else victory(currentPlayers[0].getUsername());
            return;
        }

        Player[] newPlayers = new Player[currentPlayers.length - 1];
        int index = 0, newIndex = 0;
        for (; index < currentPlayers.length; index++){
            if (!playerToRemove.equals(currentPlayers[index])){
                newPlayers[newIndex] = currentPlayers[index];
                newIndex++;
            }
            else {
                if (index == 0){
                    currentGame.setCurrentPlayer(currentPlayers[2]);
                    currentGame.setStarterPlayer(currentPlayers[1].getUsername());
                }
                if (index == 1) {
                    currentGame.setCurrentPlayer(currentPlayers[index-1]);
                    currentGame.setStarterPlayer(currentPlayers[index+1].getUsername());
                }
                if (index == newPlayers.length){
                    currentGame.setCurrentPlayer(newPlayers[newPlayers.length-1]);
                    currentGame.setStarterPlayer(newPlayers[0].getUsername());
                }
            }
        }
        currentGame.setPlayers(newPlayers);
        removePlayersWorkers(playerToRemove);
    }

    /**
     * Removes the workers of a player from the game.
     * @param playerToRemove the player removed from the game.
     */
    protected void removePlayersWorkers(Player playerToRemove){
        Worker[] workersToRemove = playerToRemove.getWorkers();
        for (Worker workerToRemove : workersToRemove) workerToRemove.getWorkerPosition().setWorkerInPlace(null);
    }
    /**
     * Dequeue the messages waiting for a valid message.
     * If the message is a disconnection, the game is stopped.
     * @param senderVirtualView virtual view of the client from who the controller needs a message
     * @param messageIDs list of accepted messages.
     * @throws PlayerDisconnectedException When a disconnection is detected.
     */
    private void waitValidMessage(VirtualView senderVirtualView,int[] messageIDs) throws PlayerDisconnectedException {
        boolean receivedValidMessage;

        do {
            while((receivedMessage = senderVirtualView.dequeueFirstMessage()) == null) gameThreadWait();
            receivedValidMessage = checkMessageValidity(receivedMessage,messageIDs);
            if (receivedMessage.getMessageID() == Message.DISCONNECTION_MESSAGE){
                for (VirtualView player:virtualViewsList) player.closeConnection();
                running = false;}
        } while (!receivedValidMessage);
    }

    private void gameThreadWait() throws PlayerDisconnectedException {
        try {
            Thread.sleep(GameController.RESPONSE_MESSAGE_WAIT_TIMEOUT);
        } catch (InterruptedException e) {
            if (disconnectionDetected) throw new PlayerDisconnectedException();
        }
    }

    /**
     * Gets the worker selected by the player. If the selected worker is invalid, sends an error message to the client.
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
     * Creates the boolean matrix of possible moves for the second move of the player having Artemis god card.
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
     * Builds the matrix of positions where a player having Hestia can build the second time (using the power of the card)
     * @param workerX X coordinate of the moved worker.
     * @param workerY Y coordinate of the moved worker.
     * @return boolean matrix with "true" in positions the worker can build.
     */
    protected boolean[][] checkHestiaAllowedBuilds(int workerX, int workerY){
        boolean[][] allowedBuild = checkPossibleBuilds(workerX,workerY);//matrix is built using the regular rules of the game
        //now the matrix perimeter values are set to false;
        int tempX = 0, tempY;
        for (tempY = 0; tempY < IslandBoard.TABLE_DIMENSION; ++tempY) allowedBuild[tempX][tempY] = false;
        for (tempX = 0, tempY = IslandBoard.TABLE_DIMENSION - 1; tempX < IslandBoard.TABLE_DIMENSION; ++tempX) allowedBuild[tempX][tempY] = false;
        for (tempX = tempY = IslandBoard.TABLE_DIMENSION - 1; tempY > 0; --tempY) allowedBuild[tempX][tempY] = false;
        for (tempX = IslandBoard.TABLE_DIMENSION - 1,tempY = 0; tempX > 0; --tempX) allowedBuild[tempX][tempY] = false;
        return allowedBuild;
    }

    /**
     * Method used to get the current game Board
     * @return the current game board.
     */
    protected IslandBoard getCurrentBoard(){
        return currentGame.getGameBoard();
    }

    protected void setDisconnectionDetected(){
        disconnectionDetected = true;
    }
    //GOD POWER HANDLERS

    /**
     * Uses Apollo's power.
     * @param destSpaceWorker The worker to change position with.
     */
    protected void useApolloPower(Worker destSpaceWorker){
        destSpace.removeWorkerInPlace();
        moveWorker.changePosition(destSpace);
        destSpaceWorker.setWorkerPosition(startSpace);
        startSpace.setWorkerInPlace(destSpaceWorker);
    }

    /**
     * Uses Minotaur power.
     * @param destSpaceWorker Worker to push into the next space in move direction.
     * @param destX Coordinate X of the destination space.
     * @param destY Coordinate Y of the destination space.
     */
    protected void useMinotaurPower(Worker destSpaceWorker, int destX, int destY){
        int startX = startSpace.getCoordinateX(), startY = startSpace.getCoordinateY();
        Space pushIntoSpace = currentGame.getGameBoard().getSpace(destX + (destX - startX),destY + (destY - startY));
        destSpaceWorker.changePosition(pushIntoSpace);
        moveWorker.changePosition(destSpace);
    }

    /**
     * Implements Artemis power. It checks if the power can be used and then send a request to the client.
     * @param previousPosition the start position during first move of the worker selected by the player.
     * @throws PlayerDisconnectedException When a disconnection is detected.
     */
    private void handleArtemisPower(Space previousPosition) throws PlayerDisconnectedException {
        startSpace = moveWorker.getWorkerPosition();
        allowedMoves = possibleArtemisSecondMoveDestinations(previousPosition,startSpace);
        if (!workerCanMakeMove(allowedMoves)) return;
        currentClient.sendMessage(new UsePowerRequest());
        waitValidMessage(currentClient,new int[]{Message.USE_POWER_RESPONSE});
        if (((UsePowerResponse)receivedMessage).wantUsePower()) do{
            moveMade = false;
            currentClient.sendMessage(new MoveRequest(allowedMoves,false));
            waitValidMessage(currentClient,new int[]{Message.MOVE_RESPONSE});
            int destX = ((MoveResponse) receivedMessage).getDestCoordinateX(), destY = ((MoveResponse) receivedMessage).getDestCoordinateY();
            if (allowedMoves[destX-1][destY-1]){
                makeMove(destX,destY);
                notifyGameStatusToAll();
                checkWorkerMovedUp();
                if (moveVictoryConditionSatisfied()) victory(currentPlayer.getUsername());
            }
            else {
                currentClient.sendMessage(new InvalidMoveError());
                //currentClient.sendMessage(new MoveRequest(allowedMoves,false));
            }
        }while(!moveMade);
    }

    /**
     * Implements Demeter power. It checks if the power can be used and then sends the request to the player.
     * @param builtSpace The space the player has previously built in.
     * @throws PlayerDisconnectedException When a disconnection is detected.
     */
    protected void handleDemeterPower(Space builtSpace) throws PlayerDisconnectedException {
        int workerX = moveWorker.getWorkerPosition().getCoordinateX(), workerY = moveWorker.getWorkerPosition().getCoordinateY();
        allowedBuilds = checkPossibleBuilds(workerX,workerY);
        allowedBuilds[builtSpace.getCoordinateX() - 1][builtSpace.getCoordinateY() - 1] = false;
        if(!workerCanMakeMove(allowedBuilds)) return;
        secondBuildMake(allowedBuilds,currentClient);
        notifyGameStatusToAll();
    }

    /**
     * Implements Hephaestus power. If the power can be used it sends to player a request.
     * If the player wants to use the power, the height of the block previously built increments by one block.
     * @param BuildSpace Space the player previously has built in.
     * @throws PlayerDisconnectedException When a disconnection is detected.
     */
    protected void handleHephaestusPower(Space BuildSpace) throws PlayerDisconnectedException {
        if (BuildSpace.getHeight() >= Space.DOME_LEVEL - 1) return;
        currentClient.sendMessage(new UsePowerRequest());
        waitValidMessage(currentClient, new int[]{Message.USE_POWER_RESPONSE});
        if (!((UsePowerResponse)receivedMessage).wantUsePower()) return;
        BuildSpace.incrementHeight();
        notifyGameStatusToAll();
    }

    /**
     * Verifies if a player has Pan's power then if the move made is winning.
     * @return "true" if the move verifies Pan's win condition, "false" otherwise.
     */
    protected boolean isPanVictoryMove(){
        return (currentPlayerHasPower(Power.TWO_BLOCK_FALL_VICTORY_POWER) && (startSpace.getHeight() - destSpace.getHeight() >= 2));
    }

    /**
     * Implements Prometheus power. It checks if the player has the power and if it's possible to use it.
     * In this case, with Prometheus the power is usable only if the power does not lead to loose the game.
     * @throws PlayerDisconnectedException When a disconnection is detected.
     */
    protected void handlePrometheusPower() throws PlayerDisconnectedException {
        //if the player has not got the power, the method ends
        if(!currentPlayerHasPower(Power.NOT_MOVE_UP_DOUBLE_BUILD_POWER)) return;
        //check if at least one worker can build before moving without locking himself. If moves are not possible, the method ends.
        if (!checkPossiblePreBuild()) return;
        notifyGameStatusToAll();
        currentClient.sendMessage(new UsePowerRequest());
        waitValidMessage(currentClient,new int[]{Message.USE_POWER_RESPONSE});
        //if player does not want to use the power, the method ends.
        if(!((UsePowerResponse)receivedMessage).wantUsePower()) return;
        //The worker for the turn is selected and the the Build request is sent.
        do{
            currentClient.sendMessage(new SelectWorkerRequest());
            waitValidMessage(currentClient,new int[]{Message.SELECT_WORKER_RESPONSE});
            moveWorker = getWorkerByCoordinates(((SelectWorkerResponse)receivedMessage).getCoordinateX(),((SelectWorkerResponse)receivedMessage).getCoordinateY());
        } while (moveWorker == null || !moveWorker.getOwner().equals(currentPlayer.getUsername()));

        startSpace = moveWorker.getWorkerPosition();
        boolean[][] allowedBuild = checkPossibleBuilds(startSpace.getCoordinateX(),startSpace.getCoordinateY());
        do{
            currentClient.sendMessage(new BuildRequest(allowedBuild));
            waitValidMessage(currentClient,new int[]{Message.BUILD_RESPONSE});
        }while(!allowedBuild[(((BuildResponse)receivedMessage).getBuildCoordinateX()-1)][(((BuildResponse)receivedMessage).getBuildCoordinateY()-1)]);
        Space buildSpace = gameBoard.getSpace(((BuildResponse)receivedMessage).getBuildCoordinateX(),((BuildResponse)receivedMessage).getBuildCoordinateY());
        blockAddInSpace(buildSpace);
        notifyGameStatusToAll();
        canMoveUp = canSelectWorker = false;
        handleCronusPower();

    }

    /**
     * Implements Ares power. It checks if the player has the power, then if it's possible to use it,
     * if yes it asks the player to use the power and finally removes the block from selected space.
     * @throws PlayerDisconnectedException When a disconnection is detected.
     */
    protected void handleAresPower() throws PlayerDisconnectedException {
        //if the player has not Ares Power, the method stops immediately.
        if (!currentPlayerHasPower(Power.BLOCK_REMOVE_POWER)||!currentClient.getInGame()) return;
        //getting the unmoved worker and checking possible moves
        Worker unmovedWorker = getPlayersOtherWorker(currentPlayer,moveWorker);
        int unmovedX = unmovedWorker.getWorkerPosition().getCoordinateX(), unmovedY = unmovedWorker.getWorkerPosition().getCoordinateY();
        boolean[][] allowedRemovals = checkPossibleRemovals(unmovedX,unmovedY);
        //if there are not possible moves, the method ends.
        if (!workerCanMakeMove(allowedRemovals)) return;
        currentClient.sendMessage(new UsePowerRequest());
        waitValidMessage(currentClient, new int[]{Message.USE_POWER_RESPONSE});
        //if player does not want to use the power, the method ends.
        if (!((UsePowerResponse)receivedMessage).wantUsePower()) return;
        //sending the block removal request
        int removeX, removeY;
        do {
            currentClient.sendMessage(new BlockRemovalRequest(allowedRemovals));
            waitValidMessage(currentClient, new int[]{Message.BLOCK_REMOVAL_RESPONSE});
            //removing a block from the selected place and then notify the game status to all players.
            removeX = ((BlockRemovalResponse) receivedMessage).getRemoveCoordinateX();
            removeY = ((BlockRemovalResponse) receivedMessage).getRemoveCoordinateY();
        } while (!allowedRemovals[removeX-1][removeY-1]);
        gameBoard.getSpace(removeX,removeY).decrementHeight();

        notifyGameStatusToAll();
    }

    /**
     * Checks if a player has Cronus god card and the if the victory condition is satisfied.
     * In case the condition is satisfied it calls victory method.
     */
    protected void handleCronusPower(){
        if ((opponentHasPower(Power.FIVE_TOWER_VICTORY_POWER)||currentPlayerHasPower(Power.FIVE_TOWER_VICTORY_POWER))&&(gameBoard.getNumberCompleteTowers()>=5))
            for (Player player : currentGame.getPlayers()) if (player.getGod().getSinglePower(0) == Power.FIVE_TOWER_VICTORY_POWER) victory(player.getUsername());
    }

    /**
     * Checks if the opponent has Hera's power and then verifies that the power can be used.
     * @return "true" value if a victory can be denied the Hera power, "false" otherwise.
     */
    protected boolean victoryDeniedByHeraPower(){
        return (opponentHasPower(Power.PERIMETER_VICTORY_DENY_POWER) && destSpace.isPerimeter());
    }

    /**
     * Implements Hestia power. Checks if the power can be used and send a power usage request.
     * @throws PlayerDisconnectedException When a disconnection is detected.
     */
    protected void handleHestiaPower() throws PlayerDisconnectedException {
        int workerX = moveWorker.getWorkerPosition().getCoordinateX(), workerY = moveWorker.getWorkerPosition().getCoordinateY();
        allowedBuilds = checkHestiaAllowedBuilds(workerX, workerY);
        if(workerCanMakeMove(allowedBuilds)) secondBuildMake(allowedBuilds,currentClient);
        notifyGameStatusToAll();
    }

    //POWERS AUXILIARY METHODS

    /**
     * Handle the double build powers. Receives a matrix with allowed moves
     * and asks the player if he want to use the power. If affirmative answer has been receives, the normal build process is made.
     * @param allowedBuild matrix with allowed build positions.
     * @param client virtual view of the player.
     * @throws PlayerDisconnectedException When a disconnection is detected.
     */
    private void secondBuildMake(boolean[][] allowedBuild,VirtualView client) throws PlayerDisconnectedException {
        if (workerCanMakeMove(allowedBuild)){
            client.sendMessage(new UsePowerRequest());
            waitValidMessage(client,new int[]{Message.USE_POWER_RESPONSE});
            if (((UsePowerResponse)receivedMessage).wantUsePower()){
                client.sendMessage(new BuildRequest(allowedBuild));
                waitValidMessage(client,new int[]{Message.BUILD_RESPONSE});
                int buildX = ((BuildResponse) receivedMessage).getBuildCoordinateX(), buildY = ((BuildResponse) receivedMessage).getBuildCoordinateY();
                if (allowedBuild[buildX-1][buildY-1]) blockAddInSpace(gameBoard.getSpace(buildX,buildY));
                else client.sendMessage(new InvalidMoveError());
            }
        }
    }

    /**
     * Builds the matrix of possible blocks can be removed by Ares Power.
     * @param workerX Coordinate X of the unmoved worker.
     * @param workerY Coordinate Y of the unmoved worker.
     * @return boolean matrix indicating with true the spaces where a block can be removed, false otherwise.
     */
    protected boolean [][] checkPossibleRemovals(int workerX,int workerY){
        boolean[][] removals = initializeMatrix(false);
        for (int X = workerX - 2; X < workerX + 1;++ X){
            for (int Y = workerY - 2; Y < workerY + 1;++Y){
                if((X >= 0 && X < IslandBoard.TABLE_DIMENSION) && (Y >= 0 && Y < IslandBoard.TABLE_DIMENSION)){
                    int currentHeight = gameBoard.getSpace(X+1,Y+1).getHeight();
                    boolean hasDome = gameBoard.getSpace(X+1,Y+1).getHasDome();
                    Worker workerInPlace = gameBoard.getSpace(X+1,Y+1).getWorkerInPlace();
                    if((currentHeight != 0)&&(!hasDome)&& workerInPlace == null) removals[X][Y] = true;
                }
            }
        }
        return removals;
    }

    /**
     * Checks if a player having Prometheus god card can use the power without loosing the game.
     * @return False if the use of the power leads to loosing the game, true otherwise.
     */
    protected boolean checkPossiblePreBuild(){
        for (Worker worker:currentPlayer.getWorkers()) {
            Space workerPosition = worker.getWorkerPosition();
            int workerX = workerPosition.getCoordinateX(), workerY = workerPosition.getCoordinateY();
            allowedMoves = checkPossibleMoves(worker.getWorkerPosition().getCoordinateX(),worker.getWorkerPosition().getCoordinateY());
            allowedBuilds = checkPossibleBuilds(worker.getWorkerPosition().getCoordinateX(),worker.getWorkerPosition().getCoordinateY());
            int possibleMoves = countPossibleMoves(allowedMoves);
            if (possibleMoves == 0) break;
            if (possibleMoves == 1){
                int[] indexes = getOnlyMoveIndexes(allowedMoves);
                int i = indexes[0], j = indexes[1];
                int possibleBuilds = countPossibleMoves(allowedBuilds);
                if (possibleBuilds == 1) {
                    int[] buildIndexes = getOnlyMoveIndexes(allowedBuilds);
                    int f = buildIndexes[0], g = buildIndexes[1];
                    if (f == i && g == j && (gameBoard.getSpace(workerX,workerY).getHeight() >= gameBoard.getSpace(f+1,g+1).getHeight()+1)) return true;
                }
            }
            else return true;
        }
        return false;
    }
}