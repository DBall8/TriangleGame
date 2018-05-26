package dcball;

import dcball.Singletons.Settings;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class GameLauncher extends Application {

    private GameManager game;

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent screen;
        try {
            screen = FXMLLoader.load(getClass().getClassLoader().getResource("GameScreen.fxml"));
        } catch (IOException e){
            System.err.println("Failed to load main FXML file!");
            e.printStackTrace();
            return;
        }

        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        double width = primaryScreenBounds.getWidth();
        double height = primaryScreenBounds.getHeight();
        Settings.getInstance().HEIGHT = (int)height;
        Settings.getInstance().WIDTH = (int)width;

        screen.prefWidth(width);
        screen.prefHeight(height);

        Group root = new Group();
        Scene scene = new Scene(root, width, height);

        game = new GameManager((int)width, (int)height, scene);

        primaryStage.setTitle("Triangle Wars");
        root.getChildren().add(screen);
        root.getChildren().add(game.getVisuals());
        if(Settings.isDebug()){
            root.getChildren().add(game.getDebug());
        }

        primaryStage.setScene(scene);
        primaryStage.show();

        attemptConnection();

    }

    private void attemptConnection(){
        ClientSocket client = new ClientSocket(game);
        client.connect("localhost", 8080);

    }
}
