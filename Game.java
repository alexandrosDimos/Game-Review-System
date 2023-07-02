package sample;

import javafx.scene.image.Image;
import java.util.Date;

public class Game {
    private String name;
    private double rating;
    private Image image;
    private Date date;

    public Game(String name, double rating, Image image,Date pub_date){
        this.name = name;
        this.rating = rating;
        this.image = image;
        this.date = pub_date;
    }

    public String getName(){
        return name;
    }

    public double getRating(){
        return rating;
    }

    public Image getImage(){
        return image;
    }

    public Date getDate(){
        return date;
    }

    @Override
    public String toString() {
        return name;
    }
}
