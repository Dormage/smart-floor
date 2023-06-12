package eu.innorenew.smartfloorfx;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class Server extends Thread{
    private int          port;
    private List<Client> clients;
    private DataManager  dataManager;

    public Server(int port, DataManager dataManager) {
        this.port        = port;
        this.dataManager = dataManager;
        this.clients     = new LinkedList<>();
    }
    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(this.port);
            System.out.println(Constants.STATUS + "Server started on port: " + this.port);
            while (true) {
                Socket connection = serverSocket.accept();
                System.out.println(Constants.SUCCESS + "New connection accepted from " + connection.getInetAddress() + Constants.RESET);
                Client client     = new Client(connection, dataManager);
                clients.add(client);
                client.start();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
