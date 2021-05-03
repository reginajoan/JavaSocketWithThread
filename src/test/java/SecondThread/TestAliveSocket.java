package SecondThread;

import org.junit.Test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class TestAliveSocket {
    private static String host = "172.16.1.33";
    private static int port = 9000;
    private static Socket clientSocket = null;
    private static String server = null;
    private static boolean connected = false;
    private static DataInputStream in = null;
    private static DataOutputStream out = null;
    private static String data = null;
    static String dataDB = "0957ATMDPRLOAN60110220002004844602211520018992  20200615103547000133005400000133  NGTLNAP6064201240152425    7303AAA13481D50C                                                                                                                                                                                                                                                                                                                                                                        20200615103547                                                                                                                                                                                                                                                                                                                                                                                                                                                             -SendAdToHLI\"";
    @Test
    public void TestSocket() throws InterruptedException, IOException {
        //while (true)
        connect(host, port);
        //clearScreen();
    }

    static void clearScreen() throws IOException, InterruptedException {
        //Runtime.getRuntime().exec("cls");
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
    }

    static String Refresing(){
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
                    clearScreen();
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
            clearScreen();
            System.out.println("Connected!");
            data = Refresing();
            SendAndGetFromHLI(dataDB);
        }catch(Exception e){
            System.out.println("Can't connect !");
            connected = false;
        }
        return data;
    }
    public static String SendAndGetFromHLI(String dataDB) throws IOException {
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
        } catch (IOException ex) {
           ex.getMessage();
        } catch (InterruptedException ie) {
            ie.getMessage();
        } catch (Exception e) {
            e.getMessage();
        }finally {
            clientSocket.close();
        }
        return print;
    }
}
