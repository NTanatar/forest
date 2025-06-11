package com.nata.nasa;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequiredArgsConstructor
public class NasaController {

    private final ImageCache imageCache;

    @GetMapping("/largest")
    public RedirectView getLargest(@RequestParam Integer sol) {
        Image largest = imageCache.getLargest(sol);
        // TODO handle null
        return new RedirectView(largest.getUrl());
    }
}
