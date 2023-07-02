package sample;

import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;

import javafx.scene.text.Text;


public class GameItemController {
    @FXML
    private ImageView setImage;
    @FXML
    private Text setName,setRating;
    @FXML
    private ProgressBar ratingBar;

    private Game game;
    private MyListener mylistener;

    public void setData(Game game,MyListener mylistener){
        this.game = game;
        this.mylistener = mylistener;
        setImage.setImage(game.getImage());
        setName.setText(game.getName());
        setRating.setText(String.valueOf(game.getRating()));
        ratingBar.setProgress(game.getRating()/10.0);
    }

    public void click(javafx.scene.input.MouseEvent mouseEvent) {
        mylistener.onClickListener(game);
    }
}
