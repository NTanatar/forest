package com.nata.nasa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageCache {

    private static final String IMAGE_CACHE_NAME = "imageCache";

    private final NasaService nasaService;

    @CacheEvict(IMAGE_CACHE_NAME)
    public void evictCache(long sol) {
        log.info("Evicting cache: {}", sol);
    }

    @Cacheable(IMAGE_CACHE_NAME)
    public Image getLargest(int sol) {
        return nasaService.getLargest(sol);
    }
}
