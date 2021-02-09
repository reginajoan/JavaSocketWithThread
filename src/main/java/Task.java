import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Callable;

class Task implements Callable<String> {
    private static String host = "172.16.1.243";
    private static int port = 9400;
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
            return "err call";
        }
    }

    public static String SendAndGetFromHLI(String dataDB){
        String print = "";
        try{
            clientSocket = new Socket(host,port);
            clientSocket.getOutputStream().write(dataDB.getBytes("ASCII"));
            clientSocket.setKeepAlive(true);
            while (clientSocket.getInputStream().available() == 0) {
                Thread.sleep(100L);
            }
            byte[] data = new byte[clientSocket.getInputStream().available()];
            int bytes = clientSocket.getInputStream().read(data, 0, data.length);
            print = new String(data, 0, bytes, "ASCII");//.substring(4,bytes);
            System.out.println("from server : "+print);
            clientSocket.close();
            return print;
        } catch (IOException ex) {
            return ex.getMessage();
        } catch (InterruptedException ie) {
            return ie.getMessage();
        } catch (Exception e) {
            return e.getMessage();
        }
    }
    public static void close() throws IOException {
        clientSocket.close();
    }
}