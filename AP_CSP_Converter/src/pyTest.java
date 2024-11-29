import gui.controller;
import gui.filer;
import scripts.processPy;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import static gui.controller.finalSavingPath;

public class pyTest {
    public static void main(String[]args) throws IOException {
        Scanner louie = new Scanner(System.in);
        System.out.println("选择测试:\n[0]标准测试\n[1]MIDTERM测试\n[2]嵌套测试\n[3]MIDTERM测试（CLASS）\n[4]Jin's main");
        String introo = louie.nextLine();
        int read = 11;
        try{
            read = Integer.parseInt(introo);
        }
        catch(Exception ignored){

        }
        String testnam = "";
        if (read == 0){
            testnam = "standardTest";
        }
        else if(read == 1){
            testnam = "termTest";
        }
        else if(read == 2){
            testnam = "loopAndCondition";
        }
        else if(read == 3){
            testnam = "termTheClassTest";
        }
        else if(read == 4){
            testnam = "main";
        }
        else{
            System.out.println("目标测试不可用");
            System.exit(813);
        }
        int fileNam = 0;
        boolean isAva = false;
        finalSavingPath = "output\\test" + fileNam + ".txt";
        filer filer = new filer();
        processPy dealer = new processPy();
        dealer.setData(filer.readFile("output\\testGround\\" + testnam + ".py"));
        dealer.turnRefPy();
        while(!isAva){
            File file = new File(finalSavingPath);
            if (file.exists()){
                fileNam++;
            }
            else {
                isAva = true;
            }
            finalSavingPath = "output\\test" + fileNam + ".txt";
        }
        filer.saveData(dealer.getData());
    }
}
