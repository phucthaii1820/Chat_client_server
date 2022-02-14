package vn.edu.hcmus._19127514.Chat_client_server;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server extends JPanel {
    Map<String, String> listUser = new HashMap<>();

    public Server(JFrame frame) throws IOException {

        System.out.println(Integer.toString(listUser.size()));
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20,20,20,20));
        setPreferredSize(new Dimension(500, 300));

        JPanel jPanelTop = new JPanel();
        jPanelTop.setLayout(new BorderLayout());

        //------------------------------

        JPanel jPanelPort = new JPanel();
        jPanelPort.setLayout(new FlowLayout());

        JLabel labelPort = new JLabel("PORT");
        JTextField textFieldPort = new JTextField(5);

        jPanelPort.add(labelPort);
        jPanelPort.add(textFieldPort);

        //-------------------------------
        JButton btnStart = new JButton("START");

        //-------------------------------
        JTextArea textNotification = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textNotification);

        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ServerSocket ss = new ServerSocket(Integer.parseInt(textFieldPort.getText()));
                    listUser = readData("data.txt");
                    textNotification.append("Server start success!! \n");

                    Thread th = new Thread() {
                        public void run() {
                            try {
                                while (true) {
                                    Socket s = ss.accept();
                                    ThreadClient threadClient = new ThreadClient(s, textNotification, listUser);

                                    Thread thread = new Thread(threadClient);

                                    if(s.isClosed()) {
                                        System.out.println("is close");
                                    }
                                    else
                                        thread.start();

                                }
                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }
                        }
                    };
                    th.start();
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });

        jPanelTop.add(jPanelPort, BorderLayout.LINE_START);
        jPanelTop.add(btnStart, BorderLayout.LINE_END);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ThreadClient.CloseServer();
                SaveData();
            }
        });


        add(jPanelTop, BorderLayout.PAGE_START);
        add(scrollPane, BorderLayout.CENTER);
    }

    private static void createAndShowGUI() throws IOException {
        //Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);

        //Create and set up the window.
        JFrame frame = new JFrame("SERVER");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        Server main = new Server(frame);
        main.setOpaque(true);

        frame.setContentPane(main);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public Map<String, String> readData(String path) throws IOException {
        Map<String, String> temp = new HashMap<>();

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("data.txt"));
            String line = bufferedReader.readLine();
            while (line != null) {
                String []arrString = line.split("`");

                temp.put(arrString[0], arrString[1]);

                line = bufferedReader.readLine();
            }
        }catch (Exception e) {
            System.out.println(e);
        }

        return temp;
    }

    public void SaveData() {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("data.txt"));
            for(Map.Entry<String, String> entry:listUser.entrySet()) {
                String line = entry.getKey() + "`" + entry.getValue() + "\n";
                bufferedWriter.write(line);
            }

            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        createAndShowGUI();
    }
}
