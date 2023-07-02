package sample;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class userProfileController {
    @FXML
    private TextField setUsername,setEmail,setAge,setPassword,setConfirmationPassword,ratedGames,commentedGames;
    @FXML
    private Label setMessage;
    @FXML
    private ScrollPane userActivityPane;
    User currentUser;
    public void getUserInformation(User connectedUser) {
        String username = null,email = null,password = null;
        int age = 0;
        int column = 0  ,row = 1;
        currentUser = connectedUser;
        DatabaseConnector connectNow = new DatabaseConnector();
        Connection connectDB = connectNow.getConnection();

        String userInfo = "SELECT * FROM account WHERE username = '"+connectedUser.getUser()+"'";

        try {
            Statement statement = connectDB.createStatement();
            ResultSet userInformation = statement.executeQuery(userInfo);
            if(userInformation.first()){
                username = userInformation.getString(2);
                password = userInformation.getString(3);
                email = userInformation.getString(4);
                age = userInformation.getInt(5);
            }
        }catch(SQLException e){
            e.printStackTrace();
            e.getCause();
        }

        setUsername.setText(username);
        setEmail.setText(email);
        setPassword.setText(password);
        setConfirmationPassword.setText(password);
        setAge.setText(String.valueOf(age));
        Text userRated = new Text();
        userRated.setText("You have rated the following games");
        userRated.setFill(Color.YELLOW);
        GridPane rated = new GridPane();

        Text userCommented = new Text();
        userCommented.setText("You have commented on the following games");
        userCommented.setFill(Color.YELLOW);
        GridPane commented = new GridPane();

        AnchorPane layout = new AnchorPane();
        ScrollPane layout2 = new ScrollPane();
        VBox components = new VBox();

        String userComments = "SELECT game_name FROM player_activity WHERE username = '"+currentUser.getUser()+"' AND  comment_text IS NOT NULL";
        String userRates = "SELECT game_name FROM player_activity WHERE username = '"+currentUser.getUser()+"'  AND  rate IS NOT NULL";
        ResultSet commentedGames = null;
        Statement fetchUserRatedGames = null;
        try {
            Statement fetchUserCommentedGames = connectDB.createStatement();
            commentedGames = fetchUserCommentedGames.executeQuery(userComments);
            fetchUserRatedGames= connectDB.createStatement();
            ResultSet ratedGames = fetchUserRatedGames.executeQuery(userRates);
            while(ratedGames.next()){
                Text game_name = new Text();
                game_name.setText(ratedGames.getString(1));
                game_name.setFill(Color.WHITE);
                rated.add(game_name,column,row);
                row++;
            }
            row = 1;
            while(commentedGames.next()){
                Text game_name = new Text();
                game_name.setText(commentedGames.getString(1));
                game_name.setFill(Color.WHITE);
                commented.add(game_name,column,row);
                row++;
            }
            connectDB.close();
        }catch(SQLException e){
            e.getCause();
            e.printStackTrace();
        }
        components.getChildren().addAll(userRated,rated,userCommented,commented);
        userActivityPane.setContent(components);
    }

    public void changeUserInfo(){

        if(!setPassword.getText().equals(setConfirmationPassword.getText())){
            setMessage.setText("Password and confirmation password do not match");
        }
        else {
            DatabaseConnector connectNow = new DatabaseConnector();
            Connection connectDB = connectNow.getConnection();
            String updateUserInfo = "UPDATE account SET username = '" + setUsername.getText() + "', password = '" + setPassword.getText() + "',email = '" + setEmail.getText() + "', age = '" + setAge.getText() + "' WHERE username = '" + currentUser.getUser() + "';";
            try {
                Statement updateInfo = connectDB.createStatement();
                updateInfo.executeUpdate(updateUserInfo);
                setMessage.setText("Your info have been updated");
                labelFade(3);
                connectDB.close();
            } catch (SQLException e) {
                setMessage.setText("Something went wrong");
                labelFade(3);
                e.printStackTrace();
                e.getCause();
            }
            getUserInformation(currentUser);
        }

    }

    private void labelFade(int dur){
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(dur), setMessage);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.play();
    }

    public void cancelButton(ActionEvent event){
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("mainPanel.fxml"));
                Parent root = (Parent) fxmlLoader.load();
                MainPanelController controller = fxmlLoader.getController();
                controller.connectedUser(currentUser);
                Stage stage = (Stage) setUsername.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Gamers Area");
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
    }

}
