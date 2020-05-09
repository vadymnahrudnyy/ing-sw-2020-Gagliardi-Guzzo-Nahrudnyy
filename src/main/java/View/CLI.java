package View;

import java.util.ArrayList;
import java.util.Scanner;

import Model.*;


public class CLI implements UI {

    int i;
    int j;
    Scanner input;
    String circle= "\uD83D\uDD35";

    public CLI() {
    }



    @Override
    public void gameInfo() {
        System.out.println("Benvenuto in Santorini!");
        System.out.println("Dovrai scegliere una carta divinità, la quale possiede un potere particolare, che potrai utilizzare nel gioco. Inoltre disporrai di 2 worker.");
        System.out.println("Durante il tuo turno potrai muoverti: il movimento può essere solo nelle caselle direttamente adiacenti alla tua posizione.");
        System.out.println("Dopodiché potrai costruire, ci sono 4 tipo di blocchi: livello 1, livello 2, livello 3 e cupola 4.");
        System.out.println("");
    }

    @Override
    public void chooseServerAddress() {
        System.out.println("Inserisci l'indirizzo del server a cui vuoi collegarti per giocare: ");
    }

    @Override
    public void chooseUsername() {
        System.out.println("Scegli il tuo username! Deve essere un'unica parola.");
    }

    @Override
    public void chooseNumPlayers() {
        System.out.println("Quanti giocatori desideri che abbia la partita? Scegli digitando: 2 o 3");
    }

    @Override
    public void printLobbyStatus(int selectedLobby, int slotsOccupied) {
        System.out.println("Lobby e numero di giocatori attualmente presenti:");
    }

    @Override
    public void startNotification() {
        System.out.println("Lobby piena: il gioco può cominciare, buona partita!");
        System.out.println("");
    }

    @Override
    public void showGodList(ArrayList<God> gods, int numPlayers) {
        System.out.println("Scegli " + numPlayers + " carte tra quelle disponibili.");
        for (God god : gods) {
            System.out.println(god.getName() + ", " + god.getDescription());
        }
    }

    @Override
    public void printAllPlayers(Player[] players, String username) {
        System.out.print("Scegli tra questi giocatori quale dovrà iniziare: ");
        for (Player player : players) {
            if(!(username.equals(player.getUsername())))
                System.out.print(player.getUsername() + " ");
        }
    }

    @Override
    public void chooseGod(ArrayList<God> godList, ArrayList<God> unavailableList) {
        System.out.println("Questi sono tutti gli dei scelti per la partita: ");
        for (God god : godList) {
            System.out.print(god.getName() + "   ");
        }
        System.out.println("");
        System.out.println("Dei già scelti (non disponibili): ");
        for (God god : unavailableList) {
            System.out.print(god.getName() + ", ");
        }

        System.out.println("Devi sceglierne uno evitando quelli già scelti.");
    }

    @Override
    public void printLastGod(ArrayList<God> godList, God lastGod) {
        System.out.println("Questi sono tutti gli dei che hai scelto per la partita: ");
        for (God god : godList) {
            System.out.print(god.getName() + "   ");
        }
        System.out.println("");
        System.out.println("I giocatori hanno scelto le loro divinità. La tua divinità è " + lastGod.getName());
    }


    @Override
    public void chooseWorker() {

        System.out.println("Scegli le coordinate del worker che desideri muovere: ");
    }

    @Override
    public boolean confirmChoice() {
        System.out.println("Confermi di voler muovere il worker scelto? Rispondi y o n. ");
        input = new Scanner(System.in);
        String choice = input.nextLine();
        if ("n".equals(choice)) {
            System.out.print("Inserisci le coordinate del worker che vuoi scegliere: ");
            return true;
        } else
            return false;
    }

    @Override
    public void moveWorker() {
        System.out.println("Scegli in quale casella vuoi spostare il tuo worker indicando le coordinate numeriche x e y.");
    }


    @Override
    public void otherWorker() {
        System.out.println("Il lavoratore scelto non poteva muoversi, seleziona una mossa per l'altro worker: ");
    }


