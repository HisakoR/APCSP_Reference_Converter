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
                listLine.set(deta, simplePlacement(listLine.get(deta), "while", "REPEAT UNTIL", false));
                //处理行方法 while例
                listLine.set(deta, simplePlacement(listLine.get(deta), "print", "DISPLAY", false));
                //print例
                listLine.set(deta, simplePlacement(listLine.get(deta), "def", "PROCEDURE", false));
                //方程例
                //isMark = false例不可用, 请修复
                listLine.set(deta, simplePlacement(listLine.get(deta), "=", "<-", true));
                //赋值例
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
            System.out.println("检测到" + target + ", 正在进行更改");
            ArrayList<Integer> targetIndex = findString(targerLine, target);
            //targerLine中所有target的index
            ArrayList<Integer> availableIndex = new ArrayList<>();
            //可用目标的index
            for (int index : targetIndex) {
                boolean isAvailable = false; //标记该索引是否有效
                //目标字符串的前后字符
                char beforeChar = (index > 0) ? targerLine.charAt(index - 1) : ' ';
                char afterChar = (index + target.length() < targerLine.length()) ? targerLine.charAt(index + target.length()) : ' ';

                if (isMark) {
                    //如果isMark为true, 前后字符都不能是target
                    if (beforeChar != target.charAt(0) && afterChar != target.charAt(0)) {
                        isAvailable = true;
                    }
                }
                else {
                    //如果isMark为false, 前后必须为空格或括号
                    if ((beforeChar == ' ' || beforeChar == '(') && (afterChar == ' ' || afterChar == ')')) {
                        isAvailable = true;
                    }
                }
                //如果是可用的, 添加到availableIndex
                if (isAvailable) {
                    availableIndex.add(index);
                }
            }
            //按可用索引替换
            for (int i = availableIndex.size() - 1; i >= 0; i--) { //从后往前替换，避免索引改变
                int replaceIndex = availableIndex.get(i);
                output = output.substring(0, replaceIndex) + replacement + output.substring(replaceIndex + target.length());
            }

            //例过程
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
            //isMark: 目标替换是否为符号
            //例: + - * /
            //制作+= 或 -=检测

            return output;
        }
        else {
            System.out.println("未进行有关" + target + "的更改，目标不存在");
            return output;
        }
    }
}
