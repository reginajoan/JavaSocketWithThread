package TestThread;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.Socket;
import java.util.concurrent.Callable;

class Task1 implements Callable<String> {
    private static String host;
    private static int port;
    private String dataDB;
    private static Socket clientSocket = null;

    public Task1(String dataDB, String host, int port){
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

    private String SendAndGetFromHLI(String dataDB) throws IOException, InterruptedIOException {
        String print = "";
        try{
            clientSocket = new Socket(host,port);
            clientSocket.getOutputStream().write(dataDB.getBytes("UTF-8"));
                while (clientSocket.getInputStream().available() == 0) {
                    Thread.sleep(100L);
                }
                byte[] data = new byte[clientSocket.getInputStream().available()];
                int bytes = clientSocket.getInputStream().read(data, 0, data.length);
                print = new String(data, 0, bytes, "UTF-8");//.substring(4,bytes);
                System.out.println("from server : "+print);
        } catch (IOException ex) {
            ex.printStackTrace();
            ex.getMessage();
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            ie.getMessage();
        } catch (Exception e) {
            e.getMessage();
        }finally{
            clientSocket.close();
        }
        return print;
    }
}