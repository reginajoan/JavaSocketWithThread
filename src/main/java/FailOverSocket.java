import ClientServer.Server;
import TestThread.RunningPrograms;

import java.io.IOException;

public class FailOverSocket{
    public static void main(String[] args) throws Exception {
//        Server server = new Server();
//        server.start();
        String dataDB = "0957ATMDPRLOAN60110220002004844602211520018992  20200615103547000133005400000133  NGTLNAP6064201240152425    7303AAA13481D50C                                                                                                                                                                                                                                                                                                                                                                        20200615103547                                                                                                                                                                                                                                                                                                                                                                                                                                                             -SendAdToHLI\"";
        RunningPrograms run = new RunningPrograms();
        run.RunningProgram1(dataDB, 78,"172.16.1.33",9000);
    }
}