package CodeMechanic;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Server {
    public static final Integer port = 9000;
    private final ServerSocket server;
    private final ExecutorService service = Executors.newFixedThreadPool(10);
    private final AtomicInteger idGenerator = new AtomicInteger(0);

    public Server() throws IOException {
        this.server = new ServerSocket(port);
    }

    public void start() {
        try {
            while (true) {
                System.out.println("Waiting Transaction...");
                Worker worker = new Worker(idGenerator.incrementAndGet(), server.accept());
                service.execute(worker);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
