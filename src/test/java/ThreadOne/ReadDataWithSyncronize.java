package ThreadOne;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ReadDataWithSyncronize extends Thread{
    @Override
    public void run() {
        try {
            InputStream is = new FileInputStream("D:/data.txt");
            int data;
            while((data = is.read()) != -1){
                System.out.println(data);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
