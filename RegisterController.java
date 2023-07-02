package sample;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;


public class RegisterController {

@FXML
private Hyperlink backToLogIn;
@FXML
private Button cancelButton;
@FXML
private Label messageLabel;
@FXML
private TextField SetEmail;
@FXML
private TextField SetUsername;
@FXML
private PasswordField SetPassword;
@FXML
private PasswordField ConfirmPassword;
@FXML
private TextField SetAge;

public void exitButtonAction(ActionEvent event){
    Stage stage = (Stage) cancelButton.getScene().getWindow();
    stage.close();
}

public void registerButtonActionEvent(ActionEvent event){
    if(SetUsername.getText().isEmpty() || SetAge.getText().isEmpty() || SetEmail.getText().isEmpty() || SetPassword.getText().isEmpty() || ConfirmPassword.getText().isEmpty()){
        messageLabel.setText("Please set all the required info");
        messageLabel.setTextFill(Color.RED);
        labelFade(3);
    }
    else{
        int psswdChck = passwordCheck(SetPassword.getText(),ConfirmPassword.getText());
        int emailcheck = emailCheck(SetEmail.getText());
        if(psswdChck == 0 && emailcheck == 0) {
            boolean emailDuplicateCheck = checkForEmailDuplicate(SetEmail.getText());
            boolean usernameDuplicateCheck = checkForUsernameDuplicate(SetUsername.getText());
            if(!emailDuplicateCheck && !usernameDuplicateCheck) {
                registerAccount();
            }
            else if(emailDuplicateCheck && usernameDuplicateCheck){
                messageLabel.setTextFill(Color.RED);
                messageLabel.setText("Username and Email are already in use");
                labelFade(3);
            }
            else if(usernameDuplicateCheck && !emailDuplicateCheck){
                messageLabel.setTextFill(Color.RED);
                messageLabel.setText("This username is already in use");
                labelFade(3);
            }
            else if(emailDuplicateCheck && !usernameDuplicateCheck){
                messageLabel.setTextFill(Color.RED);
                messageLabel.setText("This email is already in use");
                labelFade(3);
            }
        }
        else if(psswdChck == 1 && emailcheck == 0){
            messageLabel.setTextFill(Color.RED);
            messageLabel.setText("Check your confirmation password");
            labelFade(4);
        }
        else if(psswdChck == 0 && emailcheck == 1){
            messageLabel.setTextFill(Color.RED);
            messageLabel.setText("Check your email format");
            labelFade(4);
        }
        else if(psswdChck == 1 && emailcheck == 1){
            messageLabel.setTextFill(Color.RED);
            messageLabel.setText("Check your email format and your confirmation password");
            labelFade(4);
        }
    }
};

public void registerAccount(){
    DatabaseConnector connectNow = new DatabaseConnector();
    Connection connectDB = connectNow.getConnection();

    String putNewUser = "INSERT INTO account(username,password,email,age) VALUES ('"+SetUsername.getText()+"','"+SetPassword.getText()+"','"+SetEmail.getText()+"','"+Integer.parseInt(SetAge.getText())+"')";
    try{
        Statement statement = connectDB.createStatement();
        statement.executeUpdate(putNewUser);

        messageLabel.setTextFill(Color.BLACK);
        messageLabel.setText("You are now a true gamer");
        labelFade(3);
    }catch(Exception e){
        e.printStackTrace();
        e.getCause();
    }
}

private int passwordCheck(String password,String confirmpassword){

    if(password.equals(confirmpassword)){
        return 0;
    }
    return 1;
}

private int emailCheck(String email){

    if(email.contains("@")){
        return 0;
    }
    return 1;
}

private boolean checkForEmailDuplicate(String insertedEmail){
    DatabaseConnector connectNow = new DatabaseConnector();
    Connection connectDB = connectNow.getConnection();

    String emailCheck = "SELECT count(1) FROM account WHERE email = '"+insertedEmail+"'";
    try{
        Statement statement = connectDB.createStatement();
        ResultSet queryResult = statement.executeQuery(emailCheck);
        while(queryResult.next()) {
            if (queryResult.getInt(1) == 1) {
                return true;
            } else {
                return false;
            }
        }
    }catch(Exception e){
        e.printStackTrace();
        e.getCause();
    }
    return false;
}

private boolean checkForUsernameDuplicate(String insertedUsername){
    DatabaseConnector connectNow = new DatabaseConnector();
    Connection connectDB = connectNow.getConnection();

    String usernameCheck = "SELECT count(1) FROM account WHERE username = '"+insertedUsername+"'";
    try{
        Statement statement = connectDB.createStatement();
        ResultSet queryResult = statement.executeQuery(usernameCheck);
        while(queryResult.next()) {
            if (queryResult.getInt(1) == 1) {
                return true;
            } else {
                return false;
            }
        }
    }catch(Exception e){
        e.printStackTrace();
        e.getCause();
    }
    return false;

}

public void logInButtonAction(ActionEvent event){
    try {
        Parent root = FXMLLoader.load(getClass().getResource("loginPanel.fxml"));
        Stage registerStage = new Stage();
        registerStage.setTitle("Welcome to GamersWorld");
        registerStage.setScene(new Scene(root, 746, 536));
        registerStage.show();
        Stage stage = (Stage) backToLogIn.getScene().getWindow();
        stage.close();
    }
    catch(Exception e){
        e.printStackTrace();
        e.getCause();
    }

}

private void labelFade(int dur){
    FadeTransition fadeTransition = new FadeTransition(Duration.seconds(dur), messageLabel);
    fadeTransition.setFromValue(1.0);
    fadeTransition.setToValue(0.0);
    fadeTransition.play();
}

}


