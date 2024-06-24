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

public class ComicPane {
    private GameEngine model;
    private AppWindow view;
    private ListView<Character> characterList = new ListView<>();

    public ComicPane(GameEngine model, AppWindow view){
        this.model = model;
        this.view = view;
    }

    public BorderPane createPane(Comic comic){
        characterList = new ListView<>();
        view.buildClickAction();
        Label comicName = new Label(comic.getName());
        String imageSource = comic.getThumbnail().getPath() + model.getIMAGEVARIANT() + "." + comic.getThumbnail().getExtension();
        ImageView imageView = new ImageView(new Image(imageSource));
        characterList.getItems().clear();
        characterList.getItems().addAll(comic.getCharacters());
        BorderPane pane = new BorderPane();
        VBox thumbnails = new VBox(comicName, imageView);
        thumbnails.setSpacing(10);
        thumbnails.setPadding(new Insets(0,10,0,0));
        pane.setLeft(thumbnails);
        pane.setCenter(characterList);
        pane.setPadding(new Insets(10));
        return pane;
    }

    public ListView getCharacterList(){
        return characterList;
    }
}
