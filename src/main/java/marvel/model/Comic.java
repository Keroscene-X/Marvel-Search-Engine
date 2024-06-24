package marvel.model;

import java.util.List;

public class Comic {
    private int id;
    private Thumbnail thumbnail;
    private String resourceURI;
    private String name;
    private List<Character> characters;

    public Comic(String resourceURI, String name){
        this.resourceURI = resourceURI;
        this.name = name;
    }
    public Comic(int id, String name){
        this.id = id;
        this.name = name;
    }

    public String getResourceURI() {
        return resourceURI;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString(){
        return name;
    }

    public List<Character> getCharacters() {
        return characters;
    }

    public void setCharacters(List<Character> characters) {
        this.characters = characters;
    }

    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }
}
