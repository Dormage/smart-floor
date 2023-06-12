package eu.innorenew.smartfloorfx;

public class Measurement {
    private int[] sensorData;

    public Measurement(int sensorData[]) {
        this.sensorData = sensorData;
    }

    public int[] getSensorData() {
        return sensorData;
    }

    public void setSensorData(int[] sensorData) {
        this.sensorData = sensorData;
    }
}
