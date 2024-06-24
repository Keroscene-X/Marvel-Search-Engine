package marvel.view;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import marvel.model.GameEngine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class SearchPane {

    private BorderPane pane;
    private List<String> wordList = new ArrayList<>();
    private HBox searchBar;
    private ListView<String> autocomplete;
    private GameEngine model;
    private Task<Integer> task;
    private final ExecutorService pool;
    private final ProgressIndicator progressIndicator;

    public SearchPane(GameEngine model, ExecutorService pool, Task<Integer> task, ProgressIndicator progressIndicator){
        buildListView();
        buildSearchBar();
        this.model = model;
        this.pane = new BorderPane();
        this.pane.setTop(searchBar);
        this.pane.setCenter(autocomplete);
        this.pane.setPadding(new Insets(10));
        this.task = task;
        this.pool = pool;
        this.progressIndicator = progressIndicator;
    }

    private void buildSearchBar(){
        TextField searchField = new TextField();
        Button searchBtn = new Button("Search");
        searchBtn.setOnAction(event -> {
            progressIndicator.setVisible(true);
            task = new Task<Integer>() {
                @Override
                protected Integer call() throws Exception {
                    try{
                        wordList = model.getCharacters(searchField.getText());
                        Platform.runLater(() ->{
                            autocomplete.getItems().clear();
                            autocomplete.getItems().addAll(wordList);
                        });
                    }catch (IllegalArgumentException e){
                        Platform.runLater(() -> {
                            Alert error = new Alert(Alert.AlertType.ERROR,e.getMessage());
                            error.show();
                        });
                    }
                    Platform.runLater(() -> {
                        progressIndicator.setVisible(false);
                    });
                    return null;
                }
            };
            pool.execute(task);
        });
        this.searchBar = new HBox(searchField, searchBtn);
        HBox.setHgrow(searchField,Priority.ALWAYS);
        searchBar.setPadding(new Insets(10,0,10,0));
        searchBar.setSpacing(10);
    }

    private void buildListView(){
        this.autocomplete = new ListView<>();
        autocomplete.getItems().addAll(wordList);
    }

    public Pane getPane(){
        return pane;
    }
    public ListView getListView(){
        return autocomplete;
    }
}
