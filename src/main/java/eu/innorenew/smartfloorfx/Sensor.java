package eu.innorenew.smartfloorfx;

import javafx.animation.FillTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.text.DecimalFormat;

public class Sensor extends Rectangle {
    private int    id;
    private double measurement;
    private double upper_bound;
    private double lower_bound;
    private Color  default_color;
    private Text   label;
    private double width;
    private double height;
    private double previousMeasurement;

    public Sensor(double width, double height) {
        super(width, height);
        default_color = Color.web("0x71e05e");
        this.setDisable(true);
        this.measurement = 0;
        this.setStyle("-fx-arc-height:30; -fx-arc-width: 30;");
        this.setFill(default_color);
        this.setStroke(default_color);
    }

    public void applyReadings() {
        DecimalFormat df = new DecimalFormat("#.##");
        if (this.measurement > 0.01) {
            DropShadow shadow = new DropShadow();
            shadow.setColor(getColor(measurement));
            shadow.setRadius(1000);
            Timeline shadowAnimation = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(shadow.colorProperty(), getColor(previousMeasurement))),
                    new KeyFrame(Duration.millis(1),
                            new KeyValue(shadow.colorProperty(), getColor(measurement))));

            this.setEffect(shadow);
            shadowAnimation.play();
            FillTransition ft = new FillTransition(Duration.millis(1), this, getColor(previousMeasurement),
                    getColor(measurement));
            ft.setCycleCount(1);
            ft.setAutoReverse(true);
            ft.play();
            this.setEffect(shadow);
        } else {
            DropShadow shadow = new DropShadow();
            shadow.setColor(default_color);
            shadow.setRadius(1000);
            this.setFill(default_color);
            this.setStroke(default_color);
            this.setEffect(shadow);
        }
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getMeasurement() {
        return measurement;
    }

    public void setMeasurement(int measurement) {
        this.previousMeasurement = this.measurement;
        this.measurement = measurement;
    }

    public Color getColor(double power) {
        power = power /4096;
        power = 1 - power;
        double H = power * 0.2 * 360; // Hue (note 0.4 = Green)
        double S = 0.9; // Saturation
        double B = 0.9; // Brightness
        return Color.hsb(H, S, B, 1);
    }
}
