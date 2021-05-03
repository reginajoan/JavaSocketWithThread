package TestThread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

import java.util.*;
import java.util.concurrent.*;

public class RunningPrograms {
    private final static AtomicInteger idGenerator = new AtomicInteger(0);
    protected static String getFromServer(String dataDB) {
        final int timeout = 10;
        String data = null;
        NavigableMap<Object, Object> sort = new TreeMap<>().descendingMap();
        sort.put(32000, List.of("192.168.88.99"));
        sort.put(1212, List.of("192.168.88.99","192.168.88.98"));
        for(Map.Entry portAndHost : sort.entrySet()){
            for(String Host : (List<String>) portAndHost.getValue()){
                //jika data yang dikirim hasilnya != null maka akan keluar, kalau tidak akan melakukan pengecekan ip dan port sampai selesai looping
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
        System.out.println("running server : "+id);
        if(!ChekHostAvailable.isSocketAlive(host,port)){
            System.out.println("can't connnect!!");
            return null;
        }
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(new Task(dataDB, host, port));
        try {
            System.out.println("Started..");
            getFromHli = future.get(timeout, TimeUnit.SECONDS);
            System.out.println("Finished!");
        }catch(TimeoutException e){
            System.out.println("Timeout");
            getFromHli = null;
        }catch (Exception e) {
            future.cancel(true);
            System.out.println("Timeout!");
            e.getMessage();
            getFromHli = null;
        }finally {
            executor.shutdownNow();
        }
        return getFromHli;
    }

    public static String RunningProgram1(String dataDB, int timeout, String host, int port) {
        String getFromHli = null;
        System.out.println("Server to AD running");
        if(!ChekHostAvailable.isSocketAlive(host,port)){
            return null;
        }
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(new Task1(dataDB, host, port));
        try {
            System.out.println("Started..");
            getFromHli = future.get(timeout, TimeUnit.SECONDS);
            System.out.println("Finished!");
        } catch (Exception e) {
            future.cancel(true);
            executor.shutdownNow();
            e.getMessage();
            System.out.println("Timeout!");
        }finally {
            executor.shutdownNow();
        }
        return getFromHli;
    }
}
