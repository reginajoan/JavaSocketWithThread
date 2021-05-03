package TestThread;

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

    public static void saveDataTxtNew(List<String> datas){
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = dateFormat.format(date);
        String fileName = "";
        try {
            for(String data : datas){
                if(data.equals(null) || data.isEmpty()){
                    return;
                }
                String joinName = "traceAD_MSG_SaveData"+data.substring(18,26)+"--"+strDate;
                fileName = joinName;
                File dir = new File("/data01/dataHLI-Regi/Java/"+data.substring(18,26)+"");
                if(!dir.exists()){
                    dir.mkdirs();
                }
                dir = new File("/data01/dataHLI-Regi/Java/"+data.substring(18,26)+"/"+strDate);
                if(!dir.exists()){
                    dir.mkdirs();
                }
                File file = new File("/data01/dataHLI-Regi/Java/"+data.substring(18,26)+"/"+strDate+"/"+joinName+".txt");
                FileWriter fw = new FileWriter(file, true);
                BufferedWriter bw = new BufferedWriter(fw);
                if (!file.exists()) {
                    file.createNewFile();
                }
                bw.write(new Date().toString()+" "+data+"\n");
                bw.close();
            }
            System.out.println("Data successfully appended at the end of file, file name : "+fileName);
        } catch (IOException ioe) {
            System.out.println("Exception occurred:");
            ioe.printStackTrace();
        }
    }

    public static void saveDataTxtTest(List<String> datas, String name){
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = dateFormat.format(date);
        String fileName = "";
        try {
            for(String data : datas){
                String joinName = "traceAD_MSG_SaveData"+name+"--"+strDate;
                fileName = joinName;
                File dir = new File("/data01/dataHLI-Regi/Java/"+name+"");
                if(!dir.exists()){
                    dir.mkdirs();
                }
                dir = new File("/data01/dataHLI-Regi/Java/"+name+"/"+strDate);
                if(!dir.exists()){
                    dir.mkdirs();
                }
                File file = new File("/data01/dataHLI-Regi/Java/"+name+"/"+strDate+"/"+joinName+".txt");
                FileWriter fw = new FileWriter(file, true);
                BufferedWriter bw = new BufferedWriter(fw);
                if (!file.exists()) {
                    file.createNewFile();
                }
                bw.write(new Date().toString()+" "+data+"\n");
                bw.close();
            }
            System.out.println("Data successfully appended at the end of file, file name : "+fileName);
        } catch (IOException ioe) {
            System.out.println("Exception occurred:");
            ioe.printStackTrace();
        }
    }
}