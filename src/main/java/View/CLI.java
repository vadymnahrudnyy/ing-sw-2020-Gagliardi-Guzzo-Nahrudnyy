package View;


import java.util.ArrayList;
import java.util.Scanner;

import Client.Client;
import Client.NetworkHandler;
import Messages.*;
import Model.*;



public class CLI implements UI {

    private static int numPlayers;
    private static String username;
    private Scanner input;
    private String redCircle= "\uD83D\uDD34";
    private String blackCircle= "\u2B24";
    private String bluCircle= "\uD83D\uDD35";
    int n=1;
    private String color;
    boolean isUsed;


    public CLI(){
    }




    @Override
    public void gameInfo() {
        System.out.println("Welcome in Santorini!");
        System.out.println("You will have to choose a deity card, which has a special power, that you can use in the game. You will also have 2 workers.");
        System.out.println("During your turn you can move: the movement can only be in the boxes directly adjacent to your position.");
        System.out.println("Then you can build, there are 4 types of blocks: level 1, level 2, level 3 and dome 4.");
        System.out.println("It can happen that a dome is built at any level, in that case it will be indicated with a D and flanked by the level below.");
        System.out.println("");
    }

    @Override
    public void chooseServerAddress() {
        String serverAddress;
        do{
            System.out.println("Enter the address of the server you want to connect to in order to play: ");
            input=new Scanner(System.in);
            serverAddress= input.nextLine();
        } while(serverAddress==null);
        Client.setServerAddress(serverAddress);
    }

    @Override
    public void errorServerAddress(){
        System.out.println("Server Address is not valid!");
        chooseServerAddress();
    }

    @Override
    public void chooseUsername() {
        System.out.println("Choose your username! It must be a single word.");
        input=new Scanner(System.in);
        username= input.nextLine();
        Client.setUsername(username);
        NetworkHandler.sendMessage(new UsernameResponse(username));
    }

    @Override
    public void usernameError() {
        System.out.println("Username already chosen by another player!");
    }

    @Override
    public void chooseNumPlayers() {
        System.out.println("How many players do you want in the game? Choose by typing: 2 or 3");
        input = new Scanner(System.in);
        numPlayers = input.nextInt();
        while(numPlayers != 2 && numPlayers != 3) {
            System.out.println("Invalid number of players selected. Please try again.");
            input = new Scanner(System.in);
            numPlayers = input.nextInt();
        }
        Client.setNumPlayer(numPlayers);
        NetworkHandler.sendMessage(new NumPlayersResponse(numPlayers));

    }

    @Override
    public void printLobbyStatus(int selectedLobby, int slotsOccupied) {
        System.out.println("Lobby and number of players currently in: ");
    }

    @Override
    public void startNotification() {
        System.out.println("Full lobby: the game can begin, good luck!");
        System.out.println("");
    }

    @Override
    public void showGodList(ArrayList<God> gods) {
        System.out.println("Choose " + numPlayers + " cards among those available.");
        for (God god : gods) {
            System.out.println(god.getName() + ", " + god.getDescription());
        }
    }


