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
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        PrintDATA print = new PrintDATA();
        ServerSocket ss = new ServerSocket(9000);

        try {
            while(true) {
                long mili = date.getTime();
                System.out.println(mili);
                String tgl = date.toString();
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
                    sendPingRequest("172.16.1.244");
                    String dataFromHobis = getFromServer(dataDB);
                    //get and send data from data->hobis
                    clientSocket.getOutputStream().write(dataFromHobis.getBytes("UTF-8"));
                    List<String> printData = new ArrayList<String>();
                    printData.add(tgl+" "+dataDB);
                    printData.add(tgl+" "+dataFromHobis);
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
        String getFromHli = "";
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(new Task1(dataDB));

        try {
            System.out.println("Server 1 running");
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
        String getFromHli = "";
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(new Task(dataDB));
        try {
            System.out.println("Server 2 running");
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
    public static void sendPingRequest(String ipAddress)
            throws UnknownHostException, IOException
    {
        InetAddress geek = InetAddress.getByName(ipAddress);
        System.out.println("Sending Ping Request to " + ipAddress);
        if (geek.isReachable(5000))
            System.out.println("Host is reachable");
        else
            System.out.println("Sorry ! We can't reach to this host");
    }

    public static String SendToAd(String host, int port, String dataDB){
        Socket clientSocket = null;
        String print = "";
        try{
            clientSocket = new Socket(host,port);
            clientSocket.getOutputStream().write(dataDB.getBytes("ASCII"));
            while (clientSocket.getKeepAlive()){
                while (clientSocket.getInputStream().available() == 0) {
                    Thread.sleep(100L);
                }
                byte[] data = new byte[clientSocket.getInputStream().available()];
                int bytes = clientSocket.getInputStream().read(data, 0, data.length);
                print = new String(data, 0, bytes, "ASCII");//.substring(4,bytes);
                System.out.println("from server : "+print);
            }
            return print;
        } catch (IOException ex) {
            return ex.getMessage();
        } catch (InterruptedException ie) {
            return ie.getMessage();
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
