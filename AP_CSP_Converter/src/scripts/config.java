package scripts;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class config {
    private HashMap<String, String> chineseDic = new HashMap<>();
    private static ArrayList<String> config;
    private int language;
    /*
    0 - 英语
    1 - 中文
     */
    public static int versions = 10014;

    public void initialize() throws IOException {
        filer filer = new filer();
        File saver = new File("output");
        ArrayList<String> lines = new ArrayList<>();
        lines.add("0");
        //用户语言
        lines.add(saver.getAbsolutePath());
        //用户默认保存路径
        filer.createConfig(lines);
        config = filer.readFile("config.txt");
        filer.setUserPath(config.get(1));
        initializeDic();
        //初始化字典
    }
    public void initializeDic(){
        chineseDic.put("save", "保存");
        chineseDic.put("setting", "设置");
    }
    public String getChineseDic(String target){
        try{
            return chineseDic.get(target);
        }
        catch (Exception ignored){
            System.out.println("获取翻译值失败");
        }
        return null;
    }
    public int getLanguage(){
        return Integer.parseInt(config.get(0));
    }
    public String getPath(){
        return config.get(1);
    }
}
