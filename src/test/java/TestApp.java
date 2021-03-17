import org.junit.Test;

import java.io.IOException;
import java.net.Socket;

public class TestApp {
    @Test
    public void test(){
        String host = "192.168.88.99";
        int port = 1212;
        String dataDB = "0957ATMDPRLOAN60110220002004844602211520018992  20200615103547000133005400000133  NGTLNAP6064201240152425    7303AAA13481D50C                                                                                                                                                                                                                                                                                                                                                                        20200615103547                                                                                                                                                                                                                                                                                                                                                                                                                                                             -SendAdToHLI\"";
        //dataDB = "1387ATMDBALINQ60110220002004844602211520018992  202006151036010001330 01400002508  NBALHNBIDR    OA                484                                                                                                         4602211520018992=1225                                                                                                                                                                       0CECDB747795EE83                                                           20200615103601                                                        MAGSTRIPE                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         -SendAdToHLI\"";
        Socket clientSocket;
        String print = "";
        try{
            clientSocket = new Socket(host,port);
            clientSocket.getOutputStream().write(dataDB.getBytes("UTF-8"));
            clientSocket.setKeepAlive(true);
            while (clientSocket.getInputStream().available() == 0) {
                Thread.sleep(100L);
            }
            byte[] data = new byte[clientSocket.getInputStream().available()];
            int bytes = clientSocket.getInputStream().read(data, 0, data.length);
            print = new String(data, 0, bytes, "UTF-8");//.substring(4,bytes);
            System.out.println("from server : "+print);
            dataDB = "";
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        } catch (Exception e) {
            e.getMessage();
        }
    }

}