    @Override
    public void printPossibleAction(boolean[][] allowed) {
        System.out.println("Mosse possibili (in coordinate): ");
        for (i = 0; i < 5; i++) {
            for (j = 0; j < 5; j++) {
                if (allowed[i][j])
                    System.out.println("(" + (i + 1) + "," + (j + 1) + "), ");
            }
        }
    }

    @Override
    public void buildTower() {
        System.out.println("Scegli in quale casella vuoi costruire, indicando le coordinate numeriche x e y:");
    }


    @Override
    public boolean askPowerUsage() {
        System.out.println("Vuoi usare il potere del tuo dio? Rispondi y o n");
        input = new Scanner(System.in);
        String choice = input.nextLine();
        return "y".equals(choice);
    }


    @Override
    public void noPossibleMoves() {
        System.out.println("Non hai più possibilità di muoverti! Mi dispiace, hai perso.");
    }


    @Override
    public void chooseRemoval() {
        System.out.println("Inserisci le coordinate del blocco che vuoi rimuovere: ");
    }

    @Override
    public void invalidMove() {
        System.out.println("Mossa non consentita!");
    }

    @Override
    public void printCurrentBoard(Game updatedGame) {
        System.out.println(" ______________________________");
        for (int j = 0; j < IslandBoard.TABLE_DIMENSION; j++){
            for (int i = 0; i < IslandBoard.TABLE_DIMENSION; i++) {
                Worker checkedWorker = updatedGame.getGameBoard().getSpace(i + 1, j + 1).getWorkerInPlace();
                int checkedHeight = updatedGame.getGameBoard().getSpace(i + 1, j + 1).getHeight();

                if (checkedWorker != null && checkedHeight != 0) {
                    int workerColor = checkedWorker.getColor();
                    if (workerColor == 1) {
                        if (i == 4)
                            System.out.print("|" + checkedHeight + "__" + Color.ANSI_BRIGHT_YELLOW + circle + Color.RESET + "|");
                        else
                            System.out.print("|" + checkedHeight + "__" + Color.ANSI_BRIGHT_YELLOW + circle + Color.RESET);
                    } else if (workerColor == 2) {
                        if (i == 4)
                            System.out.print("|" + checkedHeight + "__" + Color.ANSI_BLUE + circle + Color.RESET + "|");
                        else
                            System.out.print("|" + checkedHeight + "__" + Color.ANSI_BLUE + circle + Color.RESET);
                    } else if (workerColor == 3) {
                        if (i == 4)
                            System.out.print("|" + checkedHeight + "__" + Color.ANSI_WHITE + circle + Color.RESET + "|");
                        else
                            System.out.print("|" + checkedHeight + "__" + Color.ANSI_WHITE + circle + Color.RESET);
                    }
                }

                else if (checkedHeight != 0) {
                    if (i == 4)
                        System.out.print("|__" + checkedHeight + "__|");
                    else
                        System.out.print("|__" + checkedHeight + "__");
                }

                else if (checkedWorker != null) {
                    int workerColor = checkedWorker.getColor();
                    if (workerColor == 1) {
                        if (i == 4)
                            System.out.print("|__" + Color.ANSI_BRIGHT_YELLOW + circle + Color.RESET + "_|");
                        else
                            System.out.print("|___" + Color.ANSI_BRIGHT_YELLOW + circle + Color.RESET);
                    } else if (workerColor == 2) {
                        if (i == 4)
                            System.out.print("|__" + Color.ANSI_WHITE + circle + Color.RESET + "_|");
                        else
                            System.out.print("|___" + Color.ANSI_WHITE + circle + Color.RESET);
                    } else if (workerColor == 3) {
                        if (i == 4)
                            System.out.print("|__" + Color.ANSI_BLUE + circle + Color.RESET + "_|");
                        else
                            System.out.print("|___" + Color.ANSI_BLUE + circle + Color.RESET + "_");
                    }
                }

                else {
                    if (i == 4)
                        System.out.print("|_____|");
                    else
                        System.out.print("|_____");
                }
            }
        System.out.println("");}
    }

    @Override
    public void isWinner(String winner) {
        System.out.println("Fine del gioco! Ha vinto: " + winner);
    }

}
