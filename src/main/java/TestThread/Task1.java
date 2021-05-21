package TestThread;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
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

    private String SendAndGetFromHLI(String dataDB) throws IOException {
        String print = null;
        try {
            clientSocket = new Socket(host, port);
            clientSocket.getOutputStream().write(dataDB.getBytes("ASCII"));
            while (clientSocket.getInputStream().available() == 0) {
                Thread.sleep(100L);
            }
            byte[] data = new byte[clientSocket.getInputStream().available()];
            int bytes = clientSocket.getInputStream().read(data, 0, data.length);
            print = new String(data, 0, bytes, "ASCII"); // .substring(4,bytes);
            clientSocket.close();
        } catch (ConnectException ce) {
            System.out.println("Connection refused!");
        } catch (SocketException se) {
            System.out.println("Socket closed!");
        } catch (IOException ex) {
            if (clientSocket != null && !clientSocket.isClosed()) {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace(System.err);
                }
            }
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            e.getMessage();
        } finally {
            try {
                if (clientSocket != null && !clientSocket.isClosed()) {
                    clientSocket.close();
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        return print;
    }
}