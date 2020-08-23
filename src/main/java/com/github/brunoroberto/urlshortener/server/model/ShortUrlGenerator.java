package com.github.brunoroberto.urlshortener.server.model;

import com.github.brunoroberto.urlshortener.controller.dto.ShortUrlRequest;
import com.github.brunoroberto.urlshortener.server.slug.IDGenerator;

import java.time.LocalDateTime;

public class ShortUrlGenerator {

    public static ShortUrl generate(ShortUrlRequest shortUrlRequest) {
        ShortUrl shortUrl = new ShortUrl();
        shortUrl.setUrlID(IDGenerator.generate());
        shortUrl.setOriginalUrl(shortUrlRequest.getUrl());
        shortUrl.setExpireOn(LocalDateTime.now().plusHours(shortUrlRequest.getExpireTimeInHours()));
        return shortUrl;
    }

}
