package com.github.brunoroberto.urlshortener.server;

import com.github.brunoroberto.urlshortener.server.config.UrlShortenerConfig;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

@Component
public class UrlIDGenerator {

    private final UrlShortenerConfig urlShortenerConfig;

    public UrlIDGenerator(UrlShortenerConfig urlShortenerConfig) {
        this.urlShortenerConfig = urlShortenerConfig;
    }

    public String generate() {
        return RandomStringUtils.randomAlphanumeric(urlShortenerConfig.getUrlIDMaxChars());
    }

}
