import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.Callable;

class Task implements Callable<String> {
    private static String host;
    private static int port;
    private String dataDB;
    private static Socket clientSocket = null;

    public Task(String dataDB, String host, int port){
        this.dataDB = dataDB;
        this.host = host;
        this.port = port;
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

    public String SendAndGetFromHLI(String dataDB) throws IOException, InterruptedIOException {
        String print = "";
        try{
            clientSocket = new Socket(host,port);
            clientSocket.getOutputStream().write(dataDB.getBytes("UTF-8"));
            clientSocket.setKeepAlive(true);
            while(clientSocket.getKeepAlive()){
                while (clientSocket.getInputStream().available() == 0) {
                    Thread.sleep(100L);
                }
                byte[] data = new byte[clientSocket.getInputStream().available()];
                int bytes = clientSocket.getInputStream().read(data, 0, data.length);
                print = new String(data, 0, bytes, "UTF-8");//.substring(4,bytes);
                System.out.println("from server : "+print);
                dataDB = "";
            }
            clientSocket.close();
            return print;
        } catch (IOException ex) {
            ex.printStackTrace();
            return ex.getMessage();
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            return ie.getMessage();
        } catch (Exception e) {
            e.getMessage();
            return e.getMessage();
        }
    }
}