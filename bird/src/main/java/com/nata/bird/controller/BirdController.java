package com.nata.bird.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/bird")
public class BirdController {

    @GetMapping("/sound")
    @ResponseBody
    public String getBirdSound() {
        return "chirps";
    }

    @PostMapping("/feed")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public FeedResponse feed(@RequestBody String food) {
        return new FeedResponse("thanks for " + food);
    }
}
