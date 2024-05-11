package TaskFromLab56UDPSockets.updWork;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPServer {

    private static final int BUFFER_SIZE = 512;

    private final ActiveUsers users;
    private DatagramSocket socket;
    private DatagramPacket packet;
    private InetAddress inetAddress;
    private int port;

    public UDPServer(int port) {
        try {
            socket = new DatagramSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        users = new ActiveUsers();
    }

    private void work() {
        System.out.println("Server start");
        try {
            while (true) {
                waitUserData();
                log();
                sendUserData();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            socket.close();
        }
    }


    private void waitUserData() throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        inetAddress = packet.getAddress();
        port = packet.getPort();
        User user = new User(inetAddress, port);
        if (!users.contains(user)) {
            users.add(user);
        }
    }

    private void sendUserData() throws IOException {
        byte[] buffer;
        for (User user : users) {
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                 ObjectOutputStream oos = new ObjectOutputStream(baos)) {

                oos.writeObject(user);
                buffer = baos.toByteArray();
                packet = new DatagramPacket(buffer, buffer.length, inetAddress, port);
                socket.send(packet);
            }
        }
        buffer = "End".getBytes();
        packet = new DatagramPacket(buffer, 0,inetAddress,port);
        socket.send(packet);
    }

    private void log() {
        System.out.println("Address: " + inetAddress + " port: " + port);
    }


    public static void main(String[] args) {
        new UDPServer(1501).work();
    }
}