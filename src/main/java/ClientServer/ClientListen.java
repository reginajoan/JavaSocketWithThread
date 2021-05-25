package ClientServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientListen {
    private static String host;
    private static int port;
    private static Socket clientSocket = null;
    private static boolean connected = false;
    private static DataInputStream in = null;
    private static DataOutputStream out = null;
    private static String data = null;

    public ClientListen(String host, int port){
        this.host = host;
        this.port = port;

        connect(host, port);
    }

    public static String Refresing(){
        try{
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());

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
                        connect(host, port);
                }
            }
        }catch (Exception e){
            connected = false;
        }
        return data;
    }
    public static String connect(String host, int port){
        try{
            clientSocket = new Socket(host, port);
            connected=true;
            System.out.println("Connected!");
            data = Refresing();
        }catch(Exception e){
            System.out.println("Can't connect !");
            connected = false;
        }
        return data;
    }
}
