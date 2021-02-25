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
    public boolean setPrintATM(String date, long mili, String Datas){
        if(PrintATM(date,mili ,Datas)){
            return true;
        }else {
            return false;
        }
    }
    private boolean PrintATM(String tgl,long mili, String Datas) {
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = dateFormat.format(date);
        String name = "traceAD_FROM_HLI"+ strDate;
        try {
            File file = new File("/data01/dataHLI-Regi/"+name+".txt");
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            if (!file.exists()) {
                file.createNewFile();
            }
            bw.write(tgl+mili+Datas+"\n");

            bw.close();
            System.out.println("Data successfully appended at the end of file, file name : "+name);
            return true;
        } catch (IOException ioe) {
            System.out.println("Exception occurred:");
            ioe.printStackTrace();
            return false;
        }
    }

    public void printMsgToHli(String tgl,long mili, String msg){
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = dateFormat.format(date);
        String name = "traceAD_MSG_TO_HLI"+ strDate;
        try {
            File file = new File("/data01/dataHLI-Regi/"+name+".txt");
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            if (!file.exists()) {
                file.createNewFile();
            }
            bw.write(tgl+mili+msg+"\n");

            bw.close();
            System.out.println("Data successfully appended at the end of file, file name : "+name);
        } catch (IOException ioe) {
            System.out.println("Exception occurred:");
            ioe.printStackTrace();
        }
    }

    public void saveDataTxt(List<String> datas){
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = dateFormat.format(date);
        String name = "traceAD_MSG_SaveData"+ strDate;
        try {
            File file = new File("/data01/dataHLI-Regi/"+name+".txt");
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            if (!file.exists()) {
                file.createNewFile();
            }
            for(String data : datas){
                bw.write(data+"\n");
            }
            bw.close();
            System.out.println("Data successfully appended at the end of file, file name : "+name);
        } catch (IOException ioe) {
            System.out.println("Exception occurred:");
            ioe.printStackTrace();
        }
    }
}
