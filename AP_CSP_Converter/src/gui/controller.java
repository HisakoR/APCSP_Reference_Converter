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
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import scripts.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class controller {
    //文件处理器
    private final scripts.filer filer = new filer();
    //设置法
    private config configer = new config();
    //转译模式
    private static String mode = "";
    //更新机
    private static updater updater = new updater();
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
    private Button closeComp;
    @FXML
    private TextField defaultSaver;
    @FXML
    private Button returnBolcker;

    public static void setStage(Stage primStage){
        stage = primStage;
    }
    //跳转到github官方页面
    @FXML
    public void gotoGit(MouseEvent event){
        if(config.versions < updater.getVersionNumber()){
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                try {
                    URI uri = new URI("https://github.com/HisakoR/Nans_APCSP_Pseudocode_Converter/releases");
                    desktop.browse(uri);
                }
                catch (Exception ignored) {
                }
            }
            else {
                System.out.println("无法打开浏览器");
            }
        }
        else{
            dropWindow("updaterBlocker", "Announcement");
        }
    }
    //选择目标文件
    public String getTargeto(){
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
    //选择目标文件夹
    public String getDectory(){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Folder Path");
        try{
            File choosenFile = directoryChooser.showDialog(stage);
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
        String pathway = getTargeto();
        if(pathway != null){
            filePath.setText(pathway);
        }
    }

    //设置文件夹路径的按钮
    @FXML
    public void setDirectAth(MouseEvent event){
        String pathway = getDectory();
        if(pathway != null){
            savePath.setText(pathway);
        }
    }

    //设置文件夹路径的按钮
    @FXML
    public void setDefaultSaver(MouseEvent event){
        String pathway = getDectory();
        if(pathway != null){
            defaultSaver.setText(pathway);
        }
    }
    //重设设置
    @FXML
    public void resetConfig() throws IOException {
        ArrayList<String> lines = new ArrayList<>();
        lines.add("0");
        lines.add(defaultSaver.getText());
        filer.rewriteFile(lines, "config.txt");
        closeWindow();
        configer.initialize();
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
    public static FXMLLoader dropWindow(String guiPart, String guiName){
        FXMLLoader loader = new FXMLLoader(controller.class.getResource(guiPart + ".fxml"));
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
        controller controller = dropWindow("setting", "Setting").getController();
        controller.setDefaultSaver();
    }
    //设定 显示默认保存路径
    public void setDefaultSaver(){
        defaultSaver.setText(filer.userPath);
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
    //关闭完成界面的方法
    @FXML
    public void closeComplete(){
        Stage stage = (Stage) closeComp.getScene().getWindow();
        stage.close();
    }
    //关闭更新拒绝界面的方法
    @FXML
    public void closeBlocker(){
        Stage stage = (Stage) returnBolcker.getScene().getWindow();
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
