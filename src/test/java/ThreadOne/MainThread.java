package ThreadOne;

import org.junit.Test;

public class MainThread {
    @Test
    public void main(){
        //Thread Class(berbentuk class) / Runnable Interface(berbentuk interface)
//        MyThread t1 = new MyThread();
//        t1.start();
//        //memanggil thread implements runnable
//        MyThread1 m1 = new MyThread1();
//        Thread t2 = new Thread(m1);
//        t2.start();
//
//        for(int i=0;i<5;i++){
//            System.out.println("Hello From Main");
//        }
//        ThreadOne.ReadDataWithSyncronize read = new ThreadOne.ReadDataWithSyncronize();
//        ThreadOne.ReadDataWithSyncronize read2 = new ThreadOne.ReadDataWithSyncronize();
//        read.start();
//        read2.start();
//        String[] dataToShare = {"udin","ucup","otong","bambang"};
//        ThreadResource sharedResource = new ThreadResource(dataToShare);
//        ThreadSyncronization thread1 = new ThreadSyncronization("Thread 1", sharedResource);
//        SecondThread second = new SecondThread("Second", sharedResource);
//        second.start();
//        thread1.start();

    }

    String addNewString(String data){
        return data +="halo bos";
    }
    void cekData(Object data){
        try{
            if(Integer.parseInt((String) data) != 1){
                return;
            }
        }catch (Exception e){
            System.out.println("error");
        }finally {
            System.out.println("done!");
        }
    }
    @Test
    public void TestTryCatch(){
        cekData(true);
    }
}
