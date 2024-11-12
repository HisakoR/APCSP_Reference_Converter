package gui;

import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
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
    private MenuButton langSelect;
    @FXML
    private Text outputTex;

    @FXML
    public void getFileInfo(MouseEvent event) throws IOException {
        if (!filer.readFile(filePath.getText()).isEmpty()){
            outputTex.setText("FINISH: Result file path: " + filer.getUserPath());
            if(langSelect.getText().equals("JAVA")){
                filer.saveData(filer.readFile(filePath.getText()));
            }
            else{
                outputTex.setText("ERROR: Language not available!");
            }
        }
        else{
            outputTex.setText("ERROR: Path not available!");
        }
    }
    @FXML
    public void setLangPY(){
        langSelect.setText("PYTHON");
    }
    @FXML
    public void setLangJV(){
        langSelect.setText("JAVA");
    }
    @FXML
    public void exitApp(){
        System.exit(701);
    }
}
