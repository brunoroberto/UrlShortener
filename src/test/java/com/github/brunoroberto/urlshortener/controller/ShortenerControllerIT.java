package com.github.brunoroberto.urlshortener.controller;

import com.github.brunoroberto.urlshortener.controller.dto.ShortUrlRequest;
import com.github.brunoroberto.urlshortener.controller.dto.ShortUrlResponse;
import com.github.brunoroberto.urlshortener.server.UrlIDGenerator;
import com.github.brunoroberto.urlshortener.server.config.UrlShortenerConfig;
import com.github.brunoroberto.urlshortener.server.model.ShortUrl;
import com.github.brunoroberto.urlshortener.server.repository.ShortUrlRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.List;

import static com.github.brunoroberto.urlshortener.util.TestData.*;
import static com.github.brunoroberto.urlshortener.util.TestUtil.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ShortenerControllerIT {

    private static final String CREATE_PATH = "/";
    private static final String GET_PATH = "/";

    private static final String URL_ID_FIELD = "$.urlID";
    private static final String EXPIRE_ON_FIELD = "$.expireOn";

    @LocalServerPort
    private int serverPort;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ShortUrlRepository shortUrlRepository;

    @Autowired
    private UrlShortenerConfig urlShortenerConfig;

    @Autowired
    private UrlIDGenerator urlIDGenerator;

    private ShortUrlRequest shortUrlRequest;

    @BeforeEach
    void init() {
        shortUrlRequest = new ShortUrlRequest();
        shortUrlRequest.setUrl(VALID_URL);
        shortUrlRequest.setExpireTimeInHours(VALID_EXPIRE_TIME_IN_HOURS);
    }

    @AfterEach
    void cleanUp() {
        shortUrlRepository.deleteAll();
    }

    @Test
    void createShortUrl_validUrlAndValidExpireTime_shortUrlCreated() throws Exception {
        LocalDateTime expectedExpireOn = LocalDateTime.now().plusHours(shortUrlRequest.getExpireTimeInHours());

        MvcResult result = mockMvc.perform(post(createUrlWithServerPort() + CREATE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(shortUrlRequest))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath(URL_ID_FIELD).exists())
                .andExpect(jsonPath(EXPIRE_ON_FIELD).exists())
                .andReturn();

        ShortUrlResponse shortUrlResponse = (ShortUrlResponse) toObject(result, ShortUrlResponse.class);

        ShortUrl shortUrlDB = shortUrlRepository.findByUrlID(shortUrlResponse.getUrlID());

        assertNotNull(shortUrlDB);
        assertNotNull(shortUrlDB.getCreatedOn());
        assertEquals(shortUrlResponse.getUrlID(), shortUrlDB.getUrlID());
        assertExpireTime(expectedExpireOn, shortUrlResponse.getExpireOn());
    }

    @Test
    void createShortUrl_validUrlAndNoExpireTime_noShortUrlCreatedAndBadRequest() throws Exception {
        shortUrlRequest.setExpireTimeInHours(0);

        mockMvc.perform(post(CREATE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(shortUrlRequest))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        List<ShortUrl> shortUrls = shortUrlRepository.findAll();
        assertEquals(0, shortUrls.size());
    }

    @Test
    void createShortUrl_validUrlAndExceedExpireTime_noShortUrlCreatedAndBadRequest() throws Exception {
        shortUrlRequest.setExpireTimeInHours(urlShortenerConfig.getExpireTimeInHours() + 1);

        mockMvc.perform(post(CREATE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(shortUrlRequest))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        List<ShortUrl> shortUrls = shortUrlRepository.findAll();
        assertEquals(0, shortUrls.size());
    }

    @Test
    void createShortUrl_noUrlAndValidExpireTime_noShortUrlCreatedAndBadRequest() throws Exception {
        shortUrlRequest.setUrl(null);

        mockMvc.perform(post(CREATE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(shortUrlRequest))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        List<ShortUrl> shortUrls = shortUrlRepository.findAll();
        assertEquals(0, shortUrls.size());
    }

    @Test
    void createShortUrl_invalidUrlAndValidExpireTime_noShortUrlCreatedAndBadRequest() throws Exception {
        shortUrlRequest.setUrl(INVALID_URL);

        mockMvc.perform(post(CREATE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(shortUrlRequest))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        List<ShortUrl> shortUrls = shortUrlRepository.findAll();
        assertEquals(0, shortUrls.size());
    }

    @Test
    void getShortUrl_validUrlID_redirectToCorrectUrl() throws Exception {
        MvcResult result = mockMvc.perform(post(createUrlWithServerPort() + CREATE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(shortUrlRequest))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath(URL_ID_FIELD).exists())
                .andExpect(jsonPath(EXPIRE_ON_FIELD).exists())
                .andReturn();

        ShortUrlResponse shortUrlResponse = (ShortUrlResponse) toObject(result, ShortUrlResponse.class);

        mockMvc.perform(get(createUrlWithServerPort() + GET_PATH + shortUrlResponse.getUrlID()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(shortUrlRequest.getUrl()));
    }

    @Test
    void getShortUrl_invalidUrlID_notFound() throws Exception {
        String urlID = URL_ID;
        mockMvc.perform(get(createUrlWithServerPort() + GET_PATH + urlID))
                .andExpect(status().isNotFound());
    }

    @Test
    void getShortUrl_validUrlIDAndExpired_redirectToCorrectUrlAndDeleteIt() throws Exception {
        ShortUrl shortUrl = new ShortUrl();
        shortUrl.setUrlID(urlIDGenerator.generate());
        shortUrl.setOriginalUrl(VALID_URL);
        shortUrl.setExpireOn(LocalDateTime.now().minusHours(1));

        shortUrl = shortUrlRepository.save(shortUrl);

        mockMvc.perform(get(createUrlWithServerPort() + GET_PATH + shortUrl.getUrlID()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(shortUrlRequest.getUrl()));

        ShortUrl shortUrlFromDB = shortUrlRepository.findByUrlID(shortUrl.getUrlID());
        assertNull(shortUrlFromDB);
    }

    private String createUrlWithServerPort() {
        return String.format("http://localhost:%d", serverPort);
    }
}
