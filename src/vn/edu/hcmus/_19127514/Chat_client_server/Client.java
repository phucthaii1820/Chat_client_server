package vn.edu.hcmus._19127514.Chat_client_server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.List;

/**
 * vn.edu.hcmus._19127514.Chat_client_server
 * Created by phucthaii1820 - 19127514
 * Date 20/12/2021 - 16:01
 * Description: ...
 */
public class Client {
    BufferedReader br;
    BufferedWriter bw;
    DataOutputStream dataOut;
    DataInputStream dataIn;
    Socket s;
    String username;
    String pass;
    ArrayList<String> listOnline;
    boolean isLogin = false;

    public String getUsername() {
        return username;
    }

    public Client(Socket s, String username, String pass) throws IOException {
        this.br = new BufferedReader(new InputStreamReader(s.getInputStream()));
        this.bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
        this.dataIn = new DataInputStream(new BufferedInputStream(s.getInputStream()));
        this.dataOut = new DataOutputStream(new BufferedOutputStream(s.getOutputStream()));
        this.s = s;
        this.username = username;
        this.pass = pass;
        listOnline = new ArrayList<>();
    }

    public boolean Register() {
        try {
            bw.write("@19127514@@Register@@hcmus@");
            bw.newLine();
            bw.flush();

            Thread.sleep(200);

            bw.write(username);
            bw.newLine();
            bw.flush();

            Thread.sleep(200);

            bw.write(pass);
            bw.newLine();
            bw.flush();

            String mess = br.readLine();
            if(mess.equals("@@RegisterSuccess@@")) {
                System.out.println("register Success");
                s.close();
                bw.close();
                br.close();
                return true;
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean checkLogin() {
        try {
            bw.write("@19127514@@Login@@hcmus@");
            bw.newLine();
            bw.flush();

            if(!isLogin) {
                bw.write(username);
                bw.newLine();
                bw.flush();

                Thread.sleep(200);

                bw.write(pass);
                bw.newLine();
                bw.flush();

                String mess = br.readLine();
                if(mess.equals("@@LoginSuccess@@")) {
                    System.out.println("login Success");
                    return true;
                }
                isLogin = true;
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        try {
            s.close();
            bw.close();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void send(String mess, JPanel textArea, String name, List<String> oldMess, File fileSelect, JFrame frame, List<byte[]> oldFile) {
        try {
            if(fileSelect != null) {
                FileInputStream fis = new FileInputStream(fileSelect);

                byte[] bytesFile = new byte[(int) fileSelect.length()];
                fis.read(bytesFile);

                bw.write(name + "@19127514@@sendFile@@hcmus@" + username + "@19127514@@sendFile@@hcmus@" + fileSelect.getName() +  "@19127514@@sendFile@@hcmus@" + bytesFile.length);
                bw.newLine();
                bw.flush();

                Thread.sleep(200);

                dataOut.write(bytesFile);
                dataOut.flush();
                oldFile.add(bytesFile);
                oldMess.add(name + "@19127514@@sendFile@@hcmus@" + username + "@19127514@@sendFile@@hcmus@" + fileSelect.getName() +  "@19127514@@sendFile@@hcmus@" + bytesFile.length + "@19127514@@sendFile@@hcmus@" + (oldFile.size() - 1));

                fis.close();

                JButton jButton = new JButton("Me" + " send file " + fileSelect.getName());

                jButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            JFileChooser fileChooser = new JFileChooser();
                            fileChooser.setDialogTitle("Download location");
                            int valueFile = fileChooser.showSaveDialog(null);

                            if(valueFile == JFileChooser.APPROVE_OPTION) {
                                String temp = fileSelect.getName();
                                String[] arrNameFile = temp.split("\\.");
                                System.out.println(fileChooser.getSelectedFile() + arrNameFile[arrNameFile.length -1]);
                                File file = new File(fileChooser.getSelectedFile() + "." + arrNameFile[arrNameFile.length -1]);
                                FileOutputStream fileOutputStream = new FileOutputStream(file);
                                fileOutputStream.write(bytesFile);
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
                textArea.add(jButton);
                frame.validate();
            }
            else {
                JLabel label = new JLabel("ME: " + mess);
                textArea.add(label);
                frame.validate();
                mess = name + "@19127514@@send@@hcmus@"+username+"@19127514@@send@@hcmus@"+mess;
                oldMess.add(mess);
                bw.write(mess);
                bw.newLine();
                bw.flush();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void listenMessage(JPanel textArea, JList jListOline, List<String> oldMess, JFrame frame, List<byte[]> oldFile, JFrame frameCenter) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String mess = "";

                while (s.isConnected()) {
                    try {
                        mess = br.readLine();
                        if(mess != null) {
                            if(mess.equals("19127514@@list@@hcmus")) {
                                listOnline.removeAll(listOnline);
                                String index = br.readLine();

                                for(int i = 0; i < Integer.parseInt(index); i++) {
                                    String userOther = br.readLine();
                                    if(!userOther.equals(username)) {
                                        listOnline.add(userOther);
                                    }
                                }

                                Vector<String> e = new Vector<>(listOnline);
                                jListOline.setListData(e);
                            }
                            else if (mess.contains("@19127514@@send@@hcmus@")){
                                System.out.println(mess);
                                String[] arrString = mess.split("@19127514@@send@@hcmus@");
                                if(jListOline.getSelectedValue() != null) {
                                    if(jListOline.getSelectedValue().toString().equals(arrString[1])) {
                                        JLabel label = new JLabel(arrString[1] + ": " + arrString[2]);
                                        textArea.add(label);
                                        frame.validate();
                                    }
                                    else {
                                    }
                                }
                                oldMess.add(mess);
                            }
                            else if(mess.contains("@19127514@@quit@@hcmus@")) {
                                s.close();
                                br.close();
                                bw.close();
                                System.out.println("quit");

                                JFrame message = new JFrame("Message");
                                JOptionPane.showMessageDialog(message, "Server disconnect");

                                frameCenter.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                                return;
                            }
                            else if(mess.contains("@19127514@@sendFile@@hcmus@")) {
                                String[] arr = mess.split("@19127514@@sendFile@@hcmus@");

                                System.out.println(mess);

                                int fileSize = Integer.parseInt(arr[3]);

                                byte[] fileSend = new byte[fileSize];
                                dataIn.readFully(fileSend, 0, fileSize);

                                oldFile.add(fileSend);
                                oldMess.add(mess + "@19127514@@sendFile@@hcmus@" + (oldFile.size() - 1));


                                if(jListOline.getSelectedValue() != null) {
                                    if(jListOline.getSelectedValue().toString().equals(arr[1])) {
                                        JButton jButton = new JButton(arr[1] + " send file " + arr[2]);

                                        jButton.addActionListener(new ActionListener() {
                                            @Override
                                            public void actionPerformed(ActionEvent e) {
                                                try {
                                                    JFileChooser fileChooser = new JFileChooser();
                                                    fileChooser.setDialogTitle("Download location");
                                                    int valueFile = fileChooser.showSaveDialog(null);

                                                    if(valueFile == JFileChooser.APPROVE_OPTION) {
                                                        String temp = arr[2];
                                                        String[] arrNameFile = temp.split("\\.");
                                                        System.out.println(arrNameFile.length);
                                                        System.out.println(fileChooser.getSelectedFile() + arrNameFile[arrNameFile.length -1]);
                                                        File file = new File(fileChooser.getSelectedFile() + "." + arrNameFile[arrNameFile.length -1]);
                                                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                                                        fileOutputStream.write(fileSend);
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
                                        textArea.add(jButton);
                                        frame.validate();
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void close() {
        try {
            bw.write("@19127514@@Quit@@hcmus@");
            bw.newLine();
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String [] args) throws IOException {
//        Scanner sc = new Scanner(System.in);
//        Socket s = new Socket("localhost",3311);
//
//        String username = sc.nextLine();
//        String pass = sc.nextLine();
//
//        Client client = new Client(s, username, pass);
//        client.listenMessage();
        //client.send();
    }
}
