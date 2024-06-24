package marvel.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javafx.util.Pair;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class MarvelAPIHandler implements InputAPI{

    public Pair<Integer, JsonObject> getRequest(String url) {
        if(url == null || url == ""){
            return null;
        }
        try {
            HttpRequest request = HttpRequest.newBuilder(new
                            URI(url))
                    .GET()
                    .build();
            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(response.body(), JsonObject.class);
            Pair<Integer, JsonObject> result = new Pair<>( response.statusCode(),jsonObject);
            return result;
        } catch (IOException | InterruptedException e) {
            return null;
        } catch (URISyntaxException ignored) {
            return null;
        }
    }
}
