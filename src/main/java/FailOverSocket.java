import TestThread.ChekHostAvailable;
import TestThread.PrintDATA;
import TestThread.RunningPrograms;
import TestThread.Task;
import TestThread.*;
import CodeMechanic.*;

import javax.swing.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

public class FailOverSocket{
    public static void main(String[] args) throws Exception {
        Server serv = new Server();
        serv.start();
    }
}