package vn.edu.hcmus._19127514.Chat_client_server;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * vn.edu.hcmus._19127514.Chat_client_server
 * Created by phucthaii1820 - 19127514
 * Date 21/12/2021 - 13:15
 * Description: ...
 */
public class FrameClient extends JPanel {
    public FrameClient(JFrame frame) throws IOException {

        setBorder(new EmptyBorder(20,20,20,20));
        setLayout(new GridLayout(2, 1));
        setPreferredSize(new Dimension(300, 100));

        Button btnLoginHome = new Button("LOGIN");
        Button btnRegisterHome = new Button("REGISTER");

        btnRegisterHome.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);

                JFrame frame1 = new JFrame("Register");
                frame1.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame1.setLayout(new GridLayout(6, 1));
                frame1.getRootPane().setBorder(new EmptyBorder(20, 20, 20, 20));


                //-----------
                JPanel jPanelUserRegister = new JPanel();
                jPanelUserRegister.setLayout(new FlowLayout());
                JLabel lbUser = new JLabel("User ");
                lbUser.setPreferredSize(new Dimension(100, 20));
                JTextField tfUser = new JTextField(20);

                jPanelUserRegister.add(lbUser);
                jPanelUserRegister.add(tfUser);

                //-----------
                JPanel jPanelPassRegister = new JPanel();
                jPanelPassRegister.setLayout(new FlowLayout());
                JLabel lbPass = new JLabel("Pass ");
                lbPass.setPreferredSize(new Dimension(100, 20));
                JPasswordField tfPass = new JPasswordField(20);

                jPanelPassRegister.add(lbPass);
                jPanelPassRegister.add(tfPass);

                //-----------

                JPanel jPanelIP = new JPanel();
                jPanelIP.setLayout(new FlowLayout());
                JLabel lbIP = new JLabel("IP");
                lbIP.setPreferredSize(new Dimension(100, 20));
                JTextField tfIP = new JTextField(20);

                jPanelIP.add(lbIP);
                jPanelIP.add(tfIP);

                //-----------

                JPanel jPanelPort = new JPanel();
                jPanelPort.setLayout(new FlowLayout());
                JLabel lbPort = new JLabel("Port");
                lbPort.setPreferredSize(new Dimension(100, 20));
                JTextField tfPort = new JTextField(20);

                jPanelPort.add(lbPort);
                jPanelPort.add(tfPort);

                //-----------
                JPanel jPanelPassConfirm = new JPanel();
                jPanelPassConfirm.setLayout(new FlowLayout());
                JLabel lbPassConfirm = new JLabel("Pass ");
                lbPassConfirm.setPreferredSize(new Dimension(100, 20));
                JPasswordField tfPassConfirm = new JPasswordField(20);

                jPanelPassConfirm.add(lbPassConfirm);
                jPanelPassConfirm.add(tfPassConfirm);

                //------------
                JPanel jPanelBottom = new JPanel();
                Button btnRegister = new Button("Register");
                jPanelBottom.add(btnRegister);

