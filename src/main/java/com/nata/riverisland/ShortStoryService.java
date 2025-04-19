package com.nata.riverisland;

import com.nata.annotation.Mystery;
import org.springframework.stereotype.Service;

@Service
public class ShortStoryService {

    public void create() {
        System.out.println("There was an island.");
    }

    @Mystery
    public void proceed() {
        System.out.println("And there lived..");
    }
}
