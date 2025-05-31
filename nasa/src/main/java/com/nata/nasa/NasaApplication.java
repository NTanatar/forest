package com.nata.nasa;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.OptionalLong;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NasaApplication {

    private final static String BASE_URL = "https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos";
    private final static String API_KEY = "mYISaCMwm5xo8Z8ExYGzg0xJuecrU4ab0TScQ2fT";

    private final static String URL = String.format("%s?sol=300&api_key=%s", BASE_URL, API_KEY);

    public static ArrayList<String> toImageSources(String content) {
        ArrayList<String> result = new ArrayList<>();
        JSONObject json = new JSONObject(content);
        JSONArray photos = json.optJSONArray("photos");
        for (int i = 0; i < photos.length(); i++) {
            JSONObject photo = photos.optJSONObject(i);
            result.add(photo.get("img_src").toString());
        }
        return result;
    }

    public static long getImageSize(HttpClient client, String imageSource) {
        HttpRequest first = HttpRequest.newBuilder().uri(URI.create(imageSource)).build();

        return client.sendAsync(first, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::headers)
            .thenApply(headers -> headers.firstValueAsLong("content-length"))
            .thenApply(OptionalLong::getAsLong)
            .join();
    }

    public static String getMaxImage(HttpClient client, ArrayList<String> imageSources) {
        long max = 0L;
        String src = "";

        for(String imageSource: imageSources) {
            long imageSize = getImageSize(client, imageSource);
            System.out.println("image size: " + imageSize);
            if (max < imageSize) {
                max = imageSize;
                src = imageSource;
            }
        }
        System.out.println("max size: " + max);
        return src;
    }

    public static void main(String[] args) {
        // SpringApplication.run(NasaApplication.class, args);

        try (HttpClient client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.ALWAYS).build()) {

            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(URL)).build();

            ArrayList<String> imageSources =
                client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenApply(NasaApplication::toImageSources)
                    .join();

            System.out.println(getMaxImage(client, imageSources));
        }
    }
}
