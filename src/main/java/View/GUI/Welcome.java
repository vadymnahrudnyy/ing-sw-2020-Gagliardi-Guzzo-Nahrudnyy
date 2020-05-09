package View.GUI;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

import static javafx.scene.input.MouseEvent.*;

public class Welcome extends Application {
    Stage stage=new Stage();
    SelectGod newScene = new SelectGod();
    @FXML
    private ImageView playButton;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {


        primaryStage.setScene(new Scene(new Pane()));
        setLayout(primaryStage.getScene(), "Fxml/WelcomeScene.fxml");
        primaryStage.setResizable(false);
        primaryStage.show();

        }

    <T> T setLayout(Scene scene, String path) {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(path));

        StackPane pane;
        try {
            pane = loader.load();
            scene.setRoot(pane);
        } catch (IOException e) {
            return null;
        }

        return loader.getController();
    }

    public void event(MouseEvent event) throws Exception {
        newScene.start(stage);

    }
    }

