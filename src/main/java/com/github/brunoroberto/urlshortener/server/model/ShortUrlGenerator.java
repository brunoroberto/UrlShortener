package com.github.brunoroberto.urlshortener.server.model;

import com.github.brunoroberto.urlshortener.controller.dto.ShortUrlRequest;

import java.time.LocalDateTime;

public class ShortUrlGenerator {

    public static ShortUrl generate(ShortUrlRequest shortUrlRequest) {
        ShortUrl shortUrl = new ShortUrl();
        shortUrl.setOriginalUrl(shortUrlRequest.getUrl());
        shortUrl.setExpireOn(generateExpireOn(shortUrlRequest.getExpireTimeInHours()));
        return shortUrl;
    }

    private static LocalDateTime generateExpireOn(int expireTimeInHours) {
        return LocalDateTime.now().plusHours(expireTimeInHours);
    }

}
