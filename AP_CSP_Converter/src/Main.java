import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("gui/main.fxml"));
        primaryStage.setTitle("APCSP Reference Converter 0.1");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
    //主要方法
    public static void main(String[] args) {
        launch(args);
    }
}