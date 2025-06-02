package com.nata.nasa;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.OptionalLong;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class NasaService {

    private final static String BASE_URL = "https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos";
    private final static String API_KEY = "mYISaCMwm5xo8Z8ExYGzg0xJuecrU4ab0TScQ2fT";

    public static ArrayList<Image> toImages(String content) {
        ArrayList<Image> result = new ArrayList<>();
        JSONObject json = new JSONObject(content);
        JSONArray photos = json.optJSONArray("photos");
        for (int i = 0; i < photos.length(); i++) {
            JSONObject photo = photos.optJSONObject(i);

            Image image = new Image();
            image.setId(Integer.parseInt(photo.get("id").toString()));
            image.setUrl(photo.get("img_src").toString());

            result.add(image);
        }
        return result;
    }

    public static void fetchSize(HttpClient client, Image image) {
        HttpRequest first = HttpRequest.newBuilder().uri(URI.create(image.getUrl())).build();

        long size = client.sendAsync(first, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::headers)
            .thenApply(headers ->  headers.firstValueAsLong("content-length"))
            .thenApply(OptionalLong::getAsLong)
            .join();

        image.setSize(size);
        System.out.println(image);
    }

    public String getUrlOfTheLargestPicture(int sol) {
        // Start time
        long startTime = System.currentTimeMillis();

        try (HttpClient client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.ALWAYS).build()) {

            String url = String.format("%s?sol=%s&api_key=%s", BASE_URL, sol, API_KEY);
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();

            ArrayList<Image> images =
                client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenApply(NasaService::toImages)
                    .join();

            images.forEach(image -> fetchSize(client, image));

            return images.stream()
                .max(Comparator.comparing(Image::getSize))
                .map(img -> {
                    System.out.println("max: " + img);
                    // End time
                    long endTime = System.currentTimeMillis();

                    // Calculate elapsed time in milliseconds
                    long elapsedTime = endTime - startTime;

                    System.out.println("Elapsed time: " + elapsedTime + " ms");
                    System.out.println("Number of images: " + images.size() + " -> " + elapsedTime / images.size());
                    return img.getUrl();
                }).orElse(null);
        }
    }
}
