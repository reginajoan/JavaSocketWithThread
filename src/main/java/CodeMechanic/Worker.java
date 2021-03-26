package CodeMechanic;

import TestThread.PrintDATA;
import TestThread.RunningPrograms;

import java.io.IOException;
import java.io.InputStream;
import java.net.BindException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Worker implements Runnable {
    private Integer id;
    private final Socket clientSocket;

    public Worker(Integer id, Socket client) {
        this.id = id;   //Ignore it, it was for logging purpose, how many joined
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
        RunningPrograms programs = new RunningPrograms();
        InetAddress inet = clientSocket.getInetAddress();
        String net = inet.toString();
        int port = clientSocket.getPort();
        System.out.println("inet : "+net.replace("/","") +"\nport : "+port);
        try (InputStream is = clientSocket.getInputStream()) {
            byte[] buf = new byte[MAX_INPUT];
            while ((read = is.read(buf)) != -1) {
                String dataDB = new String(buf, 0, read, "UTF-8");
                System.out.println("received data\n time : "+ new Date() +"\nlength data : " + dataDB.length());
                System.out.println(dataDB);
                String dataFromHobis = programs.getFromServer(dataDB);
                //get and send data from data->hobis
                String message = dataFromHobis;
                clientSocket.getOutputStream().write(dataFromHobis.getBytes("UTF-8"));
                List<String> printData = new ArrayList<String>();
                printData.add(dataDB);
                printData.add(dataFromHobis);
                PrintDATA.saveDataTxtNew(printData);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            clientSocket.close();
        }
    }
}

