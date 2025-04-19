package com.nata.forest;

import com.nata.annotation.Mystery;

public interface StoryService {
    @Mystery
    void create();
    void proceed();
}
