package TaskFromLab45TCPSockets.client;

import TaskFromLab45TCPSockets.Result;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;

public class ClientApp extends JFrame {


    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private Socket socket;
    private String host;
    private int port;
    private JPanel contentPane;

    private JTextArea textArea;
    private JButton calculateButton;
    private JButton clearButton;
    private JTextField textField;
    public ClientApp(String host, int port) {
        setTitle("Class Analyzer");
        setBounds(100, 100, 600, 600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        contentPane = new JPanel(new BorderLayout());
        setContentPane(contentPane);

        textArea = new JTextArea();
        JScrollPane jScrollPane = new JScrollPane(textArea);
        contentPane.add(jScrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        calculateButton = new JButton("Calculate");
        clearButton = new JButton("Clear");

        clearButton.addActionListener(e->{
            textArea.setText("");
        });
        calculateButton.addActionListener(e -> {
            try {
                giveTask();
            } catch (IOException | ClassNotFoundException ioException) {
                ioException.printStackTrace();
            }
        });

        JPanel northPanel = new JPanel(new FlowLayout());
        northPanel.add(new Label("N:"));
        textField = new JTextField(30);
        northPanel.add(textField);
        contentPane.add(northPanel,BorderLayout.NORTH);

        buttonPanel.add(calculateButton);
        buttonPanel.add(clearButton);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);
        this.host = host;
        this.port = port;
        try {
            socket = new Socket(host, port);
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void giveTask() throws IOException, ClassNotFoundException {
        String classFile = this.getClass().getResource("JobOne.class").getPath();
        oos.writeObject(classFile);
        FileInputStream fis = new FileInputStream(classFile);
        byte[] b = new byte[fis.available()];
        System.out.println(fis.read(b));
        oos.writeObject(b);

        int num = Integer.parseInt(textField.getText());
        JobOne aJob = new JobOne(num);
        oos.writeObject(aJob);

        classFile = (String) ois.readObject();
        b = (byte[]) ois.readObject();
        FileOutputStream fos = new FileOutputStream(classFile);
        fos.write(b);
        Result r = (Result) ois.readObject();
        textArea.append("result = " + r.output() + ", time taken = " + r.scoreTime() + "ns\n");
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                ClientApp frame = new ClientApp("localhost", 8080);
                frame.setVisible(true);
            } catch (Exception var1) {
                var1.printStackTrace();
            }

        });
    }
}