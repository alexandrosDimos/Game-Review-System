package sample;


import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static javafx.scene.paint.Color.WHITE;

public class gamePageController {
    @FXML
    private ImageView setMainImage,setAge;
    @FXML
    private Text setPlatforms,setGenres,setSingleplayer,setMultiplayer,setDate,setDev,setPublisher,setRating,gameTitle,numberOfReviews,messageLabel,yourReviewLabel;
    @FXML
    private Button backToGames,backToLogin,profileButton,publishRating;
    @FXML
    private TextField gameSearch, getRating;
    @FXML
    private TextArea getComment;
    @FXML
    private GridPane imageGrid,commentGrid,simGames;
    public List<Game> gameList;
    String platform,game_title,genre1,genre2,genre3;
    private User connectedUser;
    int reviews;
    int totalScore;
    int currentUserScore;
    int alreadyReviewed = 0;
    private MyListener myListener;

    public void showGame(String game_name,List<Game> previousSceneGameList,String previousPlatform,User connUser){
        double finalReview;
        int i;
        connectedUser = connUser;
        profileButton.setText(connectedUser.getUser());
        game_title = game_name;
        List<String> platforms= new ArrayList<>();
        DecimalFormat decFor = new DecimalFormat("0.0");
        platform = previousPlatform;
        gameList = previousSceneGameList;
        DatabaseConnector connectNow = new DatabaseConnector();
        Connection connectDB = connectNow.getConnection();
        try {
            String fetchGameInfo = "SELECT game_publisher,development_studio,publication_date,age_appropriation,total_rating,total_reviews,image,singleplayer,multiplayer,genre1,genre2,genre3 FROM game_info_table WHERE game_name = '"+game_name+ "'";
            String searchForUserRating = "SELECT rate FROM player_rates WHERE username = '"+connectedUser.getUser()+"' AND game_name = '"+game_name+"'";
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(fetchGameInfo);
            if (queryResult.first()) {
                String publisher = queryResult.getString(1);
                String devs = queryResult.getString(2);
                Date date = queryResult.getDate(3);
                int age = queryResult.getInt(4);
                totalScore = queryResult.getInt(5);
                reviews = queryResult.getInt(6);
                Blob blob = queryResult.getBlob(7);
                int singlePlayer = queryResult.getInt(8);
                int multiPlayer = queryResult.getInt(9);
                genre1 = queryResult.getString(10);
                genre2 = queryResult.getString(11);
                genre3 = queryResult.getString(12);
                

                gameTitle.setText(game_name);
                InputStream inputStream = blob.getBinaryStream();
                Image image = new Image(inputStream);
                setMainImage.setImage(image);
                if (totalScore == 0 || reviews == 0){
                    finalReview = 0;
                }
                else{
                    finalReview = (double) totalScore/reviews;
                }
                setPublisher.setText(publisher);
                setDev.setText(devs);
                setDate.setText(""+date.toLocalDate().getMonth()+" "+date.toLocalDate().getDayOfMonth()+","+date.toLocalDate().getYear()+"");
                setRating.setText(decFor.format(finalReview));
                numberOfReviews.setText(""+String.valueOf(reviews)+" users have rated this game");

                if(singlePlayer == 1){
                    setSingleplayer.setText("Singleplayer");
                }
                if(multiPlayer == 1){
                    setMultiplayer.setText("Multiplayer");
                }
                if(age == 3){
                    ImageView pegiImage = new ImageView(new Image(getClass().getResourceAsStream("images/age_restrictions/pegi3.png")));
                    setAge.setImage(pegiImage.getImage());
                }
                if(age == 7){
                    ImageView pegiImage = new ImageView(new Image(getClass().getResourceAsStream("images/age_restrictions/pegi7.png")));
                    setAge.setImage(pegiImage.getImage());
                }
                if(age == 12){
                    ImageView pegiImage = new ImageView(new Image(getClass().getResourceAsStream("images/age_restrictions/pegi12.png")));
                    setAge.setImage(pegiImage.getImage());
                }
                if(age == 16){
                    ImageView pegiImage = new ImageView(new Image(getClass().getResourceAsStream("images/age_restrictions/pegi16.png")));
                    setAge.setImage(pegiImage.getImage());
                }
                if(age == 18){
                    ImageView pegiImage = new ImageView(new Image(getClass().getResourceAsStream("images/age_restrictions/pegi18.png")));
                    setAge.setImage(pegiImage.getImage());
                }
                if(genre1 == null){
                    genre1 = " ";
                }
                if(genre2 == null){
                    genre2 = " ";
                }
                if(genre3 == null){
                    genre3 = " ";
                }

                setGenres.setText(""+genre1+" "+genre2+" "+genre3+"");
                String fetchGamePlatforms= "SELECT * FROM game_platform WHERE game_name = '"+game_name+"'";
                //pc,ps3,ps4,ps5,xboxOne,xboxX,xbox360,nintendoSwitch,psp,psvita,wii,android
                Statement statement2 = connectDB.createStatement();
                ResultSet queryResult2 = statement2.executeQuery(fetchGamePlatforms);
                if(queryResult2.first()) {
                    for (i = 2; i <= 13; i++) {
                        if(queryResult2.getString(i) != null) {
                            platforms.add(queryResult2.getString(i));
                        }
                    }
                }
                StringBuilder platform = new StringBuilder("");
                Iterator<String> gamePlatforms = platforms.iterator();
                while (gamePlatforms.hasNext()) {
                    String temp = gamePlatforms.next();
                    if(temp != null){
                        platform.append(""+temp+",");
                    }
                }
                setPlatforms.setText(String.valueOf(platform));

                Statement findUserReview = connectDB.createStatement();
                ResultSet userReview = findUserReview.executeQuery(searchForUserRating);
                if(userReview.first()) {
                    currentUserScore = userReview.getInt(1);
                    yourReviewLabel.setText("You have rated this game with: "+currentUserScore+"");
                    publishRating.setText("Publish a new Rating");
                    alreadyReviewed = 1;
                }
                spawnGameImages(game_name);
                showComments();
                showSimilarGames();
            }
            connectDB.close();
        } catch (SQLException e) {
            e.printStackTrace();
            e.getCause();
        }

    }

