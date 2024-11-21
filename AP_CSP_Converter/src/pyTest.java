import gui.controller;
import gui.filer;
import scripts.processPy;

import java.io.File;
import java.io.IOException;

import static gui.controller.finalSavingPath;

public class pyTest {
    public static void main(String[]args) throws IOException {
        int fileNam = 0;
        boolean isAva = false;
        finalSavingPath = "output\\test" + fileNam + ".txt";
        filer filer = new filer();
        processPy dealer = new processPy();
        dealer.setData(filer.readFile("output\\testGround\\standardTest.py"));
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
