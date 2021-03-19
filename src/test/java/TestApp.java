import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

public class TestApp {
    @Test
    public void test(){
        String data = "data";
        String homeDirectory = System.getProperty("user.home");
        File file = new File(homeDirectory, "filenames.txt");
        file.canExecute();
        System.out.println(homeDirectory);
    }

}
