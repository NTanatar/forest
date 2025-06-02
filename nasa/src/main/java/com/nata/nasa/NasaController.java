package com.nata.nasa;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequiredArgsConstructor
public class NasaController {

    private final NasaService nasaService;

    @GetMapping("/largest")
    public RedirectView getLargest(@RequestParam Integer sol) {
        return new RedirectView(nasaService.getUrlOfTheLargestPicture(sol));
    }
}
