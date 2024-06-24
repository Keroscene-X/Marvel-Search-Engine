package marvel.model;

import java.io.IOException;

public class DummyImgurAPIHandler implements OutputAPI{

    public String postImageImgur(String imageBase64) throws IOException {

        return "{\n" +
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
                "}";
    }
}
