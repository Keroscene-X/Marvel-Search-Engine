package marvel.model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.util.Pair;
import org.apache.commons.codec.binary.Hex;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.security.MessageDigest;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class GameEngine {
    private final String INPUT_API_KEY = System.getenv("INPUT_API_KEY");
    private final String INPUT_API_APP_ID = System.getenv("INPUT_API_APP_ID");
    private final String TIMESTAMP = "1";
    private String hash;
    private final String AUTHORIZATION;
    private final String QR_CODE_IMAGE_PATH = "./demo.png";
    private final int QR_CODE_IMAGE_WIDTH = 350;
    private final int QR_CODE_IMAGE_HEIGHT = 350;
    private final String IMAGEVARIANT;
    private Gson gson;
    private Character character;
    private Comic comic;
    private HistoryItem latestHistoryItem;
    private InputAPI inputHandler;
    private OutputAPI outputHandler;
    private final DatabaseManager databaseManager;
    private final String INPUT_MODE;
    private int popThreshold;




    public GameEngine(String inputMode, String outputMode, InputAPI realInputAPI, InputAPI dummyInputAPI, OutputAPI realOutputAPI, OutputAPI dummyOutputAPI, DatabaseManager databaseManager) {
        INPUT_MODE = inputMode;
        if (inputMode.equals("online")){
            IMAGEVARIANT = "/portrait_xlarge";
            inputHandler = realInputAPI;
        }
        else{
            IMAGEVARIANT = "";
            inputHandler = dummyInputAPI;
        }
        if (outputMode.equals("online")){
            outputHandler = realOutputAPI;
        }
        else{
            outputHandler = dummyOutputAPI;
        }
        String hashString = TIMESTAMP + INPUT_API_APP_ID + INPUT_API_KEY;
        /*The following is copied from https://stackoverflow.com/questions/415953/how-can-i-generate-an-md5-hash-in-java#:~:text=Call%20MessageDigest.,in%20one%20operation%20with%20md*/
        byte[] bytesOfMessage = hashString.getBytes(StandardCharsets.UTF_8);
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
            byte[] md5 = md.digest(bytesOfMessage);
            this.hash = new String(Hex.encodeHex(md5));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        /* end of the copied code*/
        this.AUTHORIZATION = "ts=" + TIMESTAMP + "&apikey=" + INPUT_API_KEY + "&hash=" + hash;
        this.gson = new Gson();
        this.databaseManager = databaseManager;
        databaseManager.createDB();
        databaseManager.setupDB();
    }

    public boolean checkCharacterCacheHit(String name){
        if (name == null || name == ""){
            throw new IllegalArgumentException("Please enter some words to search!");
        }
        if (INPUT_MODE.equals("offline")){
            return false;
        }
        String httpName = name.replaceAll(" ", "%20");
        String request = String.format("https://gateway.marvel.com:443/v1/public/characters?name=%s",httpName);
        return databaseManager.cacheHit(request);
    }

    public Character getCharacterByCache(String name){
        if (name == null || name == ""){
            throw new IllegalArgumentException("Please enter some words to search!");
        }
        String httpName = name.replaceAll(" ", "%20");
        String request = String.format("https://gateway.marvel.com:443/v1/public/characters?name=%s&%s",httpName,AUTHORIZATION);
        String cacheRequest = String.format("https://gateway.marvel.com:443/v1/public/characters?name=%s",httpName);
        JsonObject result = gson.fromJson(databaseManager.achieveData(cacheRequest), JsonObject.class);
        this.character = new Character(result.get("id").getAsInt(), result.get("name").getAsString());
        Thumbnail thumbnail = gson.fromJson(result.get("thumbnail"),Thumbnail.class);
        List<Comic> comics = new ArrayList<>();
        JsonArray comicArray = result.get("comics").getAsJsonObject().get("items").getAsJsonArray();
        for(Object o : comicArray){
            JsonObject obj = (JsonObject) o;
            comics.add(gson.fromJson(obj,Comic.class));
        }
        character.setComics(comics);
        character.setThumbnail(thumbnail);
        latestHistoryItem = new HistoryItem(request, "Character-"+character.getName());
        databaseManager.updateData(cacheRequest,gson.toJson(result),(databaseManager.achieveFrequency(cacheRequest) + 1));
        return character;
    }

    public boolean checkComicCacheHit(String uri){
        if (uri == null || uri == ""){
            throw new IllegalArgumentException("Please enter the URI");
        }
        if (INPUT_MODE.equals("offline")){
            return false;
        }
        String comicID = uri.split("/")[6];
        String request = String.format("https://gateway.marvel.com:443/v1/public/comics/%s",comicID);
        return databaseManager.cacheHit(request);
    }

    public Comic getComicByCache(String uri){
        if (uri == null || uri == ""){
            throw new IllegalArgumentException("Please enter the URI");
        }
        String comicID = uri.split("/")[6];
        String request = String.format("https://gateway.marvel.com:443/v1/public/comics/%s?%s",comicID,AUTHORIZATION);
        String cacheRequest = String.format("https://gateway.marvel.com:443/v1/public/comics/%s",comicID);
        JsonObject result = gson.fromJson(databaseManager.achieveData(cacheRequest), JsonObject.class);
        this.comic = new Comic(result.get("id").getAsInt(), result.get("title").getAsString());
        Thumbnail thumbnail = gson.fromJson(result.get("thumbnail"),Thumbnail.class);
        List<Character> characters = new ArrayList<>();
        JsonArray characterArray = result.get("characters").getAsJsonObject().get("items").getAsJsonArray();
        for(Object o : characterArray){
            JsonObject obj = (JsonObject) o;
            characters.add(gson.fromJson(obj,Character.class));
        }
        comic.setCharacters(characters);
        comic.setThumbnail(thumbnail);
        latestHistoryItem = new HistoryItem(request, "Comic-"+comic.getName());
        return comic;
    }

    public List<String> getCharacters(String name){
        if (name == null || name == ""){
            throw new IllegalArgumentException("Please enter some words to search!");
        }
        String request = String.format("https://gateway.marvel.com:443/v1/public/characters?nameStartsWith=%s&orderBy=name&%s",name,AUTHORIZATION);
        ArrayList<String> names = new ArrayList<>();
        Pair<Integer, JsonObject> response = inputHandler.getRequest(request);
        if (response.getKey() >= 200 && response.getKey() < 300){
            JsonArray results = response.getValue().get("data").getAsJsonObject().get("results").getAsJsonArray();
            for(Object o : results){
                JsonObject obj = (JsonObject) o;
                names.add(obj.get("name").getAsString());
            }
        }
        else if(response.getKey() >= 400){
            throw new IllegalArgumentException(response.getValue().get("message").getAsString());
        }
        return names;
    }

    public Character getCharacterByName(String name){
        if (name == null || name == ""){
            throw new IllegalArgumentException("Please enter some words to search!");
        }
        String httpName = name.replaceAll(" ", "%20");
        String request = String.format("https://gateway.marvel.com:443/v1/public/characters?name=%s&%s",httpName,AUTHORIZATION);
        String cacheRequest = String.format("https://gateway.marvel.com:443/v1/public/characters?name=%s",httpName);
        Pair<Integer, JsonObject> response = inputHandler.getRequest(request);
        if (response.getKey() >= 200 && response.getKey() < 300){
            JsonObject result = response.getValue().get("data").getAsJsonObject().get("results").getAsJsonArray().get(0).getAsJsonObject();
            if(INPUT_MODE.equals("online")){
                if(databaseManager.cacheHit(cacheRequest)){
                    databaseManager.updateData(cacheRequest,gson.toJson(result),(databaseManager.achieveFrequency(cacheRequest) + 1));
                }
                else{
                    databaseManager.saveData(cacheRequest, gson.toJson(result));
                }
            }
            this.character = new Character(result.get("id").getAsInt(), result.get("name").getAsString());
            Thumbnail thumbnail = gson.fromJson(result.get("thumbnail"),Thumbnail.class);
            List<Comic> comics = new ArrayList<>();
            JsonArray comicArray = result.get("comics").getAsJsonObject().get("items").getAsJsonArray();
            for(Object o : comicArray){
                JsonObject obj = (JsonObject) o;
                comics.add(gson.fromJson(obj,Comic.class));
            }
            character.setComics(comics);
            character.setThumbnail(thumbnail);
            latestHistoryItem = new HistoryItem(request, "Character-"+character.getName());
            return character;
        }
        else if(response.getKey() >= 400){
            throw new IllegalArgumentException(response.getValue().get("message").getAsString());
        }
        return null;
    }

    public Comic getComicByID(String uri){
        if (uri == null || uri == ""){
            throw new IllegalArgumentException("Please enter the URI");
        }
        String comicID = uri.split("/")[6];
        String request = String.format("https://gateway.marvel.com:443/v1/public/comics/%s?%s",comicID,AUTHORIZATION);
        String cacheRequest = String.format("https://gateway.marvel.com:443/v1/public/comics/%s",comicID);
        Pair<Integer, JsonObject> response = inputHandler.getRequest(request);
        if (response.getKey() >= 200 && response.getKey() < 300){
            JsonObject result = response.getValue().get("data").getAsJsonObject().get("results").getAsJsonArray().get(0).getAsJsonObject();
            if(INPUT_MODE.equals("online")){
                if(databaseManager.cacheHit(cacheRequest)){
                    databaseManager.updateData(cacheRequest,gson.toJson(result), (databaseManager.achieveFrequency(cacheRequest) + 1));
                }
                else{
                    databaseManager.saveData(cacheRequest, gson.toJson(result));
                }
            }
            this.comic = new Comic(result.get("id").getAsInt(), result.get("title").getAsString());
            Thumbnail thumbnail = gson.fromJson(result.get("thumbnail"),Thumbnail.class);
            List<Character> characters = new ArrayList<>();
            JsonArray characterArray = result.get("characters").getAsJsonObject().get("items").getAsJsonArray();
            for(Object o : characterArray){
                JsonObject obj = (JsonObject) o;
                characters.add(gson.fromJson(obj,Character.class));
            }
            comic.setCharacters(characters);
            comic.setThumbnail(thumbnail);
            latestHistoryItem = new HistoryItem(request, "Comic-"+comic.getName());
            return comic;
        }
        else if(response.getKey() >= 400){
            throw new IllegalArgumentException(response.getValue().get("message").getAsString());
        }
        return null;
    }

    public void generateQRCodeImage(String text)
            throws WriterException, IOException {
        if (text == null || text == ""){
            throw new IllegalArgumentException("Error Occurred when generating the image");
        }
        /* The following code was modified based on https://www.callicoder.com/generate-qr-code-in-java-using-zxing/ */
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, QR_CODE_IMAGE_WIDTH, QR_CODE_IMAGE_HEIGHT);
        Path path = FileSystems.getDefault().getPath(QR_CODE_IMAGE_PATH);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
        /* end of the modified code*/
    }

    public String uploadImage(String text) throws IOException, WriterException {
        if (text == null || text == ""){
            throw new IllegalArgumentException("Error Occurred when generating the image");
        }
        generateQRCodeImage(text);
        /* The following was modified based on https://stackoverflow.com/questions/36492084/how-to-convert-an-image-to-base64-string-in-java*/
        File f = new File(QR_CODE_IMAGE_PATH);
        FileInputStream fileInputStream = new FileInputStream(f);
        byte[] bytes = new byte[(int) f.length()];
        fileInputStream.read(bytes);
        fileInputStream.close();
        String encodedFile = Base64.getEncoder().encodeToString(bytes);
        /* end of the modified code*/
        String response = outputHandler.postImageImgur(encodedFile);
        JsonObject result = gson.fromJson(response,JsonObject.class);
        int status = result.get("status").getAsInt();
        if (status >= 200 && status < 300){
            String imageURL = result.get("data").getAsJsonObject().get("link").getAsString();
            return imageURL;
        }
        else if(status >= 400){
            throw new IllegalArgumentException(result.get("data").getAsJsonObject().get("error").getAsString());
        }
        return null;
    }

    public void clearDataBaseCache(){
        databaseManager.clearDB();
    }


    public HistoryItem getLatestHistoryItem() {
        return latestHistoryItem;
    }

    public String getIMAGEVARIANT() {
        return IMAGEVARIANT;
    }

    public void setPopThreshold(String popThreshold) {
        try{
            Integer threshold = Integer.parseInt(popThreshold);
            if(threshold < 3 || threshold > 50){
                throw new IllegalArgumentException("You can only enter a number from 3 to 50.");
            }
            this.popThreshold = threshold;
        }catch (NumberFormatException e){
            throw new IllegalArgumentException("You can only enter a number from 3 to 50.");
        }
    }

    public boolean characterPopularityCheck(String name){
        String httpName = name.replaceAll(" ", "%20");
        String cacheRequest = String.format("https://gateway.marvel.com:443/v1/public/characters?name=%s",httpName);
        boolean isPopular = databaseManager.achieveFrequency(cacheRequest) > popThreshold;
        return isPopular;
    }
}
