package com.github.brunoroberto.urlshortener.controller.dto;

import com.github.brunoroberto.urlshortener.server.model.ShortUrl;

public class ShortUrlResponseGenerator {

    public static ShortUrlResponse generate(ShortUrl shortUrl) {
        ShortUrlResponse shortUrlResponse = new ShortUrlResponse();
        shortUrlResponse.setUrlID(shortUrl.getUrlID());
        shortUrlResponse.setExpireOn(shortUrl.getExpireOn());
        return shortUrlResponse;
    }

}
