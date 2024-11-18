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
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import scripts.processJav;
import scripts.processPy;

import java.io.File;
import java.io.IOException;

public class controller {
    //文件处理器
    private final filer filer = new filer();
    //转译模式
    private static String mode = "";
    //最终保存路径
    public static String finalSavingPath = "";
    //被读取文件的路径
    private static String targetFile = "";
    private static Stage stage;

    @FXML
    private Button applyPro;
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
    private Text originalFile;
    @FXML
    private Text modeText;
    @FXML
    private Button getTargetFile;

    public static void setStage(Stage primStage){
        stage = primStage;
    }

    //选择目标文件
    public String getTargeto(){
        //需要改进
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("File Path");
        try{
            File choosenFile = fileChooser.showOpenDialog(stage);
            return choosenFile.getAbsolutePath();
        }
        catch (Exception ignored){
            System.out.println("未选择任何文件");
            return null;
        }
    }
    //设置文件路径的按钮
    @FXML
    public void setGetTargetFile(MouseEvent event){
        filePath.setText(getTargeto());
    }

    //关闭保存界面的方法
    @FXML
    public void savCloseWindow(){
        Stage stage = (Stage) saveCancel.getScene().getWindow();
        stage.close();
    }
    //检测用户输入文件路径的方法
    @FXML
    public void getFileInfo(MouseEvent event) throws IOException {
        if (!filer.readFile(filePath.getText()).isEmpty()){
            if(langSelect.getText().equals("JAVA")){
                targetFile = filePath.getText();
                mode = "java";
                showSave(event);
                outputTex.setText("");
                System.out.println("获取的模式：");
                System.out.println(mode);
            }
            else if(langSelect.getText().equals("PYTHON")){
                targetFile = filePath.getText();
                mode = "python";
                showSave(event);
                outputTex.setText("");
                System.out.println("获取的模式：");
                System.out.println(mode);
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
    //显示设置界面的方法
    @FXML
    public void showSetting(MouseEvent event){
        dropWindow("setting", "Setting");
    }
    //显示默认保存路径的方法
    public void setPathProm(){
        savePath.setPromptText(filer.userPath + "/");
    }
    //刷新目标储存路径的方法
    @FXML
    public void freshFile(){
        String path = filer.userPath;
        String reletive = "";
        if(!savePath.getText().isEmpty()){
            path = savePath.getText();
        }
        if(saveFileName.getText().isEmpty()) {
            reletive = (path + "/" + filer.getTime() + ".txt");
        }
        else {
            reletive = (path + "/" + saveFileName.getText() + ".txt");
        }
        File file = new File(reletive);
        finalSavingPath = file.getAbsolutePath();
        fileWilling.setText(finalSavingPath);
        modeText.setText("TYPE | " + mode.toUpperCase());
    }
    //显示保存界面的方法
    @FXML
    public void showSave(MouseEvent event){
        controller controller = dropWindow("save", "Save").getController();
        controller.setPathProm();
        controller.freshFile();
        controller.setOri();
    }
    //关闭设置界面的方法
    @FXML
    public void closeWindow(){
        Stage stage = (Stage) setCancel.getScene().getWindow();
        stage.close();
    }
    //退出程序的方法
    @FXML
    public void exitApp(){
        System.exit(701);
    }
    //启动处理目标文件文本的方法
    @FXML
    public void processing(MouseEvent event) throws IOException {
        System.out.println("文本源文件:");
        System.out.println(targetFile);
        System.out.println("将要输出的文件：");
        System.out.println(finalSavingPath);
        if (mode.equals("java")){
            processJav dealer = new processJav();
            dealer.setData(filer.readFile(targetFile));
            dealer.turnRefJav();
            filer.saveData(dealer.getData());
        }
        else if(mode.equals("python")){
            processPy dealer = new processPy();
            dealer.setData(filer.readFile(targetFile));
            dealer.turnRefPy();
            filer.saveData(dealer.getData());
        }
        else{
            System.out.println("ERROR: Saving failed!");
        }
        Stage stage = (Stage) applyPro.getScene().getWindow();
        stage.close();
    }

    //UI控制方法集合
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

    //设置保存界面被读取文件的方法
    @FXML
    public void setOri(){
        originalFile.setText(targetFile);
    }
}
