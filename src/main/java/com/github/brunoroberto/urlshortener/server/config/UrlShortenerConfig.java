package com.github.brunoroberto.urlshortener.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UrlShortenerConfig {

    @Value("${urlShortener.expireTimeInHours}")
    private int expireTimeInHours;

    @Value("${urlShortener.urlIDMaxChars}")
    private int urlIDMaxChars;

    public int getExpireTimeInHours() {
        return expireTimeInHours;
    }

    public int getUrlIDMaxChars() {
        return urlIDMaxChars;
    }
}
