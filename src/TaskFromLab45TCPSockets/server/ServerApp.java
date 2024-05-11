package TaskFromLab45TCPSockets.server;

import javax.swing.*;
import java.awt.*;

public class ServerApp extends JFrame {
    private JPanel contentPane;

    protected static  JTextArea textArea;
    private JButton startButton;
    private JButton stopServer;
    private JTextField textField;
    private boolean isServerRun = false;
    private Server server;

    public ServerApp(){
        setTitle("Class Analyzer");
        setBounds(100, 100, 600, 600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        contentPane = new JPanel(new BorderLayout());
        setContentPane(contentPane);

        textArea = new JTextArea();
        JScrollPane jScrollPane = new JScrollPane(textArea);
        contentPane.add(jScrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        startButton = new JButton("Start");
        stopServer = new JButton("Stop");

        stopServer.addActionListener(e -> {
            server.stopServer();
            isServerRun = false;
        });
        startButton.addActionListener(e -> {
            if (!isServerRun){
                server = new Server(Integer.parseInt(textField.getText()));
                isServerRun = true;
                System.out.println("Ok");
            }
        });

        JPanel northPanel = new JPanel(new FlowLayout());
        northPanel.add(new Label("port"));
        textField = new JTextField(30);
        northPanel.add(textField);
        contentPane.add(northPanel, BorderLayout.NORTH);

        buttonPanel.add(startButton);
        buttonPanel.add(stopServer);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                ServerApp frame = new ServerApp();
                frame.setVisible(true);
            } catch (Exception var1) {
                var1.printStackTrace();
            }

        });
    }
}