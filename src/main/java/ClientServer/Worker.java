package ClientServer;

import TestThread.PrintDATA;
import TestThread.RunningPrograms;
import java.io.IOException;
import java.io.InputStream;
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
        System.out.println("inet : "+net.replace("/","") +"\nport : "+port);
        try (InputStream is = clientSocket.getInputStream()) {
            byte[] buf = new byte[MAX_INPUT];
            while ((read = is.read(buf)) != -1) {
                String dataDB = new String(buf, 0, read, "UTF-8");
                System.out.println("received data\ntime : "+ new Date() +"\nlength data : " + dataDB.length());
                System.out.println(dataDB);
                String dataFromHobis = null;
                //get and send data from data->hobis
                /*
                if(clientSocket.getInetAddress().toString().equals("/172.16.1.244") || clientSocket.getInetAddress().toString().equals("/172.16.1.243")){
                    dataFromHobis = programs.getFromServer(dataDB);
                }
               else{
                    dataFromHobis = RunningPrograms.RunningProgram1(dataDB, 10, "172.16.1.244",9400);
                }*/
                dataFromHobis = getFromServer(dataDB);
                String fileName = dataDB.substring(18,26);
                if(dataFromHobis == null){
                    System.out.println("error null return");
                    System.out.println("Waiting transaction !!!");
                }else if(dataFromHobis != null){
                    System.out.println(clientSocket.getRemoteSocketAddress().toString());
                    System.out.println(clientSocket.getInetAddress().getHostName());
                    System.out.println("reply to : "+clientSocket.getInetAddress().getHostAddress());

                    clientSocket.getOutputStream().write(dataFromHobis.getBytes("UTF-8"));
                }
                List<String> printData = new ArrayList<String>();
                printData.add(dataDB);
                printData.add(dataFromHobis);
                PrintDATA.saveDataTxtTest(printData, dataDB.substring(18,26));
                System.out.println("Waiting transaction !!!");
            }
        }catch (SocketException se){
            System.out.println("Reset Connection \nInternet Address : "+net.replace("/","") +" and port : "+port);
        }catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            clientSocket.close();
        }
    }
}