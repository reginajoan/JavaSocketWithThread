import TestThread.ChekHostAvailable;
import TestThread.PrintDATA;
import TestThread.RunningPrograms;
import TestThread.Task;

import javax.swing.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;
public class FailOverSocket{
    private static boolean flag = false;

    public static void main(String[] args) throws Exception {
        RunningPrograms programs = new RunningPrograms();
        final ServerSocket ss = new ServerSocket(9000);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Callable<Void> callable = () -> {
            try {                    while (true) {
                    System.out.println("Waiting Transaction ..");
                    Socket clientSocket = ss.accept();
                    InetAddress inet = clientSocket.getInetAddress();
                    String net = inet.toString();
                    int port = clientSocket.getPort();
                    System.out.println("inet : " + net.replace("/", "") + "\nport : " + port);
                    clientSocket.setKeepAlive(true);
                    try {
                        while (clientSocket.getInputStream().available() == 0) {
                            Thread.sleep(100L);
                        }
                        byte[] data;
                        int bytes;
                        data = new byte[clientSocket.getInputStream().available()];
                        bytes = clientSocket.getInputStream().read(data, 0, data.length);
                        String dataDB = new String(data, 0, bytes, "UTF-8");
                        System.out.println("received data \ntime : " + new Date() + "\nlength data : " + dataDB.length());
                        System.out.println(dataDB);
                        String dataFromHobis = programs.getFromServer(dataDB);
                        //get and send data from data->hobis
                        clientSocket.getOutputStream().write(dataFromHobis.getBytes("UTF-8"));
                        List<String> printData = new ArrayList<String>();
                        printData.add(dataDB);
                        printData.add(dataFromHobis);
                        PrintDATA.saveDataTxtNew(printData);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        clientSocket.close();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    ss.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        };
        Future<Void> future = executorService.submit(callable);
        future.get();
    }
}