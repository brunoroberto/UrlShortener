package com.github.brunoroberto.urlshortener.server;

import com.github.brunoroberto.urlshortener.controller.dto.ShortUrlRequest;
import com.github.brunoroberto.urlshortener.controller.dto.ShortUrlResponse;
import com.github.brunoroberto.urlshortener.server.repository.ShortUrlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.github.brunoroberto.urlshortener.util.TestData.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ShortenerServiceTest {

    private static final int DEFAULT_EXPIRE_TIME_IN_HOURS = 48;

    @InjectMocks
    private UrlShortenerService urlShortenerService;

    @Mock
    private ShortUrlRepository shortUrlRepository;

    @BeforeEach
    void init() {
    }

    @Test
    void createShortUrl_validUrlAndValidExpireTime_shortUrlCreated() {
        ShortUrlRequest shortUrlRequest = new ShortUrlRequest() {{
            setUrl(VALID_URL);
            setExpireTimeInHours(VALID_EXPIRE_TIME_IN_HOURS);
        }};
        ShortUrlResponse shortUrlResponse = urlShortenerService.createShortUrl(shortUrlRequest);
        assertNotNull(shortUrlResponse);
        assertNotNull(shortUrlResponse.getUrlID());
        assertEquals(shortUrlResponse.getExpireOn(), shortUrlRequest.getExpireTimeInHours());
    }


    @Test
    void createShortUrl_validUrlAndNoExpireTime_shortUrlCreatedWithDefaultExpireTime() {
        ShortUrlRequest shortUrlRequest = new ShortUrlRequest() {{
            setUrl(VALID_URL);
            setExpireTimeInHours(-1);
        }};
        ShortUrlResponse shortUrlResponse = urlShortenerService.createShortUrl(shortUrlRequest);
        assertNotNull(shortUrlResponse);
        assertNotNull(shortUrlResponse.getUrlID());
        assertEquals(shortUrlResponse.getExpireOn(), DEFAULT_EXPIRE_TIME_IN_HOURS);
    }

    @Test
    void createShortUrl_noUrlAndValidExpireTime_throwIllegalArgumentException() {
        ShortUrlRequest shortUrlRequest = new ShortUrlRequest() {{
            setUrl(null);
            setExpireTimeInHours(VALID_EXPIRE_TIME_IN_HOURS);
        }};
        assertThrows(IllegalArgumentException.class, () -> urlShortenerService.createShortUrl(shortUrlRequest));
    }

    @Test
    void createShortUrl_invalidUrlAndValidExpireTime_throwsIllegalArgumentException() {
        ShortUrlRequest shortUrlRequest = new ShortUrlRequest() {{
            setUrl(INVALID_URL);
            setExpireTimeInHours(VALID_EXPIRE_TIME_IN_HOURS);
        }};
        assertThrows(IllegalArgumentException.class, () -> urlShortenerService.createShortUrl(shortUrlRequest));
    }

}
