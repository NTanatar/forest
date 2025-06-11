package com.nata.nasa;

import static org.springframework.http.HttpStatus.MOVED_PERMANENTLY;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class NasaController {

    private final ImageCache imageCache;

    @GetMapping("/largest")
    public ResponseEntity<Void> getLargest(@RequestParam Integer sol) {
        Image image =  imageCache.getLargest(sol);
        if (image == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(MOVED_PERMANENTLY)
            .location(URI.create(image.getUrl()))
            .build();
    }
}
