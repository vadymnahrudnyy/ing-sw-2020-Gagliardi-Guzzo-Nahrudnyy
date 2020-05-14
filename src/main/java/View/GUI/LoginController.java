package View.GUI;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import Messages.*;
import Client.NetworkHandler;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class is the controller for LoginScene.fxml and Home.fxml
 */
public class LoginController implements Initializable {

    @FXML private AnchorPane mainPane;
    @FXML private ImageView twoPlayers, threePlayers;
    @FXML public TextField usernameField, addressField;
    private static final int SOCKET_PORT = 50000;
    private static NetworkHandler networkHandler;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    /**
     * This methos is called when the playButton in Home.fxml is clicked. It switch scene to LoginScene.fxml
     * @param event indicates the event when mouse clicked on the button
     */
    @FXML
    private void handlePlayButton(MouseEvent event) {
        FxmlHandler object=new FxmlHandler();
        Pane view=object.getHomePage("LoginScene");
        mainPane.getChildren().setAll(view);
    }

    /**
     * This method is called when the user click on the ConnectButton in LoginScene.fxml. It
     * starts a connection with the server (through the NetworkHandler) and communicates the player's username and the number
     * of player chosen.
     * @param event indicates the event when mouse clicked on the button
     * @throws Exception throws an exception for methods sendMessage and startConnection
     */
    @FXML
    public void connectUser(MouseEvent event) throws Exception {
        String username= usernameField.getText();
        networkHandler.sendMessage(new UsernameResponse(username));
        twoPlayers.addEventHandler(MouseEvent.MOUSE_CLICKED, event1 -> {
            try {
                networkHandler.sendMessage(new NumPlayersResponse(2));
            } catch (IOException e) {
                e.printStackTrace();
            } });
        threePlayers.addEventHandler(MouseEvent.MOUSE_CLICKED, event1 -> {
            try {
                networkHandler.sendMessage(new NumPlayersResponse(3));
            } catch (IOException e) {
                e.printStackTrace();
            } });
        String address= addressField.getText();
        startConnection(address);
    }

    /**
     * This method helps the connectUser method to establish a connection with the server
     * by creating a new thread and establishing a new connection.
     * @param serverAddress indicates the address.
     */
    public static void startConnection(String serverAddress) {
        networkHandler=new NetworkHandler(serverAddress, SOCKET_PORT);
        Thread network= new Thread(networkHandler);
        network.start();
    }





}
