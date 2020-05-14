package View.GUI;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.net.URL;

public class FxmlHandler {

    private Pane pane;

    public Pane getHomePage(String fileName) {
        try {
            pane= FXMLLoader.load(getClass().getClassLoader().getResource("Fxml/" +fileName+ ".fxml"));
            if (fileName==null){
                throw new java.io.FileNotFoundException("Fxml cannot be found");
            }

        }catch (Exception e){
            System.out.println("no page found");
        }
        return pane;
    }

}

