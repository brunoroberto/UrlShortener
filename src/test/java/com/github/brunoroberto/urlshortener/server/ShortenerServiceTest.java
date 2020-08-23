package com.github.brunoroberto.urlshortener.server;

import com.github.brunoroberto.urlshortener.controller.dto.ShortUrlRequest;
import com.github.brunoroberto.urlshortener.controller.dto.ShortUrlResponse;
import com.github.brunoroberto.urlshortener.server.config.UrlShortenerConfig;
import com.github.brunoroberto.urlshortener.server.error.ShortUrlNotFoundException;
import com.github.brunoroberto.urlshortener.server.model.ShortUrl;
import com.github.brunoroberto.urlshortener.server.model.ShortUrlGenerator;
import com.github.brunoroberto.urlshortener.server.repository.ShortUrlRepository;
import com.github.brunoroberto.urlshortener.server.validator.ShortUrlRequestValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;

import static com.github.brunoroberto.urlshortener.util.TestData.*;
import static com.github.brunoroberto.urlshortener.util.TestUtil.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ShortenerServiceTest {

    @InjectMocks
    private UrlShortenerService urlShortenerService;

    @Mock
    private ShortUrlRequestValidator shortUrlRequestValidator;

    @Mock
    private ShortUrlRepository shortUrlRepository;

    @Mock
    private UrlIDGenerator urlIDGenerator;

    @Mock
    private UrlShortenerConfig urlShortenerConfig;

    private ShortUrlRequest shortUrlRequest;

    @BeforeEach
    void init() {
        shortUrlRequest = new ShortUrlRequest();
        shortUrlRequest.setUrl(VALID_URL);
        shortUrlRequest.setExpireTimeInHours(VALID_EXPIRE_TIME_IN_HOURS);

        ShortUrl fakeShortUrl = ShortUrlGenerator.generate(shortUrlRequest);
        when(shortUrlRepository.save(any())).thenReturn(fakeShortUrl);

        doNothing().when(shortUrlRequestValidator).validate(any());

        when(urlShortenerConfig.getUrlIDMaxChars()).thenReturn(5);
    }

    @Test
    void createShortUrl_validUrlAndValidExpireTime_shortUrlCreated() {
        LocalDateTime expectedExpireOn = LocalDateTime.now().plusHours(shortUrlRequest.getExpireTimeInHours());
        ShortUrlResponse shortUrlResponse = urlShortenerService.createShortUrl(shortUrlRequest);
        assertNotNull(shortUrlResponse);
        assertNull(shortUrlResponse.getUrlID());
        assertExpireTime(expectedExpireOn, shortUrlResponse.getExpireOn());
    }


    @Test
    void createShortUrl_validUrlAndNoExpireTime_throwsIllegalArgumentException() {
        doThrow(new IllegalArgumentException()).when(shortUrlRequestValidator).validate(any());
        shortUrlRequest.setExpireTimeInHours(-1);
        assertThrows(IllegalArgumentException.class, () -> urlShortenerService.createShortUrl(shortUrlRequest));
    }

    @Test
    void createShortUrl_noUrlAndValidExpireTime_throwsIllegalArgumentException() {
        doThrow(new IllegalArgumentException()).when(shortUrlRequestValidator).validate(any());
        shortUrlRequest.setUrl(null);
        assertThrows(IllegalArgumentException.class, () -> urlShortenerService.createShortUrl(shortUrlRequest));
    }

    @Test
    void createShortUrl_invalidUrlAndValidExpireTime_throwsIllegalArgumentException() {
        doThrow(new IllegalArgumentException()).when(shortUrlRequestValidator).validate(any());
        shortUrlRequest.setUrl(INVALID_URL);
        assertThrows(IllegalArgumentException.class, () -> urlShortenerService.createShortUrl(shortUrlRequest));
    }

    @Test
    void getShortUrl_validUrlID_originalUrlReturned() {
        ShortUrl fakeShortUrl = new ShortUrl();
        fakeShortUrl.setUrlID(URL_ID);
        fakeShortUrl.setOriginalUrl(VALID_URL);
        fakeShortUrl.setExpireOn(LocalDateTime.now().plusHours(VALID_EXPIRE_TIME_IN_HOURS));

        when(shortUrlRepository.findByUrlID(URL_ID)).thenReturn(fakeShortUrl);

        String originalUrl = urlShortenerService.getOriginalUrl(URL_ID);
        assertNotNull(originalUrl);
        assertEquals(fakeShortUrl.getOriginalUrl(), originalUrl);
    }

    @Test
    void getShortUrl_invalidUrlID_throwsShortUrlNotFoundException() {
        String urlID = "123";
        assertThrows(ShortUrlNotFoundException.class, () -> urlShortenerService.getOriginalUrl(urlID));
    }

}
