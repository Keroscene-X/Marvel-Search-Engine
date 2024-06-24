package marvel.view;

import javafx.scene.layout.Pane;

public record HistoryPane(String name, Pane pane) {

    @Override
    public String toString(){
        return name;
    }
}
