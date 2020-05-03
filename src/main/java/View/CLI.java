package View;

import java.util.ArrayList;
import java.util.Scanner;

import Model.*;


public class CLI implements UI {

    int i,j;
    Scanner input;

    @Override
    public void gameInfo() {
        System.out.println("Benvenuto in Santorini!");
        System.out.println("Dovrai scegliere una carta divinità, la quale possiede un potere particolare, che potrai utilizzare nel gioco. Inoltre disporrai di 2 worker.");
        System.out.println("Durante il tuo turno potrai muoverti: il movimento può essere solo nelle caselle direttamente adiacenti alla tua posizione.");
        System.out.println("Dopodiché potrai costruire, ci sono 4 tipo di blocchi: livello 1 VERDE, livello 2 ROSSO, livello 3 GIALLO e cupola BLU");
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
    public void startNotification(){
        System.out.println("Lobby piena: il gioco può cominciare, buona partita!");
    }

    @Override
    public void showGodList(ArrayList<God> gods, int numPlayers) {
        System.out.println("Scegli " + numPlayers + " carte tra quelle disponibili.");
        for(int i=0; i<gods.size(); i++) {
            System.out.println("Dio: " + gods.get(i).getName() + ", " + gods.get(i).getDescription());
        }
    }

    @Override
    public void printAllPlayers(Player[] players) {
        System.out.println("Scegli tra questi giocatori quale dovrà iniziare: ");
        for(int i=0; i<players.length; i++) {
            System.out.print(players[i] + ", ");
        }
    }

   @Override
    public void chooseGod(ArrayList<God> godList, ArrayList<God> unavailableList) {
        System.out.println("Questi sono tutti gli dei scelti per la partita: ");
        for(int i=0; i<godList.size(); i++){
            System.out.print( godList.get(i).getName() + ", ");
        }

        System.out.println("Dei già scelti (non disponibili): ");
        for(int i=0; i<unavailableList.size(); i++){
            System.out.print( unavailableList.get(i).getName() + ", ");
        }

        System.out.println("Devi sceglierne uno evitando quelli già scelti.");
    }

    @Override
    public void printLastGod(ArrayList<God> godList, God lastGod) {
        System.out.println("Questi sono tutti gli dei che hai scelto per la partita: ");
        for(int i=0; i<godList.size(); i++){
            System.out.print( godList.get(i).getName() + ", ");
        }
        System.out.println("Ne è rimasto solo uno, devi scegliere questo: " + lastGod.getName());
    }

    @Override
    public void askWorkerPosition(){
        System.out.println("Scegli dove mettere il tuo worker: ");
    }

    @Override
    public void chooseWorker() {
        System.out.println("Scegli le coordinate del worker che desideri muovere: ");
    }

    @Override
    public boolean confirmChoice() {
        System.out.println("Confermi di voler muovere il worker scelto? Rispondi Y o N. ");
         input=new Scanner(System.in);
         String choice=input.nextLine();
         if("y".equals(choice)){
             System.out.print("Inserisci le coordinate del worker che vuoi scegliere.");
             return true;
         }
         else
             return false;
    }

    @Override
    public void moveWorker() {
        System.out.println("Scegli in quale casella vuoi spostare il tuo worker indicando le coordinate numeriche x e y.");
        //stampo board con un cerchio in quella posizione
    }

    @Override
    public void printPossibleAction(boolean[][] allowed) {
        System.out.println("Mosse possibili (in coordinate): ");
        //stampo l'elenco delle mosse possibili
        for(i=0; i<5; i++){
            for(j=0;j<5;j++){
                if(allowed[i][j]==true)
                    System.out.println("(" + i+1 + "," + j+1 + "), ");
            }
        }
    }

    @Override
    public void buildTower()
    {
        System.out.println("Scegli in quale casella vuoi costruire, indicando le coordinate numeriche x e y:");
    }


    @Override
    public void askPowerUsage() {
        System.out.println("Vuoi usare il potere del tuo dio in questo turno?");
    } //si o no (booleano nella risposta)


    @Override
    public void printCurrentBoard(Game updatedGame) {
 //colore nel worker
        System.out.println(" _____________________________");
        for(i=0; i<5; i++){
            for(j=0;j<5;j++) {
                Worker checkedWorker;
                if ((checkedWorker = updatedGame.getGameBoard().getSpace(i + 1, j + 1).getWorkerInPlace())!=null){
                   int workerColor= checkedWorker.getColor();
                   //controllo livello
                    int checkedHeight = updatedGame.getGameBoard().getSpace(i + 1, j + 1).getHeight();

                }


                //stampa la board corrente
                // round, fase
            }} }

    @Override
    public void isWinner(String winner) {
        System.out.println("Fine del gioco! Ha vinto: " + winner);
    }

}
