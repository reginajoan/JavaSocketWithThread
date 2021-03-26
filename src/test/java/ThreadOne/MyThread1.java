package ThreadOne;

public class MyThread1 implements Runnable {
    @Override
    public void run() {
        for(int i=0;i<5;i++){
            System.out.println("Hello From Runnable");
        }
    }
}
