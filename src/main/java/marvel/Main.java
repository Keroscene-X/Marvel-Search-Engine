package marvel;

import javafx.application.Application;
import javafx.stage.Stage;
import marvel.model.*;
import marvel.view.AppWindow;

import java.security.NoSuchAlgorithmException;

public class Main extends Application{
    private GameEngine model;
    private AppWindow view;

    @Override
    public void start(Stage primaryStage) throws Exception {
        model = new GameEngine(getParameters().getRaw().get(0), getParameters().getRaw().get(1),
                new MarvelAPIHandler(), new DummyMarvelAPIHandler(), new ImgurAPIHandler(), new DummyImgurAPIHandler(), new DatabaseManager());
        view = new AppWindow(model);
        primaryStage.setScene(view.getScene());
        primaryStage.setTitle("Marvel");
        primaryStage.setWidth(1920);
        primaryStage.setHeight(1080);
        primaryStage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}
