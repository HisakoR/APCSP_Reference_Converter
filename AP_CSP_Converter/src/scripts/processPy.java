package scripts;

import java.util.ArrayList;
import java.util.List;

public class processPy {
    private ArrayList<String> data = new ArrayList<>();
    private ArrayList<bracket> tabSpc = new ArrayList<>();
    private boolean generate = false;
    private String typpe = "";
    public void setData(ArrayList<String> datal){
        this.data = datal;
    }

    public void turnRefPy(){
        System.out.println("=====开始转换代码=====");
        for (int x = 0; x < data.size(); x++){
            System.out.println("=====第" + (x + 1) +"行修改开始=====");
            String line = data.get(x);
            String referenceLine = "";
            if(line.contains("#")){
                referenceLine = line.substring(line.indexOf("#"));
                line = line.substring(0, line.indexOf("#"));
            }
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
                System.out.println("目标双引号数：" +  stringTarget.size());
                for (int lauken : stringTarget){
                    System.out.println(lauken);
                }
                for (int gama = 0; gama < stringTarget.size() - 1; gama++) {
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
                listLine.set(deta, simplePlacement(listLine.get(deta), "while", "REPEAT UNTIL NOT", null, false, false, false));
                //处理行方法 while例
                listLine.set(deta, simplePlacement(listLine.get(deta), "print", "DISPLAY", null, false, false, false));
                //print例
                listLine.set(deta, simplePlacement(listLine.get(deta), "def", "PROCEDURE", null, false, false, false));
                //方程例
                listLine.set(deta, simplePlacement(listLine.get(deta), ">=", "≥", null, true, false, false));
                //大等于例
                listLine.set(deta, simplePlacement(listLine.get(deta), "<=", "≤", null, true, false, false));
                //小等于例
                listLine.set(deta, simplePlacement(listLine.get(deta), "=", "<-", '!', true, false, false));
                //赋值例
                listLine.set(deta, simplePlacement(listLine.get(deta), "input", "INPUT", null, false, false, false));
                //输入例
                listLine.set(deta, simplePlacement(listLine.get(deta), "%", "MOD", null, true, false, false));
                //余数例
                listLine.set(deta, simplePlacement(listLine.get(deta), "!=", "≠", null, true, false, false));
                //非等于例
                listLine.set(deta, simplePlacement(listLine.get(deta), "==", "=", null, true, false, false));
                //等于例
                listLine.set(deta, simplePlacement(listLine.get(deta), "&", "AND", null, true, true, false));
                //and例
                listLine.set(deta, simplePlacement(listLine.get(deta), "|", "OR", null, true, true, false));
                //OR例
                listLine.set(deta, simplePlacement(listLine.get(deta), "!", "NOT", null, true, true, false));
                //非例
                listLine.set(deta, simplePlacement(listLine.get(deta), "len", "LENGTH", null, false, false, false));
                //长度例
                listLine.set(deta, repElif(listLine.get(deta)));
                listLine.set(deta, repIf(listLine.get(deta)));
                listLine.set(deta, repELSE(listLine.get(deta)));
                listLine.set(deta, repRETURN(listLine.get(deta)));
                listLine.set(deta, repFor(listLine.get(deta)));
                listLine.set(deta, repMeth("random.randint", "RANDOM", listLine.get(deta)));
                //返回例
                listLine.set(deta, repLists("append", "APPEND", listLine.get(deta)));
                listLine.set(deta, repLists("insert", "INSERT", listLine.get(deta)));
                listLine.set(deta, repLists("remove", "REMOVE", listLine.get(deta)));
                //列表方法例
            }

            String outputLine = "";
            for (String s : listLine) {
                outputLine += s;
            }
            outputLine = outputLine + referenceLine;
            data.set(x, outputLine);
            System.out.println("行输出：");
            System.out.println(outputLine);
            System.out.println("当前行类型: " + typpe);
            if(generate && !outputLine.isBlank()){
                if(tabSpc.size() >= 2){
                    System.out.println("补足行类型: " + tabSpc.get(tabSpc.size() - 2).getType());
                    while(tabSpcCheck(outputLine) && !tabSpc.get(tabSpc.size() - 1).getType().equals(typpe)){
                        String sace = "";
                        for(int l = 0; l < tabSpc.get(tabSpc.size() - 1).getSpaces() - 1; l++){
                            sace += " ";
                        }
                        data.add(x, sace + "}");
                        tabSpc.remove(tabSpc.size() - 1);
                        if (tabSpc.isEmpty()){
                            generate = false;
                            break;
                        }
                        x += 1;
                    }
                }
            }

            System.out.println("=====第" + (x + 1) +"行修改结束=====");
            typpe = "";
        }
        System.out.println("=====结束=====");
    }
    public ArrayList<Integer> findString(String line, String target){
        ArrayList<Integer> indexs = new ArrayList<>();
        for (int y = 0; y < line.length(); y++){
            if (line.contains(target)){
                if (!indexs.isEmpty()){
                    indexs.add(indexs.get(y - 1) + line.indexOf(target) + 1);
                }
                else {
                    indexs.add(line.indexOf(target));
                }
                line = line.substring(line.indexOf(target) + target.length());
            }
        }
        return indexs;
    }

