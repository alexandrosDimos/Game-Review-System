package sample;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.util.Duration;
import javafx.scene.paint.Color;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;


public class LoginController{
    @FXML
    private Button exitButton;
    @FXML
    public Hyperlink goToRegister;
    @FXML
    private Label loginMessageLabel;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordTextField;


    public void exitButtonAction(ActionEvent event){
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    public void loginButtonAction(ActionEvent event){
        if(usernameTextField.getText().isEmpty() || passwordTextField.getText().isEmpty()){
            loginMessageLabel.setTextFill(Color.RED);
            loginMessageLabel.setText("Bro no password or username");
            labelFade(3);
        }
        else {
            validateLogin();
        }
    }

    public void signUpButtonAction(ActionEvent event){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("registerPanel.fxml"));
            Stage registerStage = new Stage();
            registerStage.setTitle("Register to GamersWorld");
            registerStage.setScene(new Scene(root, 746, 563));
            registerStage.show();
            Stage stage = (Stage) goToRegister.getScene().getWindow();
            stage.close();
        }
        catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    public void validateLogin(){
        String username,password;

        DatabaseConnector connectNow = new DatabaseConnector();
        Connection connectDB = connectNow.getConnection();
        if(connectDB == null){
            System.out.println("Sth wrong with connectDB");
        }
        username = usernameTextField.getText();
        password = passwordTextField.getText();
        String verifyLogin = "SELECT count(1) FROM account WHERE username = '"+username+"' COLLATE utf8mb4_0900_as_cs AND password = '"+password+"' COLLATE utf8mb4_0900_as_cs";
        try{
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyLogin);

            while(queryResult.next()){
                if(queryResult.getInt(1) == 1){
                    if(usernameTextField.getText().equals("administrator") && passwordTextField.getText().equals("administrator")){
                        Parent root = FXMLLoader.load(getClass().getResource("gameInsertPanel.fxml"));
                        Stage mainAppStage = new Stage();
                        mainAppStage.setTitle("Game Zone");
                        mainAppStage.setScene(new Scene(root, 841, 632));
                        mainAppStage.show();
                        Stage stage = (Stage) goToRegister.getScene().getWindow();
                        stage.close();
                        break;
                    }
                    User current_user = new User(username);


                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("mainPanel.fxml"));
                    Parent root = (Parent) fxmlLoader.load();
                    MainPanelController controller = fxmlLoader.getController();
                    if(controller == null){
                        System.out.println("WTF");
                    }
                    //controller.gameList = newGames;
                    controller.connectedUser(current_user);
                    Stage stage = (Stage)usernameTextField.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Gamers Area");



                }else{
                    loginMessageLabel.setTextFill(Color.RED);
                    loginMessageLabel.setText("Try again.Invalid Login ");
                    labelFade(3);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }

    }

    private void labelFade(int dur){
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(dur), loginMessageLabel);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.play();
    }
}
