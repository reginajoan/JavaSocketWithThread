package TestThread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.*;
import java.util.concurrent.*;

public class RunningPrograms {
    private final static AtomicInteger idGenerator = new AtomicInteger(0);
    protected static String getFromServer(String dataDB) {
        final int timeout = 10;
        int port = 0;
        String host = "";
        String data = null;
        NavigableMap<Object, Object> sort = new TreeMap<>().descendingMap();
        sort.put(port, List.of("","")); // put host and port

        for(Map.Entry portAndHost : sort.entrySet()){
            for(String Host : (List<String>) portAndHost.getValue()){
                // check if data not empty then out of loop else will be loop until sort is empty
                if((data = RunningProgram(dataDB, timeout, Host, (Integer) portAndHost.getKey(), idGenerator.getAndIncrement())) != null){
                    break;
                }
            }
            if(data != null){
                break;
            }
        }
        return data;
    }

    private static String RunningProgram(String dataDB, int timeout, String host, int port, Integer id) {
        String getFromHli = null;
        System.out.println("running server : " + id);
        if (!ChekHostAvailable.isSocketAlive(host, port)) {
            System.out.println("can't connnect!");
        }
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(new Task(dataDB, host, port));
        try {
            System.out.println("Started..");
            getFromHli = future.get(timeout, TimeUnit.SECONDS);
            System.out.println("Finished!");
        } catch (TimeoutException e) {
            if (!future.isCancelled()) {
                future.cancel(true);
            }
            System.out.println("Timeout!");
            getFromHli = null;
        } catch (Exception e) {
            if (!future.isCancelled()) {
                future.cancel(true);
            }
            System.out.println("Error!");
            getFromHli = null;
        } finally {
            if (!executor.isShutdown()) {
                executor.shutdownNow();
            }
        }
        return getFromHli;
    }

    public static String RunningProgram1(String dataDB, int timeout, String host, int port) {
        String getFromHli = "";
        System.out.println("Server to AD Running");
        if (!ChekHostAvailable.isSocketAlive(host, port)) {
            return null;
        }
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(new Task(dataDB, host, port));
        try {
            System.out.println("Started..");
            getFromHli = future.get(timeout, TimeUnit.SECONDS);
            System.out.println("Finished!");
            executor.shutdownNow();
        } catch (TimeoutException e) {
            if (!future.isCancelled()) {
                future.cancel(true);
            }
            System.out.println("Timeout!");
            getFromHli = null;
        } catch (Exception e) {
            if (!future.isCancelled()) {
                future.cancel(true);
            }
            System.out.println("Error!");
            getFromHli = null;
        } finally {
            if (!executor.isShutdown()) {
                executor.shutdownNow();
            }
        }
        return getFromHli;
    }
}
