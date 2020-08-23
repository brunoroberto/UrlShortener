package com.github.brunoroberto.urlshortener.controller;

import com.github.brunoroberto.urlshortener.controller.dto.ShortUrlRequest;
import com.github.brunoroberto.urlshortener.controller.dto.ShortUrlResponse;
import com.github.brunoroberto.urlshortener.server.UrlShortenerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class ShortenerController {

    private Logger logger = LoggerFactory.getLogger(ShortenerController.class);

    private final UrlShortenerService urlShortenerService;

    public ShortenerController(UrlShortenerService urlShortenerService) {
        this.urlShortenerService = urlShortenerService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ShortUrlResponse createShortUrl(@RequestBody ShortUrlRequest shortUrlRequest) {
        logger.info("Received request to create a short url - {}", shortUrlRequest);
        return urlShortenerService.createShortUrl(shortUrlRequest);
    }

    @GetMapping("/{urlID}")
    @ResponseStatus(HttpStatus.FOUND)
    public void getOriginalUrl(@PathVariable String urlID, HttpServletResponse httpServletResponse) throws IOException {
        logger.info("Received request to retrieve the original url for urlID: {}", urlID);
        String originalUrl = urlShortenerService.getOriginalUrl(urlID);
        httpServletResponse.sendRedirect(originalUrl);
    }

}
