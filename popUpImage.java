package sample;

import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class popUpImage {


    public static void display(Image image) {
        Stage popupwindow = new Stage();

        popupwindow.initModality(Modality.APPLICATION_MODAL);
        popupwindow.setTitle("Game Images");

        Button button1 = new Button("Back");


        button1.setOnAction(e -> popupwindow.close());


        VBox layout = new VBox(10);

        ImageView gameImage = new ImageView();
        gameImage.setImage(image);
        gameImage.setFitWidth(500);
        gameImage.setFitHeight(400);
        layout.getChildren().addAll(gameImage, button1);

        layout.setAlignment(Pos.CENTER);

        Scene scene1 = new Scene(layout, 500, 430);

        popupwindow.setScene(scene1);

        popupwindow.showAndWait();

    }

}
