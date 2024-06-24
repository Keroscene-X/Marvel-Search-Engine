package marvel.model;

public record HistoryItem(String url, String  name) {
    @Override
    public String toString(){
        return name;
    }
}
