package com.techbrain.tiny.url.controller;

import com.techbrain.tiny.url.service.UrlShortenerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/")
@Tag(name = "URL Shortener API")
public class TinyURLController {

    private final UrlShortenerService service;

    public TinyURLController(UrlShortenerService service) {
        this.service = service;
    }

    @PostMapping("/shorten")
    @Operation(summary = "Create short URL")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Request body for creating short URL",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Map.class),
                    examples = @ExampleObject(
                            name = "Shorten URL Request Example",
                            value = "{\"originalUrl\": \"https://youtube.com\"}"
                    )
            )
    )
    public ResponseEntity<Map<String, String>> createShortUrl(@RequestBody Map<String, String> body) {
        String originalUrl = body.get("originalUrl");
        if (originalUrl == null || originalUrl.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "originalUrl is required"));
        }
        String shortCode = service.createShortUrl(originalUrl);
        return ResponseEntity.ok(Map.of("shortCode", shortCode));
    }

    @GetMapping("/{shortCode}")
    @Operation(summary = "Redirect to original URL")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode) {
        return service.getOriginalUrl(shortCode)
            .map(url -> {
                HttpHeaders headers = new HttpHeaders();
                headers.setLocation(URI.create(url));
                return new ResponseEntity<Void>(headers, HttpStatus.FOUND);
            }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/analytics/{shortCode}")
    @Operation(summary = "Get click analytics")
    public ResponseEntity<Map<String, Object>> getAnalytics(@PathVariable String shortCode) {
        Long clicks = service.getClickCount(shortCode);
        return ResponseEntity.ok(Map.of("shortCode", shortCode, "clickCount", clicks));
    }
}
