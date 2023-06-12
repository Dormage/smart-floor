package eu.innorenew.smartfloorfx;


import org.apache.commons.codec.digest.DigestUtils;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class Client extends Thread {
    private String          tileId;
    private Socket          socket;
    private DataInputStream inputStream;
    private int             readLength;
    private DataManager     dataManager;

    public Client(Socket socket, DataManager dataManager) throws IOException {
        this.socket      = socket;
        this.dataManager = dataManager;
        this.tileId      = "";
        this.readLength  = 12;
        this.inputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
    }

    public void run() {
        try {
            byte[] idData = inputStream.readNBytes(17);
            //this.tileId = new String(idData).replaceAll("\\r\\n", "");
            this.tileId = DigestUtils.sha256Hex(idData);
            System.out.println(tileId);
        } catch (IOException e) {
            System.out.println(Constants.ERROR + " protocol violation on controllerId");
            closeConnection();
            e.printStackTrace();
        }
        while (true) {
            byte[] buffer = new byte[readLength];
            try {
                inputStream.read(buffer);
                int data[] = deserialize(buffer);
                dataManager.setMeasurement(tileId, new Measurement(data));
                dataManager.setNetworkMap(tileId, socket.getInetAddress());
                // printSensorData(data);
            } catch (IOException e) {
                closeConnection();
            }
        }
    }

    private void closeConnection() {
        System.out.println(Constants.ERROR + "Connection: " + socket.getInetAddress() + " closed!");
        dataManager.getNetworkMap().remove(tileId);
        try {
            inputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int[] deserialize(byte[] buffer) {
        int[] sensor_data = new int[8];
        int   j;
        for (int i = 0; i < 8; i++) {
            j = i + i / 2;
            if (i % 2 == 0) {
                sensor_data[i] = (buffer[j] & 0xff) << 4;
                int val = (buffer[j + 1] & 0xff) >>> 4;
                sensor_data[i] += val;

            } else {
                sensor_data[i] = (buffer[j] << 4) & 0xff;
                sensor_data[i] = sensor_data[i] << 4;
                sensor_data[i] += buffer[j + 1] & 0xff;
            }
        }
        return sensor_data;
    }

    public void printSensorData(int[] sensor_data) {
        System.out.print(tileId + " ");
        for (int i = 0; i < sensor_data.length; i++) {
            System.out.print(sensor_data[i] + " , ");
        }
        System.out.println();
    }
}

