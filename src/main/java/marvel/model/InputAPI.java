package marvel.model;

import com.google.gson.JsonObject;
import javafx.util.Pair;

public interface InputAPI {

    Pair<Integer, JsonObject> getRequest(String url);
}
