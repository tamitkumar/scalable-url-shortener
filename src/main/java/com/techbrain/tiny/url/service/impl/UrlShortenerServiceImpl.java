package com.techbrain.tiny.url.service.impl;

import com.techbrain.tiny.url.entity.ShortUrlEntity;
import com.techbrain.tiny.url.repository.ShortUrlRepository;
import com.techbrain.tiny.url.service.UrlShortenerService;
import com.techbrain.tiny.url.utils.Base62Encoder;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;


@Service
public class UrlShortenerServiceImpl implements UrlShortenerService {

    private final ShortUrlRepository repository;
    private final RedisTemplate<String, String> redisTemplate;
    private static final String REDIS_PREFIX = "url:";
    private static final long CACHE_TTL = 1L;

    public UrlShortenerServiceImpl(ShortUrlRepository shortUrlRepository, RedisTemplate<String, String> redisTemplate) {
        this.repository = shortUrlRepository;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public String createShortUrl(String originalUrl) {
        Optional<ShortUrlEntity> existing = repository.findByOriginalUrl(originalUrl);
        if (existing.isPresent()) {
            return existing.get().getShortCode();
        }
        ShortUrlEntity entity = ShortUrlEntity.builder()
                .originalUrl(originalUrl)
                .visitCount(0L)
                .build();
        entity = repository.save(entity); // Save once to get ID

        String shortCode = Base62Encoder.encode(entity.getId());
        entity.setShortCode(shortCode);
        repository.save(entity);

        // Cache the mapping
        redisTemplate.opsForValue().set(REDIS_PREFIX + shortCode, originalUrl, CACHE_TTL, TimeUnit.DAYS);
        System.out.println("Cached key: " + REDIS_PREFIX + shortCode + " with URL: " + originalUrl);
        return shortCode;
    }

    @Override
    public Optional<String> getOriginalUrl(String shortCode) {
        // Try cache first
        String cachedUrl = redisTemplate.opsForValue().get(REDIS_PREFIX + shortCode);
        System.out.println("Cache hit for key: " + REDIS_PREFIX + shortCode + " -> " + cachedUrl);
        if (cachedUrl != null) {
            incrementClickCount(shortCode);
            return Optional.of(cachedUrl);
        }

        // Fallback to DB
        return repository.findByShortCode(shortCode).map(entity -> {
            redisTemplate.opsForValue().set(REDIS_PREFIX + shortCode, entity.getOriginalUrl(), CACHE_TTL, TimeUnit.DAYS);
            System.out.println("Cached key: " + REDIS_PREFIX + shortCode + " with URL: " + entity.getOriginalUrl());
            incrementClickCount(shortCode);
            return entity.getOriginalUrl();
        });
    }

    private void incrementClickCount(String shortCode) {
        repository.findByShortCode(shortCode).ifPresent(entity -> {
            entity.incrementClickCount();
            repository.save(entity);
        });
    }

    @Override
    public Long getClickCount(String shortCode) {
        return repository.findByShortCode(shortCode).map(ShortUrlEntity::getVisitCount).orElse(0L);
    }
}
