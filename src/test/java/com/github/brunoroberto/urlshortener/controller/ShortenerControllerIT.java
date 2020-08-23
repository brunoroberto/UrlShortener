package com.github.brunoroberto.urlshortener.controller;

import com.github.brunoroberto.urlshortener.controller.dto.ShortUrlRequest;
import com.github.brunoroberto.urlshortener.controller.dto.ShortUrlResponse;
import com.github.brunoroberto.urlshortener.server.config.UrlShortenerConfig;
import com.github.brunoroberto.urlshortener.server.model.ShortUrl;
import com.github.brunoroberto.urlshortener.server.repository.ShortUrlRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static com.github.brunoroberto.urlshortener.util.TestData.*;
import static com.github.brunoroberto.urlshortener.util.TestUtil.toJsonString;
import static com.github.brunoroberto.urlshortener.util.TestUtil.toObject;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ShortenerControllerIT {

    private static final String CREATE_PATH = "/";

    private static final String SLUG_FIELD = "slug";
    private static final String EXPIRE_TIME_IN_HOURS_FIELD = "expireTimeInHours";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ShortUrlRepository shortUrlRepository;

    @Autowired
    private UrlShortenerConfig urlShortenerConfig;

    @Test
    void createShortUrl_validUrlAndValidExpireTime_shortUrlCreated() throws Exception {
        ShortUrlRequest request = new ShortUrlRequest() {{
            setUrl(VALID_URL);
            setExpireTimeInHours(VALID_EXPIRE_TIME_IN_HOURS);
        }};

        MvcResult result = mockMvc.perform(post(CREATE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(request))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath(SLUG_FIELD).exists())
                .andExpect(jsonPath(EXPIRE_TIME_IN_HOURS_FIELD).exists())
                .andReturn();

        ShortUrlResponse shortUrlResponse = (ShortUrlResponse) toObject(result, ShortUrlResponse.class);

        ShortUrl shortUrlDB = shortUrlRepository.findBySlug(shortUrlResponse.getUrlID());

        assertNotNull(shortUrlDB);
        assertNotNull(shortUrlDB.getCreatedOn());
        assertEquals(shortUrlResponse.getUrlID(), shortUrlDB.getUrlID());
        assertEquals(shortUrlResponse.getExpireOn(), request.getExpireTimeInHours());
    }

    @Test
    void createShortUrl_validUrlAndNoExpireTime_shortUrlCreatedWithDefaultExpireTime() throws Exception {
        ShortUrlRequest request = new ShortUrlRequest() {{
            setUrl(VALID_URL);
            setExpireTimeInHours(0);
        }};

        MvcResult result = mockMvc.perform(post(CREATE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(request))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath(SLUG_FIELD).exists())
                .andExpect(jsonPath(EXPIRE_TIME_IN_HOURS_FIELD).exists())
                .andReturn();

        ShortUrlResponse shortUrlResponse = (ShortUrlResponse) toObject(result, ShortUrlResponse.class);

        ShortUrl shortUrlDB = shortUrlRepository.findBySlug(shortUrlResponse.getUrlID());

        assertNotNull(shortUrlDB);
        assertNotNull(shortUrlDB.getCreatedOn());
        assertEquals(shortUrlResponse.getUrlID(), shortUrlDB.getUrlID());
        assertEquals(shortUrlResponse.getExpireOn(), urlShortenerConfig.getExpireTimeInHours());
    }

    @Test
    void createShortUrl_noUrlAndValidExpireTime_noShortUrlCreatedAndBadRequest() throws Exception {
        ShortUrlRequest request = new ShortUrlRequest() {{
            setUrl(null);
            setExpireTimeInHours(VALID_EXPIRE_TIME_IN_HOURS);
        }};

        mockMvc.perform(post(CREATE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(request))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        List<ShortUrl> shortUrls = shortUrlRepository.findAll();
        assertEquals(0, shortUrls.size());
    }

    @Test
    void createShortUrl_invalidUrlAndValidExpireTime_noShortUrlCreatedAndBadRequest() throws Exception {
        ShortUrlRequest request = new ShortUrlRequest() {{
            setUrl(INVALID_URL);
            setExpireTimeInHours(VALID_EXPIRE_TIME_IN_HOURS);
        }};

        mockMvc.perform(post(CREATE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(request))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        List<ShortUrl> shortUrls = shortUrlRepository.findAll();
        assertEquals(0, shortUrls.size());
    }
}