    @Override
    public void chooseGameGods() {
        ArrayList<String> chosenGods = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++) {
            input = new Scanner(System.in);
            String chosen = input.nextLine();
            chosenGods.add(chosen);
        }
        NetworkHandler.sendMessage(new GodsListResponse(chosenGods));
    }

    @Override
    public void printAllPlayers(Player[] players) {
        System.out.print("Choose among these players which will start: ");
        for (Player player : players) {
            if(!(username.equals(player.getUsername())))
                System.out.print(player.getUsername() + " ");
        }
        System.out.println("");
    }

    @Override
    public void chooseFirstPlayer() {
        input=new Scanner(System.in);
        String firstPlayer=input.nextLine();
        NetworkHandler.sendMessage(new StartPlayerResponse(firstPlayer));
    }


    @Override
    public void playerError() {
        System.out.println("Attention! The selected player is invalid.");
    }

    @Override
    public void chooseGod(ArrayList<God> godList, ArrayList<God> unavailableList) {
        System.out.println("These are all the gods chosen for the match: ");
        for (God god : godList) {
            System.out.print(god.getName() + "   ");
        }
        System.out.println("");
        System.out.println("Already selected  gods (not available): ");
        for (God god : unavailableList) {
            System.out.print(god.getName() + ", ");
        }

        System.out.println("You have to choose one and avoid the ones already chosen.");
        input = new Scanner(System.in);
        String chosen = input.nextLine();
        NetworkHandler.sendMessage(new ChoseGodResponse(chosen));
    }


    @Override
    public void godChoiceError() {
        System.out.println("Attention! You did not write the name of the god correctly (the first letter must be capitalized).");
    }

    @Override
    public void showLastGod(ArrayList<God> godList, God lastGod) {
        System.out.println("Already selected  gods (not available): ");
        for (God god : godList) {
            System.out.print(god.getName() + "   ");
        }
        System.out.println("");
        System.out.println("Players have chosen their deities. Your deity is " + lastGod.getName());
    }


    @Override
    public void placeWorkerInSpace(int currentWorker, boolean[][] allowedPosition){
        System.out.println("Choose the position of the worker " + currentWorker);
        input = new Scanner(System.in);
        System.out.println("Insert coordinate X: ");
        int x = input.nextInt();
        System.out.println("Insert coordinate Y: ");
        int y = input.nextInt();
        while (((x < 0||y < 0)||(x > IslandBoard.TABLE_DIMENSION || y > IslandBoard.TABLE_DIMENSION))||(!allowedPosition[x-1][y-1])){
            System.out.println("Position is not valid.");
            input = new Scanner(System.in);
            System.out.println("Insert coordinate X: ");
            x = input.nextInt();
            System.out.println("Insert coordinate Y: ");
            y = input.nextInt();
        }

        NetworkHandler.sendMessage(new WorkerPositionResponse(x, y));

    }

    @Override
    public void chooseWorker() {
        System.out.println("Choose the coordinates of the worker you want to move: ");
        input=new Scanner(System.in);
        int x=input.nextInt();
        int y=input.nextInt();

        NetworkHandler.sendMessage(new SelectWorkerResponse(x, y));
    }

    @Override
    public void workerChosenError() {
        System.out.println("The chosen worker is not valid.");
    }

    @Override
    public boolean confirmChoice() {
        System.out.println("Do you confirm that you want to move the chosen worker? Reply y or n.");
        input = new Scanner(System.in);
        String choice = input.nextLine();
        if ("n".equals(choice)) {
            System.out.print("Enter the coordinates of the worker you want to choose: ");
            return true;
        } else
            return false;
    }

    @Override
    public void moveWorker(boolean[][] allowedMoves) {
        System.out.println("Choose which space you want to move your worker by stating the numerical coordinates x and y.");
        input=new Scanner(System.in);
        int x=input.nextInt();
        int y=input.nextInt();

        while((x<0||y<0)||(x>IslandBoard.TABLE_DIMENSION||y>IslandBoard.TABLE_DIMENSION)||(!allowedMoves[x - 1][y - 1])) {
            System.out.println("Space already occupied! Choose other coordinates!");
            input = new Scanner(System.in);
            x = input.nextInt();
            y = input.nextInt();
        }
        NetworkHandler.sendMessage(new MoveResponse(x, y));
    }


    @Override
    public void otherWorker() {
        System.out.println("The chosen worker couldn't move, select a move for the other worker: ");
    }


    @Override
    public void moveOtherWorker(boolean[][] allowedMoves) {
        input=new Scanner(System.in);
        int x=input.nextInt();
        int y=input.nextInt();

        while(!allowedMoves[x - 1][y - 1]) {
            System.out.println("Space already occupied! Choose other coordinates!");
            input = new Scanner(System.in);
            x = input.nextInt();
            y = input.nextInt();
        }
        NetworkHandler.sendMessage(new MoveResponse(x,y));
    }


    @Override
    public void printPossibleAction(boolean[][] allowed) {
        System.out.println("Possible moves (in coordinates):");
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (allowed[i][j])
                    System.out.println("(" + (i + 1) + "," + (j + 1) + "), ");
            }
        }
    }


    @Override
    public void changeWorker(boolean canChangeWorker){

        if(canChangeWorker && confirmChoice()) {
            input = new Scanner(System.in);
            int x = input.nextInt();
            int y = input.nextInt();
            NetworkHandler.sendMessage(new SelectWorkerResponse(x, y));
        }
    }


    @Override
    public void buildTower(boolean[][] allowedBuild) {
        System.out.println("Choose in which space you want to build, stating the numerical coordinates x and y:");
        input = new Scanner(System.in);
        int x = input.nextInt();
        int y = input.nextInt();

        while(!allowedBuild[x - 1][y - 1]) {
            System.out.println("You can't build here! Choose other coordinates!");
            input = new Scanner(System.in);
            x = input.nextInt();
            y = input.nextInt();
        }
        NetworkHandler.sendMessage(new BuildResponse(x,y));
    }


    @Override
    public void askPowerUsage() {
        System.out.println("Do you want to use the power of your god? Reply y o n");
        input = new Scanner(System.in);
        String choice = input.nextLine();
        isUsed= "y".equals(choice);

        NetworkHandler.sendMessage(new UsePowerResponse(isUsed));
    }


    @Override
    public void noPossibleMoves() { System.out.println("You don’t have a chance to move anymore! I’m sorry, you lost."); }

    @Override
    public void chooseRemoval(boolean[][] allowedToRemove) {
        System.out.println("Enter the coordinates of the block you want to remove: ");
        input = new Scanner(System.in);
        int x = input.nextInt();
        int y = input.nextInt();

        while(!allowedToRemove[x - 1][y - 1]) {
            System.out.println("Removal not allowed! Choose other coordinates.");
            input = new Scanner(System.in);
            x = input.nextInt();
            y = input.nextInt();
        }
        NetworkHandler.sendMessage(new BlockRemovalResponse(x,y));
    }

    @Override
    public void invalidMove() {
        System.out.println("Move not allowed!");
    }

    @Override
    public void printCurrentStatus(Game updatedGame){
        System.out.println("");
        for(int i=0; i<updatedGame.getPlayers().length; i++) {
            String playerUsername = (updatedGame.getPlayers())[i].getUsername();
            String godName = (updatedGame.getPlayers())[i].getGod().getName();
            int workersColor = (updatedGame.getPlayers())[i].getUserID();
            if (workersColor == 1)
                color = "red";
            else if (workersColor == 2)
                color = "black";
            else if (workersColor == 3)
                color = "blue";

            System.out.println("username: " + playerUsername);
            System.out.println("color of workers: " + color);
            System.out.println("god: " + godName);
            System.out.println("");
        }
    }

    @Override
    public void printCurrentBoard(Game updatedGame) {
        System.out.println("  X    1     2     3     4     5  ");
        System.out.println("Y    ______________________________");
        for (int j = 0; j < IslandBoard.TABLE_DIMENSION; j++){
            System.out.print(n +"   ");
            for (int i = 0; i < IslandBoard.TABLE_DIMENSION; i++) {
                Worker checkedWorker = updatedGame.getGameBoard().getSpace(i + 1, j + 1).getWorkerInPlace();
                int checkedHeight = updatedGame.getGameBoard().getSpace(i + 1, j + 1).getHeight();
                boolean checkedDome =  updatedGame.getGameBoard().getSpace(i + 1, j + 1).getHasDome();
                String isDome;
                if(checkedDome)
                    isDome ="D";
                else
                    isDome ="_";

                if (checkedWorker != null && checkedHeight != 0) {
                    int workerColor = checkedWorker.getColor();
                    if (workerColor == 1) {
                        if (i == 4)
                            System.out.print("|" + checkedHeight + isDome + "_" + Color.ANSI_RED + redCircle + Color.RESET + "|");
                        else
                            System.out.print("|" + checkedHeight + isDome + "_" + Color.ANSI_RED + redCircle + Color.RESET);
                    } else if (workerColor == 2) {
                        if (i == 4)
                            System.out.print("|" + checkedHeight + isDome + "_" + Color.ANSI_BLACK + blackCircle + Color.RESET + "|");
                        else
                            System.out.print("|" + checkedHeight + isDome + "_" + Color.ANSI_BLACK + blackCircle + Color.RESET);
                    } else if (workerColor == 3) {
                        if (i == 4)
                            System.out.print("|" + checkedHeight + isDome + "_" + Color.ANSI_BLUE + bluCircle + Color.RESET + "|");
                        else
                            System.out.print("|" + checkedHeight + isDome + "_" + Color.ANSI_BLUE + bluCircle + Color.RESET);
                    }
                }

                else if (checkedHeight != 0) {
                    if (i == 4)
                        System.out.print("|_" + checkedHeight + "_" + isDome + "_|");
                    else
                        System.out.print("|_" + checkedHeight + "_" + isDome + "_");
                }

                else if (checkedWorker != null) {
                    int workerColor = checkedWorker.getColor();
                    if (workerColor == 1) {
                        if (i == 4)
                            System.out.print("|__" + isDome + Color.ANSI_RED + redCircle + Color.RESET + "|");
                        else
                            System.out.print("|__" + isDome + Color.ANSI_RED + redCircle + Color.RESET);
                    } else if (workerColor == 2) {
                        if (i == 4)
                            System.out.print("|__" + isDome + Color.ANSI_BLACK + blackCircle + Color.RESET + "|");
                        else
                            System.out.print("|__" + isDome + Color.ANSI_BLACK + blackCircle + Color.RESET);
                    } else if (workerColor == 3) {
                        if (i == 4)
                            System.out.print("|__" + isDome + Color.ANSI_BLUE + bluCircle + Color.RESET + "|");
                        else
                            System.out.print("|__" + isDome + Color.ANSI_BLUE + bluCircle + Color.RESET );
                    }
                }

                else {
                    if (i == 4)
                        System.out.print("|_____|");
                    else
                        System.out.print("|_____");
                }
            }
            n++;
            System.out.println("");
        }
        n=1;
        System.out.println("");
    }

    @Override
    public void isWinner(String winner) {
        System.out.println("Game over! The winner is: " + winner);
    }

}
