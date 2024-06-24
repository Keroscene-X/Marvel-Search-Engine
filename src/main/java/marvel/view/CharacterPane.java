package marvel.view;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import marvel.model.Character;
import marvel.model.Comic;
import marvel.model.GameEngine;


public class CharacterPane {

    private GameEngine model;
    private AppWindow view;
    private ListView<Comic> comicList = new ListView<>();

    public CharacterPane(GameEngine model, AppWindow view){
        this.model = model;
        this.view = view;
    }

    public BorderPane createPane(Character character){
        comicList = new ListView<>();
        view.buildClickAction();
        Label characterName = new Label(character.getName());
        String imageSource = character.getThumbnail().getPath() + model.getIMAGEVARIANT() + "." + character.getThumbnail().getExtension();
        ImageView imageView = new ImageView(new Image(imageSource));
        comicList.getItems().clear();
        comicList.getItems().addAll(character.getComics());
        BorderPane pane = new BorderPane();
        VBox thumbnails = new VBox(characterName, imageView);
        thumbnails.setSpacing(10);
        thumbnails.setPadding(new Insets(0,10,0,0));
        pane.setLeft(thumbnails);
        pane.setCenter(comicList);
        pane.setPadding(new Insets(10));
        return pane;
    }

    public ListView getComicList(){
        return comicList;
    }
}
