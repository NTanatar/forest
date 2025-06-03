package com.nata.nasa;

import static java.lang.System.currentTimeMillis;

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

    private final HttpClient client = HttpClient.newBuilder()
        .followRedirects(HttpClient.Redirect.ALWAYS)
        .build();

    public Image getLargest(int sol) {
        long s1 = currentTimeMillis();

        ArrayList<Image> images = fetchImageList(sol);

        System.out.println("Fetched image list (" + images.size() + ") in: " + (currentTimeMillis() - s1) + " ms");
        long s2 = currentTimeMillis();

        images.forEach(this::fetchSize);

        long fetchingSizeTime = (currentTimeMillis() - s2);
        System.out.println("Fetched sizes in: " + fetchingSizeTime + " ms");
        System.out.println(" -> " + fetchingSizeTime / images.size() + " ms per image ");

        return images.stream()
            .max(Comparator.comparing(Image::getSize))
            .orElse(null);
    }

    public ArrayList<Image> fetchImageList(int sol) {
        String url = String.format("%s?sol=%s&api_key=%s", BASE_URL, sol, API_KEY);

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body)
            .thenApply(NasaService::toImages)
            .join();
    }

    private static ArrayList<Image> toImages(String content) {
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

    private void fetchSize(Image image) {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(image.getUrl())).build();

        long size = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::headers)
            .thenApply(headers ->  headers.firstValueAsLong("content-length"))
            .thenApply(OptionalLong::getAsLong)
            .join();

        image.setSize(size);
        System.out.println(size);
    }
}
