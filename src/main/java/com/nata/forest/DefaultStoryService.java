package com.nata.forest;

import org.springframework.stereotype.Service;

@Service
public class DefaultStoryService implements StoryService {

    @Override
    public void create() {
        System.out.println("Once upon a time, there was a forest.");
    }

    @Override
    public void proceed() {
        System.out.println("And there lived a ..");
    }
}
