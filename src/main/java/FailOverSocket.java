import javax.swing.*;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;
public class FailOverSocket{
    private static boolean flag = false;

    public static void main(String[] args) throws Exception {
        //JTextField textField = new JTextField(50);

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();


        PrintDATA print = new PrintDATA();
        ServerSocket ss = new ServerSocket(9000);

        try {
            while(true) {
                long mili = date.getTime();
                System.out.println(mili);
                String tgl = date.toString();
                System.out.println("Waiting Transaction ..");
                Socket clientSocket = ss.accept();

                InetAddress inet = clientSocket.getInetAddress();
                String net = inet.toString();
                int port = clientSocket.getPort();
                System.out.println("inet : "+net.replace("/","") +"\nport : "+port);
                clientSocket.setKeepAlive(true);
                try{
                    while (clientSocket.getInputStream().available() == 0) {
                        Thread.sleep(100L);
                    }
                    byte[] data;
                    int bytes;
                    data = new byte[clientSocket.getInputStream().available()];
                    bytes = clientSocket.getInputStream().read(data,0,data.length);
                    String dataDB = new String(data, 0, bytes, "UTF-8");
                    System.out.println("length data : " + dataDB.length());
                    long startTime = mili;
                    System.out.println(dataDB);
                    sendPingRequest("192.168.88.99");
                    String dataFromHobis = SendToAd("192.168.88.99",1212,dataDB);//getFromServer(dataDB);
                    //get and send data from data->hobis
                    clientSocket.getOutputStream().write(dataFromHobis.getBytes("UTF-8"));
                    List<String> printData = new ArrayList<String>();
                    printData.add(tgl+" "+dataDB);
                    printData.add(tgl+" "+dataFromHobis);

                    print.saveDataTxt(printData);

                    //print.setPrintATM(tgl,startTime,dataFromHobis);
                    print.printMsgToHli(tgl,startTime,dataDB);
                    //SendToAd(net.replace("/",""),port,dataFromHobis);
                    long endTime = date.getTime();
                    long rangeTime = startTime - endTime;
                    System.out.println("start time : "+ startTime+"\nend time: "+endTime+"\ntime arrange : "+rangeTime);
//                    while (startTime < endTime){
//                        Thread.sleep(rangeTime);
//                        //SendAndGetFromHLI("");
//                        startTime++;
//                    }

                    dataFromHobis = "";
                    dataDB = "";
                }catch(Exception e){
                    e.printStackTrace();
                }finally {
                    //clientSocket.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //ss.close();
        }
        /*
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
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
                }
            });
            t.start();

         */

        //String dataDB = "138ATMDBALINQ60110220002004844602211520018992  202006151036010001330 01400002508  NBALHNBIDR    OA                484                                                                                                         4602211520018992=1225                                                                                                                                                                       0CECDB747795EE83                                                           20200615103601                                                        MAGSTRIPE                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          -SendAdToHLI\"";
        //dataDB = "0957ATMDPRLOAN60110220002004844602211520018992  20200615103547000133005400000133  NGTLNAP6064201240152425    7303AAA13481D50C                                                                                                                                                                                                                                                                                                                                                                        20200615103547                                                                                                                                                                                                                                                                                                                                                                                                                                                             -SendAdToHLI\"";
        //dataDB = "1386ATMDBALINQ60110210200004846064201620716526  20210301104258000336200000000336  NBALHNBIDR    OA                484                                                                                                         6064201620716526=24122200199999999999                                                                                                                                                       D103CCF1EE1A4A53                                                           20210301104258                                                        5CAM000482027400950580800480005F2A0203605F3401019A032103019C01309F02060000000000009F03060000000000009F101C0101A000000000E108FEC200000000000000000000000000000000009F1A0203609F2608A9B1DA310E15C43D9F3602005B9F370430303031                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        -SendAdToHLI\"";
        //SendToAd("192.168.88.99",1212,dataDB);
    }
    public static String getFromServer(String dataDB) throws Exception {
        final int timeout = 10;
        String data = "";

        data = RunningProgram(dataDB, timeout);
        if(flag){
            return data;
        }else {
            return RunningProgram1(dataDB, timeout);
        }
    }

    public static String RunningProgram(String dataDB, int timeout) throws Exception{
        String getFromHli = "";
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(new Task1(dataDB));

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
            e.getMessage();
            return getFromHli;
        }
    }

    public static String RunningProgram1(String dataDB, int timeout) throws Exception{
        String getFromHli = "";
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(new Task(dataDB));
        try {
            System.out.println("Server 2 running");
            System.out.println("Started..");
            getFromHli = future.get(timeout, TimeUnit.SECONDS);
            System.out.println("Finished!");
            flag = true;
            return getFromHli;
        } catch (Exception e) {
            future.cancel(true);
            System.out.println("Terminated!");
            flag = false;
            executor.shutdownNow();
            e.getMessage();
            return getFromHli;
        }
    }
    public static void sendPingRequest(String ipAddress)
            throws UnknownHostException, IOException
    {
        InetAddress geek = InetAddress.getByName(ipAddress);
        System.out.println("Sending Ping Request to " + ipAddress);
        if (geek.isReachable(5000))
            System.out.println("Host is reachable");
        else
            System.out.println("Sorry ! We can't reach to this host");
    }

    public static String SendToAd(String host, int port, String dataDB){
        Socket clientSocket = null;
        String print = "";
        try{
            clientSocket = new Socket(host,port);
            clientSocket.getOutputStream().write(dataDB.getBytes("ASCII"));
            while (clientSocket.getKeepAlive()){
                while (clientSocket.getInputStream().available() == 0) {
                    Thread.sleep(100L);
                }
                byte[] data = new byte[clientSocket.getInputStream().available()];
                int bytes = clientSocket.getInputStream().read(data, 0, data.length);
                print = new String(data, 0, bytes, "ASCII");//.substring(4,bytes);
                System.out.println("from server : "+print);
            }
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

class LogWriter implements Runnable {

    Socket client;

    private static ThreadLocal<Date> date = new ThreadLocal<Date>() {
        @Override
        protected Date initialValue() {
            return new Date();
        };
    };

    private static ThreadLocal<SimpleDateFormat> formatter = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy_MM_dd");
        };
    };
    private Object CentralizedLogging;

    public LogWriter(Socket client) {
        this.client = client;
    }

    public void run() {
        try {
            write(this.client.getInputStream(), new File(
                    "D" + File.separator
                            + client.getInetAddress().getHostName() + "_"
                            + ".log"));
            this.client.close();
        } catch (Exception e) {
            try {
                e.printStackTrace();
                write(new ByteArrayInputStream(e.getMessage().getBytes()),
                        new File(CentralizedLogging.toString() + File.separator
                                + "centralLoggingError.log"));
            } catch (IOException io) {

            }
        }
    }


    public synchronized void write(InputStream in, File file)
            throws IOException {
        RandomAccessFile writer = new RandomAccessFile(file, "rw");
        writer.seek(file.length()); // append the file content into existing if it not exist creating a new one.
        writer.write(read(in));
        writer.close();
    }

    public static byte[] read(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int read = -1;
        byte[] buffer = new byte[1024];
        read = in.read(buffer);
        do {
            out.write(buffer, 0, read);
        } while((read = in.read(buffer)) > -1);
        return out.toByteArray();
    }
}