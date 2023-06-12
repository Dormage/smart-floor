package eu.innorenew.smartfloorfx;

import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Tile extends GridPane {
    private Sensor[][]  sensors;
    private int         sensorsPerTile;
    private String      tileId;
    private boolean     initialized;
    private DataManager dataManager;
    private int         rows;
    private int         columns;
    private Text[][]    labels;


    public Tile(DataManager dataManager, int sensorsPerTile, String tileId) {
        this.rows           = sensorsPerTile / 4;
        this.columns        = sensorsPerTile / 2;
        this.dataManager    = dataManager;
        this.initialized    = false;
        this.tileId         = tileId;
        this.sensorsPerTile = sensorsPerTile;
        sensors             = new Sensor[rows][columns];
        this.setVgap(rows);
        this.setHgap(columns);
        this.disableTile();
        labels = new Text[rows][columns];

        this.setPadding(new Insets(20, 20, 20, 20));

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                StackPane p = new StackPane();
                Sensor    s = new Sensor(55, 55);
                Text      l = new Text("" + s.getMeasurement());
                labels[i][j] = l;
                p.getChildren().addAll(s, l);
                sensors[i][j] = s;
                s.getStyleClass().add("sensor");
                this.add(p, j, i);
            }
        }
    }

    public void update() {

        Measurement measurement = dataManager.getMeasurement(tileId);
        if (measurement == null) return;
        int[] sensorData = measurement.getSensorData();
        if (sensorData == null) return;
        boolean print = false;
        for (int i = 0; i < sensorData.length; i++) {
            if (sensorData[i] > 1000) print = true;
        }
        if (print) printSensorData(sensorData);
        if (dataManager.getNetworkMap().containsKey(tileId)) {
            this.setStyle("-fx-border-color: green");
        } else {
            this.setStyle("-fx-border-color: red");
        }
        for (int row = 0; row < sensors.length; row++) {
            for (int column = 0; column < sensors[row].length; column++) {
                Sensor sensor = sensors[row][column];
                int    data   = sensorData[row * rows + column];
                sensor.setMeasurement(data);
                sensor.applyReadings();
                labels[row][column].setText("" + sensor.getMeasurement());
                if (sensor.getMeasurement() > 300) {
                    //System.out.println(tileId + " | " + getKeysByValue(dataManager.getMapping(), tileId));
                }
            }
        }
    }

    public void initialize(String tileId) {
        this.tileId      = tileId;
        this.initialized = true;
    }

    public static <T, E> Set<T> getKeysByValue(Map<T, E> map, E value) {
        return map.entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getValue(), value))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }

    private void disableTile() {
        this.setStyle("-fx-border-color: red");
        this.setStyle("-fx-opacity: 0.8");
        this.setStyle("-fx-background-color: red");
    }

    private void printSensorData(int[] sensorData) {
        for (int i = 0; i < sensorData.length; i++) {
            System.out.print(sensorData[i] + ", ");
        }
        System.out.println();
    }

    private void enableTile() {
        this.setStyle("-fx-border-color: green");
        this.setStyle("-fx-opacity: 0.8");
        this.setStyle("-fx-background-color: green");
    }

}
