package sample;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;


public class GameInsert {

@FXML
private TextField GameName;
@FXML
private TextField gamePublisher;
@FXML
private TextField devStudio;
@FXML
private TextField pubYear;
@FXML
private TextField Rating,reviewNum;
@FXML
private TextField Age;
@FXML
private TextField imagePath,getImagePath,getTitle;
@FXML
private TextField genre1,genre2,genre3;
@FXML
private RadioButton setSingleplayerOption;
@FXML
private RadioButton setMultiplayerOption;
@FXML
private RadioButton pc,ps3,ps4,ps5,xboxOne,xbox360,xboxX,nintendoSwitch,psp,wii,psvita,android;
@FXML
private ImageView previewImage;
@FXML
private Label setMessage,messageLabel;
@FXML
private Button backToLogin;


public void insertButtonAction(ActionEvent event){
    String name,publisher,studio,image,image1,image2,image3,pc_plat = null,ps3_plat = null,ps4_plat = null,ps5_plat = null,xbox0ne_plat = null,xbox360_plat = null,xboxX_plat = null,switch_plat = null,psp_plat = null, psvita_plat = null, wii_plat = null, android_plat = null;
    int ageRating,isSingleplayer,isMultiplayer,totalReviews,score;
    String pub_date ,genres1,genres2,genres3;

    if(GameName.getText().isEmpty() || gamePublisher.getText().isEmpty() || devStudio.getText().isEmpty() || pubYear.getText().isEmpty() || Age.getText().isEmpty() || Rating.getText().isEmpty()){
        setMessage.setText("Please insert all required info");
        labelFade(3);
    }
    else {
        name = GameName.getText();
        publisher = gamePublisher.getText();
        studio = devStudio.getText();
        image = imagePath.getText();
        pub_date =pubYear.getText();
        ageRating = Integer.parseInt(Age.getText());
        score = Integer.parseInt(Rating.getText());
        totalReviews = Integer.parseInt(reviewNum.getText());

        if(setSingleplayerOption.isSelected()){
            isSingleplayer = 1;
        }
        else{
            isSingleplayer = 0;
        }
        if(setMultiplayerOption.isSelected()) {
            isMultiplayer = 1;
        }
        else{
            isMultiplayer = 0;
        }
        if(pc.isSelected()){
            pc_plat = "'PC'";
        }
        if(ps3.isSelected()){
            ps3_plat = "'PS3'";
        }
        if(ps4.isSelected()){
            ps4_plat = "'PS4'";
        }
        if(ps5.isSelected()){
            ps5_plat = "'PS5'";
        }
        if(xboxOne.isSelected()){
            xbox0ne_plat = "'XBOX-ONE'";
        }
        if(xbox360.isSelected()){
            xbox360_plat = "'XBOX-360'";
        }
        if(xboxX.isSelected()){
            xboxX_plat = "'XBOX-X'";
        }
        if(nintendoSwitch.isSelected()){
            switch_plat = "'Nintendo-Switch'";
        }
        if(psp.isSelected()){
            psp_plat = "'PSP'";
        }
        if(psvita.isSelected()){
            psvita_plat = "'PS-Vita'";
        }
        if(android.isSelected()){
            android_plat = "'Android'";
        }
        if(wii.isSelected()){
            wii_plat = "'Wii'";
        }
        if(!genre1.getText().isEmpty()){
            genres1 = "'"+genre1.getText()+"'";
        }
        else{
            genres1 = null;
        }
        if(!genre2.getText().isEmpty()){
            genres2 = "'"+genre2.getText()+"'";
        }
        else{
            genres2 = null;
        }
        if(!genre3.getText().isEmpty()){
            genres3 = "'"+genre3.getText()+"'";
        }
        else{
            genres3 = null;
        }

        DatabaseConnector connectNow = new DatabaseConnector();
        Connection connectDB = connectNow.getConnection();
        String putNewGame = "INSERT INTO game_info_table(game_name,game_publisher,development_studio,publication_date,age_appropriation,total_rating,total_reviews,image,singleplayer,multiplayer) VALUES ('" + name + "','" + publisher + "','" + studio + "','" + pub_date + "','" + ageRating + "','" + score + "','"+totalReviews+"',?,'"+isSingleplayer+"','"+isMultiplayer+"')";
        String insertGamePlatforms = "INSERT INTO game_platform(game_name,pc,ps3,ps4,ps5,xboxOne,xboxX,xbox360,nintendoSwitch,psp,psvita,wii,android) VALUES('" + name + "'," + pc_plat + "," + ps3_plat + ","+ps4_plat+","+ps5_plat+","+xbox0ne_plat+","+xboxX_plat+","+xbox360_plat+","+switch_plat+","+psp_plat+","+psvita_plat+","+wii_plat+","+android_plat+")";
        String updateGenres = "UPDATE genres SET genre1 = "+genres1+", genre2 = "+genres2+",genre3 = "+genres3+" WHERE game_name = '" + name + "';";

        try {
           PreparedStatement preparedStatement = connectDB.prepareStatement(putNewGame);
            preparedStatement.setBytes(1,readFile(image));
            preparedStatement.executeUpdate();
            Statement insertPlatforms = connectDB.createStatement();
            insertPlatforms.executeUpdate(insertGamePlatforms);
            Statement insertGenres = connectDB.createStatement();
            insertGenres.executeUpdate(updateGenres);
            setMessage.setText("Game inserted");
            labelFade(3);
            connectDB.close();
        } catch (Exception e) {
            setMessage.setText("Game probably already in DB");
            labelFade(3);
            e.printStackTrace();
            e.getCause();
        }
    }

}

public void deleteButtonAction(ActionEvent event){
    DatabaseConnector connectNow = new DatabaseConnector();
    Connection connectDB = connectNow.getConnection();
    String deleteGame = "DELETE FROM game_info_table WHERE game_name = '"+GameName.getText()+"'";
    try{
        Statement deleteGameStatement = connectDB.createStatement();
        deleteGameStatement.executeUpdate(deleteGame);
        connectDB.close();
        setMessage.setText("Game Deleted");
        labelFade(3);
    }catch(SQLException e){
        e.getCause();
        e.printStackTrace();
    }
}

public void insertImageButtonAction(ActionEvent event){
    String image;
    String game_name,image_title;
    game_name = GameName.getText();
    image = getImagePath.getText();
    image_title = getTitle.getText();
    DatabaseConnector connectNow = new DatabaseConnector();
    Connection connectDB = connectNow.getConnection();
    String putNewImage = "INSERT INTO game_images(image_title,game_name,image) VALUES ('" + image_title + "','"+game_name+"',?)";
    try {
        PreparedStatement preparedStatement = connectDB.prepareStatement(putNewImage);
        preparedStatement.setBytes(1, readFile(image));
        preparedStatement.executeUpdate();
        setMessage.setText("Image added successfully");
        labelFade(3);
        connectDB.close();
    }catch(Exception e){
        e.printStackTrace();
        e.getCause();
    }


}

public void previewCoverImage(ActionEvent event){
    //InputStream inputStream = readFile(imagePath.getText());
   // javafx.scene.image.Image image = new Image(inputStream);
    try {
        InputStream stream = new FileInputStream(imagePath.getText());
        Image image = new Image(stream);
        previewImage.setImage(image);
    }
    catch(FileNotFoundException e){
        e.printStackTrace();
    }
}

public void previewImage(ActionEvent event){
    //InputStream inputStream = readFile(imagePath.getText());
    // javafx.scene.image.Image image = new Image(inputStream);
    try {
        InputStream stream = new FileInputStream(getImagePath.getText());
        Image image = new Image(stream);
        previewImage.setImage(image);
    }
    catch(FileNotFoundException e){
        e.printStackTrace();
    }
}

private byte[] readFile(String imageFile) {
        ByteArrayOutputStream bos = null;
        try {
            File image = new File(imageFile);
            FileInputStream fis = new FileInputStream(image);
            byte[] buffer = new byte[1024];
            bos = new ByteArrayOutputStream();
            for (int len; (len = fis.read(buffer)) != -1;) {
                bos.write(buffer, 0, len);
            }
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (IOException e2) {
            System.err.println(e2.getMessage());
        }
        return bos != null ? bos.toByteArray() : null;
}

private void labelFade(int dur){
    FadeTransition fadeTransition = new FadeTransition(Duration.seconds(dur), setMessage);
    fadeTransition.setFromValue(1.0);
    fadeTransition.setToValue(0.0);
    fadeTransition.play();
}

public void enableLabel(){
    messageLabel.setOpacity(1);
}

public void disableLabel(){
    messageLabel.setOpacity(0);
}

public void goBackToLogin(ActionEvent event){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("loginPanel.fxml"));
            Stage registerStage = new Stage();
            registerStage.setTitle("Welcome to GamersWorld");
            registerStage.setScene(new Scene(root, 746, 536));
            registerStage.show();
            Stage stage = (Stage) backToLogin.getScene().getWindow();
            stage.close();
        }
        catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }
}
