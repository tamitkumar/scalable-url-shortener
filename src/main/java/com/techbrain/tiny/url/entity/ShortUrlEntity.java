package com.techbrain.tiny.url.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@Entity
@Table(name = "short_urls")
@NoArgsConstructor
@AllArgsConstructor
public class ShortUrlEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2048)
    private String originalUrl;

    @Column(nullable = false, unique = true)
    private String shortCode;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Long visitCount;

    @PrePersist
    public void prePersist() {
        if (shortCode == null || shortCode.isEmpty()) {
            this.shortCode = generateShortCode();
        }
        if (createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (visitCount == null) {
            this.visitCount = 0L;
        }
    }

    private String generateShortCode() {
        return UUID.randomUUID().toString().substring(0, 8); // Example
    }

    public void incrementClickCount() {
        this.visitCount++;
    }
}