    public void backButton(ActionEvent event){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("gamesByPlatformPanel.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            gamesByPlatformController controller = fxmlLoader.getController();
            controller.spawnGames(gameList,platform,0,connectedUser);
            Stage stage = (Stage)setPlatforms.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Gamers Area");


        }catch (NullPointerException | IOException e){
            e.printStackTrace();
            e.getCause();
        }
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

    public void spawnGameImages(String game_name){
        int column = 0 , row = 0;
        DatabaseConnector connectNow = new DatabaseConnector();
        Connection connectDB = connectNow.getConnection();
        imageGrid.getChildren().clear();
        try {
            String fetchGameImage = "SELECT image FROM game_images WHERE game_name = '"+game_name+ "'";
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(fetchGameImage);
            while(queryResult.next()){
                Blob blob = queryResult.getBlob(1);
                InputStream inputStream = blob.getBinaryStream();
                Image image = new Image(inputStream);
                AnchorPane imagePane = new AnchorPane();
                ImageView gameImage = new ImageView();
                gameImage.setImage(image);
                gameImage.setFitHeight(89);
                gameImage.setFitWidth(143);
                gameImage.setCursor(Cursor.HAND);

                gameImage.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        popUpImage newWindow = new popUpImage();
                        newWindow.display(image);
                    }

                });
                imagePane.getChildren().add(gameImage);
                imagePane.setPadding(new Insets(5, 0, 0, 0));

                row++;
                imageGrid.add(imagePane, column, row);
                GridPane.setMargin(imagePane, new Insets(0,0,0,20));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void searchGame(ActionEvent event){
        String searchBarText;
        java.util.Date pub_date;
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

    public void goToNextScene(List<Game> games,String platform){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("gamesByPlatformPanel.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            gamesByPlatformController controller = fxmlLoader.getController();

            controller.spawnGames(games,platform,0,connectedUser);
            Stage stage = (Stage)gameTitle.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Gamers Area");
        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    public void publishYourRating() {
        String checkScore = getRating.getText();

        if(checkScore.indexOf(".") >= 0){
            messageLabel.setText("Do not rate with a decimal");
            labelFade(3);
            return;
        }else if(getRating.getText().isEmpty()){
            messageLabel.setText("Please insert a rating");
            labelFade(3);
            return;
        }

        int newScore = Integer.parseInt(getRating.getText());

        String updateRatings;
        String addNewUserRating;

        DatabaseConnector connectNow = new DatabaseConnector();
        Connection connectDB = connectNow.getConnection();

        if (alreadyReviewed == 1) {
            totalScore = totalScore - currentUserScore;
            totalScore = totalScore + newScore;
            addNewUserRating = "UPDATE player_rates SET rate = '" + newScore + "' WHERE game_name = '" + game_title + "' AND username ='" + connectedUser.getUser() + "'";
            updateRatings = "UPDATE game_info_table SET total_rating = '" + totalScore + "',total_reviews = '" + reviews + "' WHERE game_name = '" + game_title + "'";
        } else{
            totalScore = totalScore + newScore;
            reviews = reviews + 1;
            updateRatings = "UPDATE game_info_table SET total_rating = '" + totalScore + "',total_reviews = '" + reviews + "' WHERE game_name = '" + game_title + "'";
            addNewUserRating = "INSERT INTO player_rates(game_name,username,rate) VALUES('" + game_title + "','" + connectedUser.getUser() + "','" + newScore + "')";
        }
        try {
            Statement insertRatings = connectDB.createStatement();
            insertRatings.executeUpdate(updateRatings);
            Statement insertRelation = connectDB.createStatement();
            insertRelation.executeUpdate(addNewUserRating);
            messageLabel.setText("Thanks for your rating");
            labelFade(3);
            connectDB.close();
        }catch(SQLException e){
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

    public void publishComment(){
        String comment;
        int flag = 0;

        if(getComment.getText().isEmpty()){
            System.out.println("No comment inserted");
        }
        else{
            comment = getComment.getText();
            DatabaseConnector connectNow = new DatabaseConnector();
            Connection connectDB = connectNow.getConnection();
            String CheckForUser = "SELECT count(*) FROM game_comments WHERE game_name = '"+game_title+"' AND username ='"+connectedUser.getUser()+"'";
            try {
                Statement statement = connectDB.createStatement();
                ResultSet userComments = statement.executeQuery(CheckForUser);
                if (userComments.next()) {
                    if (userComments.getInt(1) == 1) {
                        getComment.setText("You are limited to one comment");
                        flag = 1;
                        connectDB.close();
                    }
                }

            }catch(SQLException e){
                e.getCause();
                e.printStackTrace();
            }
            if(flag == 0) {
                try {
                    String storeComment = "INSERT INTO game_comments(game_name,username,comment_text) VALUES('" + game_title + "','" + connectedUser.getUser() + "','" + comment + "')";
                    Statement statement = connectDB.createStatement();
                    statement.executeUpdate(storeComment);
                    getComment.setText("Thanks for your comment");
                    connectDB.close();
                } catch (SQLException e) {
                    e.getCause();
                    e.printStackTrace();
                }
            }
        }
        commentGrid.getChildren().clear();
        showComments();
    }

    public void showComments(){
        int column = 0,row = 0;
        DatabaseConnector connectNow = new DatabaseConnector();
        Connection connectDB = connectNow.getConnection();

        String fetchComments = "SELECT comment_text,username FROM game_comments WHERE game_name = '"+game_title+"'";
        try{
            Statement fetchCommentsStatement = connectDB.createStatement();
            ResultSet comments = fetchCommentsStatement.executeQuery(fetchComments);
            while(comments.next()){
                String comment = comments.getString(1);
                String username = comments.getString(2);
                HBox commentBox = new HBox();
                Hyperlink usernameLink = new Hyperlink();
                Text commentText = new Text(comment);
                usernameLink.setText(""+username+" stated :");
                commentText.setFill(WHITE);
                commentBox.getChildren().addAll(usernameLink,commentText);

                row++;
                commentGrid.add(commentBox, column, row);

                GridPane.setMargin(commentGrid, new Insets(90,0,0,20));

            }
            connectDB.close();
        }
        catch(SQLException e){
            e.getCause();
            e.printStackTrace();
        }
    }

    public void showSimilarGames(){
        int collumn = 0,row = 0;
        Date pub_date;
        double finalReview;
        DecimalFormat decFor = new DecimalFormat("0.0");
        List<Game> similars = new ArrayList<>();

        DatabaseConnector connectNow = new DatabaseConnector();
        Connection connectDB = connectNow.getConnection();
        String similarGames = "SELECT game_name,total_rating,total_reviews,image,publication_date  FROM game_info_table WHERE (genre1 = '"+genre1+"' OR genre2 = '"+genre1+"' OR genre3 = '"+genre1+"') AND (genre1 = '"+genre2+"' OR genre2 = '"+genre2+"' OR genre3 = '"+genre2+"') OR(genre1 = '"+genre3+"' OR genre2 = '"+genre3+"' OR genre3 = '"+genre3+"')";
        try{
            Statement fetchCommentsStatement = connectDB.createStatement();
            ResultSet similargames = fetchCommentsStatement.executeQuery(similarGames);
            while(similargames.next()) {
                Blob blob = similargames.getBlob(4);
                int totalRating = similargames.getInt(2);
                int totalReviews = similargames.getInt(3);
                String name = similargames.getString(1);
                pub_date = similargames.getDate(5);
                InputStream inputStream = blob.getBinaryStream();
                Image image = new Image(inputStream);
                if (totalRating == 0 || totalReviews == 0) {
                    finalReview = 0;
                } else {
                    finalReview = (double) totalRating / totalReviews;


                }
                Game newGame = new Game(name, Double.parseDouble(decFor.format(finalReview)), image, pub_date);
                if(!name.equals(game_title)) {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("gameItem.fxml"));
                    AnchorPane anchorPane = fxmlLoader.load();

                    GameItemController gameItemController = fxmlLoader.getController();
                    myListener = new MyListener() {
                        @Override
                        public void onClickListener(Game game) {
                            try {
                                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("gamePage.fxml"));
                                Parent root = (Parent) fxmlLoader.load();
                                gamePageController controller = fxmlLoader.getController();
                                controller.showGame(game.getName(),gameList,platform,connectedUser);
                                Stage stage = (Stage)gameTitle.getScene().getWindow();
                                stage.setScene(new Scene(root));
                                stage.setTitle("Gamers Area");
                            }catch (Exception e){
                                e.printStackTrace();
                                e.getCause();
                            }
                        }
                    };
                    gameItemController.setData(newGame, myListener);
                    row++;
                    anchorPane.setPadding(new Insets(0,0,0,0));
                    simGames.add(anchorPane, collumn, row);
                    GridPane.setMargin(anchorPane, new Insets(0,30,0,0));

                }
            }

        }
        catch(SQLException | IOException e){
            e.getCause();
            e.printStackTrace();
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
