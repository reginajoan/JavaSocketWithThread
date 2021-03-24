package TestThread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class RunningPrograms {
    private static boolean flag = false;

    public static String getFromServer(String dataDB) throws Exception {
        final int timeout = 15;
        String data = "";
        data = RunningProgram(dataDB, timeout);
        if(flag){
            return data;
        }else {
            return RunningProgram1(dataDB, timeout);
        }
    }

    private static String RunningProgram(String dataDB, int timeout) throws Exception{
        final String host = "192.168.88.99";
        final int port = 32000;
        String getFromHli = "";
        ChekHostAvailable chek = new ChekHostAvailable();
        System.out.println("Server 1 running");
        if(!chek.isSocketAlive(host,port)){
            flag = false;
            return "Connection Refused!!!";
        }
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(new Task(dataDB, host, port));
        try {
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

    private static String RunningProgram1(String dataDB, int timeout) throws Exception{
        String host = "192.168.88.98";
        int port = 1212;
        String getFromHli = "";
        ChekHostAvailable chek = new ChekHostAvailable();
        System.out.println("Server 2 running");
        if(!chek.isSocketAlive(host,port)){
            flag = false;
            return "Connection Refused!!!";
        }
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(new Task1(dataDB, host, port));
        try {
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
            System.out.println("Timeout!");
            return getFromHli;
        }
    }
}
