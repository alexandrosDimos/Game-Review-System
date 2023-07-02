package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("loginPanel.fxml"));
        primaryStage.setTitle("Welcome to gamers area");
        primaryStage.setScene(new Scene(root, 746, 536));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
