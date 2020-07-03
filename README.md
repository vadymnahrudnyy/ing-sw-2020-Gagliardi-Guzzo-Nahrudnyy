# Prova Finale Ingegneria del Software - a.a 2019/2020

![Santorini](src/main/resources/Images/Backgrounds/background_readme.png)

Lo scopo del progetto è quello di implementare il gioco da tavolo [Santorini](http://www.craniocreations.it/prodotto/santorini/) utilizzando il pattern architetturale Model View Controller.Nel risulato finale sono state implementate le regole del gioco complete al quale si può giocare sia da interfaccia da linea di comando (CLI) che grafica (GUI).

## Documentazione

#### UML
I seguenti schemi propongono una visione strutturale del proramma. In particolare: l'UML ad alto livello è una rappresentazione della struttura del gioco contenente solo le classi più importanti. L'UML di dettaglio invece propone una visione di tutti i package e le classi delle applicazioni.
- [UML di alto livello](https://github.com/vadymnahrudnyy/ing-sw-2020-Gagliardi-Guzzo-Nahrudnyy/tree/master/Deliveries/final/uml)
- [UML di dettaglio](https://github.com/vadymnahrudnyy/ing-sw-2020-Gagliardi-Guzzo-Nahrudnyy/tree/master/Deliveries/final/uml)

#### JavaDoc
La documentazione in dettaglio delle classi e dei metodi utilizzati può essere consulatata al seguente indirizzo: [JavaDoc](https://github.com/vadymnahrudnyy/ing-sw-2020-Gagliardi-Guzzo-Nahrudnyy/tree/master/Deliveries/final/javadoc).

####Coverage Report
La documentazione relativa ai test e coverage report può essere consultata al seguente indirizzo: [Coverage Report](https://github.com/vadymnahrudnyy/ing-sw-2020-Gagliardi-Guzzo-Nahrudnyy/tree/master/Deliveries/final/coverage)

#### Librerie e Plugins

- [Maven](https://maven.apache.org/) - strumento di gestione di progetti software Java e build automation
- [IntelliJ](https://www.jetbrains.com/idea/) - IDE
- [JavaFX](https://openjfx.io/) - libreria grafica di Java 
- [JUnit](https://junit.org/junit5/) - framework utilizzato per unit testing


## Funzionalità Implementate

- [x] Regole Complete
- [x] CLI
- [x] GUI
- [x] Socket
- [x] Partite Multiple
- [ ] Persistenza
- [x] Divinità Avanzate
- [ ] Undo

## Esecuzione dei jar
Attraverso il repository GitHub è possibile scaricare i file jar eseguibili da [questo indirizzo](https://github.com/vadymnahrudnyy/ing-sw-2020-Gagliardi-Guzzo-Nahrudnyy/tree/master/Deliveries/final/jar).

[NOTA: Per poter eseguire i file jar sul proprio calcolatore è necessario scaricare la JDK adatta al proprio sistema operativo da [questo sito](https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html)].

Per eseguire correttamente i jar potrebbe essere necessario dopo l'installazione della jdk impostare la variabile di sistema PATH in modo corretto.
La guida per ciascun sistema operativo compatibile è reperibile sul [sito ufficiale di java](https://www.java.com/it/download/help/path.xml).


#### Server
Per lanciare il server: occorre eseguire da terminale l'istruzione `java -jar santoriniServer.jar`.

#### Client
Allo stesso modo del server, per lanciare il client occorre eseguire da terminale l'istruzione `java -jar santoriniClient.jar`
Una volta eseguito il jar del client sarà possibile scegliere la modalità d'interazione con l'applicazione: GUI o CLI.

Su Windows, inoltre, è possibile lanciare il client facendo doppio click sul file; tuttavia in questo modo non sarà possibile utilizzare l'applicazione in modalità CLI.
Per utilizzare l'interfaccia CLI su Windows 10, è necessario installare il sottosistema linux per Windows 10, è possibile farlo seguendo la guida al [seguente link](https://github.com/michele-bertoni/W10JavaCLI/blob/master/README.md)


Componenti del gruppo
--
- Gagliardi Alessia
- Guzzo Alessia
- Nahrudnyy Vadym 
