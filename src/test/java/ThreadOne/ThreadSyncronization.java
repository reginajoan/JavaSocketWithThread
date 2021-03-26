package ThreadOne;

public class ThreadSyncronization extends Thread {
    private ThreadResource resource;

    public ThreadSyncronization(String name, ThreadResource resource){
        this.setName(name);
        this.resource = resource;
    }

    @Override
    public void run() {
            for(int i =0; i<2; i++){
                System.out.println("Dari Thread "+this.getName()+ " = " + resource.getData());
            }
        resource.notify();
    }
}
