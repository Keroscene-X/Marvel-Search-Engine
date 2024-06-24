package marvel.model;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.util.Pair;

public class DummyMarvelAPIHandler implements InputAPI{

    public Pair<Integer, JsonObject> getRequest(String url) {
        if(url == null || url == "")
        {
            return null;
        }
        if(url.contains("https://gateway.marvel.com:443/v1/public/characters?nameStartsWith=")){
            JsonObject obj = JsonParser.parseString("{\n" +
                    "\"data\": {\n" +
                    "   \"results\": [\n" +
                    "       {\n" +
                    "           \"name\": \"Spider-dok\"" +
                    "       },\n" +
                    "       {\n" +
                    "           \"name\": \"Spider-Girl (Anya Corazon)\"" +
                    "       },\n" +
                    "       {\n" +
                    "           \"name\": \"Spider-Girl (May Parker)\"" +
                    "       }]}}"
            ).getAsJsonObject();
            Pair<Integer, JsonObject> result = new Pair<>(200,obj);
            return result;
        }
        else if(url.contains("https://gateway.marvel.com:443/v1/public/characters?name=")){
            JsonObject obj = JsonParser.parseString("{\n" +
                    "\"data\": {\n" +
                    "   \"results\": [\n" +
                    "       {\n" +
                    "           \"id\": 1009157," +
                    "           \"name\": \"Spider-Girl (Anya Corazon)\"," +
                    "           \"thumbnail\": {\n" +
                    "          \"path\": \"cat\",\n" +
                    "          \"extension\": \"jpg\"\n" +
                    "           }," +
                    "           \"comics\": {\n" +
                    "          \"items\": [\n" +
                    "            {\n" +
                    "              \"resourceURI\": \"http://gateway.marvel.com/v1/public/comics/735\",\n" +
                    "              \"name\": \"Amazing Fantasy (2004) #1\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "              \"resourceURI\": \"http://gateway.marvel.com/v1/public/comics/529\",\n" +
                    "              \"name\": \"Amazing Fantasy (2004) #2\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "              \"resourceURI\": \"http://gateway.marvel.com/v1/public/comics/764\",\n" +
                    "              \"name\": \"Amazing Fantasy (2004) #3\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "              \"resourceURI\": \"http://gateway.marvel.com/v1/public/comics/858\",\n" +
                    "              \"name\": \"Amazing Fantasy (2004) #4\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "              \"resourceURI\": \"http://gateway.marvel.com/v1/public/comics/922\",\n" +
                    "              \"name\": \"Amazing Fantasy (2004) #5\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "              \"resourceURI\": \"http://gateway.marvel.com/v1/public/comics/45\",\n" +
                    "              \"name\": \"Amazing Fantasy (2004) #6\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "              \"resourceURI\": \"http://gateway.marvel.com/v1/public/comics/30305\",\n" +
                    "              \"name\": \"Amazing Spider-Man (1999) #630\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "              \"resourceURI\": \"http://gateway.marvel.com/v1/public/comics/32402\",\n" +
                    "              \"name\": \"Amazing Spider-Man (1999) #630 (QUINONES VARIANT)\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "              \"resourceURI\": \"http://gateway.marvel.com/v1/public/comics/37039\",\n" +
                    "              \"name\": \"Amazing Spider-Man (1999) #634 (2ND PRINTING VARIANT)\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "              \"resourceURI\": \"http://gateway.marvel.com/v1/public/comics/30310\",\n" +
                    "              \"name\": \"Amazing Spider-Man (1999) #635\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "              \"resourceURI\": \"http://gateway.marvel.com/v1/public/comics/33626\",\n" +
                    "              \"name\": \"Amazing Spider-Man (1999) #635 (50/50 VARIANT)\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "              \"resourceURI\": \"http://gateway.marvel.com/v1/public/comics/30312\",\n" +
                    "              \"name\": \"Amazing Spider-Man (1999) #637\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "              \"resourceURI\": \"http://gateway.marvel.com/v1/public/comics/34135\",\n" +
                    "              \"name\": \"Amazing Spider-Man (1999) #648\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "              \"resourceURI\": \"http://gateway.marvel.com/v1/public/comics/37045\",\n" +
                    "              \"name\": \"Amazing Spider-Man (1999) #648 (CASELLI VARIANT)\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "              \"resourceURI\": \"http://gateway.marvel.com/v1/public/comics/30323\",\n" +
                    "              \"name\": \"Amazing Spider-Man (1999) #648 (CAMPBELL VARIANT)\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "              \"resourceURI\": \"http://gateway.marvel.com/v1/public/comics/36937\",\n" +
                    "              \"name\": \"Amazing Spider-Man (1999) #648 (BLANK COVER VARIANT)\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "              \"resourceURI\": \"http://gateway.marvel.com/v1/public/comics/38844\",\n" +
                    "              \"name\": \"Amazing Spider-Man (1999) #648 (2ND PRINTING VARIANT)\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "              \"resourceURI\": \"http://gateway.marvel.com/v1/public/comics/37073\",\n" +
                    "              \"name\": \"Amazing Spider-Man (1999) #648 (WRAPAROUND VARIANT)\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "              \"resourceURI\": \"http://gateway.marvel.com/v1/public/comics/35513\",\n" +
                    "              \"name\": \"Amazing Spider-Man (1999) #666\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "              \"resourceURI\": \"http://gateway.marvel.com/v1/public/comics/35528\",\n" +
                    "              \"name\": \"Amazing Spider-Man (1999) #667\"\n" +
                    "            }\n" +
                    "          ]\n" +
                    "        }" +
                    "       }]}}"
            ).getAsJsonObject();
            Pair<Integer, JsonObject> result = new Pair<>(200,obj);
            return result;
        }
        else if(url.contains("https://gateway.marvel.com:443/v1/public/comics/")){
            JsonObject obj = JsonParser.parseString("{\n" +
                    "\"data\": {\n" +
                    "   \"results\": [\n" +
                    "       {\n" +
                    "           \"id\": 735," +
                    "           \"title\": \"Amazing Fantasy (2004) #1\"," +
                    "           \"thumbnail\": {\n" +
                    "          \"path\": \"cat\",\n" +
                    "          \"extension\": \"jpg\"\n" +
                    "        }," +
                    "           \"characters\": {\n" +
                    "          \"available\": 1,\n" +
                    "          \"collectionURI\": \"http://gateway.marvel.com/v1/public/comics/735/characters\",\n" +
                    "          \"items\": [\n" +
                    "            {\n" +
                    "              \"resourceURI\": \"http://gateway.marvel.com/v1/public/characters/1009157\",\n" +
                    "              \"name\": \"Spider-Girl (Anya Corazon)\"\n" +
                    "            }\n" +
                    "          ],\n" +
                    "          \"returned\": 1\n" +
                    "        }"+
                    "       }]}}"
            ).getAsJsonObject();
            Pair<Integer, JsonObject> result = new Pair<>(200,obj);
            return result;
        }
        return null;

    }
}