    public ArrayList<String> getData(){
        return data;
    }

    public String simplePlacement(String targerLine, String target, String replacement, Character expections, boolean isMark, boolean isStandard, boolean isMethod){
        //参数解析
        //targerLine - 需要进行替换的数据
        //target - 被替换的内容
        //replacement - 要替换成的内容
        //expections - 如果修改过程中遇到此符号，则直接跳过
        //isMark - 是否是=或+-*/这样的符号，这样的字符在处理时会跳过包含两个连续的字符的指数，反之，目标前后必须带有空格或括号
        //isStandard - 是否需要在前后添加空格, 适用于原本为符号，但在改写后变为自然语言格式的代码
        //isMethod - 是否要将目标后的内容填充进括号中，例如return expression改写为return(expression)
        String output = targerLine;
        if (targerLine.contains(target)){
            System.out.println("检测到" + target + ", 正在进行更改");
            ArrayList<Integer> targetIndex = findString(targerLine, target);
            System.out.println("目标index包括");
            for(int laufen : targetIndex){
                System.out.println(laufen);
            }
            //targerLine中所有target的index
            ArrayList<Integer> availableIndex = new ArrayList<>();
            //可用目标的index
            for (int index : targetIndex) {
                boolean isAvailable = false; //标记该索引是否有效
                //目标字符串的前后字符
                char beforeChar;
                if(index > 0){
                    beforeChar = targerLine.charAt(index - 1);
                    System.out.println("目标前一个字符为:" + beforeChar);
                }
                else {
                    beforeChar = 'L';
                    System.out.println("目标前没有字符");
                }
                char afterChar = (index + target.length() < targerLine.length()) ? targerLine.charAt(index + target.length()) : ' ';
                System.out.println("目标后一个字符为:" + afterChar);

                if (isMark) {
                    //如果isMark为true, 前后字符都不能是target, 也不能是expections
                    if (beforeChar != target.charAt(0) && afterChar != target.charAt(0)) {
                        if(expections == null || (beforeChar != expections && afterChar != expections)){
                            System.out.println("前后判定: " + target.charAt(0));
                            isAvailable = true;
                            System.out.println("目标index可用" + index);
                        }
                    }
                }
                else {
                    //如果isMark为false, 前后必须为空格或括号
                    if ((beforeChar == ' ' || beforeChar == '.' || beforeChar == '(' || beforeChar == ')' || index == 0) &&
                            (afterChar == ' '|| afterChar == ':' || afterChar == '(' || afterChar == ')')) {
                        isAvailable = true;
                        System.out.println("目标index可用" + index);
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
                if(isStandard){
                    String stReplacement = replacement;
                    if(output.charAt(replaceIndex + 1) != ' '){
                        stReplacement = " " + stReplacement;
                    }
                    if (output.charAt(replaceIndex + target.length()) != ' ')
                        stReplacement = stReplacement + " ";
                    output = output.substring(0, replaceIndex) + stReplacement + output.substring(replaceIndex + target.length());
                }
                else if(isMethod){
                    output = output.substring(0, replaceIndex) + replacement + "(" + output.substring(replaceIndex + target.length() + 1) + ")";
                }
                else {
                    output = output.substring(0, replaceIndex) + replacement + output.substring(replaceIndex + target.length());
                }
            }

            return output;
        }
        else {
            System.out.println("未进行有关" + target + "的更改，目标不存在");
            return output;
        }
    }
    //替换方法的方法
    public String repMeth(String target, String replacement, String line){
        if (line.contains(target)){
            line = line.substring(0, line.indexOf(target)) + replacement + line.substring(line.indexOf(target) + target.length());
        }
        return line;
    }
    //替换list方法的方法
    public String repLists(String target, String replacement, String line){
        if (line.contains(target)){
            String listName = line.substring(0, line.indexOf(target) - 1);

            String[] containment = line.substring(line.indexOf("(") + 1, line.indexOf(")")).split(",");
            String outputLine = "";
            //需要改进
            outputLine = replacement + "(" + listName;
            for (String s : containment) {
                outputLine = outputLine + ", " + s.strip();
            }
            outputLine = outputLine + ")" + line.substring(line.indexOf(")") + 1);
            line = outputLine;
        }
        return line;
    }
    public boolean tabSpcCheck(String line){
        int testSpc = 0;
        for(int x = 0; x < line.length(); x++){
            if(line.charAt(x) != ' '){
                break;
            }
            testSpc += 1;
        }
        return testSpc <= tabSpc.get(tabSpc.size() - 1).getSpaces();
    }
    //替换for的方法
    public String repFor(String line){
        if (line.contains("for")){
            String condition = line.substring(line.indexOf("for"), line.indexOf(":") + 1);
            condition = "FOR EACH" + condition.substring(condition.indexOf("for") + 3, condition.indexOf("in")) + "IN" + condition.substring(condition.indexOf("in") + 2, condition.indexOf(":"));
            line = line.substring(0, line.indexOf("for")) + condition + line.substring(line.indexOf(":"));

            line = line.substring(0, line.length() - 1) + "{";
            int tpc = 0;
            for(int x = 0; x < line.length(); x++){
                if(line.charAt(x) != ' '){
                    break;
                }
                tpc += 1;
            }
            tpc += 1;
            tabSpc.add(new bracket("for", tpc));
            generate = true;
            System.out.println("前方含有" + (tpc - 1) + "个空格");
            typpe = "for";
        }
        return line;
    }
    //替换if的方法
    public String repIf(String line){
        if (line.contains("if")){
            String condition = line.substring(line.indexOf("if"), line.indexOf(":") + 1);
            condition = "IF" + condition.substring(condition.indexOf("if") + 2, condition.indexOf(":"));
            line = line.substring(0, line.indexOf("if")) + condition + line.substring(line.indexOf(":"));

            line = line.substring(0, line.length() - 1) + "{";
            int tpc = 0;
            for(int x = 0; x < line.length(); x++){
                if(line.charAt(x) != ' '){
                    break;
                }
                tpc += 1;
            }
            tpc += 1;
            tabSpc.add(new bracket("if", tpc));
            generate = true;
            System.out.println("前方含有" + (tpc - 1) + "个空格");
            typpe = "if";
        }
        return line;
    }
    //替换else if的方法
    public String repElif(String line){
        if (line.contains("elif")){
            String condition = line.substring(line.indexOf("elif"), line.indexOf(":") + 1);
            condition = "ELSE IF" + condition.substring(condition.indexOf("elif") + 4, condition.indexOf(":"));
            line = line.substring(0, line.indexOf("elif")) + condition + line.substring(line.indexOf(":"));

            line = line.substring(0, line.length() - 1) + "{";
            int tpc = 0;
            for(int x = 0; x < line.length(); x++){
                if(line.charAt(x) != ' '){
                    break;
                }
                tpc += 1;
            }
            tpc += 1;
            tabSpc.add(new bracket("elif", tpc));
            generate = true;
            System.out.println("前方含有" + (tpc - 1) + "个空格");
            typpe = "elif";
        }
        return line;
    }
    //替换else if的方法
    public String repELSE(String line){
        if (line.contains("else")){
            String condition = line.substring(line.indexOf("else"), line.indexOf(":") + 1);
            condition = "ELSE" + condition.substring(condition.indexOf("else") + 4, condition.indexOf(":"));
            line = line.substring(0, line.indexOf("else")) + condition + line.substring(line.indexOf(":"));

            line = line.substring(0, line.length() - 1) + "{";
            int tpc = 0;
            for(int x = 0; x < line.length(); x++){
                if(line.charAt(x) != ' '){
                    break;
                }
                tpc += 1;
            }
            tpc += 1;
            tabSpc.add(new bracket("else", tpc));
            generate = true;
            System.out.println("前方含有" + (tpc - 1) + "个空格");
            typpe = "else";
        }
        return line;
    }
    //替换return的方法
    public String repRETURN(String line){
        if(line.contains("return")){
            line = line.substring(0, line.indexOf("return")) + "RETURN(" + line.substring(line.indexOf("return") + 6).strip() + ")";
        }
        return line;
    }
}
