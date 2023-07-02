package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.io.InputStream;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Date;
import java.time.*;

public class MainPanelController {
@FXML
private ImageView gameCover,gameCover2,gameCover3;
@FXML
private Button backToLogin,profileButton;
@FXML
private AnchorPane platformsPane,gamesPane;
@FXML
private GridPane grid;
@FXML
private Button pcGames,backToPlatform;
@FXML
private TextField gameSearch;
private User connectedUser;


public void connectedUser(User connUser){
    connectedUser = connUser;
    profileButton.setText(connectedUser.getUser());
}

public void showMeGames(List<String> games,String platform){
    int i = 0,pub_year;
    double finalReview;
    Date pub_date;
    DecimalFormat decFor = new DecimalFormat("0.0");

    List<Game> newGames = new ArrayList<>();
    DatabaseConnector connectNow = new DatabaseConnector();
    Connection connectDB = connectNow.getConnection();

    Iterator<String> gameIterator = games.iterator();
    while(gameIterator.hasNext()) {
        try {
            String choosePicture = "SELECT image,total_rating,total_reviews,game_name,publication_date FROM game_info_table WHERE game_name = '"+gameIterator.next()+ "'";
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(choosePicture);
            if (queryResult.first()) {
                Blob blob = queryResult.getBlob(1);
                int totalRating = queryResult.getInt(2);
                int totalReviews = queryResult.getInt(3);
                String name = queryResult.getString(4);
                pub_date = queryResult.getDate(5);
                InputStream inputStream = blob.getBinaryStream();
                Image image = new Image(inputStream);
                if (totalRating == 0 || totalReviews == 0){
                    finalReview = 0;
                }
                else{
                    finalReview = (double) totalRating/totalReviews;


                }
                Game newGame = new Game(name,Double.parseDouble(decFor.format(finalReview)),image,pub_date);
                newGames.add(newGame);
                i++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            e.getCause();
        }
    }
    goToNextScene(newGames,platform);
}

public void findMePCGames(ActionEvent event){
    ResultSet pcGames = null;
    String platform = "PC";
    String dbCollumn = "pc";
    
    try {
        pcGames = searchGamesViaPlatform(dbCollumn, platform);
    }catch(Exception e){
        e.getCause();
    }
    
    List<String> pcgames;
    pcgames = extractGamesFromQuery(pcGames);
    showMeGames(pcgames,platform);
}

public void findMePS3Games(ActionEvent event){
    ResultSet ps3Games = null;
    String platform = "PS3";
    String dbCollumn = "ps3";
    try {
        ps3Games = searchGamesViaPlatform(dbCollumn, platform);
    }catch(Exception e){
        e.getCause();
    }
    List<String> ps3games;
    ps3games = extractGamesFromQuery(ps3Games);
    showMeGames(ps3games,platform);
}

public void findMePS4Games(ActionEvent event){
    ResultSet ps4Games = null;
    String platform = "PS4";
    String dbCollumn = "ps4";
    try {
        ps4Games = searchGamesViaPlatform(dbCollumn, platform);
    }catch(Exception e){
        e.getCause();
    }
    List<String> ps4games;
    ps4games = extractGamesFromQuery(ps4Games);
    showMeGames(ps4games,platform);
}

public void findMePS5Games(ActionEvent event){
    ResultSet ps5Games = null;
    String platform = "PS5";
    String dbCollumn = "ps5";
    try {
        ps5Games = searchGamesViaPlatform(dbCollumn, platform);
    }catch(Exception e){
        e.getCause();
    }
    List<String> ps5games;
    ps5games = extractGamesFromQuery(ps5Games);
    showMeGames(ps5games,platform);
}

public void findXboxOneGames(ActionEvent event){
    ResultSet xboxOneGames = null;
    String platform = "XBOX-ONE";
    String dbCollumn = "xboxOne";
    try {
        xboxOneGames = searchGamesViaPlatform(dbCollumn, platform);
    }catch(Exception e){
        e.getCause();
    }
    List<String> xboxOnegames;
    xboxOnegames = extractGamesFromQuery(xboxOneGames);
    showMeGames(xboxOnegames,platform);
}

public void findXbox360Games(ActionEvent event){
    ResultSet xbox360Games = null;
    String platform = "XBOX-360";
    String dbCollumn = "xbox360";
    try {
        xbox360Games = searchGamesViaPlatform(dbCollumn, platform);
    }catch(Exception e){
        e.getCause();
    }
    List<String> Xbox360games;
    Xbox360games = extractGamesFromQuery(xbox360Games);
    showMeGames(Xbox360games,platform);
}

public void findMeXboxXGames(ActionEvent event){
    ResultSet xboxXGames = null;
    String platform = "XBOX-X";
    String dbCollumn = "xboxX";
    try {
        xboxXGames = searchGamesViaPlatform(dbCollumn, platform);
    }catch(Exception e){
        e.getCause();
    }
    List<String> XboxXgames;
    XboxXgames = extractGamesFromQuery(xboxXGames);
    showMeGames(XboxXgames,platform);
}

public void findMeNSGames(ActionEvent event){
    ResultSet nsGames = null;
    String platform = "Nintendo-Switch";
    String dbCollumn = "nintendoSwitch";
    try {
        nsGames = searchGamesViaPlatform(dbCollumn, platform);
    }catch(Exception e){
        e.getCause();
    }

    List<String> nsgames;
    nsgames = extractGamesFromQuery(nsGames);
    showMeGames(nsgames,platform);
}

public void findMePSPGames(ActionEvent event){
    ResultSet pspGames = null;
    String platform = "PSP";
    String dbCollumn = "psp";
    try {
        pspGames = searchGamesViaPlatform(dbCollumn, platform);
    }catch(Exception e){
        e.getCause();
    }

    List<String> pspgames;
    pspgames = extractGamesFromQuery(pspGames);
    showMeGames(pspgames,platform);
}

public void findMeWiiGames(ActionEvent event){
    ResultSet wiiGames = null;
    String platform = "Wii";
    String dbCollumn = "wii";
    try {
        wiiGames = searchGamesViaPlatform(dbCollumn, platform);
    }catch(Exception e){
        e.getCause();
    }

    List<String> wiigames;
    wiigames = extractGamesFromQuery(wiiGames);
    showMeGames(wiigames,platform);
}

public void findMePSVitaGames(ActionEvent event){
    ResultSet psVitaGames = null;
    String platform = "PSVita";
    String dbCollumn = "psvita";
    try {
        psVitaGames = searchGamesViaPlatform(dbCollumn, platform);
    }catch(Exception e){
        e.getCause();
    }

    List<String> psVitagames;
    psVitagames = extractGamesFromQuery(psVitaGames);
    showMeGames(psVitagames,platform);
}

public void findMeAndroidGames(ActionEvent event){
    ResultSet androidGames = null;
    String platform = "Android";
    String dbCollumn = "android";
    try {
        androidGames = searchGamesViaPlatform(dbCollumn, platform);
    }catch(Exception e){
        e.getCause();
    }

    List<String> androidgames;
    androidgames = extractGamesFromQuery(androidGames);
    showMeGames(androidgames,platform);
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

public void findMeTopRatedGames(ActionEvent event){
        Date pub_date;
        double finalReview;
        List<Game> newGames = new ArrayList<>();

        DecimalFormat decFor = new DecimalFormat("0.0");
        DatabaseConnector connectNow = new DatabaseConnector();
        Connection connectDB = connectNow.getConnection();
        try{
            String choosePicture = "SELECT image,total_rating,total_reviews,game_name,publication_date FROM game_info_table WHERE total_rating/total_reviews>9";
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(choosePicture);

            while(queryResult.next()){
                Blob blob = queryResult.getBlob(1);
                int totalRating = queryResult.getInt(2);
                int totalReviews = queryResult.getInt(3);
                String name = queryResult.getString(4);
                pub_date = queryResult.getDate(5);
                InputStream inputStream = blob.getBinaryStream();
                Image image = new Image(inputStream);
                if (totalRating == 0 || totalReviews == 0){
                    finalReview = 0;
                }
                else{
                    finalReview = (double) totalRating/totalReviews;
                }
                Game newGame = new Game(name,Double.parseDouble(decFor.format(finalReview)),image,pub_date);
                newGames.add(newGame);
            }
        }catch(SQLException e){
            e.printStackTrace();
            e.getCause();
        }
        goToNextScene(newGames,"Top Rated Games");
}

public void findMePopularGames(ActionEvent event){
    Date pub_date;
    double finalReview;
    List<Game> newGames = new ArrayList<>();

    DecimalFormat decFor = new DecimalFormat("0.0");
    DatabaseConnector connectNow = new DatabaseConnector();
    Connection connectDB = connectNow.getConnection();
    try{
        String choosePicture = "SELECT image,total_rating,total_reviews,game_name,publication_date FROM game_info_table WHERE total_reviews>10";
        Statement statement = connectDB.createStatement();
        ResultSet queryResult = statement.executeQuery(choosePicture);

        while(queryResult.next()){
            Blob blob = queryResult.getBlob(1);
            int totalRating = queryResult.getInt(2);
            int totalReviews = queryResult.getInt(3);
            String name = queryResult.getString(4);
            pub_date = queryResult.getDate(5);
            InputStream inputStream = blob.getBinaryStream();
            Image image = new Image(inputStream);
            if (totalRating == 0 || totalReviews == 0){
                finalReview = 0;
            }
            else{
                finalReview = (double) totalRating/totalReviews;
            }
            Game newGame = new Game(name,Double.parseDouble(decFor.format(finalReview)),image,pub_date);
            newGames.add(newGame);
        }
    }catch(SQLException e){
        e.printStackTrace();
        e.getCause();
    }
    goToNextScene(newGames,"Most Popular Games");
}

public void findMeUpcomingGames(ActionEvent event){
    Date pub_date;
    double finalReview;
    List<Game> newGames = new ArrayList<>();
    LocalDate presentDate;
    presentDate = LocalDate.now();


    DecimalFormat decFor = new DecimalFormat("0.0");
    DatabaseConnector connectNow = new DatabaseConnector();
    Connection connectDB = connectNow.getConnection();
    try{
        String choosePicture = "SELECT image,total_rating,total_reviews,game_name,publication_date FROM game_info_table WHERE publication_date>'"+presentDate+"'";
        Statement statement = connectDB.createStatement();
        ResultSet queryResult = statement.executeQuery(choosePicture);

        while(queryResult.next()){
            Blob blob = queryResult.getBlob(1);
            int totalRating = queryResult.getInt(2);
            int totalReviews = queryResult.getInt(3);
            String name = queryResult.getString(4);
            pub_date = queryResult.getDate(5);
            InputStream inputStream = blob.getBinaryStream();
            Image image = new Image(inputStream);
            if (totalRating == 0 || totalReviews == 0){
                finalReview = 0;
            }
            else{
                finalReview = (double) totalRating/totalReviews;
            }
            Game newGame = new Game(name,Double.parseDouble(decFor.format(finalReview)),image,pub_date);
            newGames.add(newGame);
        }
    }catch(SQLException e){
        e.printStackTrace();
        e.getCause();
    }
    goToNextScene(newGames,"Upcoming Games");
}

public void findMeNewestGames(ActionEvent event){
    Date pub_date;
    double finalReview;
    LocalDate dateThreshold;
    dateThreshold = LocalDate.now().minusYears(2);
    List<Game> newGames = new ArrayList<>();


    DecimalFormat decFor = new DecimalFormat("0.0");
    DatabaseConnector connectNow = new DatabaseConnector();
    Connection connectDB = connectNow.getConnection();
    try{
        String choosePicture = "SELECT image,total_rating,total_reviews,game_name,publication_date FROM game_info_table WHERE publication_date>'"+dateThreshold+"'";
        Statement statement = connectDB.createStatement();
        ResultSet queryResult = statement.executeQuery(choosePicture);

        while(queryResult.next()){
            Blob blob = queryResult.getBlob(1);
            int totalRating = queryResult.getInt(2);
            int totalReviews = queryResult.getInt(3);
            String name = queryResult.getString(4);
            pub_date = queryResult.getDate(5);
            InputStream inputStream = blob.getBinaryStream();
            Image image = new Image(inputStream);
            if (totalRating == 0 || totalReviews == 0){
                finalReview = 0;
            }
            else{
                finalReview = (double) totalRating/totalReviews;
            }
            Game newGame = new Game(name,Double.parseDouble(decFor.format(finalReview)),image,pub_date);
            newGames.add(newGame);
        }
    }catch(SQLException e){
        e.printStackTrace();
        e.getCause();
    }
    goToNextScene(newGames,"Newest Games");
}

public void searchGame(ActionEvent event){
    String searchBarText;
    Date pub_date;
    double finalReview;
    searchBarText = gameSearch.getText();
    List<Game> newGames = new ArrayList<>();
    DecimalFormat decFor = new DecimalFormat("0.0");
    DatabaseConnector connectNow = new DatabaseConnector();
    Connection connectDB = connectNow.getConnection();
    try {
        String choosePicture = "SELECT image,total_rating,total_reviews,game_name,publication_date FROM game_info_table WHERE game_name LIKE '%" + searchBarText + "%'";
        Statement statement = connectDB.createStatement();
        ResultSet queryResult = statement.executeQuery(choosePicture);
        while(queryResult.next()){
            Blob blob = queryResult.getBlob(1);
            int totalRating = queryResult.getInt(2);
            int totalReviews = queryResult.getInt(3);
            String name = queryResult.getString(4);
            pub_date = queryResult.getDate(5);
            InputStream inputStream = blob.getBinaryStream();
            Image image = new Image(inputStream);
            if (totalRating == 0 || totalReviews == 0){
                finalReview = 0;
            }
            else{
                finalReview = (double) totalRating/totalReviews;
            }
            Game newGame = new Game(name,Double.parseDouble(decFor.format(finalReview)),image,pub_date);
            newGames.add(newGame);
        }
    }
    catch (SQLException e){
        e.printStackTrace();
        e.getCause();
    }
    goToNextScene(newGames,"You searched");
}

private ResultSet searchGamesViaPlatform(String dbCollumn,String platform) throws SQLException {
    ResultSet queryResult = null;
    DatabaseConnector connectNow = new DatabaseConnector();
    Connection connectDB = connectNow.getConnection();
    try{
        //String chooseGames = "SELECT game_name FROM game_platform WHERE "+dbCollumn+" = '"+platform+"'";
        String chooseGames = "SELECT game_name FROM game_platform WHERE "+dbCollumn+" IS NOT NULL";
        Statement statement = connectDB.createStatement();
        queryResult = statement.executeQuery(chooseGames);
    } catch (SQLException throwables) {
        throwables.printStackTrace();
    }

    return queryResult;
}

private List<String> extractGamesFromQuery(ResultSet games) {
    List<String> game_names = new ArrayList<String>();
    try {
        while (games.next()) {
            game_names.add(games.getString(1));
        }
    } catch (Exception e) {
        e.getCause();
    }
    return game_names;
}

public void goToNextScene(List<Game> games,String platform){
    try {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("gamesByPlatformPanel.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        gamesByPlatformController controller = fxmlLoader.getController();
        controller.spawnGames(games,platform,0,connectedUser);
        Stage stage = (Stage)pcGames.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Gamers Area");
    }catch (Exception e){
        e.printStackTrace();
        e.getCause();
    }
}

public void goToMyProfile(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("userProfile.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            userProfileController controller = fxmlLoader.getController();
            controller.getUserInformation(connectedUser);
            Stage userStage = (Stage)profileButton.getScene().getWindow();
            userStage.setScene(new Scene(root,854,630));
            userStage.setTitle("Gamers Area");
        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

}