package ClientServer;

import TestThread.PrintDATA;
import TestThread.RunningPrograms;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Worker extends RunningPrograms implements Runnable {
    private Integer id;
    private final Socket clientSocket;

    public Worker(Integer id, Socket client) {
        this.id = id;
        this.clientSocket = client;
    }

    @Override
    public void run() {
        try {
            listenClientMessages();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void listenClientMessages() throws IOException {
        final int MAX_INPUT = 1500;
        int read;
        InetAddress inet = clientSocket.getInetAddress();
        String net = inet.toString();
        int port = clientSocket.getPort();
        System.out.println("inet : " + net.replace("/", "") + "\nport : " + port);
        try (InputStream is = clientSocket.getInputStream()) {
            byte[] buf = new byte[MAX_INPUT];
            while ((read = is.read(buf)) != -1) {
                // dataDB is where data save temporary
                String dataDB = new String(buf, 0, read, "ASCII");
                System.out.println("received data\ntime : " + new Date() + "\nlength data : " + dataDB.length());
                System.out.println(dataDB);
                String dataFromHobis = null;
                // getFromServer is method for send data to HLi and save temporary in
                // dataFromHobis
                dataFromHobis = RunningPrograms.getFromServer(dataDB);
                try {
                    if (dataFromHobis != null) {
                        System.out.println(
                                "reply to : " + clientSocket.getRemoteSocketAddress().toString().replace("/", ""));
                        System.out.println("message from hobis : " + dataFromHobis);
                        clientSocket.getOutputStream().write(dataFromHobis.getBytes("ASCII"));
                    }
                    System.out.println("Success!");
                } catch (Exception e) {
                    System.out.println("data from hobis : " + dataFromHobis);
                }
                List<String> printData = new ArrayList<String>();
                printData.add(dataDB);
                printData.add(dataFromHobis);
                PrintDATA.saveDataTxtTest(printData, dataDB.substring(18, 26));
            }
            clientSocket.close();
        } catch (ConnectException ce) {
            System.out.println("Connection refused!");
        } catch (SocketException se) {
            System.out
                    .println("Loss connection with Internet Address : " + net.replace("/", "") + " and port : " + port);
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (clientSocket != null || !clientSocket.isClosed()) {
                try {
                    clientSocket.close();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}