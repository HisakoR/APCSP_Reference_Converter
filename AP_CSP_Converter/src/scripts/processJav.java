package scripts;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class processJav {
    private ArrayList<String> data = new ArrayList<>();
    public void setData(ArrayList<String> datal){
        this.data = datal;
    }
    public void turnRefJav(){
        for (int x = 0; x < data.size(); x++){
            data.set(x, whileMeth(data.get(x)));
            //处理行方法
        }
    }
    public String whileMeth(String line){
        if (line.contains("while")){
            return line.replaceAll("while", "REPEAT UNTIL");
        }
        return line;
    }
    public ArrayList<String> getData(){
        return data;
    }
}
