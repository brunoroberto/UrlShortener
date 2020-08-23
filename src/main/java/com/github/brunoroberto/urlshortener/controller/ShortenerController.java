package com.github.brunoroberto.urlshortener.controller;

import com.github.brunoroberto.urlshortener.controller.dto.ShortUrlRequest;
import com.github.brunoroberto.urlshortener.controller.dto.ShortUrlResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShortenerController {

    @GetMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ShortUrlResponse createShortUrl(@RequestBody ShortUrlRequest shortUrlRequest) {
        return null;
    }

}
