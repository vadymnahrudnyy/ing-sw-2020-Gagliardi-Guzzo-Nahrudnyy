package View.GUI;

import javafx.animation.TranslateTransition;
import javafx.application.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.*;
import javafx.util.Duration;


import java.io.IOException;

/**
 * This class start the GUI application
 */
public class MainScene extends Application {


    private static Stage primaryStage;


    /**
     * This method launches the application
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

    public static Stage getStage() {
        return primaryStage;
    }



    /**
     * This method load Home.fxml file and shows it into a stage
     * @param stage indicates the stage on which the scene will be projected.
     * @throws IOException throws an exception for method load
     **/
    @Override
    public void start(Stage stage) throws IOException {

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("Fxml/Home.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        primaryStage = stage;

    }
}





