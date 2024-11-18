import gui.controller;
import gui.filer;
import scripts.processPy;

import java.io.IOException;
public class test {
    public static void main(String[]args) throws IOException {
        filer filer = new filer();
        processPy dealer = new processPy();
        dealer.setData(filer.readFile("C:\\Users\\TEST\\Desktop\\tes.txt"));
        dealer.turnRefPy();
        controller.finalSavingPath = "C:\\Users\\TEST\\Desktop\\ouser\\tele\\lol.txt";
        filer.saveData(dealer.getData());
    }
}
