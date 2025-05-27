package com.techbrain.tiny.url.service;


import java.util.Optional;

public interface UrlShortenerService {
    String createShortUrl(String originalUrl);
    Optional<String> getOriginalUrl(String shortCode);
    Long getClickCount(String shortCode);
}
