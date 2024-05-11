package TaskFromLab45TCPSockets.server;

import TaskFromLab45TCPSockets.Executable;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private boolean work = true;
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        Thread thread = new Thread(this);
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public void run() {
        try {
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
            while(work) {
                String classFile = (String) ois.readObject();
                classFile = classFile.replaceFirst("client", "server"); //Важно: подправить полное имя
                byte[] b = (byte[]) ois.readObject();
                FileOutputStream fos = new FileOutputStream(classFile);
                fos.write(b);

                Executable ex = (Executable) ois.readObject();

                double startTime = System.nanoTime();
                Object output = ex.execute();
                double endTime = System.nanoTime();
                double completionTime = endTime - startTime;

                ResultImpl r = new ResultImpl(output, completionTime);
                classFile = this.getClass().getResource("ResultImpl.class").getPath();
                oos.writeObject(classFile);
                FileInputStream fis = new FileInputStream(classFile);
                byte[] bo = new byte[fis.available()];
                fis.read(bo);
                oos.writeObject(bo);
                oos.writeObject(r);
                if (ex.execute().toString().equals("1")){
                    work = false;
                    ServerApp.textArea.append("Client :" + socket + " disconnect\n");
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}