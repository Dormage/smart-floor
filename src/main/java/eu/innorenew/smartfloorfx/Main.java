package eu.innorenew.smartfloorfx;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;

public class Main extends Application {
    double width;
    double height;
    Gson   gson;

    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        primaryStage.setTitle("Force detection");
        gson = new Gson();
        JsonReader  reader      = new JsonReader(new FileReader("src/tileMapping.json"));
        DataManager dataManager = new DataManager(gson.fromJson(reader, HashMap.class));
        new Server(8788, dataManager).start();

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(25, 25, 25, 25));
        gridPane.setVgap(5);
        gridPane.setHgap(6);
        gridPane.setAlignment(Pos.CENTER);

        int id = 1;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 6; j++) {
                String tileId = dataManager.getMapping().get("" + id);
                //System.out.println(dataManager.getMapping().keySet());
                Tile tile = new Tile(dataManager, 8, tileId);
                dataManager.addTile(tileId, tile);
                gridPane.add(tile, i, j);
                id++;
            }
        }

        Scene grid_scene = new Scene(gridPane);
        primaryStage.setScene(grid_scene);
        primaryStage.setMaximized(true);
        width  = primaryStage.getWidth();
        height = primaryStage.getHeight();
        primaryStage.show();

        Timeline fiveSecondsWonder = new Timeline(
                new KeyFrame(Duration.millis(23), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        dataManager.getTiles().values().forEach(Tile::update);
                    }
                }));
        fiveSecondsWonder.setCycleCount(Timeline.INDEFINITE);
        fiveSecondsWonder.play();
    }
}