                btnRegister.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(tfPass.getText().equals(tfPassConfirm.getText())) {
                            if(!tfUser.getText().equals("") && !tfPass.getText().equals("")) {
                                Thread th = new Thread() {
                                    public void run() {
                                        try {
                                            Socket s = new Socket(tfIP.getText(), Integer.parseInt(tfPort.getText()));
                                            Client client = new Client(s, tfUser.getText(), tfPass.getText());
                                            boolean check = client.Register();
                                            if(check) {
                                                JFrame message = new JFrame("Message");
                                                JOptionPane.showMessageDialog(message, "Register success!");

                                                frame1.dispose();
                                                frame.setVisible(true);
                                            }
                                            else {
                                                JFrame message = new JFrame("Message");
                                                JOptionPane.showMessageDialog(message, "Account already exists!");
                                            }
                                        } catch (UnknownHostException unknownHostException) {
                                            unknownHostException.printStackTrace();
                                            JFrame message = new JFrame("Message");
                                            JOptionPane.showMessageDialog(message, "Connection to server error!");
                                        } catch (IOException ioException) {
                                            ioException.printStackTrace();
                                            JFrame message = new JFrame("Message");
                                            JOptionPane.showMessageDialog(message, "Connection to server error!");
                                        }
                                    }
                                };
                                th.start();
                            }
                            else {
                                JFrame message = new JFrame("Message");
                                JOptionPane.showMessageDialog(message, "You can not leave it blank");
                            }
                        } else {
                            JFrame message = new JFrame("Message");
                            JOptionPane.showMessageDialog(message, "Password confirm was wrong!!");
                        }
                    }
                });


                //-----------
                frame1.add(jPanelUserRegister);
                frame1.add(jPanelPassRegister);
                frame1.add(jPanelPassConfirm);
                frame1.add(jPanelIP);
                frame1.add(jPanelPort);
                frame1.add(jPanelBottom);

                frame1.pack();
                frame1.setVisible(true);
                frame1.setDefaultCloseOperation(frame1.DISPOSE_ON_CLOSE);

                frame1.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        frame.setVisible(true);
                    }
                });

            }
        });

        btnLoginHome.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);

                JFrame frame1 = new JFrame("Login");
                frame1.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame1.setLayout(new GridLayout(5, 1));
                frame1.getRootPane().setBorder(new EmptyBorder(20, 20, 20, 20));


                //-----------
                JPanel jPanelUserLogin = new JPanel();
                jPanelUserLogin.setLayout(new FlowLayout());
                JLabel lbUser = new JLabel("User ");
                lbUser.setPreferredSize(new Dimension(100, 20));
                JTextField tfUser = new JTextField(20);

                jPanelUserLogin.add(lbUser);
                jPanelUserLogin.add(tfUser);

                //-----------
                JPanel jPanelPassLogin = new JPanel();
                jPanelPassLogin.setLayout(new FlowLayout());
                JLabel lbPass = new JLabel("Pass ");
                lbPass.setPreferredSize(new Dimension(100, 20));
                JPasswordField tfPass = new JPasswordField(20);

                jPanelPassLogin.add(lbPass);
                jPanelPassLogin.add(tfPass);

                //-----------

                JPanel jPanelIP = new JPanel();
                jPanelIP.setLayout(new FlowLayout());
                JLabel lbIP = new JLabel("IP");
                lbIP.setPreferredSize(new Dimension(100, 20));
                JTextField tfIP = new JTextField(20);

                jPanelIP.add(lbIP);
                jPanelIP.add(tfIP);

                //-----------

                JPanel jPanelPort = new JPanel();
                jPanelPort.setLayout(new FlowLayout());
                JLabel lbPort = new JLabel("Port");
                lbPort.setPreferredSize(new Dimension(100, 20));
                JTextField tfPort = new JTextField(20);

                jPanelPort.add(lbPort);
                jPanelPort.add(tfPort);

                //-----------
                JPanel jPanelBottom = new JPanel();
                Button btnLogin = new Button("Login");
                jPanelBottom.add(btnLogin);
                btnLogin.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Thread th = new Thread() {
                            public void run() {
                                try {
                                    List<String> oldMess = new ArrayList<>();
                                    List<byte[]> oldFile = new ArrayList<>();
                                    File[] fileSelect = new File[1];
                                    Socket s = new Socket(tfIP.getText().toString(), Integer.parseInt(tfPort.getText().toString()));
                                    System.out.println(tfUser.getText() + ":" + tfPass.getText());
                                    Client client = new Client(s, tfUser.getText(), tfPass.getText());
                                    boolean check = client.checkLogin();
                                    if(check == true) {
                                        frame1.setVisible(false);
                                        JFrame frameCenter = new JFrame("CHAT");
                                        frameCenter.getRootPane().setBorder(new EmptyBorder(20, 20, 20, 20));
                                        frameCenter.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                        frameCenter.setLayout(new BorderLayout());
                                        frameCenter.setPreferredSize(new Dimension(500, 400));

                                        //------------------------------------

                                        JPanel jPanelName = new JPanel();
                                        jPanelName.setLayout(new BorderLayout());
                                        JLabel jLabelName = new JLabel(client.getUsername());
                                        jLabelName.setForeground(Color.RED);
                                        jLabelName.setFont(new Font("Serif", Font.PLAIN, 20));
                                        JLabel hi = new JLabel("Hi ");
                                        hi.setFont(new Font("Serif", Font.PLAIN, 20));

                                        JButton btnLogOut = new JButton("Logout");


                                        jPanelName.add(hi, BorderLayout.LINE_START);
                                        jPanelName.add(jLabelName, BorderLayout.CENTER);
                                        jPanelName.add(btnLogOut, BorderLayout.LINE_END);

                                        //------------------------------------
                                        JPanel textAreaShowMess = new JPanel();
                                        textAreaShowMess.setLayout(new BoxLayout(textAreaShowMess, BoxLayout.Y_AXIS));
                                        //JTextArea textAreaShowMess = new JTextArea();
                                        JScrollPane jScrollPane = new JScrollPane(textAreaShowMess,
                                                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                                                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

                                        //------------------------------------

                                        JPanel jPanelCenterBottom = new JPanel();
                                        jPanelCenterBottom.setLayout(new BorderLayout());

                                        JPanel jPanelSend = new JPanel();
                                        jPanelSend.setLayout(new FlowLayout());

                                        JTextField textAreaChat = new JTextField();
                                        JScrollPane jScrollPaneChat = new JScrollPane(textAreaChat);

                                        JButton btnSend = new JButton("Send");
                                        JButton btnChooseFile = new JButton("Choose file");

                                        jPanelSend.add(btnSend);
                                        jPanelSend.add(btnChooseFile);


                                        JLabel labelSpace = new JLabel(" ");
                                        labelSpace.setPreferredSize(new Dimension(10, 100));


                                        jPanelCenterBottom.add(labelSpace, BorderLayout.PAGE_START);
                                        jPanelCenterBottom.add(jScrollPaneChat, BorderLayout.CENTER);
                                        jPanelCenterBottom.add(jPanelSend, BorderLayout.LINE_END);

                                        //------------------------------------

                                        JPanel jPanelCenterRight = new JPanel();
                                        jPanelCenterRight.setLayout(new BorderLayout());


                                        JList jListOline = new JList();
                                        jListOline.setPreferredSize(new Dimension(100, 200));

                                        jListOline.addListSelectionListener(new ListSelectionListener() {
                                            @Override
                                            public void valueChanged(ListSelectionEvent e) {
                                                if (!e.getValueIsAdjusting()){
                                                    //System.out.println(jListOline.getSelectedValue());
                                                    if(jListOline.getSelectedValue() != null) {
                                                        textAreaShowMess.removeAll();
                                                        textAreaShowMess.revalidate();
                                                        textAreaShowMess.repaint();
                                                        for(int i = 0; i < oldMess.size(); i++) {
                                                            if(oldMess.get(i).contains("@19127514@@send@@hcmus@")) {
                                                                String[] arrString = oldMess.get(i).split("@19127514@@send@@hcmus@");
                                                                if(arrString[0].equals(jListOline.getSelectedValue().toString())) {
                                                                    JLabel jLabel = new JLabel("ME: " + arrString[2]);
                                                                    textAreaShowMess.add(jLabel);
                                                                }
                                                                if(arrString[1].equals(jListOline.getSelectedValue().toString())) {
                                                                    JLabel jLabel = new JLabel(arrString[1] + ": " + arrString[2]);
                                                                    textAreaShowMess.add(jLabel);
                                                                }
                                                            }
                                                            else if(oldMess.get(i).contains("@19127514@@sendFile@@hcmus@")) {
                                                                String[] arrString = oldMess.get(i).split("@19127514@@sendFile@@hcmus@");
                                                                JButton btn = new JButton();
                                                                if(arrString[0].equals(jListOline.getSelectedValue().toString())) {
                                                                    btn.setText("Me" + " send file " + arrString[2]);
                                                                }
                                                                if(arrString[1].equals(jListOline.getSelectedValue().toString())) {
                                                                    btn.setText(arrString[1] + " send file " + arrString[2]);
                                                                }

                                                                if(arrString[0].equals(jListOline.getSelectedValue().toString()) || arrString[1].equals(jListOline.getSelectedValue().toString())) {
                                                                    btn.addActionListener(new ActionListener() {
                                                                        @Override
                                                                        public void actionPerformed(ActionEvent e) {
                                                                            try {
                                                                                JFileChooser fileChooser = new JFileChooser();
                                                                                fileChooser.setDialogTitle("Download location");
                                                                                int valueFile = fileChooser.showSaveDialog(null);

                                                                                if(valueFile == JFileChooser.APPROVE_OPTION) {
                                                                                    String temp = arrString[2];
                                                                                    String[] arrNameFile = temp.split("\\.");
                                                                                    System.out.println(fileChooser.getSelectedFile() + arrNameFile[arrNameFile.length -1]);
                                                                                    File file = new File(fileChooser.getSelectedFile() + "." + arrNameFile[arrNameFile.length -1]);
                                                                                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                                                                                    fileOutputStream.write(oldFile.get(Integer.parseInt(arrString[4])));
                                                                                    fileOutputStream.close();

                                                                                    JFrame message = new JFrame("Message");
                                                                                    JOptionPane.showMessageDialog(message, "Download success!");
                                                                                }
                                                                            } catch (FileNotFoundException fileNotFoundException) {
                                                                                fileNotFoundException.printStackTrace();
                                                                                JFrame message = new JFrame("Message");
                                                                                JOptionPane.showMessageDialog(message, "Download Fail!");
                                                                            } catch (IOException ioException) {
                                                                                ioException.printStackTrace();
                                                                                JFrame message = new JFrame("Message");
                                                                                JOptionPane.showMessageDialog(message, "Download Fail!");
                                                                            }
                                                                        }
                                                                    });
                                                                    textAreaShowMess.add(btn);
                                                                    frameCenter.validate();
                                                                }
                                                            }
                                                        }

                                                        frameCenter.validate();
                                                    }
                                                }
                                            }
                                        });


                                        jPanelCenterRight.add(jListOline, BorderLayout.CENTER);
                                        jPanelCenterRight.add(labelSpace, BorderLayout.LINE_START);

                                        // ------------------------------------

                                        btnSend.addActionListener(new ActionListener() {
                                            @Override
                                            public void actionPerformed(ActionEvent e) {
                                                if(jListOline.getSelectedValue() != null) {
                                                    client.send(textAreaChat.getText(), textAreaShowMess, jListOline.getSelectedValue().toString(), oldMess, fileSelect[0], frameCenter, oldFile);
                                                    System.out.println("----------------------------------------------");
                                                    for(int i = 0; i < oldMess.size(); i++)
                                                        System.out.println(oldMess.get(i));

                                                    fileSelect[0] = null;
                                                }
                                                else {
                                                    JFrame message = new JFrame("Message");
                                                    JOptionPane.showMessageDialog(message, "Please select anyone to chat");
                                                }

                                                textAreaChat.setText("");
                                            }
                                        });
                                        // ------------------------------------

                                        btnChooseFile.addActionListener(new ActionListener() {
                                            @Override
                                            public void actionPerformed(ActionEvent e) {
                                                JFileChooser jFileChooser = new JFileChooser();
                                                jFileChooser.setDialogTitle("Choose file to send");
                                                int valueFile = jFileChooser.showOpenDialog(null);

                                                if(valueFile == JFileChooser.APPROVE_OPTION) {
                                                    fileSelect[0] = jFileChooser.getSelectedFile();
                                                    textAreaChat.setText("File" + fileSelect[0].getName());
                                                }
                                            }
                                        });


                                        // ------------------------------------

                                        btnLogOut.addActionListener(new ActionListener() {
                                            @Override
                                            public void actionPerformed(ActionEvent e) {
                                                client.close();
                                                frameCenter.dispose();
                                                frame1.setVisible(true);
                                            }
                                        });
                                        // ------------------------------------


                                        //frameCenter.add(jPanelCenterTop, BorderLayout.PAGE_START);
                                        frameCenter.add(jPanelName, BorderLayout.PAGE_START);
                                        frameCenter.add(jScrollPane, BorderLayout.CENTER);
                                        frameCenter.add(jPanelCenterBottom, BorderLayout.PAGE_END);
                                        frameCenter.add(jPanelCenterRight, BorderLayout.LINE_END);

                                        frameCenter.pack();
                                        frameCenter.setVisible(true);
                                        frameCenter.setDefaultCloseOperation(frameCenter.DISPOSE_ON_CLOSE);

                                        frameCenter.addWindowListener(new WindowAdapter() {
                                            @Override
                                            public void windowClosing(WindowEvent e) {
                                                client.close();
                                                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                                                //frame1.setVisible(true);
                                            }
                                        });

                                        client.listenMessage(textAreaShowMess, jListOline, oldMess, frameCenter, oldFile, frameCenter);
                                    }
                                    else {
                                        JFrame message = new JFrame("Message");
                                        JOptionPane.showMessageDialog(message, "user or password error!");
                                    }
                                } catch (UnknownHostException unknownHostException) {
                                    unknownHostException.printStackTrace();
                                    JFrame message = new JFrame("Message");
                                    JOptionPane.showMessageDialog(message, "Connection to server error!");
                                } catch (IOException ioException) {
                                    ioException.printStackTrace();
                                    JFrame message = new JFrame("Message");
                                    JOptionPane.showMessageDialog(message, "Connection to server error!");
                                }
                            }
                        };
                        th.start();
                    }
                });


                frame1.add(jPanelUserLogin);
                frame1.add(jPanelPassLogin);
                frame1.add(jPanelIP);
                frame1.add(jPanelPort);

                frame1.add(jPanelBottom);

                frame1.pack();
                frame1.setVisible(true);
                frame1.setDefaultCloseOperation(frame1.DISPOSE_ON_CLOSE);

                frame1.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        frame.setVisible(true);
                    }
                });
            }
        });

        add(btnLoginHome);
        add(btnRegisterHome);
    }

    private static void createAndShowGUI() throws IOException {
        //Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);

        //Create and set up the window.
        JFrame frame = new JFrame("SERVER");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        FrameClient frameClient = new FrameClient(frame);
        frameClient.setOpaque(true);

        frame.setContentPane(frameClient);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }



    public static void main(String[] args) throws IOException {
        createAndShowGUI();
    }
}
