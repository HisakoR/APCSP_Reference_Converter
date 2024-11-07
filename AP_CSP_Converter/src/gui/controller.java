package gui;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;

public class controller {
    private final filer filer = new filer();
    @FXML
    private Text texter;

    @FXML
    public void getFileInfo(MouseEvent event) throws IOException {
        texter.setText(filer.readFile("src/lol.txt").get(2));
        filer.saveData(filer.readFile("src/lol.txt"));

    }
}
