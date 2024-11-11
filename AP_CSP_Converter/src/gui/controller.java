package gui;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;

public class controller {
    private final filer filer = new filer();
    @FXML
    private Text texter;
    @FXML
    private TextField filePath;

    @FXML
    public void getFileInfo(MouseEvent event) throws IOException {
        texter.setText(filer.readFile(filePath.getText()).get(0));
        filer.saveData(filer.readFile(filePath.getText()));

    }
}
