package marvel.model;

import okhttp3.*;

import java.io.IOException;

public class ImgurAPIHandler implements OutputAPI{

    public String postImageImgur(String imageBase64) throws IOException {
        /*The following is copied from https://apidocs.imgur.com/#c85c9dfc-7487-4de2-9ecd-66f727cf3139*/
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("image",imageBase64)
                .build();
        Request request = new Request.Builder()
                .url("https://api.imgur.com/3/image")
                .method("POST", body)
                .addHeader("Authorization", "Client-ID "+System.getenv("IMGUR_API_KEY"))
                .build();
        Response response = client.newCall(request).execute();
        /* end of the copied code*/
        String result = response.body().string();
        client.dispatcher().executorService().shutdown();
        client.connectionPool().evictAll();
        return result;
    }
}
