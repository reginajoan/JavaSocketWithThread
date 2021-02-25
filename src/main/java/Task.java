import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.Callable;

class Task implements Callable<String> {
    private static String host = "192.168.88.99";
    private static int port = 32000;
    private String dataDB;
    private static Socket clientSocket = null;
    public Task(String dataDB){
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

    public String SendAndGetFromHLI(String dataDB) throws IOException {
        sendPingRequest(host);
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
            ex.printStackTrace();
            return print;
        } catch (InterruptedException ie) {
            ie.printStackTrace();
            return print;
        } catch (Exception e) {
            e.getMessage();
            return print;
        }
    }

    public static void sendPingRequest(String ipAddress)
            throws UnknownHostException, IOException
    {
        InetAddress geek = InetAddress.getByName(ipAddress);
        System.out.println("Sending Ping Request to " + ipAddress);
        if (geek.isReachable(1000))
            System.out.println("Host is reachable");
        else
            System.out.println("Sorry ! We can't reach to this host");
    }
}