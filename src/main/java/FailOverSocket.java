import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;
public class FailOverSocket {
    private static boolean flag = false;

    public static void main(String[] args) throws Exception {
                try {
                    ServerSocket ss = new ServerSocket(9000);
                    do {
                        System.out.println("Waiting Transaction ..");
                        Socket clientSocket = ss.accept();
                        clientSocket.setKeepAlive(true);
                        while (clientSocket.getInputStream().available() == 0) {
                            Thread.sleep(100L);
                        }
                        byte[] data;
                        int bytes;
                        data = new byte[clientSocket.getInputStream().available()];
                        bytes = clientSocket.getInputStream().read(data,0,data.length);
                        String dataDB = new String(data, 0, bytes, "ASCII");
                        System.out.println(dataDB);
                        String dataFromHobis = getFromServer(dataDB);
                        //System.out.println("data from hobis " + dataFromHobis);
                        if(dataFromHobis != null){
                            clientSocket.getOutputStream().write(dataFromHobis.getBytes("ASCII"));
                        }
                    } while (true);
                } catch (Exception e) {
                    e.printStackTrace();
                }


        //String dataDB = "138ATMDBALINQ60110220002004844602211520018992  20200615103601000133001400002508  NBALHNBIDR    OA                484                                                                                                         4602211520018992=1225                                                                                                                                                                       0CECDB747795EE83                                                           20200615103601                                                        MAGSTRIPE                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          -SendAdToHLI\"";
        //dataDB = "0957ATMDPRLOAN60110220002004844602211520018992  20200615103547000133005400000133  NGTLNAP6064201240152425    7303AAA13481D50C                                                                                                                                                                                                                                                                                                                                                                        20200615103547                                                                                                                                                                                                                                                                                                                                                                                                                                                             -SendAdToHLI\"";
        //getFromServer(dataDB);
    }

    public static String getFromServer(String dataDB) throws Exception {
        final int timeout = 10;
        String data = "";

        //while (true){
        data = RunningProgram(dataDB, timeout);
        if(flag){
            flag = false;
            return data;
        }else {
            flag = true;
            return RunningProgram1(dataDB, timeout);
        }
    }
    public static String RunningProgram(String dataDB, int timeout) throws Exception{
        String getFromHli = "";
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(new Task(dataDB));

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
            return e.getMessage();
        }
    }
    public static String RunningProgram1(String dataDB, int timeout) throws Exception{
        String getFromHli = "";
        ExecutorService executor = Executors.newSingleThreadExecutor();
        //Executors.
        Future<String> future = executor.submit(new Task1(dataDB));
        try {
            System.out.println("Server 2 running");
            System.out.println("Started..");
            //System.out.println(future.get(5, TimeUnit.SECONDS));
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
            return e.getMessage();
        }
    }
}