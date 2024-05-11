package TaskFromLab45TCPSockets.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {

    private int port;
    private ServerSocket server;
    private boolean work;


    public Server(int port) {
        this.port = port;
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            server = new ServerSocket(port);
            work = true;
            ServerApp.textArea.append("Server started: " + server + "\n");
            while (work) {
                Socket clientSocket = server.accept();
                ServerApp.textArea.append("Connected : " + clientSocket + "\n");
                new ClientHandler(clientSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopServer(){
        work = false;
        try {
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
