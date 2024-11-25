import gui.filer;

import java.io.IOException;
import java.util.ArrayList;

import static gui.controller.finalSavingPath;

public class seTes {
    public static void main(String[]args) throws IOException {
        filer robot = new filer();
        ArrayList<String> lines = robot.readFile("E:\\javaProject\\CSP reference converter\\apCSPReferenceConverter\\AP_CSP_Converter\\output\\test104.txt");
        for (int x = 0; x < lines.size(); x++){
            String text = lines.get(x).substring(0, lines.get(x).indexOf("\"") + 1) + "U.S.ARMY | " + lines.get(x).substring(lines.get(x).indexOf("\"") + 1);
            lines.set(x, text);
        }
        finalSavingPath = "E:\\javaProject\\CSP reference converter\\apCSPReferenceConverter\\AP_CSP_Converter\\output\\test103MOD.txt";
        robot.saveData(lines);
    }
}
