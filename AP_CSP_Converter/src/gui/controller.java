package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class controller {
    private final filer filer = new filer();
    @FXML
    private Text texter;
    @FXML
    private TextField filePath;
    @FXML
    private MenuButton langSelect;
    @FXML
    private MenuButton langSetting;
    @FXML
    private Text outputTex;
    @FXML
    private Button setCancel;

    @FXML
    private Text fileWilling;
    @FXML
    private Button saveCancel;
    @FXML
    private TextField savePath;
    @FXML
    private TextField saveFileName;

    @FXML
    public void savCloseWindow(){
        Stage stage = (Stage) saveCancel.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void getFileInfo(MouseEvent event) throws IOException {
        if (!filer.readFile(filePath.getText()).isEmpty()){
            if(langSelect.getText().equals("JAVA")){
                showSave(event);
                outputTex.setText("FINISH: Result file path: " + filer.getUserPath());
            }
            else{
                outputTex.setText("ERROR: Language not available!");
            }
        }
        else{
            outputTex.setText("ERROR: Path not available!");
        }
    }
    //搬运自凯乐兹，切换场景的方法
    public FXMLLoader setScene(MouseEvent event, String resource) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(resource));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        //返回loader以便使用controller
        return loader;
    }
    //弹出窗口的方法
    public FXMLLoader dropWindow(String guiPart, String guiName){
        FXMLLoader loader = new FXMLLoader(getClass().getResource(guiPart + ".fxml"));
        try {
            Stage newWindow = new Stage();
            newWindow.initModality(Modality.APPLICATION_MODAL);
            Parent root = loader.load();
            newWindow.setTitle(guiName);
            newWindow.setScene(new Scene(root));
            newWindow.show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return loader;
    }
    @FXML
    public void showSetting(MouseEvent event){
        dropWindow("setting", "Setting");
    }
    public void setPathProm(){
        savePath.setPromptText(filer.userPath + "/");
    }
    @FXML
    public void freshFile(){
        String path = filer.userPath;
        if(!savePath.getText().isEmpty()){
            path = savePath.getText();
        }
        if(saveFileName.getText().isEmpty()) {
            fileWilling.setText(path + "/" + saveFileName.getPromptText() + ".txt");
        }
        else {
            fileWilling.setText(path + "/" + saveFileName.getText() + ".txt");
        }
    }
    @FXML
    public void showSave(MouseEvent event){
        controller controller = dropWindow("save", "Save").getController();
        controller.setPathProm();
        controller.freshFile();
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
    public void setENG(){
        langSetting.setText("English");
    }
    @FXML
    public void setCHI(){
        langSetting.setText("Chinese");
    }
    @FXML
    public void closeWindow(){
        Stage stage = (Stage) setCancel.getScene().getWindow();
        stage.close();
    }
    @FXML
    public void exitApp(){
        System.exit(701);
    }
}
