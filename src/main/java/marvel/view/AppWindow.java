package marvel.view;


import com.google.zxing.WriterException;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import marvel.model.Character;
import marvel.model.Comic;
import marvel.model.GameEngine;
import org.controlsfx.control.BreadCrumbBar;
import javafx.embed.swing.SwingFXUtils;

import javax.imageio.ImageIO;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class AppWindow {
    private final Scene scene;
    private final SearchPane searchPane;
    private final CharacterPane characterPane;
    private final ComicPane comicPane;
    private final GameEngine model;
    private MenuBar menuBar;
    private BorderPane mainPane;
    private VBox loginPane;
    private BreadCrumbBar<HistoryPane> breadCrumbBar;
    private TreeItem<HistoryPane> latestTreeItem;
    private MediaPlayer mediaPlayer;
    private final ProgressIndicator progressIndicator = new ProgressIndicator(-1.0f);

    private Task<Integer> task;

    private final ExecutorService pool = Executors.newFixedThreadPool(2, runnable -> {
        Thread thread = new Thread(runnable);
        thread.setDaemon(true);
        return thread ;
    });

    public AppWindow(GameEngine model){
        this.model = model;
        this.searchPane = new SearchPane(model, pool, task, progressIndicator);
        this.characterPane = new CharacterPane(model,this);
        this.comicPane = new ComicPane(model,this);
        this.mainPane = new BorderPane();
        buildLoginPane();
        mainPane.setMaxWidth(720);
        mainPane.setCenter(loginPane);
        this.scene = new Scene(mainPane);

        Media media = new Media(getClass().getResource("/Melancholy.wav").toExternalForm());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
    }
    private void updateBreadCrumbBar(HistoryPane historyPane){
        TreeItem<HistoryPane> historyPaneTreeItem = new TreeItem<>(historyPane);
        latestTreeItem.getChildren().clear();
        latestTreeItem.getChildren().add(historyPaneTreeItem);
        breadCrumbBar.setSelectedCrumb(historyPaneTreeItem);
        latestTreeItem = historyPaneTreeItem;
    }

    public void buildClickAction(){
        ButtonType cache = new ButtonType("cache data", ButtonBar.ButtonData.OK_DONE);
        ButtonType fresh = new ButtonType("fresh data", ButtonBar.ButtonData.CANCEL_CLOSE);
        ListView searchList = searchPane.getListView();
        searchList.setOnMouseClicked(event -> {
            progressIndicator.setVisible(true);
            task = new Task<Integer>() {
                @Override
                protected Integer call() throws Exception {
                    try{
                        String characterName = searchList.getSelectionModel().getSelectedItem().toString();
                        if(model.checkCharacterCacheHit(characterName)){
                            Platform.runLater(() -> {
                                Optional<ButtonType> result = cacheHitAlert(cache, fresh);
                                if (result.isPresent() && result.get() == cache){
                                    Character character = model.getCharacterByCache(characterName);
                                    setupCharacterPane(character);
                                    popularAlert(characterName);
                                }else if(result.isPresent() && result.get() == fresh){
                                    Character character = model.getCharacterByName(characterName);
                                    setupCharacterPane(character);
                                    popularAlert(characterName);
                                }
                            });
                        }else{
                            Character character = model.getCharacterByName(characterName);
                            setupCharacterPane(character);
                            popularAlert(characterName);
                        }

                    } catch (IllegalArgumentException e){
                        Platform.runLater(() ->{
                            Alert error = new Alert(Alert.AlertType.ERROR,e.getMessage());
                            error.show();
                        });
                    } catch(NullPointerException e){

                    }
                    Platform.runLater(() -> {
                        progressIndicator.setVisible(false);
                    });
                    return null;
                }
            };
            pool.execute(task);

        });
        ListView comicList = characterPane.getComicList();
        comicList.setOnMouseClicked(event -> {
            progressIndicator.setVisible(true);
            task = new Task<Integer>() {
                @Override
                protected Integer call() throws Exception {
                    try{
                        Comic selectedComic = (Comic) comicList.getSelectionModel().getSelectedItem();
                        String comicURI = selectedComic.getResourceURI();
                        if(model.checkComicCacheHit(comicURI)){
                            Platform.runLater(() -> {
                                Optional<ButtonType> result = cacheHitAlert(cache,fresh);
                                if (result.isPresent() && result.get() == cache){
                                    Comic comic = model.getComicByCache(comicURI);
                                    setupComicPane(comic);
                                }else if(result.isPresent() && result.get() == fresh){
                                    Comic comic = model.getComicByID(comicURI);
                                    setupComicPane(comic);
                                }
                            });
                        }else{
                            Comic comic = model.getComicByID(comicURI);
                            setupComicPane(comic);
                        }
                    }catch (IllegalArgumentException e){
                        Platform.runLater(() -> {
                            Alert error = new Alert(Alert.AlertType.ERROR,e.getMessage());
                            error.show();
                        });
                    } catch(NullPointerException e){

                    }
                    Platform.runLater(() -> {
                        progressIndicator.setVisible(false);
                    });
                    return null;
                }
            };
            pool.execute(task);
        });

        ListView characterList = comicPane.getCharacterList();
        characterList.setOnMouseClicked(event -> {
            progressIndicator.setVisible(true);
            task = new Task<Integer>() {
                @Override
                protected Integer call() throws Exception {
                    try{
                        String characterName = characterList.getSelectionModel().getSelectedItem().toString();
                        if(model.checkCharacterCacheHit(characterName)){
                            Platform.runLater(() -> {
                                Optional<ButtonType> result = cacheHitAlert(cache,fresh);
                                if (result.isPresent() && result.get() == cache){
                                    Character character = model.getCharacterByCache(characterName);
                                    setupCharacterPane(character);
                                    popularAlert(characterName);
                                }else if(result.isPresent() && result.get() == fresh){
                                    Character character = model.getCharacterByName(characterName);
                                    setupCharacterPane(character);
                                    popularAlert(characterName);
                                }
                            });
                        }else{
                            Character character = model.getCharacterByName(characterName);
                            setupCharacterPane(character);
                            popularAlert(characterName);
                        }

                    }catch (IllegalArgumentException e){
                        Platform.runLater(() -> {
                            Alert error = new Alert(Alert.AlertType.ERROR,e.getMessage());
                            error.show();
                        });
                    } catch(NullPointerException e){

                    }
                    Platform.runLater(() -> {
                        progressIndicator.setVisible(false);
                    });
                    return null;
                }
            };
            pool.execute(task);
        });
    }

    public void setupCharacterPane(Character character){
        Platform.runLater(() ->{
            Pane cPane = characterPane.createPane(character);
            mainPane.setCenter(cPane);
            this.updateBreadCrumbBar(new HistoryPane(model.getLatestHistoryItem().name(),cPane));
        });
    }

    public void setupComicPane(Comic comic){
        Platform.runLater(() ->{
            Pane cPane = comicPane.createPane(comic);
            mainPane.setCenter(cPane);
            this.updateBreadCrumbBar(new HistoryPane(model.getLatestHistoryItem().name(), cPane));
        });
    }

    public HBox buildNavigation(){

        Button backwardBtn = new Button("<");
        backwardBtn.setOnAction(event -> {
            if(latestTreeItem.getParent() != null){
                this.latestTreeItem = latestTreeItem.getParent();
                mainPane.setCenter(latestTreeItem.getValue().pane());
                breadCrumbBar.setSelectedCrumb(latestTreeItem);
            }
        });
        Button forwardBtn = new Button(">");
        forwardBtn.setOnAction(event -> {
            if (latestTreeItem.getChildren().size() > 0){
                this.latestTreeItem = latestTreeItem.getChildren().get(0);
                mainPane.setCenter(latestTreeItem.getValue().pane());
                breadCrumbBar.setSelectedCrumb(latestTreeItem);
            }
        });
        HBox navigationBar = new HBox(backwardBtn, forwardBtn);
        navigationBar.setSpacing(10);
        return navigationBar;
    }

    public void buildMenuBar(){
        Menu actionMenu = new Menu("Menu");

        MenuItem searchItm = new MenuItem("Search");
        searchItm.setOnAction(event -> {
            mainPane.setCenter(searchPane.getPane());
            updateBreadCrumbBar(new HistoryPane("Search",searchPane.getPane()));
        });

        MenuItem clearCacheItm = new MenuItem("Clear Cache");
        clearCacheItm.setOnAction(event -> {
            model.clearDataBaseCache();
            Alert cacheCleared = new Alert(Alert.AlertType.INFORMATION,"Cache Cleared!");
            cacheCleared.show();
        });

        MenuItem playMusicItm = new MenuItem("Play Music");
        playMusicItm.setOnAction(event -> {mediaPlayer.play();});

        MenuItem pauseMusicItm = new MenuItem("Pause Music");
        pauseMusicItm.setOnAction(event -> {mediaPlayer.pause();});

        actionMenu.getItems().addAll(searchItm, clearCacheItm, playMusicItm, pauseMusicItm);
        this.menuBar = new MenuBar();
        menuBar.getMenus().add(actionMenu);
    }

    private void buildSideBar(){
        Button imgur = new Button("Generate Report");
        imgur.setPrefWidth(150);
        imgur.setOnAction(event -> {
            progressIndicator.setVisible(true);
            task = new Task<Integer>() {
                @Override
                protected Integer call() throws Exception {
                    try {
                        String imageURI = model.uploadImage(generateHistory());
                        Platform.runLater(() -> {
                            TextInputDialog dialog = new TextInputDialog(imageURI);
                            dialog.setTitle("Generate&Upload Successful!");
                            dialog.setHeaderText("The QR code has been successfully generated and upload to imgur\nHere is your URL to the QR code:");
                            dialog.show();
                        });
                    } catch (IOException e) {
                        Platform.runLater(() -> {
                            Alert error = new Alert(Alert.AlertType.ERROR,"unable to generate the QR code");
                            error.show();
                        });
                    } catch (WriterException e){
                        Platform.runLater(() -> {
                            Alert error = new Alert(Alert.AlertType.ERROR,"unable to generate the QR code");
                            error.show();
                        });
                    } catch (IllegalArgumentException e){
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

        Button snapshot = new Button("Screenshot");
        snapshot.setPrefWidth(150);
        snapshot.setOnAction(event -> {
            /* The following code was modified based on https://docs.oracle.com/javase/8/javafx/user-interface-tutorial/file-chooser.htm#CCHICECF */
            WritableImage image = mainPane.snapshot(null,null);
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Screenshot");
            fileChooser.setInitialFileName("screenshot.png");
            Stage stage = new Stage();
            File file = fileChooser.showSaveDialog(stage);
            if(file != null){
                try{
                    ImageIO.write(SwingFXUtils.fromFXImage(image,null),"png",file);
                }catch (IOException e){
                    Alert error = new Alert(Alert.AlertType.ERROR,e.getMessage());
                    error.show();
                }
            }
            /* end of the modified code*/
        });

        progressIndicator.setVisible(false);
        progressIndicator.setTranslateX(90);
        progressIndicator.setTranslateY(820);

        VBox sideBar = new VBox(imgur, snapshot, progressIndicator);
        sideBar.setPadding(new Insets(10,10,10,10));
        sideBar.setSpacing(10);
        mainPane.setRight(sideBar);
    }

    private String generateHistory(){
        String searchHistory = "";
        while (true){
            if(latestTreeItem.getParent() == null){
                searchHistory += latestTreeItem.getValue().name();
                break;
            }
            searchHistory  = searchHistory + latestTreeItem.getValue().toString() + " ";
            latestTreeItem = latestTreeItem.getParent();
        }
        while (latestTreeItem.getChildren().size() != 0){
            latestTreeItem = latestTreeItem.getChildren().get(0);
        }
        return  searchHistory;
    }

    //page to set detail settings when the application starts
    public void buildLoginPane(){
        Label popThresholdLabel = new Label("Popularity threshold:");
        Label errorLabel = new Label();
        errorLabel.setTextFill(Color.RED);
        TextField popThresholdField = new TextField("3");
        Button enter = new Button("Enter");
        enter.setOnAction(event -> {
            try{
                model.setPopThreshold(popThresholdField.getText());
                buildMainPane();
            }catch (IllegalArgumentException e){
                errorLabel.setText(e.getMessage());
            }
        });
        loginPane = new VBox(popThresholdLabel, popThresholdField, enter, errorLabel);
        loginPane.setAlignment(Pos.CENTER);
        loginPane.setSpacing(10);
        loginPane.setPadding(new Insets(510));
    }

    public void buildMainPane(){
        mainPane.setCenter(searchPane.getPane());
        buildClickAction();
        buildMenuBar();
        buildSideBar();
        HBox navigationBar = buildNavigation();
        this.latestTreeItem = new TreeItem<>(new HistoryPane("Search",searchPane.getPane()));

        breadCrumbBar = new BreadCrumbBar<>();
        breadCrumbBar.setSelectedCrumb(latestTreeItem);
        breadCrumbBar.setAutoNavigationEnabled(false);
        VBox topBar = new VBox(menuBar, breadCrumbBar, navigationBar);
        mainPane.setTop(topBar);
    }

    public Optional<ButtonType> cacheHitAlert(ButtonType cache, ButtonType fresh){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Use cache or request fresh data from the API?",cache, fresh);
        alert.setTitle("Cache Hit");
        return alert.showAndWait();
    }

    public void popularAlert(String name){
        Platform.runLater(() -> {
            if(model.characterPopularityCheck(name)){
                Alert popular = new Alert(Alert.AlertType.NONE,"This character is popular!",ButtonType.OK);
                popular.show();
            }
        });
    }


    public Scene getScene() {
        scene.getStylesheets().add("style.css");
        return scene;
    }
}
