import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PrintDATA {
    public boolean setPrintATM(String Datas){
        if(PrintATM(Datas)){
            return true;
        }else {
            return false;
        }
    }
    private boolean PrintATM(String Datas) {
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = dateFormat.format(date);
        String name = "traceAD_HLI"+ strDate;
        try {
            File file = new File("regina/home/javaProject"+name);
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            if (!file.exists()) {
                file.createNewFile();
            }
            bw.write(Datas + "\n");

            bw.close();
            System.out.println("Data successfully appended at the end of file, file name : "+name);
            return true;
        } catch (IOException ioe) {
            System.out.println("Exception occurred:");
            ioe.printStackTrace();
            return false;
        }

    }

}
