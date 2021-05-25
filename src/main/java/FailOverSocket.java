import ClientServer.ClientListen;
import ClientServer.Server;
import TestThread.RunningPrograms;

import java.io.IOException;

public class FailOverSocket{
    public static void main(String[] args) throws Exception {
        Server server = new Server();
        server.start();
    }
}