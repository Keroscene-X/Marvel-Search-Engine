package marvel.model;

import java.util.List;

public class Character {
    private String resourceURI;
    private int id;
    private String name;
    private Thumbnail thumbnail;
    private List<Comic> comics;

    public Character(int id, String name){
        this.id = id;
        this.name = name;
    }

    public Character(String resourceURI, String name){
        this.resourceURI = resourceURI;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }

    public List<Comic> getComics() {
        return comics;
    }

    public void setComics(List<Comic> comics) {
        this.comics = comics;
    }

    @Override
    public String toString(){
        return name;
    }
}
