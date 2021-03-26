package ThreadOne;

public class ThreadResource {
    private String[] data;
    private int currIndex;
    public ThreadResource(String[] data){
        this.data = data;
        currIndex = 0;
    }
    public String getData(){
        if(currIndex == data.length){
            return null;
        }
        String retValue = data[currIndex];
        currIndex++;
        return retValue;
    }
}