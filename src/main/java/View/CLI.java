package View;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Scanner;

import Model.*;


public class CLI implements UI {

    int i;
    int j;
    Scanner input;
    String circle= "\uD83D\uDD35";
    String isDome;
    int n=1;
    String username;
    int workersColor;
    String godName;
    String color;

    public CLI() {
    }



    @Override
    public void gameInfo() {
        System.out.println("Welcome in Santorini!");
        System.out.println("You will have to choose a deity card, which has a special power, that you can use in the game. You will also have 2 workers.");
        System.out.println("During your turn you can move: the movement can only be in the boxes directly adjacent to your position.");
        System.out.println("Then you can build, there are 4 types of blocks: level 1, level 2, level 3 and dome 4.");
        System.out.println("It can happen that a dome is built at any level, in that case it will be indicated with a C and flanked by the level below.");
        System.out.println("");
    }

    @Override
    public void chooseServerAddress() {
        System.out.println("Enter the address of the server you want to connect to in order to play: ");
    }

    @Override
    public void chooseUsername() {
        System.out.println("Choose your username! It must be a single word.");
    }

    @Override
    public void chooseNumPlayers() {
        System.out.println("How many players do you want in the game? Choose by typing: 2 or 3");
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
    public void showGodList(ArrayList<God> gods, int numPlayers) {
        System.out.println("Choose " + numPlayers + " cards among those available.");
        for (God god : gods) {
            System.out.println(god.getName() + ", " + god.getDescription());
        }
    }

    @Override
    public void printAllPlayers(Player[] players, String username) {
        System.out.print("Choose among these players which will start: ");
        for (Player player : players) {
            if(!(username.equals(player.getUsername())))
                System.out.print(player.getUsername() + " ");
        }
        System.out.println("");
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
    }

    @Override
    public void printLastGod(ArrayList<God> godList, God lastGod) {
        System.out.println("Already selected  gods (not available): ");
        for (God god : godList) {
            System.out.print(god.getName() + "   ");
        }
        System.out.println("");
        System.out.println("Players have chosen their deities. Your deity is " + lastGod.getName());
    }


    @Override
    public void chooseWorker() {
        System.out.println("Choose the coordinates of the worker you want to move: ");
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
    public void moveWorker() {
        System.out.println("Choose which space you want to move your worker by stating the numerical coordinates x and y.");
    }


    @Override
    public void otherWorker() {
        System.out.println("The chosen worker couldn't move, select a move for the other worker: ");
    }


    @Override
    public void printPossibleAction(boolean[][] allowed) {
        System.out.println("Possible moves (in coordinates):");
        for (i = 0; i < 5; i++) {
            for (j = 0; j < 5; j++) {
                if (allowed[i][j])
                    System.out.println("(" + (i + 1) + "," + (j + 1) + "), ");
            }
        }
    }

    @Override
    public void buildTower() {
        System.out.println("Choose in which space you want to build, stating the numerical coordinates x and y:");
    }


    @Override
    public boolean askPowerUsage() {
        System.out.println("Do you want to use the power of your god? Reply y o n");
        input = new Scanner(System.in);
        String choice = input.nextLine();
        return "y".equals(choice);
    }


    @Override
    public void noPossibleMoves() { System.out.println("You don’t have a chance to move anymore! I’m sorry, you lost."); }

    @Override
    public void chooseRemoval() {
        System.out.println("Enter the coordinates of the block you want to remove: ");
    }

    @Override
    public void invalidMove() {
        System.out.println("Move not allowed!");
    }

    @Override
    public void printCurrentStatus(Game updatedGame){

        for(i=0; i<updatedGame.getPlayers().length; i++) {
            username = (updatedGame.getPlayers())[i].getUsername();
            godName = (updatedGame.getPlayers())[i].getGod().getName();
            workersColor = (updatedGame.getPlayers())[i].getUserID();
            if (workersColor == 1)
                color = "yellow";
            else if (workersColor == 2)
                color = "white ";
            else if (workersColor == 3)
                color = "blue  ";

            System.out.println("username: " + username);
            System.out.println("color of workers: " + color);
            System.out.println("god: " + godName);
            System.out.println("");
        }
        System.out.println("");
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
                if(checkedDome==true)
                    isDome="C";
                else
                    isDome="_";

                if (checkedWorker != null && checkedHeight != 0) {
                    int workerColor = checkedWorker.getColor();
                    if (workerColor == 1) {
                        if (i == 4)
                            System.out.print("|" + checkedHeight + isDome + "_" + Color.ANSI_BRIGHT_YELLOW + circle + Color.RESET + "|");
                        else
                            System.out.print("|" + checkedHeight + isDome + "_" + Color.ANSI_BRIGHT_YELLOW + circle + Color.RESET);
                    } else if (workerColor == 2) {
                        if (i == 4)
                            System.out.print("|" + checkedHeight + isDome + "_" + Color.ANSI_WHITE + circle + Color.RESET + "|");
                        else
                            System.out.print("|" + checkedHeight + isDome + "_" + Color.ANSI_WHITE + circle + Color.RESET);
                    } else if (workerColor == 3) {
                        if (i == 4)
                            System.out.print("|" + checkedHeight + isDome + "_" + Color.ANSI_BLUE + circle + Color.RESET + "|");
                        else
                            System.out.print("|" + checkedHeight + isDome + "_" + Color.ANSI_BLUE + circle + Color.RESET);
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
                            System.out.print("|__" + isDome + Color.ANSI_BRIGHT_YELLOW + circle + Color.RESET + "|");
                        else
                            System.out.print("|__" + isDome + Color.ANSI_BRIGHT_YELLOW + circle + Color.RESET);
                    } else if (workerColor == 2) {
                        if (i == 4)
                            System.out.print("|__" + isDome + Color.ANSI_WHITE + circle + Color.RESET + "|");
                        else
                            System.out.print("|__" + isDome + Color.ANSI_WHITE + circle + Color.RESET);
                    } else if (workerColor == 3) {
                        if (i == 4)
                            System.out.print("|__" + isDome + Color.ANSI_BLUE + circle + Color.RESET + "|");
                        else
                            System.out.print("|__" + isDome + Color.ANSI_BLUE + circle + Color.RESET );
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
