package com.github.brunoroberto.urlshortener.server.model;

import com.github.brunoroberto.urlshortener.controller.dto.ShortUrlRequest;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static com.github.brunoroberto.urlshortener.util.TestData.VALID_EXPIRE_TIME_IN_HOURS;
import static com.github.brunoroberto.urlshortener.util.TestData.VALID_URL;
import static com.github.brunoroberto.urlshortener.util.TestUtil.assertExpireTime;
import static org.junit.jupiter.api.Assertions.*;

public class ShortUrlGeneratorTest {

    @Test
    void generate_nullShortUrlRequest_throwsNullPointerException() {
        ShortUrlRequest shortUrlRequest = null;
        assertThrows(NullPointerException.class, () -> ShortUrlGenerator.generate(shortUrlRequest));
    }

    @Test
    void generate_validShortUrlRequest_shortUrlGenerated() {
        ShortUrlRequest shortUrlRequest = new ShortUrlRequest();
        shortUrlRequest.setUrl(VALID_URL);
        shortUrlRequest.setExpireTimeInHours(VALID_EXPIRE_TIME_IN_HOURS);

        LocalDateTime expectedExpireOn = LocalDateTime.now().plusHours(shortUrlRequest.getExpireTimeInHours());

        ShortUrl shortUrl = ShortUrlGenerator.generate(shortUrlRequest);
        assertNotNull(shortUrl);
        assertNull(shortUrl.getUrlID());
        assertNull(shortUrl.getId());
        assertNull(shortUrl.getCreatedOn());
        assertEquals(shortUrlRequest.getUrl(), shortUrl.getOriginalUrl());
        assertExpireTime(expectedExpireOn, shortUrl.getExpireOn());
    }

}
