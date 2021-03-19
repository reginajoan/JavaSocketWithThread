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
        ServerSocket ss = new ServerSocket(9000);
        try {
            while(true) {
                System.out.println("Waiting Transaction ..");
                Socket clientSocket = ss.accept();
                InetAddress inet = clientSocket.getInetAddress();
                String net = inet.toString();
                int port = clientSocket.getPort();
                System.out.println("inet : "+net.replace("/","") +"\nport : "+port);
                clientSocket.setKeepAlive(true);
                try{
                    while (clientSocket.getInputStream().available() == 0) {
                        Thread.sleep(100L);
                    }
                    byte[] data;
                    int bytes;
                    data = new byte[clientSocket.getInputStream().available()];
                    bytes = clientSocket.getInputStream().read(data,0,data.length);
                    String dataDB = new String(data, 0, bytes, "UTF-8");
                    System.out.println("length data : " + dataDB.length());
                    System.out.println(dataDB);
                    String dataFromHobis = getFromServer(dataDB);
                    //get and send data from data->hobis
                    clientSocket.getOutputStream().write(dataFromHobis.getBytes("UTF-8"));
                    List<String> printData = new ArrayList<String>();
                    printData.add(dataDB);
                    printData.add(dataFromHobis);
                    PrintDATA.saveDataTxtNew(printData);
                    dataFromHobis = "";
                    dataDB = "";
                }catch(Exception e){
                    e.printStackTrace();
                }finally {
                    clientSocket.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ss.close();
        }
    }

    public static String getFromServer(String dataDB) throws Exception {
        final int timeout = 15;
        String data = "";

        data = RunningProgram(dataDB, timeout);
        if(flag){
            return data;
        }else {
            return RunningProgram1(dataDB, timeout);
        }
    }
    public static String RunningProgram(String dataDB, int timeout) throws Exception{
        final String host = "192.168.88.98";
        final int port = 32000;
        String getFromHli = "";
        ChekHostAvailable chek = new ChekHostAvailable();
        System.out.println("Server 1 running");
        if(!chek.isSocketAliveUitlitybyCrunchify(host,port)){
            flag = false;
            return "Connection Refused!!!";
        }
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(new Task(dataDB, host, port));
        try {
            System.out.println("Started..");
            getFromHli = future.get(timeout, TimeUnit.SECONDS);
            System.out.println("Finished!");
            flag = true;
            executor.shutdownNow();
            return getFromHli;
        } catch (Exception e) {
            future.cancel(true);
            System.out.println("Terminated!");
            flag = false;
            executor.shutdownNow();
            e.getMessage();
            return getFromHli;
        }
    }

    public static String RunningProgram1(String dataDB, int timeout) throws Exception{
        String host = "192.168.88.99";
        int port = 1212;
        String getFromHli = "";
        ChekHostAvailable chek = new ChekHostAvailable();
        System.out.println("Server 2 running");
        if(!chek.isSocketAliveUitlitybyCrunchify(host,port)){
            flag = false;
            return "Connection Refused!!!";
        }
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(new Task1(dataDB, host, port));
        try {
            System.out.println("Started..");
            getFromHli = future.get(timeout, TimeUnit.SECONDS);
            System.out.println("Finished!");
            flag = true;
            executor.shutdownNow();
            return getFromHli;
        } catch (Exception e) {
            future.cancel(true);
            System.out.println("Terminated!");
            flag = false;
            executor.shutdownNow();
            e.getMessage();
            System.out.println("Timeout!");
            return getFromHli;
        }
    }
}
