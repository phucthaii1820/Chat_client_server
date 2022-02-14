package vn.edu.hcmus._19127514.Chat_client_server;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * vn.edu.hcmus._19127514.Chat_client_server
 * Created by phucthaii1820 - 19127514
 * Date 20/12/2021 - 16:00
 * Description: ...
 */
public class ThreadClient implements Runnable{
    JTextArea textNotification;
    public static ArrayList<ThreadClient> threadClients = new ArrayList<>();
    public static ArrayList<String> listUser = new ArrayList<>();
    Socket socket;
    String username;
    String pass;
    BufferedReader br;
    BufferedWriter bw;
    DataOutputStream dataOut;
    DataInputStream dataIn;
    //ObjectOutputStream objO;

    public ThreadClient(Socket socket, JTextArea textNotification, Map<String, String> list) throws IOException {
        try {
            this.socket = socket;
            br = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            bw = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
            this.dataIn = new DataInputStream(new BufferedInputStream(this.socket.getInputStream()));
            this.dataOut = new DataOutputStream(new BufferedOutputStream(this.socket.getOutputStream()));
            //objO = new ObjectOutputStream(this.socket.getOutputStream());

            this.textNotification = textNotification;

            //first mess
            String mess = br.readLine();
            System.out.println(mess);
            if(mess.equals("@19127514@@Login@@hcmus@")) {
                this.username = br.readLine();
                this.pass = br.readLine();

                System.out.println(username + ":" +pass);

                if(list.get(username).equals(pass)) {
                    bw.write("@@LoginSuccess@@");
                }
                else {
                    bw.write("@@LoginFail@@");
                    bw.close();
                    br.close();
                    socket.close();

                    return;
                }
                bw.newLine();
                bw.flush();
            }
            else if (mess.equals("@19127514@@Register@@hcmus@")) {
                this.username = br.readLine();
                this.pass = br.readLine();

                System.out.println(username + ":" +pass);

                if(list.get(username) == null) {
                    list.put(this.username, this.pass);
                    bw.write("@@RegisterSuccess@@");
                    this.textNotification.append(username + " register success!\n");
                }
                else  {
                    bw.write("@@RegisterFail@@");
                    this.textNotification.append(username + " register fail!\n");
                }

                bw.newLine();
                bw.flush();

                bw.close();
                br.close();
                socket.close();
                return;
            }

            textNotification.append(username + " is connect! \n");
            threadClients.add(this);
            listUser.add(username);

            sendListClient();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void run() {
        String messFromClient;
        while (true) {
            try {
                messFromClient = br.readLine();

                if(messFromClient.equals("@19127514@@Quit@@hcmus@")) {
                    listUser.remove(username);
                    threadClients.remove(this);
                    sendListClient();

                    bw.write("@19127514@@Quit@@hcmus@");
                    bw.newLine();
                    bw.flush();

                    socket.close();
                    br.close();
                    bw.close();

                    textNotification.append(username + " quit!\n");

                    return;
                }
                else {
                    System.out.println(messFromClient);
                    sendAllClient(messFromClient);
                }
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }

        }
    }

    public void sendAllClient(String mess) {
        for(ThreadClient client: threadClients) {
            try{
                if(mess.contains("@19127514@@sendFile@@hcmus@")) {
                    String[] arr = mess.split("@19127514@@sendFile@@hcmus@");
                    if(client.username.equals(arr[0])) {
                        client.bw.write(mess);
                        client.bw.newLine();
                        client.bw.flush();

                        Thread.sleep(200);

                        int fileSize = Integer.parseInt(arr[3]);

                        byte[] fileSend = new byte[fileSize];
                        dataIn.readFully(fileSend, 0, fileSize);

                        System.out.println(fileSend);

                        client.dataOut.write(fileSend);
                        client.dataOut.flush();

                    }
                }
                else {
                    String[] arr = mess.split("@19127514@@send@@hcmus@");
                    if(client.username.equals(arr[0])) {
                        client.bw.write(mess);
                        client.bw.newLine();
                        client.bw.flush();
                    }
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendListClient() {
        for(ThreadClient client: threadClients) {
            try{
                client.bw.write("19127514@@list@@hcmus");
                client.bw.newLine();
                client.bw.flush();

                Thread.sleep(200);

                String count = Integer.toString(listUser.size());

                client.bw.write(count);
                client.bw.newLine();
                client.bw.flush();

                for(int i = 0; i < listUser.size(); i++) {
                    client.bw.write(listUser.get(i));
                    client.bw.newLine();
                    client.bw.flush();
                    Thread.sleep(50);
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void CloseServer() {
        try {
            for(ThreadClient client: threadClients) {
                client.bw.write("@19127514@@quit@@hcmus@");
                client.bw.newLine();
                client.bw.flush();

                client.socket.close();
                client.br.close();
                client.bw.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
