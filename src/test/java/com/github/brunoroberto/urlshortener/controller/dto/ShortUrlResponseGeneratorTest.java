package com.github.brunoroberto.urlshortener.controller.dto;

import com.github.brunoroberto.urlshortener.server.model.ShortUrl;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ShortUrlResponseGeneratorTest {

    @Test
    void generate_nullShortUrl_throwsNullPointerException() {
        ShortUrl shortUrl = null;
        assertThrows(NullPointerException.class, () -> ShortUrlResponseGenerator.generate(shortUrl));
    }

    @Test
    void generate_validShortUrl_shortUrlResponseGenerated() {
        ShortUrl shortUrl = new ShortUrl();
        shortUrl.setUrlID("urlID");
        shortUrl.setExpireOn(LocalDateTime.now());

        ShortUrlResponse shortUrlResponse = ShortUrlResponseGenerator.generate(shortUrl);
        assertNotNull(shortUrlResponse);
        assertEquals(shortUrl.getUrlID(), shortUrlResponse.getUrlID());
        assertEquals(shortUrl.getExpireOn(), shortUrlResponse.getExpireOn());
    }

}
