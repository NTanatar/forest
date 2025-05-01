package com.nata.bird.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class BirdController {

    @GetMapping("/sound")
    @ResponseBody
    public String getBirdSound() {
        return "chirps";
    }

    @PostMapping("/feed")
    @ResponseBody
    public FeedResponse feed() {
        return new FeedResponse("thanks");
    }
}
