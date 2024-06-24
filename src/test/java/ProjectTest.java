import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.zxing.WriterException;
import javafx.util.Pair;
import marvel.model.*;
import marvel.model.Character;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class ProjectTest {
    private GameEngine model;
    private InputAPI realInput;
    private InputAPI dummyInput;
    private OutputAPI realOutput;
    private OutputAPI dummyOutput;
    private DatabaseManager databaseManager;

    @BeforeEach
    public void setup(){
        realInput = mock(MarvelAPIHandler.class);
        dummyInput = mock(DummyMarvelAPIHandler.class);
        realOutput = mock(ImgurAPIHandler.class);
        dummyOutput =  mock(DummyImgurAPIHandler.class);
        databaseManager = mock(DatabaseManager.class);
    }

    @AfterEach
    public void clear(){
        model.clearDataBaseCache();
    }

    @Test
    public void databaseCreationTest(){
        model = new GameEngine("offline","offline",realInput,dummyInput,realOutput,dummyOutput, databaseManager);
        verify(databaseManager).createDB();
        verify(databaseManager).setupDB();
    }

    @Test
    public void checkCharacterCacheHitNullOrEmptyParameterTest() throws NoSuchAlgorithmException {
        model = new GameEngine("offline","offline",realInput,dummyInput,realOutput,dummyOutput, databaseManager);
        assertThrows(IllegalArgumentException.class, ()->{model.checkCharacterCacheHit(null);});
        assertThrows(IllegalArgumentException.class, ()->{model.checkCharacterCacheHit("");});
    }

    @Test
    public void checkCharacterCacheHitOnlineOnlyTest() throws NoSuchAlgorithmException {
        model = new GameEngine("online","offline",realInput,dummyInput,realOutput,dummyOutput, databaseManager);
        when(databaseManager.cacheHit(anyString())).thenReturn(true);
        assertTrue( model.checkCharacterCacheHit("aaa"));
        model = new GameEngine("offline","offline",realInput,dummyInput,realOutput,dummyOutput, databaseManager);
        assertFalse(model.checkCharacterCacheHit("aaa"));
        verify(databaseManager, times(1)).cacheHit(anyString());
    }

    @Test
    public void getCharacterByCacheNullOrEmptyParameterTest() throws NoSuchAlgorithmException {
        model = new GameEngine("offline","offline",realInput,dummyInput,realOutput,dummyOutput, databaseManager);
        assertThrows(IllegalArgumentException.class, ()->{model.getCharacterByCache(null);});
        assertThrows(IllegalArgumentException.class, ()->{model.getCharacterByCache("");});
    }

    @Test
    public void getCharacterByCacheNormalTest() throws NoSuchAlgorithmException {
        model = new GameEngine("offline","offline",realInput,dummyInput,realOutput,dummyOutput, databaseManager);
        String result = "       {\n" +
                "           \"id\": 1009157," +
                "           \"name\": \"Spider-Girl (Anya Corazon)\"," +
                "           \"thumbnail\": {\n" +
                "          \"path\": \"http://i.annihil.us/u/prod/marvel/i/mg/a/10/528d369de3e4f\",\n" +
                "          \"extension\": \"jpg\"\n" +
                "           }," +
                "           \"comics\": {\n" +
                "          \"items\": [\n" +
                "            {\n" +
                "              \"resourceURI\": \"http://gateway.marvel.com/v1/public/comics/735\",\n" +
                "              \"name\": \"Amazing Fantasy (2004) #1\"\n" +
                "            }\n" +
                "          ]\n" +
                "        }" +
                "       }";
        when(databaseManager.achieveData(anyString())).thenReturn(result);
        when(databaseManager.achieveFrequency(anyString())).thenReturn(anyInt());
        Character character = model.getCharacterByCache("name");
        assertTrue(character.getComics().get(0).getName().equals("Amazing Fantasy (2004) #1"));
        verify(databaseManager,times(1)).achieveData(anyString());
        verify(databaseManager,times(1)).achieveFrequency(anyString());
    }

    @Test
    public void checkComicCacheHitNullOrEmptyParameterTest() throws NoSuchAlgorithmException {
        model = new GameEngine("offline","offline",realInput,dummyInput,realOutput,dummyOutput, databaseManager);
        assertThrows(IllegalArgumentException.class, ()->{model.checkComicCacheHit(null);});
        assertThrows(IllegalArgumentException.class, ()->{model.checkComicCacheHit("");});
    }

    @Test
    public void checkComicCacheHitOnlineOnlyTest() throws NoSuchAlgorithmException {
        model = new GameEngine("online","offline",realInput,dummyInput,realOutput,dummyOutput, databaseManager);
        when(databaseManager.cacheHit(anyString())).thenReturn(true);
        assertTrue( model.checkComicCacheHit("a/a/a/a/a/a/a/a/a/a/a"));
        model = new GameEngine("offline","offline",realInput,dummyInput,realOutput,dummyOutput, databaseManager);
        assertFalse(model.checkComicCacheHit("a/a/a/a/a/a/a/a/a/a/a"));
        verify(databaseManager, times(1)).cacheHit(anyString());
    }

    @Test
    public void getComicByCacheNullOrEmptyParameterTest() throws NoSuchAlgorithmException {
        model = new GameEngine("offline","offline",realInput,dummyInput,realOutput,dummyOutput, databaseManager);
        assertThrows(IllegalArgumentException.class, ()->{model.getComicByCache(null);});
        assertThrows(IllegalArgumentException.class, ()->{model.getComicByCache("");});
    }

    @Test
    public void getComicByCacheNormalTest() throws NoSuchAlgorithmException {
        model = new GameEngine("offline","offline",realInput,dummyInput,realOutput,dummyOutput, databaseManager);
        String result = "       {\n" +
                "           \"id\": 735," +
                "           \"title\": \"Amazing Fantasy (2004) #1\"," +
                "           \"thumbnail\": {\n" +
                "          \"path\": \"http://i.annihil.us/u/prod/marvel/i/mg/9/80/56cb7746bd44a\",\n" +
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
                "       }";
        when(databaseManager.achieveData(anyString())).thenReturn(result);
        Comic comic = model.getComicByCache("a/a/a/a/a/a/a/a/a/a/a");
        assertTrue(comic.getCharacters().get(0).getName().equals("Spider-Girl (Anya Corazon)"));
        verify(databaseManager,times(1)).achieveData(anyString());
    }

    @Test
    public void getCharactersNullOrEmptyParameterTest() throws NoSuchAlgorithmException {
        model = new GameEngine("offline","offline",realInput,dummyInput,realOutput,dummyOutput, databaseManager);
        assertThrows(IllegalArgumentException.class, ()->{model.getCharacters(null);});
        assertThrows(IllegalArgumentException.class, ()->{model.getCharacters("");});
    }

    @Test
    public void getCharactersOnlineTest() throws NoSuchAlgorithmException {
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
        when(realInput.getRequest(anyString())).thenReturn(result);
        model = new GameEngine("online","online",realInput,dummyInput,realOutput,dummyOutput, databaseManager);
        List<String> names = model.getCharacters("name");
        assertTrue(names.contains("Spider-dok"));
        assertTrue(names.contains("Spider-Girl (Anya Corazon)"));
        assertTrue(names.contains("Spider-Girl (May Parker)"));
        verify(realInput,times(1)).getRequest(anyString());
    }

    @Test
    public void getCharactersOfflineTest_1() throws NoSuchAlgorithmException {
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
        when(dummyInput.getRequest(anyString())).thenReturn(result);
        model = new GameEngine("offline","online",realInput,dummyInput,realOutput,dummyOutput, databaseManager);
        List<String> names = model.getCharacters("name");
        assertTrue(names.contains("Spider-dok"));
        assertTrue(names.contains("Spider-Girl (Anya Corazon)"));
        assertTrue(names.contains("Spider-Girl (May Parker)"));
        verify(dummyInput, times(1)).getRequest(anyString());
    }

    @Test
    public void getCharactersOfflineTest_2() throws NoSuchAlgorithmException {
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
        when(dummyInput.getRequest(anyString())).thenReturn(result);
        model = new GameEngine("offline","online",realInput,dummyInput,realOutput,dummyOutput, databaseManager);
        List<String> names = model.getCharacters("Spider_dok");
        assertTrue(names.contains("Spider-dok"));
        assertTrue(names.contains("Spider-Girl (Anya Corazon)"));
        assertTrue(names.contains("Spider-Girl (May Parker)"));
        verify(dummyInput, times(1)).getRequest(anyString());
    }

    @Test
    public void getCharacterByNameNullOrEmptyParameterTest() throws NoSuchAlgorithmException {
        model = new GameEngine("offline","offline",realInput,dummyInput,realOutput,dummyOutput, databaseManager);
        assertThrows(IllegalArgumentException.class, ()->{model.getCharacterByName(null);});
        assertThrows(IllegalArgumentException.class, ()->{model.getCharacterByName("");});
    }

    @Test
    public void getCharacterByNameOnlineTest() throws NoSuchAlgorithmException {
        JsonObject obj = JsonParser.parseString("{\n" +
                "\"data\": {\n" +
                "   \"results\": [\n" +
                "       {\n" +
                "           \"id\": 1009157," +
                "           \"name\": \"Spider-Girl (Anya Corazon)\"," +
                "           \"thumbnail\": {\n" +
                "          \"path\": \"http://i.annihil.us/u/prod/marvel/i/mg/a/10/528d369de3e4f\",\n" +
                "          \"extension\": \"jpg\"\n" +
                "           }," +
                "           \"comics\": {\n" +
                "          \"items\": [\n" +
                "            {\n" +
                "              \"resourceURI\": \"http://gateway.marvel.com/v1/public/comics/735\",\n" +
                "              \"name\": \"Amazing Fantasy (2004) #1\"\n" +
                "            }\n" +
                "          ]\n" +
                "        }" +
                "       }]}}"
        ).getAsJsonObject();
        Pair<Integer, JsonObject> result = new Pair<>(200,obj);
        when(realInput.getRequest(anyString())).thenReturn(result);
        model = new GameEngine("online","online",realInput,dummyInput,realOutput,dummyOutput, databaseManager);
        Character character = model.getCharacterByName("name");
        assertTrue(character.getComics().get(0).getName().equals("Amazing Fantasy (2004) #1"));
        verify(realInput,times(1)).getRequest(anyString());
    }

    @Test
    public void getCharacterByNameOnlineSaveDataTest() throws NoSuchAlgorithmException {
        JsonObject obj = JsonParser.parseString("{\n" +
                "\"data\": {\n" +
                "   \"results\": [\n" +
                "       {\n" +
                "           \"id\": 1009157," +
                "           \"name\": \"Spider-Girl (Anya Corazon)\"," +
                "           \"thumbnail\": {\n" +
                "          \"path\": \"http://i.annihil.us/u/prod/marvel/i/mg/a/10/528d369de3e4f\",\n" +
                "          \"extension\": \"jpg\"\n" +
                "           }," +
                "           \"comics\": {\n" +
                "          \"items\": [\n" +
                "            {\n" +
                "              \"resourceURI\": \"http://gateway.marvel.com/v1/public/comics/735\",\n" +
                "              \"name\": \"Amazing Fantasy (2004) #1\"\n" +
                "            }\n" +
                "          ]\n" +
                "        }" +
                "       }]}}"
        ).getAsJsonObject();
        Pair<Integer, JsonObject> result = new Pair<>(200,obj);
        when(realInput.getRequest(anyString())).thenReturn(result);
        when(databaseManager.cacheHit(anyString())).thenReturn(false);
        model = new GameEngine("online","online",realInput,dummyInput,realOutput,dummyOutput, databaseManager);
        Character character = model.getCharacterByName("name");
        assertTrue(character.getComics().get(0).getName().equals("Amazing Fantasy (2004) #1"));
        verify(databaseManager,times(1)).saveData(anyString(), anyString());
    }

    @Test
    public void getCharacterByNameOnlineUpdateDataTest() throws NoSuchAlgorithmException {
        JsonObject obj = JsonParser.parseString("{\n" +
                "\"data\": {\n" +
                "   \"results\": [\n" +
                "       {\n" +
                "           \"id\": 1009157," +
                "           \"name\": \"Spider-Girl (Anya Corazon)\"," +
                "           \"thumbnail\": {\n" +
                "          \"path\": \"http://i.annihil.us/u/prod/marvel/i/mg/a/10/528d369de3e4f\",\n" +
                "          \"extension\": \"jpg\"\n" +
                "           }," +
                "           \"comics\": {\n" +
                "          \"items\": [\n" +
                "            {\n" +
                "              \"resourceURI\": \"http://gateway.marvel.com/v1/public/comics/735\",\n" +
                "              \"name\": \"Amazing Fantasy (2004) #1\"\n" +
                "            }\n" +
                "          ]\n" +
                "        }" +
                "       }]}}"
        ).getAsJsonObject();
        Pair<Integer, JsonObject> result = new Pair<>(200,obj);
        when(realInput.getRequest(anyString())).thenReturn(result);
        when(databaseManager.cacheHit(anyString())).thenReturn(true);
        model = new GameEngine("online","online",realInput,dummyInput,realOutput,dummyOutput, databaseManager);
        Character character = model.getCharacterByName("name");
        assertTrue(character.getComics().get(0).getName().equals("Amazing Fantasy (2004) #1"));
        verify(databaseManager,times(1)).updateData(anyString(), anyString(), anyInt());
        verify(databaseManager, times(1)).achieveFrequency(anyString());
    }

    @Test
    public void getCharacterByNameOfflineTest_1() throws NoSuchAlgorithmException {
        JsonObject obj = JsonParser.parseString("{\n" +
                "\"data\": {\n" +
                "   \"results\": [\n" +
                "       {\n" +
                "           \"id\": 1009157," +
                "           \"name\": \"Spider-Girl (Anya Corazon)\"," +
                "           \"thumbnail\": {\n" +
                "          \"path\": \"http://i.annihil.us/u/prod/marvel/i/mg/a/10/528d369de3e4f\",\n" +
                "          \"extension\": \"jpg\"\n" +
                "           }," +
                "           \"comics\": {\n" +
                "          \"items\": [\n" +
                "            {\n" +
                "              \"resourceURI\": \"http://gateway.marvel.com/v1/public/comics/735\",\n" +
                "              \"name\": \"Amazing Fantasy (2004) #1\"\n" +
                "            }\n" +
                "          ]\n" +
                "        }" +
                "       }]}}"
        ).getAsJsonObject();
        Pair<Integer, JsonObject> result = new Pair<>(200,obj);
        when(dummyInput.getRequest(anyString())).thenReturn(result);
        model = new GameEngine("offline","online",realInput,dummyInput,realOutput,dummyOutput, databaseManager);
        Character character = model.getCharacterByName("name");
        assertTrue(character.getComics().get(0).getName().equals("Amazing Fantasy (2004) #1"));
        verify(dummyInput,times(1)).getRequest(anyString());
    }

    @Test
    public void getCharacterByNameOfflineTest_2() throws NoSuchAlgorithmException {
        JsonObject obj = JsonParser.parseString("{\n" +
                "\"data\": {\n" +
                "   \"results\": [\n" +
                "       {\n" +
                "           \"id\": 1009157," +
                "           \"name\": \"Spider-Girl (Anya Corazon)\"," +
                "           \"thumbnail\": {\n" +
                "          \"path\": \"http://i.annihil.us/u/prod/marvel/i/mg/a/10/528d369de3e4f\",\n" +
                "          \"extension\": \"jpg\"\n" +
                "           }," +
                "           \"comics\": {\n" +
                "          \"items\": [\n" +
                "            {\n" +
                "              \"resourceURI\": \"http://gateway.marvel.com/v1/public/comics/735\",\n" +
                "              \"name\": \"Amazing Fantasy (2004) #1\"\n" +
                "            }\n" +
                "          ]\n" +
                "        }" +
                "       }]}}"
        ).getAsJsonObject();
        Pair<Integer, JsonObject> result = new Pair<>(200,obj);
        when(dummyInput.getRequest(anyString())).thenReturn(result);
        model = new GameEngine("offline","online",realInput,dummyInput,realOutput,dummyOutput, databaseManager);
        Character character = model.getCharacterByName("Spider-Girl (Anya Corazon)");
        assertTrue(character.getComics().get(0).getName().equals("Amazing Fantasy (2004) #1"));
        verify(dummyInput,times(1)).getRequest(anyString());
    }

    @Test
    public void getComicByIDNullOrEmptyParameterTest() throws NoSuchAlgorithmException {
        model = new GameEngine("offline","offline",realInput,dummyInput,realOutput,dummyOutput, databaseManager);
        assertThrows(IllegalArgumentException.class, ()->{model.getComicByID(null);});
        assertThrows(IllegalArgumentException.class, ()->{model.getComicByID("");});
    }

    @Test
    public void getComicByIDOnlineTest() throws NoSuchAlgorithmException {
        JsonObject obj = JsonParser.parseString("{\n" +
                "\"data\": {\n" +
                "   \"results\": [\n" +
                "       {\n" +
                "           \"id\": 735," +
                "           \"title\": \"Amazing Fantasy (2004) #1\"," +
                "           \"thumbnail\": {\n" +
                "          \"path\": \"http://i.annihil.us/u/prod/marvel/i/mg/9/80/56cb7746bd44a\",\n" +
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
        when(realInput.getRequest(anyString())).thenReturn(result);
        model = new GameEngine("online","online",realInput,dummyInput,realOutput,dummyOutput,databaseManager);
        Comic comic = model.getComicByID("a/a/a/a/a/a/a/a/a/a/a");
        assertTrue(comic.getCharacters().get(0).getName().equals("Spider-Girl (Anya Corazon)"));
        verify(realInput,times(1)).getRequest(anyString());
    }

    @Test
    public void getComicByIDOnlineSaveDataTest() throws NoSuchAlgorithmException {
        JsonObject obj = JsonParser.parseString("{\n" +
                "\"data\": {\n" +
                "   \"results\": [\n" +
                "       {\n" +
                "           \"id\": 735," +
                "           \"title\": \"Amazing Fantasy (2004) #1\"," +
                "           \"thumbnail\": {\n" +
                "          \"path\": \"http://i.annihil.us/u/prod/marvel/i/mg/9/80/56cb7746bd44a\",\n" +
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
        when(realInput.getRequest(anyString())).thenReturn(result);
        when(databaseManager.cacheHit(anyString())).thenReturn(false);
        model = new GameEngine("online","online",realInput,dummyInput,realOutput,dummyOutput,databaseManager);
        Comic comic = model.getComicByID("a/a/a/a/a/a/a/a/a/a/a");
        assertTrue(comic.getCharacters().get(0).getName().equals("Spider-Girl (Anya Corazon)"));
        verify(databaseManager,times(1)).saveData(anyString(),anyString());
    }

    @Test
    public void getComicByIDOnlineUpdateDataTest() throws NoSuchAlgorithmException {
        JsonObject obj = JsonParser.parseString("{\n" +
                "\"data\": {\n" +
                "   \"results\": [\n" +
                "       {\n" +
                "           \"id\": 735," +
                "           \"title\": \"Amazing Fantasy (2004) #1\"," +
                "           \"thumbnail\": {\n" +
                "          \"path\": \"http://i.annihil.us/u/prod/marvel/i/mg/9/80/56cb7746bd44a\",\n" +
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
        when(realInput.getRequest(anyString())).thenReturn(result);
        when(databaseManager.cacheHit(anyString())).thenReturn(true);
        model = new GameEngine("online","online",realInput,dummyInput,realOutput,dummyOutput,databaseManager);
        Comic comic = model.getComicByID("a/a/a/a/a/a/a/a/a/a/a");
        assertTrue(comic.getCharacters().get(0).getName().equals("Spider-Girl (Anya Corazon)"));
        verify(databaseManager,times(1)).updateData(anyString(),anyString(), anyInt());
        verify(databaseManager, times(1)).achieveFrequency(anyString());
    }

    @Test
    public void getComicByIDOfflineTest() throws NoSuchAlgorithmException {
        JsonObject obj = JsonParser.parseString("{\n" +
                "\"data\": {\n" +
                "   \"results\": [\n" +
                "       {\n" +
                "           \"id\": 735," +
                "           \"title\": \"Amazing Fantasy (2004) #1\"," +
                "           \"thumbnail\": {\n" +
                "          \"path\": \"http://i.annihil.us/u/prod/marvel/i/mg/9/80/56cb7746bd44a\",\n" +
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
        when(dummyInput.getRequest(anyString())).thenReturn(result);
        model = new GameEngine("offline","online",realInput,dummyInput,realOutput,dummyOutput, databaseManager);
        Comic comic = model.getComicByID("a/a/a/a/a/a/a/a/a/a/a");
        assertTrue(comic.getCharacters().get(0).getName().equals("Spider-Girl (Anya Corazon)"));
        verify(dummyInput,times(1)).getRequest(anyString());
    }

    @Test
    public void uploadImageNullOrEmptyParameterTest() throws NoSuchAlgorithmException {
        model = new GameEngine("offline","offline",realInput,dummyInput,realOutput,dummyOutput,databaseManager);
        assertThrows(IllegalArgumentException.class, ()->{model.uploadImage(null);});
        assertThrows(IllegalArgumentException.class, ()->{model.uploadImage("");});
    }

    @Test
    public void uploadImageOnlineTest() throws NoSuchAlgorithmException, IOException, WriterException {
        String result = "{\n" +
                "  \"data\": {\n" +
                "    \"id\": \"orunSTu\",\n" +
                "    \"title\": null,\n" +
                "    \"description\": null,\n" +
                "    \"datetime\": 1495556889,\n" +
                "    \"type\": \"image/gif\",\n" +
                "    \"animated\": false,\n" +
                "    \"width\": 1,\n" +
                "    \"height\": 1,\n" +
                "    \"size\": 42,\n" +
                "    \"views\": 0,\n" +
                "    \"bandwidth\": 0,\n" +
                "    \"vote\": null,\n" +
                "    \"favorite\": false,\n" +
                "    \"nsfw\": null,\n" +
                "    \"section\": null,\n" +
                "    \"account_url\": null,\n" +
                "    \"account_id\": 0,\n" +
                "    \"is_ad\": false,\n" +
                "    \"in_most_viral\": false,\n" +
                "    \"tags\": [],\n" +
                "    \"ad_type\": 0,\n" +
                "    \"ad_url\": \"\",\n" +
                "    \"in_gallery\": false,\n" +
                "    \"deletehash\": \"x70po4w7BVvSUzZ\",\n" +
                "    \"name\": \"\",\n" +
                "    \"link\": \"http://i.imgur.com/orunSTu.gif\"\n" +
                "  },\n" +
                "  \"success\": true,\n" +
                "  \"status\": 200\n" +
                "}";;
        when(realOutput.postImageImgur(anyString())).thenReturn(result);
        model = new GameEngine("offline","online",realInput,dummyInput,realOutput,dummyOutput, databaseManager);
        String image = model.uploadImage("name");
        assertTrue(image.equals("http://i.imgur.com/orunSTu.gif"));
        verify(realOutput,times(1)).postImageImgur(anyString());
    }

    @Test
    public void uploadImageOfflineTest() throws NoSuchAlgorithmException, IOException, WriterException {
        String result = "{\n" +
                "  \"data\": {\n" +
                "    \"id\": \"orunSTu\",\n" +
                "    \"title\": null,\n" +
                "    \"description\": null,\n" +
                "    \"datetime\": 1495556889,\n" +
                "    \"type\": \"image/gif\",\n" +
                "    \"animated\": false,\n" +
                "    \"width\": 1,\n" +
                "    \"height\": 1,\n" +
                "    \"size\": 42,\n" +
                "    \"views\": 0,\n" +
                "    \"bandwidth\": 0,\n" +
                "    \"vote\": null,\n" +
                "    \"favorite\": false,\n" +
                "    \"nsfw\": null,\n" +
                "    \"section\": null,\n" +
                "    \"account_url\": null,\n" +
                "    \"account_id\": 0,\n" +
                "    \"is_ad\": false,\n" +
                "    \"in_most_viral\": false,\n" +
                "    \"tags\": [],\n" +
                "    \"ad_type\": 0,\n" +
                "    \"ad_url\": \"\",\n" +
                "    \"in_gallery\": false,\n" +
                "    \"deletehash\": \"x70po4w7BVvSUzZ\",\n" +
                "    \"name\": \"\",\n" +
                "    \"link\": \"http://i.imgur.com/orunSTu.gif\"\n" +
                "  },\n" +
                "  \"success\": true,\n" +
                "  \"status\": 200\n" +
                "}";;
        when(dummyOutput.postImageImgur(anyString())).thenReturn(result);
        model = new GameEngine("offline","offline",realInput,dummyInput,realOutput,dummyOutput, databaseManager);
        String image = model.uploadImage("name");
        assertTrue(image.equals("http://i.imgur.com/orunSTu.gif"));
        verify(dummyOutput,times(1)).postImageImgur(anyString());
    }

    @Test
    public void generateQRCodeImageNullOrEmptyParameterTest() throws NoSuchAlgorithmException {
        model = new GameEngine("offline","offline",realInput,dummyInput,realOutput,dummyOutput, databaseManager);
        assertThrows(IllegalArgumentException.class, ()->{model.generateQRCodeImage(null);});
        assertThrows(IllegalArgumentException.class, ()->{model.generateQRCodeImage("");});
    }

    @ParameterizedTest
    @ValueSource(strings = {"-10", "-1", "0" , "1", "2", "51", "100", "a", "b", ""})
    public void setPopThresholdInvalidParametersTest(String number){
        model = new GameEngine("offline","offline",realInput,dummyInput,realOutput,dummyOutput, databaseManager);
        assertThrows(IllegalArgumentException.class, ()->{model.setPopThreshold(number);});
    }

    @Test
    public void setPopThresholdNullParameterTest() throws NoSuchAlgorithmException {
        model = new GameEngine("offline","offline",realInput,dummyInput,realOutput,dummyOutput, databaseManager);
        assertThrows(IllegalArgumentException.class, ()->{model.setPopThreshold(null);});
    }

    @Test
    public void characterPopularityCheckDoesNotPopularTest(){
        when(databaseManager.achieveFrequency(anyString())).thenReturn(1);
        model = new GameEngine("online","online",realInput,dummyInput,realOutput,dummyOutput, databaseManager);
        model.setPopThreshold("3");
        assertFalse(model.characterPopularityCheck("name"));
        verify(databaseManager, times(1)).achieveFrequency(anyString());
    }

    @Test
    public void characterPopularityCheckVeryPopularTest(){
        when(databaseManager.achieveFrequency(anyString())).thenReturn(51);
        model = new GameEngine("online","online",realInput,dummyInput,realOutput,dummyOutput, databaseManager);
        model.setPopThreshold("50");
        assertTrue(model.characterPopularityCheck("name"));
        verify(databaseManager, times(1)).achieveFrequency(anyString());
    }
}
