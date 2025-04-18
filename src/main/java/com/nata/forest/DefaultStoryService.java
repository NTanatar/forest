package com.nata.forest;

import org.springframework.stereotype.Service;

@Service
public class DefaultStoryService implements StoryService {

    @Override
    public String create() {
        return "Once upon a time, there was a forest.";
    }
}
