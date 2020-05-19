package View.Gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;


/**
 * This class manages the loading of new fxml files  and checks whether the file exists or not
 */
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
