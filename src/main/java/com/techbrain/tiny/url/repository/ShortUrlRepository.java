package com.techbrain.tiny.url.repository;

import com.techbrain.tiny.url.entity.ShortUrlEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShortUrlRepository extends JpaRepository<ShortUrlEntity, Long> {
    Optional<ShortUrlEntity> findByShortCode(String shortCode);
    Optional<ShortUrlEntity> findByOriginalUrl(String originalUrl);
}
