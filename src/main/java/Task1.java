import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Callable;

class Task1 implements Callable<String> {

    private static String host = "192.168.88.99";
    private static int port = 1212;
    private String dataDB;
    private static Socket clientSocket = null;
    public Task1(String dataDB){
        this.dataDB = dataDB;
    }

    @Override
    public String call() {
        try {
            return SendAndGetFromHLI(dataDB);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public static String SendAndGetFromHLI(String dataDB){
        String print = "";
        try{
            clientSocket = new Socket(host,port);
            clientSocket.getOutputStream().write(dataDB.getBytes("UTF-8"));
            clientSocket.setKeepAlive(true);
            while (clientSocket.getInputStream().available() == 0) {
                Thread.sleep(100L);
            }
            byte[] data = new byte[clientSocket.getInputStream().available()];
            int bytes = clientSocket.getInputStream().read(data, 0, data.length);
            print = new String(data, 0, bytes, "UTF-8");//.substring(4,bytes);
            System.out.println("from server : "+print);
            dataDB = "";
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

/*
    private static String host = "192.168.88.98";
    private static int port = 1212;
    private static String dataDB;
    private static Socket clientSocket = null;
    private static String server = null;
    private static boolean connected = false;
    private static DataInputStream in = null;
    private static DataOutputStream out = null;
    private static String data = null;

    public Task1(String dataDB){
        this.dataDB = dataDB;
    }

    @Override
    public String call() {
        try {
            return connect(host,port);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
    static String Refresing(){
        try{
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());

            /*
            Thread thread = new Thread(){
                String msg = "";
                public void run() {
                    while(connected){
                        try {
                            msg = in.readUTF();
                            System.out.println(msg);
                            data = msg;
                        }catch(IOException e){
                            connected = false;
                            System.out.println("Reconnecting...");
                            while(!connected)
                                connect(server, port);
                        }
                    }
                }
            };
            thread.start();

            String msg = "";
            while(connected){
                try {
                    msg = in.readUTF();
                    System.out.println(msg);
                    data = msg;
                }catch(IOException e){
                    connected = false;
                    System.out.println("Reconnecting...");
                    while(!connected)
                        connect(server, port);
                }
            }
        }catch (Exception e){
            connected = false;
        }
        return data;
    }

    static String connect(String hostt, int portt){
        try{
            clientSocket = new Socket(hostt, portt);
            server = hostt;
            port = portt;
            connected=true;
            data = Refresing();
            System.out.println("Connected!");
            SendAndGetFromHLI(dataDB);
        }catch(Exception e){
            System.out.println("Can't connect !");
            connected = false;
        }
        return data;
    }
    public static String SendAndGetFromHLI(String dataDB){
        String print = "";
        System.out.println("Send data to server : "+dataDB);
        try {
            clientSocket = new Socket(host,port);
            clientSocket.getOutputStream().write(dataDB.getBytes("ASCII"));
            while (clientSocket.getInputStream().available() == 0) {
                Thread.sleep(100L);
            }
            byte[] data = new byte[clientSocket.getInputStream().available()];
            int bytes = clientSocket.getInputStream().read(data, 0, data.length);
            print = new String(data, 0, bytes, "ASCII");//.substring(4,bytes);
            System.out.println("from server : "+print);
            dataDB = "";
            return print;
        } catch (IOException ex) {
            return ex.getMessage();
        } catch (InterruptedException ie) {
            return ie.getMessage();
        } catch (Exception e) {
            return e.getMessage();
        }
    }
     */