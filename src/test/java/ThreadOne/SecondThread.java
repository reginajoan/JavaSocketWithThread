package ThreadOne;

import ThreadOne.ThreadResource;

public class SecondThread extends Thread {
    private ThreadResource resource;

    public SecondThread(String name, ThreadResource resource){
        this.setName(name);
        this.resource = resource;
    }
    @Override
    public void run() {
        try {
            resource.wait();
            synchronized (resource){
                System.out.println("Dari Thread Second "+ resource.getData());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
