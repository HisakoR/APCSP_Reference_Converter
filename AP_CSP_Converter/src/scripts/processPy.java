package scripts;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class processPy {
    private ArrayList<String> data = new ArrayList<>();
    private ArrayList<bracket> tabSpc = new ArrayList<>();
    private boolean generate = false;
    private boolean isReplaceLine = false;
    private boolean shouldBaked = false;
    private boolean retbaked = false;
    private boolean modded = false;

    /*
    编码表
    cspc6126<-      （还原为=）
    cspc6086        （还原为:）
    */

    //设置data数据
    public void setData(ArrayList<String> datal){
        this.data = datal;
    }
    //java8补充
    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    //开始处理的方法
    public void turnRefPy(){
        System.out.println("=====开始转换代码=====");
        for (int x = 0; x < data.size(); x++){
            System.out.println("=====第" + (x + 1) +"行修改开始=====");
            String line = data.get(x);
            String referenceLine = "";
            isReplaceLine = false;
            //检测当前行是否满足添加}的条件
            System.out.println("添加}:" + (generate && !isBlank(line)));
            if(generate && !isBlank(line)){
                System.out.println("当前行：\n" + line);
                System.out.println("替换行位置: \n" + tabSpc.get(tabSpc.size() - 1).getLineNum());
                System.out.println("目标行替换所需空格数：\n" + tabSpc.get(tabSpc.size() - 1).getSpaces());
                System.out.println("目标空格数小于等于替换行数:\n" + tabSpcCheck((line)));
                System.out.println("目标位置不等于替换行位置:\n" + (tabSpc.get(tabSpc.size() - 1).getLineNum() != x));
                while(tabSpcCheck((line + referenceLine)) && tabSpc.get(tabSpc.size() - 1).getLineNum() != x){
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
                    x++;
                }
            }
            line = data.get(x);
            //20241126可能出现bug↑

            if(line.contains("#")){
                referenceLine = line.substring(line.indexOf("#"));
                line = line.substring(0, line.indexOf("#"));
            }

            if(line.contains("'''")){
                x++;
                while(x < data.size()){
                    if (!data.get(x).contains("'''")){
                        x++;
                    }
                    else{
                        break;
                    }
                }
            }
            else{
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
                    modded = false;

                    listLine.set(deta, simplePlacement(listLine.get(deta), "print", "DISPLAY", null, false, false, false));
                    listLine.set(deta, simplePlacement(listLine.get(deta), ">=", "≥", null, true, false, false));
                    listLine.set(deta, simplePlacement(listLine.get(deta), "<=", "≤", null, true, false, false));
                    //listLine.set(deta, simplePlacement(listLine.get(deta), "=", "<-", '!', true, false, false));
                    listLine.set(deta, simplePlacement(listLine.get(deta), "input", "INPUT", null, false, false, false));
                    listLine.set(deta, simplePlacement(listLine.get(deta), "%", "MOD", null, true, false, false));
                    listLine.set(deta, simplePlacement(listLine.get(deta), "!=", "≠", null, true, false, false));
                    //listLine.set(deta, simplePlacement(listLine.get(deta), "==", "=", null, true, false, false));
                    listLine.set(deta, simplePlacement(listLine.get(deta), "&", "AND", null, true, true, false));
                    listLine.set(deta, simplePlacement(listLine.get(deta), "|", "OR", null, true, true, false));
                    listLine.set(deta, simplePlacement(listLine.get(deta), "!", "NOT", null, true, true, false));
                    listLine.set(deta, simplePlacement(listLine.get(deta), "not", "NOT", null, false, false, false));
                    listLine.set(deta, simplePlacement(listLine.get(deta), "or", "OR", null, false, false, false));
                    listLine.set(deta, simplePlacement(listLine.get(deta), "and", "AND", null, false, false, false));

                    listLine.set(deta, repContinuous(listLine.get(deta)));
                    listLine.set(deta, repMeth("len", "LENGTH", listLine.get(deta)));                    //替换len()
                    listLine.set(deta, repLists(listLine.get(deta)));                                                       //替换list.append()
                    listLine.set(deta, repClass(listLine.get(deta), x));                                                    //替换class
                    listLine.set(deta, repDef(listLine.get(deta), x));                                                      //替换def
                    listLine.set(deta, repWhile(listLine.get(deta), x));                                                    //替换while
                    listLine.set(deta, repElif(listLine.get(deta), x));                                                     //替换elif
                    listLine.set(deta, repIf(listLine.get(deta), x));                                                       //替换if
                    listLine.set(deta, repELSE(listLine.get(deta), x));                                                     //替换else
                    listLine.set(deta, repRETURN(listLine.get(deta)));                                                      //替换return
                    listLine.set(deta, repFor(listLine.get(deta), x));                                                      //替换for
                    listLine.set(deta, repMaus(line, listLine.get(deta), x));                                               //替换:
                    listLine.set(deta, repMeth("random.randint", "RANDOM", listLine.get(deta)));         //替换random.randint()

                    System.out.println("修改:" + modded);
                    if(modded){
                        deta -=2;
                    }
                }
                modded = false;
                String outputLine = "";
                for (String s : listLine) {
                    outputLine += s;
                }
                if (retbaked){
                    outputLine += ")";
                    retbaked = false;
                }
                //还原冲突项
                outputLine = outputLine.replaceAll("cspc6126", "=");
                outputLine = outputLine.replaceAll("cspc6086", ":");
                outputLine = outputLine + referenceLine;
                data.set(x, outputLine);
                System.out.println("行输出：");
                System.out.println(outputLine);
                System.out.println("=====第" + (x + 1) +"行修改结束=====");
                if(x + 1 == data.size() && tabSpc.isEmpty()){
                    data.add("--------------DONE-APCSP-CONVERTER--------------");
                    x += 1;
                }
                else if(x + 1 == data.size() && !tabSpc.isEmpty()){
                    for (int n = tabSpc.size() - 1; n > -1; n--) {
                        String spac = "";
                        for (int r = 0; r < tabSpc.get(n).getSpaces() - 1; r++) {
                            spac = spac + " ";
                        }
                        data.add(spac + "}");
                        x += 1;
                    }
                    data.add("--------------DONE-APCSP-CONVERTER--------------");
                    x += 1;
                }
            }
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
        //targerLine - 需要进行替换的数据
        //target - 被替换的内容
        //replacement - 要替换成的内容
        //expections - 如果修改过程中遇到此符号，则直接跳过
        //isMark - 是否是=或+-*/这样的符号，这样的字符在处理时会跳过包含两个连续的字符的指数，反之，目标前后必须带有空格或括号
        //isStandard - 是否需要在前后添加空格, 适用于原本为符号，但在改写后变为自然语言格式的代码
        //isMethod - 是否要将目标后的内容填充进括号中，例如return expression改写为return(expression)
        String output = targerLine;
        if (targerLine.contains(target)){
            ArrayList<Integer> targetIndex = findString(targerLine, target);
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
                }
                else {
                    beforeChar = 'L';
                }
                char afterChar = (index + target.length() < targerLine.length()) ? targerLine.charAt(index + target.length()) : ' ';

                if (isMark) {
                    //如果isMark为true, 前后字符都不能是target, 也不能是expections
                    if (beforeChar != target.charAt(0) && afterChar != target.charAt(0)) {
                        if(expections == null || (beforeChar != expections && afterChar != expections)){
                            isAvailable = true;
                        }
                    }
                }
                else {
                    //如果isMark为false, 前后必须为空格或括号
                    if ((beforeChar == ' ' || beforeChar == '.' || beforeChar == '(' || beforeChar == ')' || index == 0) &&
                            (afterChar == ' '|| afterChar == ':' || afterChar == '(' || afterChar == ')')) {
                        isAvailable = true;
                    }
                }
                //如果是可用的, 添加到availableIndex
                if (isAvailable) {
                    availableIndex.add(index);
                    System.out.println("简单替换符合修改条件");
                    modded = true;
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
            return output;
        }
    }
    
    //替换list方法集合的方法
    public String repLists(String line){
        if (!(line.contains(".append")||line.contains(".insert")||line.contains(".remove"))) {
            return line;
        }
        String appendPattern = "(\\w+)\\.append\\(";
        String insertPattern = "(\\w+)\\.insert\\(";
        String removePattern = "(\\w+)\\.remove\\(";
        line = replaceUsingPattern(line, appendPattern, "APPEND($1, ");
        line = replaceUsingPattern(line, insertPattern, "INSERT($1, ");
        line = replaceUsingPattern(line, removePattern, "REMOVE($1, ");
        modded = true;
        return line;
    }
    public String replaceUsingPattern(String line, String pattern, String replacement) {
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(line);
        return matcher.replaceAll(replacement);
    }
    //替换方法的方法
    public String repMeth(String target, String replacement, String line){
        if (variableTest(line, target)){
            if((line.indexOf(target) == 0)){
                System.out.println("目标方法后字符:" + line.charAt(line.indexOf(target) + target.length() - 1));
                if(line.charAt(line.indexOf(target) + target.length()) == '('){
                    line = line.substring(0, line.indexOf(target)) + replacement + line.substring(line.indexOf(target) + target.length());
                    System.out.println("Meth替换符合修改条件");
                    modded = true;
                }
            }
            else{
                if(line.charAt(line.indexOf(target) - 1) == ' ' || line.charAt(line.indexOf(target) - 1) == '('){
                    System.out.println("目标方法后字符:" + line.charAt(line.indexOf(target) + target.length() - 1));
                    if(line.charAt(line.indexOf(target) + target.length()) == '('){
                        line = line.substring(0, line.indexOf(target)) + replacement + line.substring(line.indexOf(target) + target.length());
                        System.out.println("Meth替换符合修改条件");
                        modded = true;
                    }
                }
            }
        }
        return line;
    }
    //替换=格式的方法
    public String repContinuous(String line){
        if (!line.contains("=")) {
            return line;
        }
        int frontSpaces = getSpaceFront(line);
        int hindSpaces = getSpaceHind(line);
        String trimmedLine = line.trim();
        trimmedLine = trimmedLine.replace("==", "cspc6126");
        trimmedLine = replaceOperators(trimmedLine);
        trimmedLine = trimmedLine.replace("=", "←");
        modded = true;
        return addSpaces(frontSpaces) + trimmedLine + addSpaces(hindSpaces);
    }
    public static String replaceOperators(String line) {
        //匹配变量运算符及其值
        String regexWithValue = "(\\w+)\\s*([+\\-*/%])=\\s*(\\w+)";
        line = line.replaceAll(regexWithValue, "$1 = $1 $2 $3");
        //匹配变量运算符但未提供值
        String regexWithoutValue = "(\\w+)\\s*([+\\-*/%])=\\s*";
        line = line.replaceAll(regexWithoutValue, "$1 = $1 $2 ");
        return line;
    }
    //空格生成器
    public static String addSpaces(int count) {
        String spaces = "";
        for (int i = 0; i < count; i++) {
            spaces += " ";
        }
        return spaces;
    }
    //将目标字符串倒置的方法
    public String reverseString (String line){
        ArrayList<String> reverseLiner = new ArrayList<>();
        for (int u = 0; u < line.length(); u++){
            reverseLiner.add(0, String.valueOf(line.charAt(u)));
        }
        String output = "";
        for (String s : reverseLiner) {
            output += s;
        }
        return output;
    }
    //检测是否符合闭合括号的条件
    public boolean tabSpcCheck(String line){
        int testSpc = 1;
        for(int x = 0; x < line.length(); x++){
            if(line.charAt(x) != ' '){
                break;
            }
            testSpc += 1;
        }
        return testSpc <= tabSpc.get(tabSpc.size() - 1).getSpaces();
    }
    //获取行前空格的方法
    public int getSpaceFront(String line){
        int cal = 0;
        for (int c = 0; c < line.length(); c++){
            if (line.charAt(c) != ' '){
                break;
            }
            cal++;
        }
        return cal;
    }
    //获取行后空格的方法
    public int getSpaceHind(String line){
        int cal = 0;
        for (int c = 1; c < line.length(); c++){
            if (line.charAt(line.length() - c) != ' '){
                break;
            }
            cal++;
        }
        return cal;
    }
    //检测目标是否为变量名
    public boolean variableTest(String line, String target){
        boolean inFrontTest = false;
        boolean afterTest = false;
        if (line.contains(target)){

            if (line.indexOf(target) != 0){
                char infrom = line.charAt(line.indexOf(target) - 1);
                if (infrom == ' ' || infrom == '(' || infrom == ',' || infrom == ')'){
                    inFrontTest = true;
                }
                if (line.indexOf(target) + target.length() != line.length()){
                    char afterom = line.charAt(line.indexOf(target) + target.length());
                    if (afterom == ' ' || afterom == '(' || afterom == ',' || afterom == ')'|| afterom == ':'){
                        afterTest = true;
                    }
                }
            }

            else{
                inFrontTest = true;
                if (line.indexOf(target) + target.length() != line.length()){
                    char afterom = line.charAt(line.indexOf(target) + target.length());
                    if (afterom == ' ' || afterom == '(' || afterom == ',' || afterom == ')'|| afterom == ':'){
                        afterTest = true;
                    }
                }
            }
        }
        return (inFrontTest && afterTest);
    }
    //替换行内冒号的方法
    public String repMaus(String fullLine, String line, int lineNum){
        System.out.println("\n====花括号添加开始====");
        if(line.contains(":") && isReplaceLine){
            String sketchLine = line;
            System.out.println("检测到冒号且不是添加行");
            if (shouldBaked){
                line = line.trim().substring(0, line.trim().length() - 1) + "){";
                System.out.println("添加){");
            }
            else{
                line = line.trim().substring(0, line.trim().length() - 1) + "{";
                System.out.println("添加{");
            }

            for (int l = 0; l < getSpaceFront(sketchLine); l++){
                line = " " + line;
            }
            for (int l = 0; l < getSpaceHind(sketchLine); l++){
                line += " ";
            }
            tabSpc.add(new bracket(lineNum, getSpaceFront(fullLine) + 1));
            System.out.println("待添加}列表空格需求新增: " + tabSpc.get(tabSpc.size() - 1).getSpaces());
            generate = true;
            shouldBaked = false;
            line = line.replaceAll(":", "cspc6086");
            System.out.println("冒号替换符合修改条件");
            modded = true;
        }
        else {
            System.out.println("不符合添加条件");
        }
        System.out.println("====花括号添加结束====");
        return line;
    }
    //替换while的方法
    public String repWhile(String line, int lineNum){
        if (variableTest(line, "while")){
            if (line.contains(":")){
                String condition = line.substring(line.indexOf("while"), line.indexOf(":") + 1);
                condition = "REPEAT UNTIL(NOT " + condition.substring(condition.indexOf("while") + 5, condition.indexOf(":")).trim() + ")";
                line = line.substring(0, line.indexOf("while")) + condition + line.substring(line.indexOf(":"));
            }
            else{
                String condition = line.substring(line.indexOf("while"));
                condition = "REPEAT UNTIL(NOT " + condition.substring(condition.indexOf("while") + 5).trim();
                shouldBaked = true;
                line = line.substring(0, line.indexOf("while")) + condition;
            }
            System.out.println("WHILE替换符合修改条件");
            modded = true;
            isReplaceLine = true;
        }
        return line;
    }
    //替换for的方法  //变量名可能被检测*恶性
    public String repFor(String line, int lineNum){
        if (variableTest(line, "for")){
            if (line.contains(":")){
                String condition = line.substring(line.indexOf("for"), line.indexOf(":") + 1);
                condition = "FOR EACH" + condition.substring(condition.indexOf("for") + 3, condition.indexOf("in")) + "IN" + condition.substring(condition.indexOf("in") + 2, condition.indexOf(":"));
                line = line.substring(0, line.indexOf("for")) + condition + line.substring(line.indexOf(":"));
            }
            else{
                String condition = line.substring(line.indexOf("for"));
                condition = "FOR EACH" + condition.substring(condition.indexOf("for") + 3, condition.indexOf("in")) + "IN" + condition.substring(condition.indexOf("in") + 2);
                line = line.substring(0, line.indexOf("for")) + condition;
            }
            System.out.println("FOR替换符合修改条件");
            modded = true;
            isReplaceLine = true;
        }
        return line;
    }
    //替换if的方法
    public String repIf(String line, int lineNum){
        if (variableTest(line, "if")){
            if (line.contains(":")){
                String condition = line.substring(line.indexOf("if"), line.indexOf(":") + 1);
                condition = "IF(" + condition.substring(condition.indexOf("if") + 2, condition.indexOf(":")).trim() + ")";
                line = line.substring(0, line.indexOf("if")) + condition + line.substring(line.indexOf(":"));
            }
            else{
                String condition = line.substring(line.indexOf("if"));
                condition = "IF(" + condition.substring(condition.indexOf("if") + 2).trim();
                shouldBaked = true;
                line = line.substring(0, line.indexOf("if")) + condition;
            }
            System.out.println("IF替换符合修改条件");
            modded = true;
            isReplaceLine = true;
        }
        return line;
    }
    //替换else if的方法
    public String repElif(String line, int lineNum){
        if (variableTest(line, "elif")){
            if (line.contains(":")){
                String condition = line.substring(line.indexOf("elif"), line.indexOf(":") + 1);
                condition = "ELSE IF(" + condition.substring(condition.indexOf("elif") + 4, condition.indexOf(":")).trim() + ")";
                line = line.substring(0, line.indexOf("elif")) + condition + line.substring(line.indexOf(":"));
            }
            else{
                String condition = line.substring(line.indexOf("elif"));
                condition = "ELSE IF(" + condition.substring(condition.indexOf("elif") + 4).trim();
                shouldBaked = true;
                line = line.substring(0, line.indexOf("elif")) + condition;
            }
            System.out.println("ELIF替换符合修改条件");
            modded = true;
            isReplaceLine = true;
        }
        return line;
    }
    //替换else的方法
    public String repELSE(String line, int lineNum){
        if (variableTest(line, "else")){
            String condition = line.substring(line.indexOf("else"), line.indexOf(":") + 1);
            condition = "ELSE" + condition.substring(condition.indexOf("else") + 4, condition.indexOf(":"));
            line = line.substring(0, line.indexOf("else")) + condition + line.substring(line.indexOf(":"));
            System.out.println("ELSE替换符合修改条件");
            modded = true;
            isReplaceLine = true;
        }
        return line;
    }
    //替换def的方法
    public String repDef(String line, int lineNum){
        System.out.println("\n====方法替换开始====");
        if (variableTest(line, "def")){
            String condition = line.substring(line.indexOf("def"));
            condition = "PROCEDURE" + condition.substring(condition.indexOf("def") + 3);
            line = line.substring(0, line.indexOf("def")) + condition;
            System.out.println("DEF替换符合修改条件");
            modded = true;
            isReplaceLine = true;
        }
        else{
            System.out.println("未检测到def");
        }
        System.out.println("====方法替换结束====");
        return line;
    }
    //替换def的方法
    public String repClass(String line, int lineNum){
        if (variableTest(line, "class")){
            String condition = line.substring(line.indexOf("class"));
            condition = "CLASS" + condition.substring(condition.indexOf("class") + 5);
            line = line.substring(0, line.indexOf("class")) + condition;
            System.out.println("CLASS替换符合修改条件");
            modded = true;
            isReplaceLine = true;
        }
        return line;
    }
    //替换return的方法
    public String repRETURN(String line){
        if(variableTest(line, "return")){
            retbaked = true;
            line = line.substring(0, line.indexOf("return")) + "RETURN(" + line.substring(line.indexOf("return") + 6).trim();
            System.out.println("RETURN替换符合修改条件");
            modded = true;
        }
        return line;
    }
}