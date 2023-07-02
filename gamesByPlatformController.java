package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.Date;

public class gamesByPlatformController{
    @FXML
    private GridPane grid;
    @FXML
    private Button backToPlatforms,goBackToLogin,profileButton;
    @FXML
    private Text gamePlatform;
    @FXML
    private VBox genreBox,creatorsBox,filterBox;
    @FXML
    private CheckBox pegi3,pegi7,pegi12,pegi16,pegi18,before2000,before2005,before2010,before2015,before2020;
    @FXML
    private TextField gameSearch;
    List<CheckBox> genresInDB = new ArrayList<>();
    List<CheckBox> creatorsInDB = new ArrayList<>();

    public List<Game> gamesByPlatform;
    public List<Game> filteredGamesByPlatform;

    public String setPlatform;
    private MyListener myListener;
    private User connectedUser;

    public void spawnGames(List<Game> gameList,String platform,int flag,User connUser){
        int collumn = 0, row = 1, i;

        if(flag == 0) {
            this.gamesByPlatform = gameList;
            this.filteredGamesByPlatform = gameList;
        }

        connectedUser = connUser;
        profileButton.setText(connectedUser.getUser());
        gamePlatform.setText(platform);
        setPlatform = platform;
        grid.getChildren().clear();
        myListener = new MyListener() {
            @Override
            public void onClickListener(Game game) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("gamePage.fxml"));
                    Parent root = (Parent) fxmlLoader.load();
                    gamePageController controller = fxmlLoader.getController();

                    controller.showGame(game.getName(),gamesByPlatform,setPlatform,connectedUser);
                    Stage stage = (Stage)gamePlatform.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Gamers Area");

                }catch (Exception e){
                    e.printStackTrace();
                    e.getCause();
                }
            }
        };
        if(gameList.size() == 0 && flag ==0){
            filterBox.getChildren().clear();
        }
        for (i = 0; i < gameList.size(); i++) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("gameItem.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                GameItemController gameItemController = fxmlLoader.getController();
               gameItemController.setData(gameList.get(i),myListener);
                if (collumn == 3) {
                    collumn = 0;
                    row++;
                }
                grid.add(anchorPane, collumn++, row);
                //GridPane.setMargin(anchorPane, new Insets(10));
            } catch (IOException e) {
                e.getCause();
            } catch (NullPointerException e) {
                e.printStackTrace();
                e.getMessage();
            }
        }
        if(flag == 0) {
            spawnGenreFilters(platform);
            spawnCreatorsFilters(platform);
        }
    }

    public void backButton(ActionEvent event){
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("mainPanel.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            MainPanelController controller = fxmlLoader.getController();
            if(controller == null){
                System.out.println("WTF");
            }

            controller.connectedUser(connectedUser);
            Stage stage = (Stage)backToPlatforms.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Gamers Area");
        }
        catch(IOException  | NullPointerException e){
            e.printStackTrace();
        }
    }

    public void alphabetical(ActionEvent event){
        Collections.sort(filteredGamesByPlatform, new Comparator<Game>() {
            public int compare(Game g1, Game g2) {
                return g1.getName().compareTo(g2.getName());
            }
        });
        spawnGames(filteredGamesByPlatform,setPlatform,1,connectedUser);
    }

    public void sortByRating(ActionEvent event){
        filteredGamesByPlatform.sort(Comparator.comparingDouble(Game::getRating).reversed());

        spawnGames(filteredGamesByPlatform,setPlatform,1,connectedUser);
    }

    public void sortByPublicationYear(ActionEvent event){
        filteredGamesByPlatform.sort(Comparator.comparing(Game::getDate).reversed());

        spawnGames(filteredGamesByPlatform,setPlatform,1,connectedUser);
    }

    public void goBackToLogin(ActionEvent event){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("loginPanel.fxml"));
            Stage registerStage = new Stage();
            registerStage.setTitle("Welcome to GamersWorld");
            registerStage.setScene(new Scene(root, 746, 536));
            registerStage.show();
            Stage stage = (Stage) goBackToLogin.getScene().getWindow();
            stage.close();
        }
        catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    public void spawnGenreFilters(String info){
        String dbCollumn = null;
        String fetchGenres;
        DatabaseConnector connectNow = new DatabaseConnector();
        Connection connectDB = connectNow.getConnection();
        genresInDB.clear();

        if(info.equals("You searched")){
            filterBox.getChildren().clear();
            return;
        }

        if(setPlatform.equals("PC")){
            dbCollumn = "pc";
        }
        if(setPlatform.equals("PS3")){
            dbCollumn = "ps3";
        }
        if(setPlatform.equals("PS4")){
            dbCollumn = "ps4";
        }
        if(setPlatform.equals("PS5")){
            dbCollumn = "ps5";
        }
        if(setPlatform.equals("XBOX-ONE")){
            dbCollumn = "xboxOne";
        }
        if(setPlatform.equals("XBOX-360")){
            dbCollumn = "xbox360";
        }
        if(setPlatform.equals("XBOX-X")){
            dbCollumn = "xboxX";
        }
        if(setPlatform.equals("Nintendo-Switch")){
            dbCollumn = "nintendoSwitch";
        }
        if(setPlatform.equals("PSP")){
            dbCollumn = "psp";
        }
        if(setPlatform.equals("Wii")){
            dbCollumn = "wii";
        }
        if(setPlatform.equals("PSVita")){
            dbCollumn = "psvita";
        }
        if(setPlatform.equals("Android")){
            dbCollumn = "android";
        }

        try {
            if(info.equals("Newest Games")){
                fetchGenres = "SELECT  DISTINCT" +
                        "        `game_info_table`.`genre1` AS `genre1`" +
                        "    FROM" +
                        "        game_info_table INNER JOIN game_platform ON game_info_table.game_name = game_platform.game_name" +
                        "    WHERE" +
                        "        (`genre1` IS NOT NULL) AND (publication_date> '2020-01-01')" +
                        "    UNION SELECT DISTINCT" +
                        "        `game_info_table`.`genre2` AS `genre1`" +
                        "    FROM" +
                        "        game_info_table INNER JOIN game_platform ON game_info_table.game_name = game_platform.game_name" +
                        "    WHERE" +
                        "        (`genre2` IS NOT NULL) AND (publication_date> '2020-01-01')" +
                        "    UNION SELECT DISTINCT" +
                        "        `game_info_table`.`genre3` AS `genre1`" +
                        "    FROM" +
                        "        game_info_table INNER JOIN game_platform ON game_info_table.game_name = game_platform.game_name" +
                        "    WHERE" +
                        "        (`genre3` IS NOT NULL) AND (publication_date> '2020-01-01')";
            }
            else if(info.equals("Top Rated Games")){
                fetchGenres = "SELECT  DISTINCT" +
                        "        `game_info_table`.`genre1` AS `genre1`" +
                        "    FROM" +
                        "        game_info_table INNER JOIN game_platform ON game_info_table.game_name = game_platform.game_name" +
                        "    WHERE" +
                        "        (`genre1` IS NOT NULL) AND (total_rating/total_reviews>9)" +
                        "    UNION SELECT DISTINCT" +
                        "        `game_info_table`.`genre2` AS `genre1`" +
                        "    FROM" +
                        "        game_info_table INNER JOIN game_platform ON game_info_table.game_name = game_platform.game_name" +
                        "    WHERE" +
                        "        (`genre2` IS NOT NULL) AND (total_rating/total_reviews>9)" +
                        "    UNION SELECT DISTINCT" +
                        "        `game_info_table`.`genre3` AS `genre1`" +
                        "    FROM" +
                        "        game_info_table INNER JOIN game_platform ON game_info_table.game_name = game_platform.game_name" +
                        "    WHERE" +
                        "        (`genre3` IS NOT NULL) AND (total_rating/total_reviews>9)";
            }
            else if(info.equals("Most Popular Games")){
                fetchGenres = "SELECT  DISTINCT" +
                        "        `game_info_table`.`genre1` AS `genre1`" +
                        "    FROM" +
                        "        game_info_table INNER JOIN game_platform ON game_info_table.game_name = game_platform.game_name" +
                        "    WHERE" +
                        "        (`genre1` IS NOT NULL) AND (total_reviews>10)" +
                        "    UNION SELECT DISTINCT" +
                        "        `game_info_table`.`genre2` AS `genre1`" +
                        "    FROM" +
                        "        game_info_table INNER JOIN game_platform ON game_info_table.game_name = game_platform.game_name" +
                        "    WHERE" +
                        "        (`genre2` IS NOT NULL) AND (total_reviews>10)" +
                        "    UNION SELECT DISTINCT" +
                        "        `game_info_table`.`genre3` AS `genre1`" +
                        "    FROM" +
                        "        game_info_table INNER JOIN game_platform ON game_info_table.game_name = game_platform.game_name" +
                        "    WHERE" +
                        "        (`genre3` IS NOT NULL) AND (total_reviews>10)";
            }
            else if(info.equals("Upcoming Games")){
                LocalDate presentDate;
                presentDate = LocalDate.now();
                fetchGenres = "SELECT  DISTINCT" +
                        "        `game_info_table`.`genre1` AS `genre1`" +
                        "    FROM" +
                        "        game_info_table INNER JOIN game_platform ON game_info_table.game_name = game_platform.game_name" +
                        "    WHERE" +
                        "        (`genre1` IS NOT NULL) AND publication_date>'"+presentDate+"'" +
                        "    UNION SELECT DISTINCT" +
                        "        `game_info_table`.`genre2` AS `genre1`" +
                        "    FROM" +
                        "        game_info_table INNER JOIN game_platform ON game_info_table.game_name = game_platform.game_name" +
                        "    WHERE" +
                        "        (`genre2` IS NOT NULL) AND publication_date>'"+presentDate+"'" +
                        "    UNION SELECT DISTINCT" +
                        "        `game_info_table`.`genre3` AS `genre1`" +
                        "    FROM" +
                        "        game_info_table INNER JOIN game_platform ON game_info_table.game_name = game_platform.game_name" +
                        "    WHERE" +
                        "        (`genre3` IS NOT NULL) AND publication_date>'"+presentDate+"'";
            }
            else {
                fetchGenres = "SELECT  DISTINCT" +
                        "        `game_info_table`.`genre1` AS `genre1`" +
                        "    FROM" +
                        "        game_info_table INNER JOIN game_platform ON game_info_table.game_name = game_platform.game_name" +
                        "    WHERE" +
                        "        (`genre1` IS NOT NULL) AND (`" + dbCollumn + "` IS NOT NULL)" +
                        "    UNION SELECT DISTINCT" +
                        "        `game_info_table`.`genre2` AS `genre1`" +
                        "    FROM" +
                        "        game_info_table INNER JOIN game_platform ON game_info_table.game_name = game_platform.game_name" +
                        "    WHERE" +
                        "        (`genre2` IS NOT NULL) AND (`" + dbCollumn + "` IS NOT NULL)" +
                        "    UNION SELECT DISTINCT" +
                        "        `game_info_table`.`genre3` AS `genre1`" +
                        "    FROM" +
                        "        game_info_table INNER JOIN game_platform ON game_info_table.game_name = game_platform.game_name" +
                        "    WHERE" +
                        "        (`genre3` IS NOT NULL) AND (`" + dbCollumn + "` IS NOT NULL)";
            }
            Statement statement = connectDB.prepareStatement(fetchGenres);
            ResultSet queryResult = statement.executeQuery(fetchGenres);
            while(queryResult.next()){
                String genre = queryResult.getString(1);
                CheckBox checkBox = new CheckBox();
                checkBox.setText(genre);
                checkBox.setId(genre);
                checkBox.setPadding(new Insets(5, 0, 0, 0));
                checkBox.setCursor(Cursor.HAND);
                genresInDB.add(checkBox);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        genreBox.getChildren().clear();
        Iterator<CheckBox> genreIterator = genresInDB.iterator();
        while (genreIterator.hasNext()) {
            CheckBox temp = genreIterator.next();
            genreBox.getChildren().add(temp);
            genreBox.setMargin(temp,new Insets(0, 0, 0, 25));
            //genreBox.setPadding();
        }
        genreBox.setAlignment(Pos.BASELINE_LEFT);

    }

    public void spawnCreatorsFilters(String searchInfo){
        String dbCollumn = null;
        String fetchCreators;
        DatabaseConnector connectNow = new DatabaseConnector();
        Connection connectDB = connectNow.getConnection();
        creatorsInDB.clear();

        if(searchInfo.equals("You searched")){
            filterBox.getChildren().clear();
            return;
        }

        if(setPlatform.equals("PC")){
            dbCollumn = "pc";
        }
        if(setPlatform.equals("PS3")){
            dbCollumn = "ps3";
        }
        if(setPlatform.equals("PS4")){
            dbCollumn = "ps4";
        }
        if(setPlatform.equals("PS5")){
            dbCollumn = "ps5";
        }
        if(setPlatform.equals("XBOX-ONE")){
            dbCollumn = "xboxOne";
        }
        if(setPlatform.equals("XBOX-360")){
            dbCollumn = "xbox360";
        }
        if(setPlatform.equals("XBOX-X")){
            dbCollumn = "xboxX";
        }
        if(setPlatform.equals("Nintendo-Switch")){
            dbCollumn = "nintendoSwitch";
        }
        if(setPlatform.equals("PSP")){
            dbCollumn = "psp";
        }
        if(setPlatform.equals("Wii")){
            dbCollumn = "wii";
        }
        if(setPlatform.equals("PSVita")){
            dbCollumn = "psvita";
        }
        if(setPlatform.equals("Android")){
            dbCollumn = "android";
        }

        try {
            if(searchInfo.equals("Newest Games")){
                fetchCreators = "SELECT DISTINCT"+
                        " `game_info_table`.`game_publisher` AS `game_publisher`"+
                        " FROM game_info_table INNER JOIN game_platform ON game_info_table.game_name = game_platform.game_name"+
                        " WHERE (publication_date> '2020-01-01')"+
                        " UNION SELECT DISTINCT"+
                        " `game_info_table`.`development_studio` AS `development_studio`"+
                        " FROM game_info_table INNER JOIN game_platform ON game_info_table.game_name = game_platform.game_name"+
                        " WHERE (publication_date> '2020-01-01')";
            }
            else if(searchInfo.equals("Top Rated Games")){
                fetchCreators = "SELECT DISTINCT"+
                        " `game_info_table`.`game_publisher` AS `game_publisher`"+
                        " FROM game_info_table INNER JOIN game_platform ON game_info_table.game_name = game_platform.game_name"+
                        " WHERE (total_rating/total_reviews>9)"+
                        " UNION SELECT DISTINCT"+
                        " `game_info_table`.`development_studio` AS `development_studio`"+
                        " FROM game_info_table INNER JOIN game_platform ON game_info_table.game_name = game_platform.game_name"+
                        " WHERE (total_rating/total_reviews>9)";
            }
            else if(searchInfo.equals("Most Popular Games")){
                fetchCreators = "SELECT DISTINCT"+
                        " `game_info_table`.`game_publisher` AS `game_publisher`"+
                        " FROM game_info_table INNER JOIN game_platform ON game_info_table.game_name = game_platform.game_name"+
                        " WHERE (total_reviews>10)"+
                        " UNION SELECT DISTINCT"+
                        " `game_info_table`.`development_studio` AS `development_studio`"+
                        " FROM game_info_table INNER JOIN game_platform ON game_info_table.game_name = game_platform.game_name"+
                        " WHERE (total_reviews>10)";
            }
            else if(searchInfo.equals("Upcoming Games")){
                LocalDate presentDate;
                presentDate = LocalDate.now();
                fetchCreators = "SELECT DISTINCT"+
                        " `game_info_table`.`game_publisher` AS `game_publisher`"+
                        " FROM game_info_table INNER JOIN game_platform ON game_info_table.game_name = game_platform.game_name"+
                        " WHERE publication_date>'"+presentDate+"'"+
                        " UNION SELECT DISTINCT"+
                        " `game_info_table`.`development_studio` AS `development_studio`"+
                        " FROM game_info_table INNER JOIN game_platform ON game_info_table.game_name = game_platform.game_name"+
                        " WHERE publication_date>'"+presentDate+"'";
            }
            else {
                fetchCreators = "SELECT DISTINCT" +
                " `game_info_table`.`game_publisher` AS `game_publisher`" +
                " FROM game_info_table INNER JOIN game_platform ON game_info_table.game_name = game_platform.game_name" +
                " WHERE `" + dbCollumn + "` IS NOT NULL" +
                " UNION SELECT DISTINCT" +
                " `game_info_table`.`development_studio` AS `development_studio`" +
                " FROM game_info_table INNER JOIN game_platform ON game_info_table.game_name = game_platform.game_name" +
                " WHERE `" + dbCollumn + "` IS NOT NULL";
            }
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(fetchCreators);
            while(queryResult.next()){
                String genre = queryResult.getString(1);
                CheckBox checkBox = new CheckBox();
                checkBox.setText(genre);
                checkBox.setId(genre);
                checkBox.setPadding(new Insets(5, 0, 0, 0));
                checkBox.setCursor(Cursor.HAND);
                creatorsInDB.add(checkBox);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        creatorsBox.getChildren().clear();
        Iterator<CheckBox> genreIterator = creatorsInDB.iterator();
        while (genreIterator.hasNext()) {
            CheckBox temp = genreIterator.next();
            creatorsBox.getChildren().add(temp);
            creatorsBox.setMargin(temp,new Insets(0, 0, 0, 25));
            //genreBox.setPadding();
        }
        creatorsBox.setAlignment(Pos.BASELINE_LEFT);
    }

    public void checkFilter(){
        int genreFilter = 0,checkFilter = 0;
        filteredGamesByPlatform = new ArrayList<>();
        DatabaseConnector connectNow = new DatabaseConnector();
        Connection connectDB = connectNow.getConnection();
        StringBuilder filteredGames = new StringBuilder("SELECT game_name FROM game_info_table WHERE ");
        String comparisson = filteredGames.toString();

        Iterator<CheckBox> genreIterator = genresInDB.iterator();
        while(genreIterator.hasNext()){
            CheckBox temp = genreIterator.next();
            if(temp.isSelected()){
                if(genreFilter == 0){
                    filteredGames.append("((genre1 = '"+temp.getText()+"' OR genre2 = '"+temp.getText()+"' OR genre3 = '"+temp.getText()+"') ");
                    genreFilter++;
                    checkFilter++;
                }
                else{
                    filteredGames.append("OR (genre1 = '"+temp.getText()+"' OR genre2 = '"+temp.getText()+"' OR genre3 = '"+temp.getText()+"')");
                    checkFilter++;
                }
            }
        }
        if(genreFilter != 0){
            filteredGames.append(") ");
        }

        int ageFilter = 0 ;
        String queryConditionConn;
        if(genreFilter == 0){
            queryConditionConn = "";
        }
        else{
            queryConditionConn = "AND";
        }
        if(pegi3.isSelected()){
            filteredGames.append(""+queryConditionConn+" (age_appropriation = 3 ");
            ageFilter ++;
            checkFilter++;
        }

        if(pegi7.isSelected()){
            checkFilter++;
            if(ageFilter == 0){
                if(genreFilter == 0){
                    queryConditionConn = "(";
                }
                else{
                    queryConditionConn = "AND (";
                }
                ageFilter++;
            }
            else{
                queryConditionConn = "OR";
                ageFilter++;
            }
            filteredGames.append(""+queryConditionConn+" age_appropriation = 7 ");
        }

        if(pegi12.isSelected()){
            checkFilter++;
            if(ageFilter == 0){
                if(genreFilter == 0){
                    queryConditionConn = "(";
                }
                else{
                    queryConditionConn = "AND (";
                }
                ageFilter++;
            }
            else{
                queryConditionConn = "OR";
                ageFilter++;
            }
            filteredGames.append(""+queryConditionConn+"  age_appropriation = 12 ");
        }

        if(pegi16.isSelected()){
            checkFilter++;
            if(ageFilter == 0){
                if(genreFilter == 0){
                    queryConditionConn = "(";
                }
                else{
                    queryConditionConn = "AND (";
                }
                ageFilter++;
            }
            else{
                queryConditionConn = "OR";
                ageFilter++;
            }
            filteredGames.append(""+queryConditionConn+" age_appropriation = 16 ");
        }

        if(pegi18.isSelected()){
            checkFilter++;
            if(ageFilter == 0){
                if(genreFilter == 0){
                    queryConditionConn = "(";
                }
                else{
                    queryConditionConn = "AND (";
                }
                ageFilter++;
            }
            else{
                queryConditionConn = "OR";
                ageFilter++;
            }
            filteredGames.append(""+queryConditionConn+" age_appropriation = 18 ");
        }

        if(ageFilter >= 1){
            filteredGames.append(") ");
        }

        int dateFilter = 0;
        if(genreFilter == 0 && ageFilter == 0){
            queryConditionConn = "(";
        }
        else{
            queryConditionConn = "AND";
        }

        if(before2000.isSelected()){
            checkFilter++;
            filteredGames.append(""+queryConditionConn+" (publication_date< '2000-01-01' ");
            dateFilter++;
        }
        if(before2005.isSelected()){
            checkFilter++;
            if(dateFilter == 0){
                if(genreFilter == 0 && ageFilter == 0){
                    queryConditionConn = "(";
                }
                else{
                    queryConditionConn = "AND (";
                }
                dateFilter++;
            }
            else{
                queryConditionConn = "OR";
                dateFilter++;
            }
            filteredGames.append(""+queryConditionConn+" (publication_date > '2000-01-01' AND publication_date< '2005-12-31') ");
        }
        if(before2010.isSelected()){
            checkFilter++;
            if(dateFilter == 0){
                if(genreFilter == 0 && ageFilter == 0){
                    queryConditionConn = "(";
                }
                else{
                    queryConditionConn = "AND (";
                }
                dateFilter++;
            }
            else{
                queryConditionConn = "OR";
                dateFilter++;
            }
            filteredGames.append(""+queryConditionConn+" (publication_date > '2006-01-01' AND publication_date< '2010-12-31') ");
        }
        if(before2015.isSelected()){
            checkFilter++;
            if(dateFilter == 0){
                if(genreFilter == 0 && ageFilter == 0){
                    queryConditionConn = "(";
                }
                else{
                    queryConditionConn = "AND (";
                }
                dateFilter++;
            }
            else{
                queryConditionConn = "OR";
                dateFilter++;
            }
            filteredGames.append(""+queryConditionConn+" (publication_date > '2011-01-01' AND publication_date< '2015-12-31') ");
        }
        if(before2020.isSelected()){
            checkFilter++;
            if(dateFilter == 0){
                if(genreFilter == 0 && ageFilter == 0){
                    queryConditionConn = "(";
                }
                else{
                    queryConditionConn = "AND (";
                }
                dateFilter++;
            }
            else{
                queryConditionConn = "OR";
                dateFilter++;
            }
            filteredGames.append(""+queryConditionConn+" (publication_date > '2016-01-01' AND publication_date< '2020-12-31') ");
        }

        if(dateFilter >= 1){
            filteredGames.append(") ");
        }

        int creatorFilter = 0;
        Iterator<CheckBox> creatorIterator = creatorsInDB.iterator();
        while(creatorIterator.hasNext()){
            CheckBox temp = creatorIterator.next();
            if(temp.isSelected()){
                checkFilter++;
                if(creatorFilter == 0){
                    if(genreFilter == 0 && ageFilter == 0 && dateFilter == 0){
                        queryConditionConn = "(";
                    }
                    else{
                        queryConditionConn = "AND (";
                    }
                    creatorFilter++;
                }
                else{
                    queryConditionConn = "OR";
                }
                filteredGames.append(""+queryConditionConn+" (game_publisher = '"+temp.getText()+"' OR development_studio = '"+temp.getText()+"') ");
            }
        }
        if(creatorFilter != 0){
            filteredGames.append(") ");
            creatorFilter = 0;
        }
        if(checkFilter == 0) {
            filteredGamesByPlatform = gamesByPlatform;
            spawnGames(gamesByPlatform,setPlatform,1,connectedUser);
        }
        else {
            try {
                Statement statement = connectDB.createStatement();
                ResultSet queryResult = statement.executeQuery(filteredGames.toString());
                while (queryResult.next()) {
                    Iterator<Game> gameListIter = gamesByPlatform.iterator();
                    while (gameListIter.hasNext()) {
                        Game temp = gameListIter.next();
                        if (temp.getName().equals(queryResult.getString(1))) {
                            filteredGamesByPlatform.add(temp);
                        }
                    }
                    String name = queryResult.getString(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }


                spawnGames(filteredGamesByPlatform, setPlatform, 1, connectedUser);

        }
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

    public void goToNextScene(List<Game> games,String platform){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("gamesByPlatformPanel.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            gamesByPlatformController controller = fxmlLoader.getController();
            controller.spawnGames(games,platform,0,connectedUser);
            Stage stage = (Stage)gamePlatform.getScene().getWindow();
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
