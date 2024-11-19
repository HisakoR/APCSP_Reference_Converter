package scripts;

import java.util.ArrayList;
import java.util.List;

public class processPy {
    private ArrayList<String> data = new ArrayList<>();
    public void setData(ArrayList<String> datal){
        this.data = datal;
    }

    public void turnRefPy(){
        System.out.println("=====开始转换代码=====");
        for (int x = 0; x < data.size(); x++){
            System.out.println("=====第" + (x + 1) +"行修改开始=====");
            String line = data.get(x);
            ArrayList<Integer> stringTarget = findString(line, "\"");
            /*
            原理：stringTarget中的双数位置为“的开头，而单数位置表示了”的结尾
            由此，操作数据时应当跳过双数位置的数值和单数位置数值中的任何数
            例{0,12,35,55}
            于是跳过string中0-12的位置与35-55的位置
            这样可以跳过用户代码中的字符串，由此避免错误的修改
            */
            //初始化位置
            ArrayList<String> listLine = new ArrayList<>();
            boolean isOdd = false;
            boolean isNan = false;
            if(stringTarget.isEmpty()) {
                System.out.println("未检测到string");
                System.out.println("将跳过可用代码列表");
                isNan = true;
            }
            else if(stringTarget.get(0) == 0){
                System.out.println("检测到开头为string");
                System.out.println("可用代码列表将从奇数开始");
                isOdd = true;
            }
            else {
                System.out.println("检测到开头为代码");
                System.out.println("可用代码列表将从偶数开始");
            }
            if(!isNan){
                listLine.add(line.substring(0, stringTarget.get(0)));
                System.out.println("处理初始队列添加：");
                System.out.println(line.substring(0, stringTarget.get(0)));
                for (int gama = 0; gama < stringTarget.size() - 1; gama++){
                    listLine.add(line.substring(stringTarget.get(gama), stringTarget.get(gama + 1)));
                    System.out.println("处理队列添加：");
                    System.out.println(line.substring(stringTarget.get(gama), stringTarget.get(gama + 1)));
                }
                listLine.add(line.substring(stringTarget.get(stringTarget.size() - 1)));
                System.out.println("处理最终队列添加：");
                System.out.println(line.substring(stringTarget.get(stringTarget.size() - 1)));
            }
            else{
                System.out.println("处理队列添加：");
                System.out.println(line);
                listLine.add(line);
            }
            /*
            向列表添加string部分和代码部分
            单数index为string部分，跳过
            偶数为代码部分，处理
            若开头为string部分，则运行相反功能
             */
            int inindex = 0;
            if (isOdd){
                inindex = 2;
            }
            for (int deta = inindex; deta < listLine.size(); deta += 2){
                //listLine.set(deta, simplePlacement(listLine.get(deta), "while", "REPEAT UNTIL"));
                //处理行方法 while例
                //listLine.set(deta, simplePlacement(listLine.get(deta), "print", "DISPLAY"));
                //print例
                //listLine.set(deta, simplePlacement(listLine.get(deta), "=", "<-"));
                //赋值例，需要修改
                //listLine.set(deta, simplePlacement(listLine.get(deta), "def", "PROCEDURE"));
                //方程例，需要修改
                //请添加判定：目标前后index是否有内容，避免错误替换
            }

            String outputLine = "";
            for (String s : listLine) {
                outputLine += s;
            }
            data.set(x, outputLine);
            System.out.println("行输出：");
            System.out.println(outputLine);
            System.out.println("=====第" + (x + 1) +"行修改结束=====");
        }
        System.out.println("=====结束=====");
    }

    public ArrayList<Integer> findString(String line, String target){
        ArrayList<Integer> indexs = new ArrayList<>();
        for (int y = 0; y < line.length(); y++){
            if (line.substring(y , y + 1).contains(target)){
                indexs.add(y);
            }
        }
        return indexs;
    }

    public ArrayList<String> getData(){
        return data;
    }

    public String simplePlacement(String targerLine, String target, String replacement, boolean isMark){
        String output = targerLine;
        if (targerLine.contains(target)){
            ArrayList<Integer> targetIndex = findString(targerLine, target);
            //targerLine中所有target的index
            ArrayList<Integer> availableIndex = new ArrayList<>();
            //可用目标的index

            //targerLine中:
            //检测有哪些index可用
            //可用条件:
            //如果isMark是true:
            //1.目标前后是空格或其他字符,不能是和target相同的字符
            //如果isMark是false:
            //1.目标前后是空格或小括号,不能是其他字符
            //添加到可用目标的数列

            //添加循环: 将所有可用index处的target替换成replacement

            //替换后,将处理过的文本储存在output中
            return output;
        }
        else {
            System.out.println("未进行有关" + target + "的更改，目标不存在");
            return output;
        }
    }
}
